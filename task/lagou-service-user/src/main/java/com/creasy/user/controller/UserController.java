package com.creasy.user.controller;

import com.creasy.pojo.LagouMessage;
import com.creasy.pojo.StatusCode;
import com.creasy.user.feignclients.ICodeClient;
import com.creasy.user.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ITokenService iTokenService;
    @Autowired
    private ICodeClient ICodeClient;

    @RequestMapping("/register/{email}/{password}/{code}")
    public LagouMessage register(@PathVariable("email") String email,
                                 @PathVariable("password") String password,
                                 @PathVariable("code") String code,
                                 HttpServletRequest request, HttpServletResponse response) {
        int integer = ICodeClient.validateCode(email, code);
        if( integer == StatusCode.ERROR.value() ){
            return new LagouMessage(integer, "验证码错误");
        }
        if( integer == StatusCode.EXPIRE.value()  ) {
            return new LagouMessage(integer, StatusCode.EXPIRE.desc());
        }
        if(iTokenService.isRegistered(email)){
            return new LagouMessage(StatusCode.HAS_REGISTER.value(), StatusCode.HAS_REGISTER.desc());
        }
        iTokenService.register(email, password, code);
        String token = iTokenService.getTokenByEmail(email);
        Cookie cookie = new Cookie("token", token);
//        cookie.setMaxAge(30*60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new LagouMessage(integer, StatusCode.CORRECT.desc());
    }

    @RequestMapping("/isRegistered/{email}")
    public boolean isRegistered(@PathVariable("email") String email){
        return iTokenService.isRegistered(email);
    }

    @RequestMapping("/login/{email}/{password}")
    public LagouMessage login(@PathVariable("email") String email,
                         @PathVariable("password") String password,
                         HttpServletRequest request, HttpServletResponse response){
        if(!iTokenService.login(email, password)){
            return new LagouMessage(StatusCode.ERROR.value(), "用户名或密码错误");
        }
        String token = iTokenService.getTokenByEmail(email);
        Cookie cookie = new Cookie("token", token);
//        cookie.setMaxAge(30*60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new LagouMessage(StatusCode.CORRECT.value(), StatusCode.CORRECT.desc());
    }

    @RequestMapping("/info/{token}")
    public LagouMessage info(@PathVariable("token") String token){
        String email = iTokenService.getEmailByToken(token);
        return new LagouMessage(StatusCode.CORRECT.value(), StatusCode.CORRECT.desc(), email);
    }
}
