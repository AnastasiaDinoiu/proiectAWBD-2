package com.library.apigateway.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CircuitBreakerMonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerMonitoringService.class);
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerMonitoringService(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void monitorCircuitBreakers() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(circuitBreaker -> {
            CircuitBreaker.State state = circuitBreaker.getState();
            CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();

            logger.info("Circuit Breaker: {} - State: {} - Failure Rate: {}% - Calls: {}",
                    circuitBreaker.getName(),
                    state,
                    metrics.getFailureRate(),
                    metrics.getNumberOfBufferedCalls());
        });
    }
}