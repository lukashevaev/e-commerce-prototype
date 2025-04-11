package com.bubusyaka.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name="delivery_allowed_city")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DeliveryAllowedCityEntity extends BaseEntity {

    @Column(name = "city_name")
    private String cityName;
}