package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.dto.MostDelayedProviders;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class CaffeineCacheService implements CacheService {

    private final CaffeineCache caffeineCache;

    @Override
    public void put(List<MostDelayedProviders> value) {
        caffeineCache.put("MostDelayedProviders", value);
    }

    @Override
    public List<MostDelayedProviders> get() {
        return (List<MostDelayedProviders>) caffeineCache.get("MostDelayedProviders");
    }
}
