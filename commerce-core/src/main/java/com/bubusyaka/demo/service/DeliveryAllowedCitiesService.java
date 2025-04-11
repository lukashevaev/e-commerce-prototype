package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.dto.DeliveryAllowedCities;
import com.bubusyaka.demo.repository.jpa.AllowedCitiesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryAllowedCitiesService {

    private final AllowedCitiesRepository allowedCitiesRepository;

    public List<DeliveryAllowedCities> findAllowedCities() {
        return allowedCitiesRepository.findAll()
                .stream()
                .map(entity -> new DeliveryAllowedCities(entity.getId(), entity.getCityName()))
                .collect(Collectors.toList());
    }
}
