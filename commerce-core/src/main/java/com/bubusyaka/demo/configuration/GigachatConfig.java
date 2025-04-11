package com.bubusyaka.demo.configuration;

import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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
