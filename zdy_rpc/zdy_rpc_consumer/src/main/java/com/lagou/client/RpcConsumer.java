package com.lagou.client;

import com.alibaba.fastjson.JSON;
import com.lagou.pojo.RpcRequest;
import com.lagou.pojo.ServerInfo;
import com.lagou.utils.Constans;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RpcConsumer {

    //创建线程池对象
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //1.创建一个代理对象 providerName：UserService#sayHello are you ok?
    public static Object createProxy(final Class<?> serviceClass){
        //借助JDK动态代理生成代理对象
        return  Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //（1）调用初始化netty客户端的方法

                if( method.getName().equals("toString") ) {//TODO 暂时不清楚为何会调用toString方法，先过滤掉
                    return "toString";
                }
                RpcRequest request = new RpcRequest();
                request.setClassName(serviceClass.getName());
                request.setMethodName(method.getName());
                request.setParameters(args);
                request.setParameterTypes(method.getParameterTypes());
                request.setRequestId(UUID.randomUUID().toString());
//                if( request.getRequestId() != null ){
//                    throw new RuntimeException("WHAT?");
//                }
                // 设置参数
                UserClientHandler userClientHandler = chooseUserClientHandler();
//                UserClientHandler userClientHandler = ClientBootStrap.SERVICES.values().iterator().next();
                if(null == userClientHandler){
                    String message = "No services available";
                    return message;
//                    throw new RuntimeException();
                }
                userClientHandler.setPara(request);
                // 去服务端请求数据

                return executor.submit(userClientHandler).get();
            }
        });


    }

    private static UserClientHandler chooseUserClientHandler() throws Exception {
        ArrayList<ServerInfo> serverInfos = new ArrayList<>();
        for (String key : ClientBootStrap.SERVICES.keySet()) {
            byte[] bytes = ClientBootStrap.client.getData().forPath(key);
            ServerInfo serverInfo = JSON.toJavaObject(JSON.parseObject(new String(bytes)), ServerInfo.class);
            serverInfos.add(serverInfo);
        }

        if( serverInfos.size() > 0 ) {
            Collections.sort(serverInfos);
            System.out.println(serverInfos);
            return ClientBootStrap.SERVICES.get(Constans.Zookeeper.PREFIX+"/"+serverInfos.get(0).getInstance());
        }
        return null;
    }


}
