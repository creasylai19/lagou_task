package com.creasy.user.feignclients;

import com.creasy.pojo.StatusCode;

public class CodeClientFallback implements ICodeClient {

    @Override
    public Integer validateCode(String email, String code) {
        return StatusCode.ERROR.value();
    }

}
