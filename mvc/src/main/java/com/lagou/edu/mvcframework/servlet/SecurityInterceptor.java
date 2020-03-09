package com.lagou.edu.mvcframework.servlet;

import com.lagou.edu.mvcframework.pojo.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ( handler instanceof Handler) {
            ((Handler) handler).getMethod();
        }
        return false;
    }
}
