package com.bubusyaka.demo.controller;

import com.bubusyaka.demo.model.dto.Provider;
import com.bubusyaka.demo.model.entity.ProviderEntity;
import com.bubusyaka.demo.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/providers")
@Tag(name = "Provider Controller", description = "Shows info about provider")
public class ProviderController {
    private final ProviderService providerService;

    @Autowired
    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @Operation(summary = "Shows all providers")
    @GetMapping
    public List<Provider> getAllProviders() {
        List<Provider> providers= new ArrayList<>();

        return providers;
    }

    }



