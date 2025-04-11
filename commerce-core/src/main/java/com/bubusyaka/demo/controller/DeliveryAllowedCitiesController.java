package com.bubusyaka.demo.controller;

import com.bubusyaka.demo.model.dto.DeliveryAllowedCities;
import com.bubusyaka.demo.service.DeliveryAllowedCitiesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/allowed")
@Tag(name = "Allowed cities controller", description = "Shows information about delivery allowed cities")
public class DeliveryAllowedCitiesController {

    private final DeliveryAllowedCitiesService deliveryAllowedCitiesService;

    public DeliveryAllowedCitiesController(DeliveryAllowedCitiesService deliveryAllowedCitiesService) {
        this.deliveryAllowedCitiesService = deliveryAllowedCitiesService;
    }

    @GetMapping
    public List<DeliveryAllowedCities> allowedCities() {
        List<DeliveryAllowedCities> allowedCities = new ArrayList<>();

        return allowedCities;
    }

    @GetMapping("/all")
    @Operation(summary = "Shows all allowed cities")
    public List<DeliveryAllowedCities> allCities() {

        return deliveryAllowedCitiesService.findAllowedCities();
    }
}
