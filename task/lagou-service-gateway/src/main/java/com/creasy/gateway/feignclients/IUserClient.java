package com.creasy.gateway.feignclients;

import com.creasy.pojo.LagouMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "lagou-service-user", fallback = UserClientFallback.class, path = "/user")
public interface IUserClient {

    @RequestMapping("/info/{token}")
    public LagouMessage info(@PathVariable("token") String token);

}
