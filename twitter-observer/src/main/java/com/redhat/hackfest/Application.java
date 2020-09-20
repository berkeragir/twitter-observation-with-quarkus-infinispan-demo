package com.redhat.hackfest;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.redhat.hackfest.model.SearchData;
import com.redhat.hackfest.model.TweetData;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class Application {
    
    public static final String CORE_CACHE_NAME = "hackfest";

    public static final String TWEETS_CACHE_NAME = "hackfest_tweets";

    @Inject
    RemoteCacheManager cacheManager;

    private static final String CACHE_CONFIG =
    "<infinispan><cache-container>" +
          "<distributed-cache name=\"%s\"></distributed-cache>" +
          "</cache-container></infinispan>";

    /**
     * Listens startup event to load the data
     */
    void onStart(@Observes @Priority(value = 1) StartupEvent ev) {
        String xml = String.format(CACHE_CONFIG, Application.CORE_CACHE_NAME);
        cacheManager.administration().getOrCreateCache(Application.CORE_CACHE_NAME, new XMLStringConfiguration(xml));

        xml = String.format(CACHE_CONFIG, Application.TWEETS_CACHE_NAME);
        cacheManager.administration().getOrCreateCache(Application.TWEETS_CACHE_NAME, new XMLStringConfiguration(xml));
    }
 
     @Produces
     public RemoteCache<String, TweetData> getTweetsRemoteCache() {
         return cacheManager.getCache(Application.TWEETS_CACHE_NAME);
     }

     @Produces
     public RemoteCache<String, SearchData> getRemoteCache() {
         return cacheManager.getCache(Application.CORE_CACHE_NAME);
     }
}
