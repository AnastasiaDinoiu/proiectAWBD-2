package com.library.user.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter userSearchCounter(MeterRegistry meterRegistry) {
        return Counter.builder("user.search.requests")
                .description("Number of user search requests")
                .register(meterRegistry);
    }

    @Bean
    public Counter userCreateCounter(MeterRegistry meterRegistry) {
        return Counter.builder("user.create.requests")
                .description("Number of user creation requests")
                .register(meterRegistry);
    }

    @Bean
    public Timer userProcessingTimer(MeterRegistry meterRegistry) {
        return Timer.builder("user.processing.time")
                .description("Time taken to process user operations")
                .register(meterRegistry);
    }
}