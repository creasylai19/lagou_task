package com.lagou.edu.controller;

import com.lagou.edu.pojo.User;
import com.lagou.edu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request, User user) {
        ModelAndView modelAndView = new ModelAndView();
        if(userService.login(user)) {
            request.getSession().setAttribute("login", true);
            modelAndView.setViewName("redirect:/resume/list");
        } else {
            request.setAttribute("message", "登录失败，请重试");
            modelAndView.setViewName("forward:" + request.getContextPath() + "/index.jsp");
        }
        return modelAndView;
    }

}
