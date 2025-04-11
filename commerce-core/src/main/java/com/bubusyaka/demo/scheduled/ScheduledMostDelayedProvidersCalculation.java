package com.bubusyaka.demo.scheduled;


import com.bubusyaka.demo.model.dto.MostDelayedProviders;
import com.bubusyaka.demo.repository.jpa.MostDelayedProvidersRepository;
import com.bubusyaka.demo.service.CacheService;
import com.bubusyaka.demo.service.ItemService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
@ConditionalOnProperty(
        value="most-delayed-providers.enabled",
        havingValue = "true")
public class ScheduledMostDelayedProvidersCalculation {

    private final MostDelayedProvidersRepository mostDelayedProvidersRepository;
    private final CacheService cacheService;
    private final RedissonClient redissonClient;

    private RLock lock;

    @PostConstruct
    public void init() {
        lock = redissonClient.getLock("lockMostDelayedProviders");
    }

    @Scheduled(fixedDelayString = "${most-delayed-providers-calculation.delay}", initialDelayString = "${most-delayed-providers-calculation.initial-delay}")
    public void scheduledMostDelayedProvidersCalculation() throws InterruptedException {
        //log.info("First scheduler before lock");
        if (lock.tryLock()) {
            try {
                var mostDelayedProviders = mostDelayedProvidersRepository.getMostDelayedProviders()
                        .stream()
                        .map(array -> new MostDelayedProviders((String) array[0], BigDecimal.valueOf((Double) array[2]), ((Long) array[1]).intValue()))
                        .collect(Collectors.toList());
                Thread.sleep(8000);

                cacheService.put(mostDelayedProviders);
            } finally {
                //log.info("First scheduler unlock {}", LocalTime.now());
                lock.unlock();
            }
        } else {
            //log.info("First scheduler cant lock lock");
        }
    }

    @Scheduled(fixedDelayString = "${most-delayed-providers-calculation.delay2}", initialDelayString = "${most-delayed-providers-calculation.initial-delay2}")
    public void scheduledMostDelayedProvidersCalculation2() throws InterruptedException {
        //log.info("Second scheduler before lock");
        if (lock.tryLock()) {
            try {
                var mostDelayedProviders = mostDelayedProvidersRepository.getMostDelayedProviders()
                        .stream()
                        .map(array -> new MostDelayedProviders((String) array[0], BigDecimal.valueOf((Double) array[2]), ((Long) array[1]).intValue()))
                        .collect(Collectors.toList());
                Thread.sleep(8000);

                cacheService.put(mostDelayedProviders);
            } finally {
                //log.info("Second scheduler unlock {}", LocalTime.now());
                lock.unlock();
            }
        } else {
            //log.info("Second scheduler cant lock lock");
        }
    }

}
