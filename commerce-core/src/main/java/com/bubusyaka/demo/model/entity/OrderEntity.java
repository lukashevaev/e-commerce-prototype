package com.bubusyaka.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Table(name="orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class OrderEntity extends BaseEntity {

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
