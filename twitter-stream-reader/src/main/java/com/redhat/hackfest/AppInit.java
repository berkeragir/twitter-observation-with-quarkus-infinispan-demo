package com.redhat.hackfest;

import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class AppInit {
    private static final Logger LOGGER = Logger.getLogger(AppInit.class.getName());

    public static final String CACHE_NAME = "hackfest_tweets";

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
       LOGGER.info("On start - clean and load");
       String xml = String.format(CACHE_CONFIG, CACHE_NAME);
       cacheManager.administration().getOrCreateCache(CACHE_NAME, new XMLStringConfiguration(xml));
    }

    @Produces
    public RemoteCache<String, TweetData> getRemoteCache() {
        return cacheManager.getCache(CACHE_NAME);
    }
}
