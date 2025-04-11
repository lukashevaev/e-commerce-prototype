package com.bubusyaka.recommendation.service;

import com.bubusyaka.recommendation.model.dto.CompositeDTO;
import com.bubusyaka.recommendation.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FullInformationAboutOrdersService {
    private final OrderRepository orderRepository;

    public List<CompositeDTO> getLast25Orders() {
        return orderRepository.findItemsInfoOrderedByUsersNoID().stream().limit(25).toList();
    }

    public String formatOutputoString(List<CompositeDTO> orders) {
        StringBuilder builder = new StringBuilder();
        orders.stream()
                .filter(entity -> entity != null) // Фильтрация null элементов
                .forEach(item -> item.addToBuilder(builder));

        return builder.toString();
    }
}
