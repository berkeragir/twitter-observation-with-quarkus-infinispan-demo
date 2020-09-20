package com.redhat.hackfest.service;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.hackfest.AppConfig;
import com.redhat.hackfest.TweetListener;

import twitter4j.ConnectionLifeCycleListener;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;


@ApplicationScoped
public class TwitterStreamService {
    
    private static final Logger LOGGER = Logger.getLogger(TwitterStreamService.class.getName());

    AppConfig config;
    TweetListener tweetListener;

    TwitterStream twitterStream;


    @Inject
    public TwitterStreamService(AppConfig appConfig, TweetListener tweetListener) {

        this.config = appConfig;
        this.tweetListener = tweetListener;

        // Initialize Twitter Stream object with the auth config.

        twitterStream = TwitterStreamFactory.getSingleton();
        twitterStream.setOAuthConsumer(config.getApiKey(), config.getApiSecretKey());

        AccessToken accessToken = new AccessToken(config.getAccessToken(), config.getAccessTokenSecret());
        twitterStream.setOAuthAccessToken(accessToken);

        // The connection life cycle listener to log what's going on with the Twitter connection.
        twitterStream.addConnectionLifeCycleListener(new ConnectionLifeCycleListener(){

			@Override
			public void onConnect() {
				LOGGER.info("Connected to Twitter stream.");
			}

			@Override
			public void onDisconnect() {
				LOGGER.info("Disconnected from Twitter stream.");
			}

			@Override
			public void onCleanUp() {
				LOGGER.info("Cleaning up Twitter stream object.");
			} 
        });
    }


    public void startStream(String keywords) {

        LOGGER.warning(config.toString());

        // Stop any preceeding stream connections if there is any...
        this.stopStream();

        FilterQuery query = new FilterQuery();

        //query.locations(new double[][] {  { 26.2, 36.28  }, { 44.46, 41.87  }  } );   // Turkey
        //query.track("redhat", "red hat", "openshift", "quarkus", "cloud", "azure", "aws");

        //query.language("tr", "en", "de", "fr");
        query.track(keywords.split(","));

        tweetListener.setSearchKeywords(keywords);

        twitterStream = twitterStream.addListener(tweetListener).filter(query);


    }

    public void stopStream() {

        twitterStream.shutdown();
        twitterStream.clearListeners();
    }
}