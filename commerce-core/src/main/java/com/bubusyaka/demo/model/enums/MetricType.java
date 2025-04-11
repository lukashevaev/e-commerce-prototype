package com.bubusyaka.demo.model.enums;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@AllArgsConstructor
public enum MetricType {

    DISTRIBUTED_SUMMARY(
            (Field field) -> DistributionSummary.class.getTypeName().equals(field.getAnnotatedType().getType().getTypeName()),
            (MetricParams metricParams) -> DistributionSummary
                    .builder(metricParams.name)
                    .baseUnit("milliseconds")
                    .register(metricParams.meterRegistry)
    ),
    COUNTER(
            (Field field) -> Counter.class.getTypeName().equals(field.getAnnotatedType().getType().getTypeName()),
            (MetricParams metricParams) -> Counter
                    .builder(metricParams.name)
                    .register(metricParams.meterRegistry)
    ),
    GAUGE(
            (Field field) -> Gauge.class.getTypeName().equals(field.getAnnotatedType().getType().getTypeName()),
            (MetricParams metricParams) -> Gauge
                    .builder(metricParams.name, new ArrayList<>(), List::size)
                    .baseUnit("milliseconds")
                    .register(metricParams.meterRegistry)
    ),
    NONE(
            (Field field) -> false,
            (MetricParams metricParamsIgnored) -> null
    );

    private final Predicate<Field> predicate;
    private final Function<MetricParams, Meter> metric;

    public static MetricType from(Field field) {
        return Arrays.stream(values())
                .filter(type -> type.predicate.test(field))
                .findFirst()
                .orElse(NONE);
    }

    public Meter init(MetricParams metricParams) {
        return metric.apply(metricParams);
    }

    public static class MetricParams {
        private String name;
        private MeterRegistry meterRegistry;

        public MetricParams(String name, MeterRegistry meterRegistry) {
            this.name = name;
            this.meterRegistry = meterRegistry;
        }
    }
}
