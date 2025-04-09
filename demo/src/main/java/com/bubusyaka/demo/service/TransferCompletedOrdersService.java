package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.dto.OrderDTO;
import com.bubusyaka.demo.model.entity.BaseEntity;
import com.bubusyaka.demo.model.entity.ItemEntity;
import com.bubusyaka.demo.model.entity.NewOrderEntity;
import com.bubusyaka.demo.model.entity.OrderEntity;
import com.bubusyaka.demo.repository.jpa.NewOrderRepository;
import com.bubusyaka.demo.repository.jpa.OrderRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@Service
@Slf4j
public class TransferCompletedOrdersService {
    private  final NewOrderRepository newOrderRepository;
    private final OrderRepository orderRepository;

    public TransferCompletedOrdersService(NewOrderRepository newOrderRepository, OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.newOrderRepository = newOrderRepository;
        this.orderRepository = orderRepository;
    }

    public void transferCompletedOrders(List<NewOrderEntity> completedOrders) {
            List<OrderEntity> orders = completedOrders.stream()
                    .map(completedOrder -> {
                        var oldOrder = new OrderEntity(completedOrder.getItemId(), completedOrder.getCityId(), completedOrder.getIsCompleted(), completedOrder.getCreationDate(), completedOrder.getCompletionDate(), completedOrder.getUserId());
                        return oldOrder;
                    })
                    .collect(Collectors.toList());
            orderRepository.saveAll(orders);
            log.info("[{}] Completed orders have been saved to Orders", orders.size());

            var ids = completedOrders
                    .stream()
                    .map(BaseEntity::getId)
                    .collect(Collectors.toList());
            newOrderRepository.deleteCompletedOrdersFromNewTable(ids);
            log.info("[{}] Completed orders have been deleted from NewOrders", ids.size());
    }
}
