package com.library.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CircuitBreakerFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerFilter.class);
    private final ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory;

    public CircuitBreakerFilter(ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String serviceName = extractServiceName(exchange.getRequest().getPath().value());

        if (serviceName != null) {
            var circuitBreaker = circuitBreakerFactory.create(serviceName);

            return circuitBreaker.run(
                    chain.filter(exchange),
                    throwable -> handleFallback(exchange, serviceName, throwable)
            );
        }

        return chain.filter(exchange);
    }

    private String extractServiceName(String path) {
        if (path.startsWith("/api/books") || path.startsWith("/api/authors") || path.startsWith("/api/categories")) {
            return "book-service";
        } else if (path.startsWith("/api/users")) {
            return "user-service";
        } else if (path.startsWith("/api/loans")) {
            return "loan-service";
        } else if (path.startsWith("/api/reviews")) {
            return "review-service";
        }
        return null;
    }

    private Mono<Void> handleFallback(ServerWebExchange exchange, String serviceName, Throwable throwable) {
        logger.error("Circuit breaker activated for service: {} due to: {}", serviceName, throwable.getMessage());

        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        exchange.getResponse().getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String fallbackResponse = String.format(
                "{\"error\":\"Service Unavailable\",\"message\":\"The %s is currently unavailable. Please try again later.\",\"timestamp\":\"%s\"}",
                serviceName, java.time.Instant.now()
        );

        var buffer = exchange.getResponse().bufferFactory().wrap(fallbackResponse.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}