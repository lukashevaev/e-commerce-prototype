package com.bubusyaka.recommendation.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name="orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity extends BaseEntity{

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @CreatedDate
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "user_id")
    private Long userId;
}
