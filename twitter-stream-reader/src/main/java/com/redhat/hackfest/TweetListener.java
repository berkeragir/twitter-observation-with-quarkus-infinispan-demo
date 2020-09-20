package com.redhat.hackfest;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCache;

import lombok.Getter;
import lombok.Setter;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;


@ApplicationScoped
public class TweetListener implements StatusListener {

    private static final Logger LOGGER = Logger.getLogger(TweetListener.class.getName());

    @Inject
	RemoteCache<String, TweetData> tweetStore;
	
	@Getter
	@Setter
	private String searchKeywords;

	@Override
	public void onException(Exception arg0) {
		LOGGER.warning("Error: " + arg0.getMessage());
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		LOGGER.warning(arg0.getMessage());
		
	}

	@Override
	public void onStatus(Status tweet) {
		
		String tweetStr = tweet.getLang() + " | " + tweet.getText();
		
		if(tweet.getGeoLocation() != null )
			tweetStr += "\n" + tweet.getGeoLocation().toString();

		tweetStore.putAsync(Long.toString(tweet.getId()), new TweetData(tweet.getText(), tweet.getCreatedAt().toString(), tweet.getLang(), searchKeywords));
        LOGGER.info(tweetStr);	
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		LOGGER.warning("onTrackLimitationNotice() is called");
		
	}


}
