package com.creasy.filter;

import com.creasy.config.IPUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransportIPFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransportIPFilter.class);
    public static final String TRANS_IP = "clientIP";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(TRANS_IP, IPUtils.ip_address.get());
        return invoker.invoke(invocation);
    }
}
