package com.creasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication8761 {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EurekaApplication8761.class, args);
    }

}
