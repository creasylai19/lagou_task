package com.creasy.pojo;

public enum StatusCode {

    CORRECT(0),
    ERROR(1),
    EXPIRE(2);

    private int value;

    StatusCode(int i) {
        this.value = i;
    }

    public int value(){
        return value;
    }
}
