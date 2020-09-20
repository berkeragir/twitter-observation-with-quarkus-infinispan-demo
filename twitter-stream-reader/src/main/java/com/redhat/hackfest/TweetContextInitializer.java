package com.redhat.hackfest;

import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;
import org.infinispan.protostream.SerializationContextInitializer;

@AutoProtoSchemaBuilder(includeClasses = { TweetData.class }, schemaPackageName = "hackfest_tweets")
public interface TweetContextInitializer extends SerializationContextInitializer {
    
}

