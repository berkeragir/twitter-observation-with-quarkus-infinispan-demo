package com.redhat.hackfest;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.redhat.hackfest.service.TweetService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;



@Path(value = "/rest")
public class TweetSearchController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TweetSearchController.class.getName());

    @Inject
    TweetService tweetSearch;

    @Inject
    EventBus bus;  
    
    @POST
    @Path("/newSearch")
    public Response newSearch(String keywords) {

        LOGGER.info("Initiating new twitter observation with keywords: " + keywords);

        bus.sendAndForget("newSearch", keywords);

        //tweetSearch.initiateNewObservation(keywords);

        return Response.status(Status.CREATED).build();
    }
}
