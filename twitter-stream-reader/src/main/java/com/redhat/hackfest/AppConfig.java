package com.redhat.hackfest;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigProperties(prefix = "com.redhat.hackfest.twitter")
public class AppConfig {

    @Getter @Setter
    private String apiKey;

    @Getter @Setter
    private String apiSecretKey;

    @Getter @Setter
    private String bearerToken;

    @Getter @Setter
    private String accessToken;

    @Getter @Setter
    private String accessTokenSecret;
}
