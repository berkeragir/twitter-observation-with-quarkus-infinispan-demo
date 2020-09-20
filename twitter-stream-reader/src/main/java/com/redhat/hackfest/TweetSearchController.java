package com.redhat.hackfest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.redhat.hackfest.service.TwitterStreamService;

import org.jboss.logging.Logger;

@Path("/")
public class TweetSearchController {
    
    private static final Logger LOGGER = Logger.getLogger(TweetSearchController.class.getName());
    
    @Inject
    TwitterStreamService twitterStreamService;

    @POST
    @Path("/searchTweets")
    public void hello(String keywords) {

        LOGGER.info("New stream follow request: " + keywords);
        twitterStreamService.startStream(keywords);

        try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @GET
    @Path("/bye")
    public void bye() {
        twitterStreamService.stopStream();
    }
}
