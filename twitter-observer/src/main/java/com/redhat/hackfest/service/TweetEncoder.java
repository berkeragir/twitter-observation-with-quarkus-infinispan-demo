package com.redhat.hackfest.service;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.redhat.hackfest.model.TweetObject;

public class TweetEncoder implements Encoder.Text<TweetObject> {
    
    @Override
    public String encode(final TweetObject tweet) throws EncodeException {
        return Json.createObjectBuilder()
                .add("id", tweet.getId())
                .add("text", tweet.getText())
                .add("createdAt", tweet.getCreatedAt())
                .add("lang", tweet.getLang())
                .add("keywords", tweet.getKeywords())
                .build().toString();
    }

	@Override
	public void init(EndpointConfig config) {
		// Not required
	}

	@Override
	public void destroy() {
		// Not required
	}    
}

