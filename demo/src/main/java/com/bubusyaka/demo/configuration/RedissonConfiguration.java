package com.bubusyaka.demo.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
public class RedissonConfiguration {

    @Value("${redisson.url}")
    private String redisUrl;

    @Bean
    public RedissonClient getRedissonClient(){
        var config = new Config();
        config.useSingleServer().setAddress(redisUrl);
        return Redisson.create(config);
    }
}

