package com.creasy.code.service.impl;

import com.creasy.code.dao.IAuthCodeDao;
import com.creasy.code.service.IAuthCodeService;
import com.creasy.pojo.LagouAuthCode;
import com.creasy.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class AuthCodeServiceImpl implements IAuthCodeService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IAuthCodeDao iAuthCodeDao;

    @Override
    public LagouAuthCode saveAuthCode(String email) {
        LagouAuthCode lagouAuthCode = new LagouAuthCode();
        Date date = new Date();
        lagouAuthCode.setCode(StringUtils.getRamdomString(6));
        lagouAuthCode.setCreatetime(date);
        lagouAuthCode.setEmail(email);
        lagouAuthCode.setExpiretime(new Date(date.getTime()+ TimeUnit.MINUTES.toMillis(10)));
        iAuthCodeDao.saveAuthCode(lagouAuthCode);
        return lagouAuthCode;
    }

    @Override
    public LagouAuthCode getLagouAuthCodeByEmailAndCode(String email, String code) {
        LagouAuthCode lagouAuthCode = new LagouAuthCode();
        lagouAuthCode.setEmail(email);
        lagouAuthCode.setCode(code);
        return iAuthCodeDao.getNewestAuthCode(lagouAuthCode);
    }


}
