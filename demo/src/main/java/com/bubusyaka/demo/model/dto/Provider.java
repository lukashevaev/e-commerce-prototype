package com.bubusyaka.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Provider {
    private Long id;
    private String providerName;
    private String providerCity;
}
