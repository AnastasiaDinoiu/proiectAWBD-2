package com.library.book.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter bookSearchCounter(MeterRegistry meterRegistry) {
        return Counter.builder("book.search.requests")
                .description("Number of book search requests")
                .register(meterRegistry);
    }

    @Bean
    public Counter bookCreateCounter(MeterRegistry meterRegistry) {
        return Counter.builder("book.create.requests")
                .description("Number of book creation requests")
                .register(meterRegistry);
    }

    @Bean
    public Timer bookProcessingTimer(MeterRegistry meterRegistry) {
        return Timer.builder("book.processing.time")
                .description("Time taken to process book operations")
                .register(meterRegistry);
    }

    @Bean
    public Counter categorySearchCounter(MeterRegistry meterRegistry) {
        return Counter.builder("category.search.requests")
                .description("Number of category search requests")
                .register(meterRegistry);
    }
}