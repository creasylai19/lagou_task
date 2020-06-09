package com.creasy.gateway.feignclients;

import com.creasy.pojo.LagouMessage;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements IUserClient{
    @Override
    public LagouMessage info(String token) {
        return null;
    }
}
