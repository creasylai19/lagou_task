package com.creasy.user.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "lagou-service-code", fallback = CodeClientFallback.class, path = "/code")
public interface ICodeClient {

    @RequestMapping("/validate/{email}/{code}")
    public Integer validateCode(@PathVariable("email") String email, @PathVariable("code") String code );

}
