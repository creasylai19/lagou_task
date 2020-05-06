package com.creasy;

import com.creasy.service.DubboAService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class TestDubbo {

    @Test
    public void testDubboAService(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:consumer.xml");
        context.start();
        DubboAService dubboAService = (DubboAService)context.getBean(DubboAService.class); // 获取远程服务代理
        String methodA = dubboAService.methodA("consumer" + new Date());
        System.out.println( methodA ); // 显示调用结果
    }


}
