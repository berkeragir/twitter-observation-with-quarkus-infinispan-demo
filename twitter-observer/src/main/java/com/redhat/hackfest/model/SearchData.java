package com.redhat.hackfest.model;

import java.util.Calendar;
import java.util.Date;

import org.infinispan.client.hotrod.Search;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class SearchData {
    
    private String id;
    private String searchKeywords;
    private Date created;

    @ProtoFactory
    public SearchData(String id, String searchKeywords) {
        this.id = id;
        this.searchKeywords = searchKeywords;
        created = Calendar.getInstance().getTime();
    }

    @ProtoField(number = 1)
	public String getId() {
		return id;
    }
    
	public void setId(String id) {
		this.id = id;
    }
    
    @ProtoField(number = 2)
	public String getSearchKeywords() {
		return searchKeywords;
    }
    
	public void setSearchKeywords(String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}

    
}
