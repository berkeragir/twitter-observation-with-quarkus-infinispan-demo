package com.redhat.hackfest.model;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbAnnotation;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class TweetData implements Serializable {

	private static final long serialVersionUID = 1232975493815216749L;

	String text;
    String createdAt;
    String lang;
	String keywords;

    @ProtoFactory
    public TweetData(String text, String createdAt, String lang, String keywords){
        this.text = text;
        this.createdAt = createdAt;
		this.lang = lang;
		this.keywords = keywords;
    }

    @ProtoField(number = 1)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

    @ProtoField(number = 2)
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

    @ProtoField(number = 3)
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@ProtoField(number = 4)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}
