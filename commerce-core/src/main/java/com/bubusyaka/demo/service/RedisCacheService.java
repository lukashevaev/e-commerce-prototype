package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.dto.MostDelayedProviders;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@RequiredArgsConstructor
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Serializable> redisTemplate;
    private final RedissonClient redissonClient;

    @Override
    public void put(List<MostDelayedProviders> value) {
        redisTemplate.opsForValue().set("MostDelayedProviders", (Serializable) value);
    }

    @Override
    public List<MostDelayedProviders> get() {
        return (List<MostDelayedProviders>) redisTemplate.opsForValue().get("MostDelayedProviders");
    }

    public void put2(List<MostDelayedProviders> value){
        RLock lock = redissonClient.getLock("lockMostDelayedProviders");
        try {
            lock.lock();
            //  TODO business logic
            redisTemplate.opsForValue().set("MostDelayedProviders", (Serializable) value);

        } finally {
            lock.unlock();
        }

    }
}
