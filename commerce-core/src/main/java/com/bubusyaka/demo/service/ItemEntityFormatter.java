package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.entity.ItemEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ITEM_FORMATTER")
public class ItemEntityFormatter implements EntityFormatter<ItemEntity> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convert(ItemEntity entity) {
        return Try.of(() -> objectMapper.writeValueAsString(entity)).get();
    }
}

