package com.creasy;

import server.*;

import java.io.IOException;

public class DemoServlet extends HttpServlet {

    @Override
    public void doGet(Request request, Response response) {


//        try {
//            Thread.sleep(100000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        String servletName = LagouServlet.class.getName();
        String content = "<h1>Demo2-->doGet.Class:" + servletName + ".Request URL:" + request.getUrl() + " </h1>";
        try {
            response.output((HttpProtocolUtil.getHttpHeader200(content.getBytes().length) + content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String servletName = LagouServlet.class.getName();
        String content = "<h1>Demo2-->doPost.Class:" + servletName + ".Request URL:" + request.getUrl() + " </h1>";
        try {
            response.output((HttpProtocolUtil.getHttpHeader200(content.getBytes().length) + content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destory() throws Exception {

    }

}
