package com.example.kafka_producer.model.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {

    private Integer itemId;
    private Integer cityId;
    private Boolean isCompleted;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Integer userId;

}
