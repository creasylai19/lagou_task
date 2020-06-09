package com.creasy.email.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RefreshScope
public class EmailController {

//    @Autowired
//    private IEmailService iEmailService;
//
//    @Value("${spring.mail.username}")
//    private String emailFrom;
//
//    @RequestMapping("/{email}/{code}")
//    public boolean sendCodeToEmail(@PathVariable("email") String email, @PathVariable("code") String code){
//        return iEmailService.sendEmail(new Email(email, emailFrom, Email.DEFAULT_SUBJECT, Email.DEFAULT_CONTENT+code));
//    }

    @Value("${email.test}")
    private String emailTest;

    @RequestMapping("info")
    public String getInfo(){
        return emailTest;
    }

}
