package com.redhat.hackfest.model;

import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(schemaPackageName = "hackfest",
      includeClasses = {SearchData.class})
public interface SearchDataContextInitializer extends SerializationContextInitializer {
}
