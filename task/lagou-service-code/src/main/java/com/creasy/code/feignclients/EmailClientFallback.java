package com.creasy.code.feignclients;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailClientFallback implements EmailClient {
    @Override
    public boolean sendCodeToEmail(String email, String code) {
        log.debug("EmailClientFallback.sendCodeToEmail is invoke!");
        return false;
    }
}
