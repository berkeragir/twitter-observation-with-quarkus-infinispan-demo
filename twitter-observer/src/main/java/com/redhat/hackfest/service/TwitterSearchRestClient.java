package com.redhat.hackfest.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient(configKey="my-twitter-stream-api")
public interface TwitterSearchRestClient {
    
    @POST
    @Path("/searchTweets")
    void initiateNewTwitterStream(String keywords);
}
