package com.creasy.code.controller;

import com.creasy.code.service.IAuthCodeService;
import com.creasy.email.IEmailService;
import com.creasy.pojo.Email;
import com.creasy.pojo.LagouAuthCode;
import com.creasy.pojo.StatusCode;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/code")
public class CodeController {

    @Autowired
    private IAuthCodeService iCodeService;
    @Reference
    private IEmailService iEmailService;

    @RequestMapping("/validate/{email}/{code}")
    public Integer validateCode(@PathVariable("email") String email, @PathVariable("code") String code ){
        LagouAuthCode lagouAuthCodeByEmail = iCodeService.getLagouAuthCodeByEmailAndCode(email, code);
        if( null == lagouAuthCodeByEmail || !lagouAuthCodeByEmail.getCode().equals(code) ){
            return StatusCode.ERROR.value();
        }
        if ( new Date().after(lagouAuthCodeByEmail.getExpiretime())){
            return StatusCode.EXPIRE.value();
        }
        return StatusCode.CORRECT.value();
    }

    @RequestMapping("/create/{email}")
    public boolean createCode(@PathVariable("email") String email){
        LagouAuthCode lagouAuthCode = iCodeService.saveAuthCode(email);
        return iEmailService.sendEmail(new Email(email, "", Email.DEFAULT_SUBJECT, Email.DEFAULT_CONTENT+lagouAuthCode.getCode()));
    }

}
