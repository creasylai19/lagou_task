package com.lagou;

import com.lagou.service.UserServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SpringBootApplication
public class ServerBootstrap implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    static {
        try {
            UserServiceImpl.startServer("127.0.0.1",8990);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ServerBootstrap.class, args);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServerBootstrap.applicationContext = applicationContext;
    }
}
