package com.creasy.controller;

import com.creasy.service.DubboAService;
import com.creasy.service.DubboBService;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

    @Reference
    private DubboAService dubboAService;
    @Reference
    private DubboBService dubboBService;

    @RequestMapping("/invokeA")
    public String getInfoFromDubboA(@RequestParam(value = "arg", required = false) String arg){
        return dubboAService.methodA(arg);
    }

    @RequestMapping("/invokeB")
    public String getInfoFromDubboB(@RequestParam(value = "arg", required = false) String arg){
        return dubboBService.methodB(arg);
    }

}
