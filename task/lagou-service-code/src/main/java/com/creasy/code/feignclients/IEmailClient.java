package com.creasy.code.feignclients;

import com.creasy.pojo.Email;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "lagou-service-email", fallback = EmailClientFallback.class, path = "/email")
public interface IEmailClient {

    @RequestMapping("/{email}/{code}")
    public boolean sendCodeToEmail(@PathVariable("email") String email, @PathVariable("code") String code);

}
