package com.bubusyaka.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeliveryAllowedCities {
    private Long id;
    private String city;
}
