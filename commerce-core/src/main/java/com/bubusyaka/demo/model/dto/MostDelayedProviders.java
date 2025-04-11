package com.bubusyaka.demo.model.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MostDelayedProviders implements Serializable {

    private String providerName;
    private BigDecimal averageDelay;
    private Integer delayCount;
}
