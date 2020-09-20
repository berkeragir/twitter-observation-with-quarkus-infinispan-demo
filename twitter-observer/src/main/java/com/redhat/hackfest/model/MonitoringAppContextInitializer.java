package com.redhat.hackfest.model;

import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(schemaPackageName = "hackfest_tweets",
      includeClasses = {TweetData.class})
public interface MonitoringAppContextInitializer extends SerializationContextInitializer {
}
