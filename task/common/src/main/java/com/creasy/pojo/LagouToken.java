package com.creasy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LagouToken implements Serializable {

    private Integer id;//⾃自增主键,
    private String email;//邮箱地址,
    private String token;//令牌,

}
