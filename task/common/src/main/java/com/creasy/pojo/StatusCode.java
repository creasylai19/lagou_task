package com.creasy.pojo;

public enum StatusCode {

    CORRECT(0,"成功"),
    ERROR(1, "失败"),
    EXPIRE(2, "验证码已过期"),
    HAS_REGISTER(3, "用户已注册");

    private final int value;
    private final String desc;

    StatusCode(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int value(){
        return value;
    }
    public String desc(){
        return desc;
    }
}
