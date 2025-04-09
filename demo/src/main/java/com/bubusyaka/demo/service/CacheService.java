package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.dto.MostDelayedProviders;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CacheService {
    void put(List<MostDelayedProviders> value);
    List<MostDelayedProviders> get();
}
