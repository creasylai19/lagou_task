package com.lagou.edu.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object login = session.getAttribute("login");
        if(null != login && ((boolean)login)) {
            return true;
        }
        request.setAttribute("message", "您未登录，请先登录");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
        return false;
    }
}
