package com.example.kafka_producer.configuration;

import com.example.kafka_producer.model.dto.OrderDTO;
import com.example.kafka_producer.model.entity.OrderEntity;
import com.example.kafka_producer.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.micrometer.core.annotation.Timed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;


@RequiredArgsConstructor
@Component
@Slf4j
public class OrderInitialization {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private final ObjectMapper objectMapper;

    @Timed
    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void init() throws JsonProcessingException {
        sendOrders();
    }

    public void sendOrders() throws JsonProcessingException {
        for (int i = 0; i < 10; i++) {
            var order = new OrderDTO();
            order.setItemId((int) (new Random().nextLong(99) + 1));
            order.setCityId((int) (new Random().nextLong(9) + 1));
            order.setIsCompleted(false);
            order.setCreationDate(LocalDateTime.now().minusDays(new Random().nextInt(7) + 1));
            order.setUserId((int) (new Random().nextLong(9) + 1));
            kafkaTemplate.send("test", objectMapper.writeValueAsString(order));
            log.info("Sending order with itemId: [{}], cityId: [{}]", order.getItemId(), order.getCityId());
        }
    }
}


