package com.example.kafka_producer.controller;

import java.util.List;

import com.example.kafka_producer.configuration.OrderInitialization;
import com.example.kafka_producer.model.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//import com.example.demo.model.User;
//import com.example.demo.service.KafkaService;

//@RestController
//public class ProducerController {
//    @Autowired
//    OrderInitialization kafkaProducer;
//
//    @PostMapping("/producer")
//    public String sendMessage(@RequestBody OrderEntity order) {
//        kafkaProducer.send(order);
//        return "Message sent successfully to the Kafka topic";
//    }
//
//}