package com.library.apigateway.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@LoadBalancerClients({
        @LoadBalancerClient(name = "user-service", configuration = LoadBalancerConfig.class),
        @LoadBalancerClient(name = "book-service", configuration = LoadBalancerConfig.class),
        @LoadBalancerClient(name = "loan-service", configuration = LoadBalancerConfig.class),
        @LoadBalancerClient(name = "review-service", configuration = LoadBalancerConfig.class)
})
public class LoadBalancerConfig {

    @Bean
    @Primary
    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier() {
        return ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
                .withHealthChecks()
                .withRetryAwareness()
                .build();
    }
}