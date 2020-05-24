package com.creasy.code.dao;

import com.creasy.pojo.LagouAuthCode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAuthCodeDao {

    public int saveAuthCode(LagouAuthCode lagouAuthCode);

    public LagouAuthCode getNewestAuthCode(LagouAuthCode lagouAuthCode);

}
