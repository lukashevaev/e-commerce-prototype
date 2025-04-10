package com.example.kafka_producer.repository;

import com.example.kafka_producer.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
