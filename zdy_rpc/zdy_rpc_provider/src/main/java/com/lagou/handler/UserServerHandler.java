package com.lagou.handler;

import com.lagou.ServerBootstrap;
import com.lagou.pojo.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

public class UserServerHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if( msg instanceof RpcRequest) {

            RpcRequest request = (RpcRequest) msg;
            try{
                String className = request.getClassName();
                String methodName = request.getMethodName();
                Class<?>[] parameterTypes = request.getParameterTypes();
                Class<?> aClass = Class.forName(className);
                Method method = aClass.getMethod(methodName, parameterTypes);
                Object obj = method.invoke(ServerBootstrap.applicationContext.getBean(aClass), request.getParameters());
                ctx.writeAndFlush(obj.toString());
            } catch (Exception e){
                //TODO 暂时不清楚为何server会调用自己的toString方法
                String s = "An exception occur." + request.getClassName() + "#" + request.getMethodName();
                System.out.println(s);
                ctx.writeAndFlush(s);
            }

        }
        // 判断是否符合约定，符合则调用本地方法，返回数据
        // msg:  UserService#sayHello#are you ok?
//        if(msg.toString().startsWith("UserService")){
//            UserServiceImpl userService = new UserServiceImpl();
//            String result = userService.sayHello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
//            ctx.writeAndFlush(result);
//        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }
}
