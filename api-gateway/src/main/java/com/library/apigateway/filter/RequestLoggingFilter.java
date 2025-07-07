package com.library.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Generate request ID
        String requestId = UUID.randomUUID().toString().substring(0, 8);

        // Log incoming request
        logger.info("API-Gateway: [{}] Incoming request: {} {} from {}",
                requestId,
                request.getMethod(),
                request.getURI(),
                request.getRemoteAddress());

        // Add request ID to headers
        ServerHttpRequest modifiedRequest = request.mutate()
                .header(REQUEST_ID_HEADER, requestId)
                .build();

        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(modifiedRequest)
                .build();

        long startTime = System.currentTimeMillis();

        return chain.filter(modifiedExchange)
                .doOnSuccess(aVoid -> {
                    long duration = System.currentTimeMillis() - startTime;
                    logger.info("API-Gateway: [{}] Request completed in {} ms with status: {}",
                            requestId,
                            duration,
                            exchange.getResponse().getStatusCode());
                })
                .doOnError(throwable -> {
                    long duration = System.currentTimeMillis() - startTime;
                    logger.error("API-Gateway: [{}] Request failed in {} ms with error: {}",
                            requestId,
                            duration,
                            throwable.getMessage());
                });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}