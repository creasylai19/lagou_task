package com.lagou.client;

import com.lagou.pojo.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;

public class UserClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserClientHandler.class);

    private ChannelHandlerContext context;
    private String result;
    private RpcRequest para;

    @Override
    public synchronized void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        result = "Error occur!";
        String key = null;
        for (Map.Entry<String, UserClientHandler> next : ClientBootStrap.SERVICES.entrySet()) {
            if (this == next.getValue()) {
                key = next.getKey();
            }
        }
        if( null != key ){
            ClientBootStrap.SERVICES.remove(key);
        }
        notify();
        LOGGER.debug("channelInactive");
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        context = ctx;
        LOGGER.debug("channelActive");
    }

    /**
     * 收到服务端数据，唤醒等待线程
     */

    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {
        result = msg.toString();
        notify();
    }

    /**
     * 写出数据，开始等待唤醒
     */

    public synchronized Object call() throws InterruptedException {
        context.writeAndFlush(para);
        wait();
        return result;
    }

    /*
     设置参数
     */
    void setPara(RpcRequest para) {
        this.para = para;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        LOGGER.debug("exceptionCaught");
    }

}
