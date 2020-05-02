package com.lagou.client;

import com.lagou.pojo.RpcRequest;
import com.lagou.service.UserService;
import com.lagou.utils.Constans;
import com.lagou.utils.JSONSerializer;
import com.lagou.utils.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.concurrent.EventExecutorGroup;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientBootStrap {

    public static final Map<String, UserClientHandler> SERVICES = new ConcurrentHashMap<>();
    public static final List<EventLoopGroup> eventLoopGroups = new ArrayList<>();
    public static CuratorFramework client;

    public static void main(String[] args) throws Exception {
        initClient();
        UserService proxy = (UserService) RpcConsumer.createProxy(UserService.class);

        while (true){
            Thread.sleep(2000);
            String result = proxy.sayHello("are you ok?");
            System.out.println(new Date() + "--->" + result);
//            System.out.println("----");
        }


    }

    //2.初始化netty客户端
    public static  void initClient() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //默认的session时间为60秒，连接超时时间为15秒
        client = CuratorFrameworkFactory.newClient(Constans.Zookeeper.INET_ADDRESS+":"+ Constans.Zookeeper.ZOOKEEPER_PORT, retryPolicy);
        client.start();

        List<String> strings = client.getChildren().usingWatcher(
                new CuratorWatcher() {
                    @Override
                    public void process(WatchedEvent event) throws Exception {
                        if( event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                            handleChildrenChanged();
                            client.getChildren().usingWatcher(this).forPath(Constans.Zookeeper.PREFIX);
                        }
                    }
                }
        ).forPath(Constans.Zookeeper.PREFIX);
        updateServices(strings);


    }

    private static void updateServices(List<String> strings) throws InterruptedException {

        SERVICES.clear();
        eventLoopGroups.forEach(EventExecutorGroup::shutdownGracefully);

        for (String str : strings) {
            UserClientHandler userClientHandler = new UserClientHandler();
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new RpcEncoder(RpcRequest.class, new JSONSerializer()));
//                        pipeline.addLast( new RpcDecoder(RpcRequest.class, new JSONSerializer()));
                            pipeline.addLast(userClientHandler);
//                            pipeline.addLast(new ReadTimeoutHandler(Constans.Other.MILLISECONDS, TimeUnit.MILLISECONDS));//读取超时 在设置时间内没有读取操作
//                            pipeline.addLast(new WriteTimeoutHandler(Constans.Other.MILLISECONDS,TimeUnit.MILLISECONDS));//写入超时 在设置时间内没有写入操作
                        }
                    });
            String[] serverInfo = str.split(":");
            bootstrap.connect(serverInfo[0], Integer.parseInt(serverInfo[1])).sync();
            SERVICES.put(Constans.Zookeeper.PREFIX+"/"+str, userClientHandler);
            eventLoopGroups.add(group);
        }
    }

    private static void handleChildrenChanged() throws Exception {
        List<String> strings = client.getChildren().forPath(Constans.Zookeeper.PREFIX);
        updateServices(strings);
    }


}
