package com.library.review.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    @ConditionalOnBean(MeterRegistry.class)
    public Counter reviewCreateCounter(MeterRegistry meterRegistry) {
        return Counter.builder("review.create.requests")
                .description("Number of review creation requests")
                .register(meterRegistry);
    }

    @Bean
    @ConditionalOnBean(MeterRegistry.class)
    public Counter reviewSearchCounter(MeterRegistry meterRegistry) {
        return Counter.builder("review.search.requests")
                .description("Number of review search requests")
                .register(meterRegistry);
    }

    @Bean
    @ConditionalOnBean(MeterRegistry.class)
    public Timer reviewProcessingTimer(MeterRegistry meterRegistry) {
        return Timer.builder("review.processing.time")
                .description("Time taken to process review operations")
                .register(meterRegistry);
    }
}