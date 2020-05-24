package com.creasy.gateway.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    /*@Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
    }

    RateLimiter myRateLimiter(){
        return new RateLimiter() {
            @Override
            public Mono<Response> isAllowed(String routeId, String id) {
                return null;
            }

            @Override
            public Map getConfig() {
                return null;
            }

            @Override
            public Class getConfigClass() {
                return null;
            }

            @Override
            public Object newConfig() {
                return null;
            }
        }
    }*/
}
