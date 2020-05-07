package com.creasy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MethodInvokeObj {

    private String methodName;
    private long invokeTime;
    private int invokeDuration;
    private String result;

}
