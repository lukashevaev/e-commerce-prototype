package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.dto.CompositeDTO;
import com.bubusyaka.demo.model.dto.Item;
import com.bubusyaka.demo.model.dto.OrderDTO;
import com.bubusyaka.demo.model.entity.OrderEntity;
import com.bubusyaka.demo.repository.jpa.OrderRepository;
import com.bubusyaka.demo.repository.jpa.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DataSetOfUsersAndItemsService {

    private final OrderRepository orderRepository;
//
//    public List<CompositeDTO> getItemsInfoOrderedByUsers(Long id) {
//        //return orderRepository.findItemsInfoOrderedByUsers(id);
//        return orderRepository.findItemsInfoOrderedByUsers(id).stream().limit(100).toList();
//    }

    public List<CompositeDTO> getItemsInfoOrderedByUsers() {
        //return orderRepository.findItemsInfoOrderedByUsers(id);
        return orderRepository.findItemsInfoOrderedByUsersNoID().stream().limit(25).toList();
    }

    public String convertToCustomString(List<CompositeDTO> orders) {
        StringBuilder builder = new StringBuilder();
        orders.stream()
                .filter(entity -> entity != null) // Фильтрация null элементов
                .forEach(item -> item.addToBuilder(builder));

        return builder.toString();
    }

    public String convertToJson() {
        // Используем Jackson для сериализации в JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse;
        try {
            jsonResponse = mapper.writeValueAsString(getItemsInfoOrderedByUsers());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonResponse;
    }

}
