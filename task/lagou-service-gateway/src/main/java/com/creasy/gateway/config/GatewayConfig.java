package com.creasy.gateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class GatewayConfig {

    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    /*
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
