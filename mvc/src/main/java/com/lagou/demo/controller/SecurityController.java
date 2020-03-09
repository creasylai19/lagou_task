package com.lagou.demo.controller;

import com.lagou.demo.service.IDemoService;
import com.lagou.edu.mvcframework.annotations.LagouAutowired;
import com.lagou.edu.mvcframework.annotations.LagouController;
import com.lagou.edu.mvcframework.annotations.LagouRequestMapping;
import com.lagou.edu.mvcframework.annotations.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Security({"lisi"})
@LagouController
@LagouRequestMapping("/security")
public class SecurityController {

    @LagouAutowired
    private IDemoService demoService;


    /**
     * 在类中配置的用户均可访问，即lisi可访问
     * @param request
     * @param response
     * @param name
     * @return
     */
    @LagouRequestMapping("/handler01")
    public String handler01(HttpServletRequest request, HttpServletResponse response, String name) {
        return demoService.get(name);
    }

    /**
     * 合并处理，即zhangsan,lisi均可访问
     * @param request
     * @param response
     * @param name
     * @return
     */
    @LagouRequestMapping("/handler02")
    @Security({"zhangsan", "lisi"})
    public String handler02(HttpServletRequest request, HttpServletResponse response, String name) {
        return demoService.get(name);
    }

    /**
     * 合并处理，即zhangsan,lisi均可访问
     * @param request
     * @param response
     * @param name
     * @return
     */
    @LagouRequestMapping("/handler03")
    @Security({"zhangsan"})
    public String handler03(HttpServletRequest request, HttpServletResponse response, String name) {
        return demoService.get(name);
    }

}
