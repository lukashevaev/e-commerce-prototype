package com.bubusyaka.demo.controller;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/bubina")
@Slf4j
@Tag(name = "Bubin controller", description = "Shows information about init duration")
public class BubaController {

    @GetMapping("/invoke")
    public void invokeAsync(){
        var startTime = System.currentTimeMillis();
        int ChunkSize = 10;
        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            runnables.add(
                    () -> {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

            );
        }

        List<List<Runnable>> groupedRunnables =
                Lists.partition(runnables, 1);

        ExecutorService executorService = Executors.newFixedThreadPool(1000);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (List<Runnable> runnableList : groupedRunnables) {
            var future =
                    CompletableFuture.runAsync(() -> {
                        for (Runnable runnable : runnableList) {
                            runnable.run();
                        }
            }, executorService);
            futures.add(future);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));

        allFutures.join();

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("Прошло времени: " + elapsedTime + " ms");
    }
}
