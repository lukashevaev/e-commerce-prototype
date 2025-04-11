package com.bubusyaka.demo.scheduled;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.redisson.api.RSemaphore;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        value="semaphore.enabled",
        havingValue = "true")
public class SemaphoreScheduledService {
    private static final Logger log = LoggerFactory.getLogger(SemaphoreScheduledService.class);
    private final RedissonClient redissonClient;
    private final int totalPermits = 2;

    private RSemaphore resourceSemaphore;

    @PostConstruct
    public void init() {
        resourceSemaphore = redissonClient.getSemaphore("semaphore");
        resourceSemaphore.trySetPermits(totalPermits);
    }

    @Scheduled(fixedDelay = 20000, initialDelay = 20000)
    public void setSemaphore1() throws InterruptedException {
        //try to acquire in 23 seconds
        resourceSemaphore.tryAcquire(23, TimeUnit.SECONDS);
        log.info("setSemaphore1");
    }

    @Scheduled(fixedDelay = 30000, initialDelay = 30000)
    public void setSemaphore2() throws InterruptedException {
        //try to acquire in 23 seconds
        resourceSemaphore.tryAcquire(23, TimeUnit.SECONDS);
        log.info("setSemaphore2");
    }

    @Scheduled(fixedDelay = 2000, initialDelay = 2000)
    public void semaphore3() {
        //постоянно проверяет готов ли семафор
        log.info("Check if semaphore is full");
        if (resourceSemaphore.availablePermits() == 0) {
            log.info("Semaphore is full");
            resourceSemaphore.release(2);
        }
    }
}
