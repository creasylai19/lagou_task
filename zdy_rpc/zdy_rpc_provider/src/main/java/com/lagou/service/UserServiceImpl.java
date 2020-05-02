package com.lagou.service;

import com.alibaba.fastjson.JSON;
import com.lagou.handler.UserServerHandler;
import com.lagou.pojo.RpcRequest;
import com.lagou.pojo.ServerInfo;
import com.lagou.utils.Constans;
import com.lagou.utils.JSONSerializer;
import com.lagou.utils.RpcDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    public static int netty_port = 0;

    public String sayHello(String word) {
        System.out.println("调用成功--参数 "+word);
        int sleepTime = new Random().nextInt(100);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            ServerInfo serverInfo = new ServerInfo(new Date(), sleepTime, Constans.Zookeeper.INET_ADDRESS+":"+netty_port);
            com.lagou.ServerBootstrap.client.setData().forPath(Constans.Zookeeper.PREFIX +"/"+ Constans.Zookeeper.INET_ADDRESS+":"+netty_port,
                    JSON.toJSON(serverInfo).toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "netty_port:"+netty_port;
    }

    //hostName:ip地址  port:端口号
    public static void startServer(String hostName,int port) throws InterruptedException {
        netty_port = port;
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast( new RpcEncoder(RpcRequest.class, new JSONSerializer()));
                        pipeline.addLast( new RpcDecoder(RpcRequest.class, new JSONSerializer()));
                        pipeline.addLast(new StringEncoder());
//                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new UserServerHandler());
//                        pipeline.addLast(new ReadTimeoutHandler(Constans.Other.MILLISECONDS, TimeUnit.MILLISECONDS));//读取超时 在设置时间内没有读取操作
//                        pipeline.addLast(new WriteTimeoutHandler(Constans.Other.MILLISECONDS,TimeUnit.MILLISECONDS));//写入超时 在设置时间内没有写入操作

                    }
                });
        serverBootstrap.bind(hostName,port).sync();


    }

}
