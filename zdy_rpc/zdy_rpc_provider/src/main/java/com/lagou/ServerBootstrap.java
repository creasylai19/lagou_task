package com.lagou;

import com.alibaba.fastjson.JSON;
import com.lagou.pojo.ServerInfo;
import com.lagou.service.UserServiceImpl;
import com.lagou.utils.Constans;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Date;

import static com.lagou.utils.Constans.Zookeeper;

@SpringBootApplication
public class ServerBootstrap implements ApplicationContextAware {

    public static ApplicationContext applicationContext;
    public static final int NETTY_PORT = 8990;
    public static CuratorFramework client;

    static {
        try {
            int netty_port = NETTY_PORT;
            String netty_port_str = System.getProperty("netty_port");
            if( null != netty_port_str ){
                netty_port = Integer.parseInt(netty_port_str);
            }
            UserServiceImpl.startServer(Zookeeper.INET_ADDRESS,netty_port);
            //----------
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            //默认的session时间为60秒，连接超时时间为15秒
            client = CuratorFrameworkFactory.newClient(Zookeeper.INET_ADDRESS+":"+Zookeeper.ZOOKEEPER_PORT, retryPolicy);
            client.start();
            ServerInfo serverInfo = new ServerInfo(new Date(), 0, Zookeeper.INET_ADDRESS+":"+netty_port);
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground()
                    .forPath(Constans.Zookeeper.PREFIX +"/"+Zookeeper.INET_ADDRESS+":"+netty_port, JSON.toJSON(serverInfo).toString().getBytes());
//            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        /**
         * /SERVERS
         *   /HOST1:{lastInvokeTime:yyyy-mm-dd HH24:mi:ss,lastInvokeTime:milliseconds}
         *   /HOST1:{lastInvokeTime:yyyy-mm-dd HH24:mi:ss,lastInvokeTime:milliseconds}
         */
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ServerBootstrap.class, args);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServerBootstrap.applicationContext = applicationContext;
    }
}
