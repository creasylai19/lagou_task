package com.creasy.service.impl;

import com.creasy.service.DubboAService;
import org.apache.dubbo.rpc.RpcContext;

public class DubboAServiceImpl implements DubboAService {

    public static final String TRANS_IP = "clientIP";

    public String methodA(String arg) {
        System.out.println(RpcContext.getContext().getAttachment(TRANS_IP));
        return "Service implementation from dubboA.arg:" + arg;
    }

}
