package com.creasy.user.service;

public interface ITokenService {

    public boolean register(String email, String password, String code);

    public boolean isRegistered(String email);

    public boolean login(String email, String password);

    public String getTokenByEmail(String email);

    public String getEmailByToken(String token);
}
