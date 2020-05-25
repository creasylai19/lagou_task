package com.creasy.gateway.globalfilters;

import com.creasy.gateway.feignclients.IUserClient;
import com.creasy.pojo.LagouMessage;
import com.creasy.pojo.StatusCode;
import com.creasy.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

@Component
@Slf4j
@Data
@ConfigurationProperties(prefix = "gateway.verify")
public class TokenVerifyFilter implements GlobalFilter, Ordered {

    @Autowired
    private IUserClient iUserClient;

    private List<String> excludes;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("custom global filter");
        if(!StringUtils.start(exchange.getRequest().getPath().value(), excludes)){
            HttpCookie token = exchange.getRequest().getCookies().getFirst("token");
            if( token == null ) {
                setResponseStatus(exchange, HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            LagouMessage info = iUserClient.info(token.getValue());
            if( info == null || !info.getStatus().equals(StatusCode.CORRECT.value()) ) {
                setResponseStatus(exchange, HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
