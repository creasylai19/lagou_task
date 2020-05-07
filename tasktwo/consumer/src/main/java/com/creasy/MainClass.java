package com.creasy;

import com.creasy.pojo.MethodInvokeObj;
import com.creasy.service.IServiceProvider;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.*;

public class MainClass {

    public static void main(String[] args) throws InterruptedException, IOException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:consumer.xml");
        context.start();
        IServiceProvider iServiceProvider = context.getBean(IServiceProvider.class); // 获取远程服务代理

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Random random = new Random();
        Method[] methods = IServiceProvider.class.getMethods();

        while (true){
            Thread.sleep(random.nextInt(30));
//            Thread.sleep(500);//每秒2此调用，5秒总共10此调用
//            System.in.read();
            executorService.submit(()->{
                try {
                    Random innerRandom = new Random();
                    Method method = methods[innerRandom.nextInt()%methods.length];
                    method.invoke(iServiceProvider);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            });
        }
    }

}
