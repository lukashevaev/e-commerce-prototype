package com.bubusyaka.recommendation.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class GigachatConfig {

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    @Value("${MEDIA_TYPE_FORM}")
    private String mediaTypeForm;

    @Value("${MEDIA_TYPE_JSON}")
    private String mediaTypeJson;

    @Value("${OAUTH_URL}")
    private String oauthURL;

    @Value("${CHAT_URL}")
    private String chatURL;

}
