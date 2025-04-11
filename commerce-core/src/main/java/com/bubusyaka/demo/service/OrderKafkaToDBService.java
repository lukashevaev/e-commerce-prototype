package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.dto.OrderDTO;
import com.bubusyaka.demo.model.entity.NewOrderEntity;
import com.bubusyaka.demo.model.entity.OrderEntity;
import com.bubusyaka.demo.repository.jpa.NewOrderRepository;
import com.bubusyaka.demo.repository.jpa.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class OrderKafkaToDBService {

        private final NewOrderRepository newOrderRepository;

        public void persistOrder(OrderDTO orderDto) {
            NewOrderEntity newOrderEntity = new NewOrderEntity(orderDto.getItemId(), orderDto.getCityId(), orderDto.getIsCompleted(), orderDto.getCreationDate(), orderDto.getCompletionDate(), orderDto.getUserId());
            NewOrderEntity persistedOrder = newOrderRepository.save(newOrderEntity);

            log.info("order persisted with id: {}, userId: {}", persistedOrder.getId(), persistedOrder.getUserId());
            //log.info("delivery time: {}", persistedOrder.getCompletionDate());
        }

    }

