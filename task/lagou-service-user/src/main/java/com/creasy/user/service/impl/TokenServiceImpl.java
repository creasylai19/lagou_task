package com.creasy.user.service.impl;

import com.creasy.pojo.LagouToken;
import com.creasy.user.dao.ITokenDao;
import com.creasy.user.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenServiceImpl implements ITokenService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ITokenDao iTokenDao;

    @Override
    public boolean register(String email, String password, String code) {
        LagouToken lagouToken = new LagouToken();
        lagouToken.setEmail(email);
        lagouToken.setToken(UUID.nameUUIDFromBytes((email + password).getBytes()).toString());
        iTokenDao.saveLagouToken(lagouToken);
        return true;
    }

    @Override
    public boolean isRegistered(String email) {
        LagouToken lagouToken = new LagouToken();
        lagouToken.setEmail(email);
        lagouToken = iTokenDao.getLagouToken(lagouToken);
        return lagouToken != null;
    }

    @Override
    public boolean login(String email, String password) {
        LagouToken lagouToken = new LagouToken();
        lagouToken.setEmail(email);
        lagouToken.setToken(UUID.nameUUIDFromBytes((email + password).getBytes()).toString());
        lagouToken = iTokenDao.getLagouToken(lagouToken);
        return lagouToken != null;
    }

    @Override
    public String getTokenByEmail(String email) {
        LagouToken lagouToken = new LagouToken();
        lagouToken.setEmail(email);
        lagouToken = iTokenDao.getLagouToken(lagouToken);
        return null == lagouToken ? null : lagouToken.getToken();
    }

    @Override
    public String getEmailByToken(String token) {
        LagouToken lagouToken = new LagouToken();
        lagouToken.setToken(token);
        lagouToken = iTokenDao.getLagouToken(lagouToken);
        return null == lagouToken ? null : lagouToken.getEmail();
    }
}
