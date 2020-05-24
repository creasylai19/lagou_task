package com.creasy.code.service;

import com.creasy.pojo.LagouAuthCode;

public interface IAuthCodeService {

    public LagouAuthCode saveAuthCode(String email);

    public LagouAuthCode getLagouAuthCodeByEmailAndCode(String email, String code);

}
