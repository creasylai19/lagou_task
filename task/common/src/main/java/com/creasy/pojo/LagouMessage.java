package com.creasy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LagouMessage implements Serializable {

    private Integer status;
    private String message;
    private String data;

    public LagouMessage(Integer status) {
        this.status = status;
    }

    public LagouMessage(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
