package server;

import server.mapper.Context;
import server.mapper.Host;
import server.mapper.Wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class RequestProcessor extends Thread {

    private Socket socket;
    private Host host;

    public RequestProcessor(Socket socket, Host host) {
        this.socket = socket;
        this.host = host;
    }

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            String url = request.getUrl();
            if( url.startsWith("/") ) {
                url = url.substring(1);
            }
            int index = url.indexOf("/");
            String docBase = url.substring(0, index);
            String urlPattern = url.substring(index);
            Context context = host.getContext(docBase);
            Wrapper wrapper = context.getWrapper(urlPattern);
            // 静态资源处理
            if(wrapper == null) {
                response.outputHtml(host.getAppBase() + url.replaceFirst(docBase, context.getPath()));
            }else{
                // 动态资源servlet请求
                wrapper.getServlet().service(request,response);
            }

            socket.close();

        }catch (Exception e) {
            try {
                socket.getOutputStream().write(HttpProtocolUtil.getHttpHeader404().getBytes());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

    }
}
