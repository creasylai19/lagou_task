package com.creasy.service.impl;

import com.creasy.service.DubboBService;
import org.apache.dubbo.rpc.RpcContext;

public class DubboBServiceImpl implements DubboBService {

    public static final String TRANS_IP = "clientIP";

    @Override
    public String methodB(String arg) {
        System.out.println(RpcContext.getContext().getAttachment(TRANS_IP));
        return "Service implementation from dubboB.arg:" + arg;
    }

}
