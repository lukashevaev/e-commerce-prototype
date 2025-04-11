package com.bubusyaka.demo.configuration;


import com.bubusyaka.demo.service.CacheService;
import com.bubusyaka.demo.service.CaffeineCacheService;
import com.bubusyaka.demo.service.RedisCacheService;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;
import java.util.Arrays;

@Configuration
//@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class CacheConfiguration {

    @Value("${most-delayed-providers-calculation-caching.type}")
    private String cacheType;

    @Bean
    public RedisTemplate<String, Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public CaffeineCache caffeineCacheConfig() {
        return new CaffeineCache("customerCache", Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .initialCapacity(1)
                .maximumSize(2000)
                .build());
    }

    @Bean
    public CacheManager caffeineCacheManager(CaffeineCache caffeineCache) {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(caffeineCache));
        return manager;
    }

    @Bean
    public CacheService cacheService(RedisTemplate<String, Serializable> redisTemplate,
                                     RedissonClient redissonClient,
                                     CaffeineCache caffeineCache) {

        if (cacheType.equals("redis")) {
            return new RedisCacheService(redisTemplate, redissonClient);
        }

        if (cacheType.equals("caffeine")) {
            return new CaffeineCacheService(caffeineCache);
        }
        throw new RuntimeException("Unsupported CacheType: " + cacheType);
    }
}
