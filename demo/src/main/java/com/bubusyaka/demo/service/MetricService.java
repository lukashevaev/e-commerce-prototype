package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.enums.MetricType;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static com.bubusyaka.demo.model.enums.MetricType.NONE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricService {

    private final MeterRegistry registry;

    private DistributionSummary deliveryTime;
    private DistributionSummary latencyTime;
    private Counter deliveredOrdersCounter;
    private Gauge deliveredOrdersGauge;

    @PostConstruct
    public void init() throws IllegalAccessException {
        Class<? extends MetricService> metricServiceClass = this.getClass();
        for (Field field : metricServiceClass.getDeclaredFields()) {
            var metricType = MetricType.from(field);
            // check metricType is not none and then set field accessible
            // if metric is not none then set new value (defined in method metricType.init() )
            // method metricType.init should be implemented and should return metrics of its type (Summary or counter)
            if (!NONE.equals(metricType)){
                field.setAccessible(true);
                field.set(this, metricType.init(new MetricType.MetricParams(field.getName(), registry)));

            }
        }
    }

    public void collectDeliveryTime(Long deliveryTime) {
        this.deliveryTime.record(deliveryTime);
    }

    public void collectLatencyTime(Long latenessTime) {
        latencyTime.record(latenessTime);
    }


}
