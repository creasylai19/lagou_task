package com.creasy.service.impl;

import com.creasy.service.IServiceProvider;

import java.util.Random;

public class ServiceProviderImpl implements IServiceProvider {
    @Override
    public String methodA() {
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "methodA";
    }

    @Override
    public String methodB() {
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "methodB";
    }

    @Override
    public String methodC() {
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "methodC";
    }
}
