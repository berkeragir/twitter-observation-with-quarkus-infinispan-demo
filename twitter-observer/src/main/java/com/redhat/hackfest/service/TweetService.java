package com.redhat.hackfest.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.hackfest.Application;
import com.redhat.hackfest.model.SearchData;
import com.redhat.hackfest.model.TweetData;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.infinispan.client.Remote;
import io.quarkus.vertx.ConsumeEvent;

@ApplicationScoped
public class TweetService {
   private static final Logger LOGGER = LoggerFactory.getLogger(TweetService.class.getName());

   @Inject
   @Remote(Application.TWEETS_CACHE_NAME)
   RemoteCache<String, TweetData> tweetStore;

   @Inject
   @Remote(Application.CORE_CACHE_NAME)
   RemoteCache<String, SearchData> searchStore;

   @Inject
   @RestClient
   TwitterSearchRestClient twitterSearchRestClient;


   public TweetData getById(String id) {
      return tweetStore.get(id);
   }

   public CompletionStage<TweetData> getByIdAsync(String id) {
      return tweetStore.getAsync(id);
   }

   /**
    * Performs a simple full-text query on tweet text
    *
    * @param term
    * @return full tweet text
    */
   public Set<String> search(String term) {
      if (tweetStore == null) {
         LOGGER.error("Unable to search...");
         throw new IllegalStateException("Tweet store is null. Try restarting the application");
      }
      QueryFactory queryFactory = Search.getQueryFactory(tweetStore);
      
      String query = "FROM hackfest.TweetData t"
      + " WHERE t.text LIKE '%"+ term + "%'";

      Query<TweetData> queryObj = queryFactory.create(query);

      QueryResult<TweetData> result = queryObj.execute();

      List<TweetData> tweets = result.list();
      return tweets.stream().map(TweetData::getText).collect(Collectors.toSet());
   }

   @ConsumeEvent(value = "newSearch", blocking = true)
   public void initiateNewObservation(String keywords) {

      twitterSearchRestClient.initiateNewTwitterStream(keywords);

      String randId = UUID.randomUUID().toString();
      searchStore.put(randId, new SearchData(randId, keywords));
   }
}
