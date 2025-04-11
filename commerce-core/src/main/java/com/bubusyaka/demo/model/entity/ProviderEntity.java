package com.bubusyaka.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "providers")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ProviderEntity extends BaseEntity {

    @Column(name = "provider_name")
    private String providerName;

    @Column(name = "provider_city")
    private String providerCity;
}
