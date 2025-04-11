package com.bubusyaka.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name="item")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class ItemEntity extends Auditable implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Long price;

    @Column(name = "provider_id")
    private Long providerId;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "age_category")
    private Long ageCategory;

    public ItemEntity(Long id, String name, Long price, String productCategory,Long ageCategory) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productCategory = productCategory;
        this.ageCategory = ageCategory;
    }

    public ItemEntity(String name) {
        this.name = name;
    }
}


