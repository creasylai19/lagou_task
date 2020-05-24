package com.creasy.user.dao;

import com.creasy.pojo.LagouToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ITokenDao {

    int saveLagouToken(LagouToken lagouToken);

    LagouToken getLagouToken(LagouToken lagouToken);
}
