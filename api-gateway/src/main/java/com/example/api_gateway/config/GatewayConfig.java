package com.example.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            
            .route("auth-service", r -> r.path("/auth/**")
                .uri("lb://auth-service"))
            
            
            .route("employee-service", r -> r.path("/api/v1/**")
                .filters(f -> f.filter(filter)) // Apply the custom filter
                .uri("lb://employee-service"))
            
            .build();
    }
}