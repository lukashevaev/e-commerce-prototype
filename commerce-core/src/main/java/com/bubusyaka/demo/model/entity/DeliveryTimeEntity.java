package com.bubusyaka.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "delivery_time")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DeliveryTimeEntity extends BaseEntity {

    @Column(name = "way_key")
    private String wayKey;

    @Column(name = "estimated_days")
    private Integer estimatedDays;

}

