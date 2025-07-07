package com.library.loan.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter loanCreateCounter(MeterRegistry meterRegistry) {
        return Counter.builder("loan.create.requests")
                .description("Number of loan creation requests")
                .register(meterRegistry);
    }

    @Bean
    public Counter loanReturnCounter(MeterRegistry meterRegistry) {
        return Counter.builder("loan.return.requests")
                .description("Number of loan return requests")
                .register(meterRegistry);
    }

    @Bean
    public Timer loanProcessingTimer(MeterRegistry meterRegistry) {
        return Timer.builder("loan.processing.time")
                .description("Time taken to process loan operations")
                .register(meterRegistry);
    }
}