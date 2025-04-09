package com.bubusyaka.demo.scheduled;

import com.bubusyaka.demo.configuration.DatabaseInitializer;
import com.bubusyaka.demo.model.entity.OrderEntity;
import com.bubusyaka.demo.repository.jpa.*;
import com.bubusyaka.demo.service.MetricService;
import com.bubusyaka.demo.service.TransferCompletedOrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        value="order-emulation.enabled",
        havingValue = "true")
public class ScheduledOrderEmulationService {

    private final MetricService metricService;
    private final TransferCompletedOrdersService transferCompletedOrdersService;

    /***
     * Для имитации времени выполнения заказа в 1 методе
     * создавать заказ с датой в прошлом (на несколько дней назад).
     * А во втором методе дату выполнения устанавливать на текущее время.
     * В методе создания заказов выбирать товар и город получателя
     * случайным образом, а время - на основе среднего по статистике
     * времени доставки за последние 7 дней.
     * (Т.е. если среднее время доставки 3 дня, то создавать заказ на дату
     * [сегодня - 3 дня].
     *
     * Дополнительно: эмулировать различные проблемы с доставкой,
     * т.е. рандомно создавать задержки
     * (создавать заказ не на 3 дня назад, а на 4-5), но
     * чтобы эти задержки были не постоянно, а появлялись периодически
     * и потом пропадали. (Если представить график среднего времени доставки,
     * он должен быть похожим на линию с периодическими случайными всплесками, типа синусойды)
     * ***/

    private final ItemRepository itemRepository;
    private final ProviderRepository providerRepository;
    private final DeliveryTimeRepository deliveryTimeRepository;
    private final NewOrderRepository newOrderRepository;
    private final OrderRepository orderRepository;
    private final AllowedCitiesRepository allowedCitiesRepository;

    private Map<Long, String> itemCities = new HashMap<>();
    private Map<String, Integer> deliveryTime = new HashMap<>();;

    private final List<DatabaseInitializer.City> cities = Arrays.stream(DatabaseInitializer.City.values()).collect(Collectors.toList());

    @Scheduled(fixedDelayString = "${order-completion-emulation.delay}")
    @Transactional
    public void emulateOrderCompletion() {
        //var completedOrders = newOrderRepository.markAllCompleted();
        var completedOrders = newOrderRepository.transferCompletedOrders();

        //transferCompletedOrdersService.transferCompletedOrders(completedOrders);

        newOrderRepository.renewEstimatedTime();

        deliveryTimeRepository.findAll()
                .forEach(entity -> deliveryTime.put(entity.getWayKey(), entity.getEstimatedDays()));
        completedOrders.forEach(order -> {
            var orderDeliveryTime = order.getCompletionDate().toInstant(ZoneOffset.UTC).toEpochMilli() - order.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli();
            metricService.collectDeliveryTime(orderDeliveryTime);
            var providerCity = newOrderRepository.findProviderCityName(order.getItemId());
            var orderCity = allowedCitiesRepository.findOrderCityName(order.getCityId());
            var wayKey = providerCity + orderCity;
            var lateness = orderDeliveryTime - deliveryTime.get(wayKey) * 24 * 60 * 60 * 1000;
            if (lateness > 0) {
                metricService.collectLatencyTime(lateness);
            }
        });

    }

}
