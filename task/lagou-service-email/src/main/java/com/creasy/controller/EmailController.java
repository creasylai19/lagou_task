package com.creasy.controller;

import com.creasy.pojo.Email;
import com.creasy.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private IEmailService iEmailService;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @RequestMapping("/{email}/{code}")
    public boolean sendCodeToEmail(@PathVariable("email") String email, @PathVariable("code") String code){
        return iEmailService.sendEmail(new Email(email, emailFrom, Email.DEFAULT_SUBJECT, Email.DEFAULT_CONTENT+code));
    }

}
