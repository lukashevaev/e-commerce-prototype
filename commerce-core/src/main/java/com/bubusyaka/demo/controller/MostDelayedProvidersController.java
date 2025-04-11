package com.bubusyaka.demo.controller;

import com.bubusyaka.demo.model.dto.MostDelayedProviders;
import com.bubusyaka.demo.service.CacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mostdelayed")
@Tag(name = "Most Delayed Providers Controller", description = "Finds most delayed providers")
public class MostDelayedProvidersController {

    private final CacheService cacheService;

    public MostDelayedProvidersController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/value")
    @Operation(summary = "Shows an average delay of most delayed providers")
    public List<MostDelayedProviders> mostDelayedProviders() {

        return cacheService.get();
    }
}
