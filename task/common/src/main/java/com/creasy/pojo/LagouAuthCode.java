package com.creasy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LagouAuthCode implements Serializable {

    private Integer id;//⾃自增主键,
    private String email;//邮箱地址,
    private String code;//验证码,
    private Date createtime;//创建时间,
    private Date expiretime;//过期时间,

}
