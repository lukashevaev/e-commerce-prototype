package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.dto.OrderDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class OrderKafkaConsumer {
    private static final String orderTopic = "test";

    private final ObjectMapper objectMapper;
    private final OrderKafkaToDBService orderKafkaToDBService;
    private final MeterRegistry meterRegistry;

    @Timed
    @KafkaListener(topics = "test")
    public void consumeMessage(String message) throws JsonProcessingException {
        OrderDTO orderDto = objectMapper.readValue(message, OrderDTO.class);
        orderKafkaToDBService.persistOrder(orderDto);
    }

}

