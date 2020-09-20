package com.redhat.hackfest.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class TweetObject implements Serializable {

	private static final long serialVersionUID = 1232975493815216749L;

	String id;
	String text;
    String createdAt;
    String lang;
    String keywords;

    public TweetObject(String id, TweetData tweetData) {
		this.id = id;
        this.text = tweetData.getText();
        this.createdAt = tweetData.getCreatedAt();
        this.lang = tweetData.getLang();
        this.keywords = tweetData.getKeywords();
    }
}
