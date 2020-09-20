package com.redhat.hackfest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.redhat.hackfest.model.SearchData;
import com.redhat.hackfest.model.TweetObject;
import com.redhat.hackfest.service.TweetEncoder;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.api.continuous.ContinuousQuery;
import org.infinispan.query.api.continuous.ContinuousQueryListener;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.infinispan.client.Remote;

@ServerEndpoint(value = "/searches")
@ApplicationScoped
public class SearchDataWebSocket {
   private static final Logger LOGGER = LoggerFactory.getLogger(SearchDataWebSocket.class.getName());

   @Inject
   @Remote(Application.CORE_CACHE_NAME)
   RemoteCache<String, SearchData> searchStore;

   Map<String, Session> sessions = new ConcurrentHashMap<>();

   @OnOpen
   public void onOpen(Session session) {
      
      sessions.put(session.getId(), session);
      
      LOGGER.info("Monitoring session started");

      if (searchStore == null) {
         LOGGER.error("Tweet store is null!");
         throw new IllegalStateException("Tweet store is null. Try restarting the application");
      }

      // Create the query. Every character that it's actually performing magic in Hogwarts
      QueryFactory queryFactory = Search.getQueryFactory(searchStore);
      Query<SearchData> query = queryFactory.create("from hackfest.SearchData");

      // Create a Continuous Query Listener
      ContinuousQueryListener<String, SearchData> listener = new ContinuousQueryListener<String, SearchData>() {
         @Override
         public void resultJoining(String key, SearchData value) {
            broadcast(value.getSearchKeywords());
         }
      };

      // Create a Continuous Query
      ContinuousQuery<String, SearchData> continuousQuery = Search.getContinuousQuery(searchStore);

      // Link the query and the listener
      continuousQuery.addContinuousQueryListener(query, listener);

      // Track a session with a listener to be able to remove the listener when the web-socket is closed or an error happens
      if(listeners.containsKey(session))
         listeners.replace(session, listener);
      else
         listeners.put(session, listener);
   }

   private Map<Session, ContinuousQueryListener<String, SearchData>> listeners = new ConcurrentHashMap<>();

   @OnClose
   public void onClose(Session session) {
      sessions.remove(session.getId());
      LOGGER.info("Twitter monitoring session closing.");
      // Removing the listener is important to avoid memory leaks
      removeListener(session);
   }

   @OnError
   public void onError(Session session, Throwable throwable) {
      sessions.remove(session.getId());
      LOGGER.error("Twitter monitoring session error!", throwable);
      // Removing the listener is important to avoid memory leaks
      removeListener(session);
   }

   private void removeListener(Session session) {
      ContinuousQueryListener<String, SearchData> queryListener = listeners.get(session);
      ContinuousQuery<String, SearchData> continuousQuery = Search.getContinuousQuery(searchStore);
      continuousQuery.removeContinuousQueryListener(queryListener);
      listeners.remove(session);
   }

   private void broadcast(String message) {
      sessions.values().forEach(s -> s.getAsyncRemote().sendObject(message, result -> {
         if (result.getException() != null) {
            LOGGER.error("Something wrong going on in the search websocket...", result.getException());
         }
      }));
   }
}
