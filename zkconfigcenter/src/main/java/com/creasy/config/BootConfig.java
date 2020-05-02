package com.creasy.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.creasy.pojo.MySQLConfig;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class BootConfig implements ApplicationContextAware {

    public static final Logger LOGGER = LoggerFactory.getLogger(BootConfig.class);
    public static final String MYSQL_CONFIG_PATH = "/mysql/config";

//    @Autowired
//    public CuratorFramework curatorFrameworkClient;

    @Bean
    public CuratorFramework curatorFrameworkClient(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //默认的session时间为60秒，连接超时时间为15秒
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();
        return client;
    }

    @Bean
    public DataSource dataSource(CuratorFramework client){
        DruidDataSource dataSource = new DruidDataSource();
        try {
            byte[] bytes = client.getData().usingWatcher(new CuratorWatcher() {
                @Override
                public void process(WatchedEvent event) throws Exception {
                    if(event.getType() == Watcher.Event.EventType.NodeDataChanged){
                        byte[] bytes1 = client.getData().usingWatcher(this).forPath(MYSQL_CONFIG_PATH);
                        MySQLConfig mySQLConfig = JSON.toJavaObject(JSON.parseObject(new String(bytes1)), MySQLConfig.class);
                        updateDataSource(dataSource, mySQLConfig);
                    }
                }
            }).forPath(MYSQL_CONFIG_PATH);
            MySQLConfig mySQLConfig = JSON.toJavaObject(JSON.parseObject(new String(bytes)), MySQLConfig.class);
            updateDataSource(dataSource, mySQLConfig);
        } catch (Exception e) {
            LOGGER.error("获取配置信息异常", e);
            throw new RuntimeException("获取配置信息异常");
        }
//        dataSource.setUrl("jdbc:mysql://localhost:3306/blog_system");
//        dataSource.setUsername("root");
//        dataSource.setPassword("aq123456");
//        dataSource.setMaxActive(10);

        return dataSource;
    }

    private void updateDataSource(DruidDataSource dataSource, MySQLConfig mySQLConfig) {
        try {
            dataSource.restart();
        } catch (SQLException e) {
            LOGGER.error("重启数据库连接池异常", e);
        }
        dataSource.setUrl(mySQLConfig.getUrl());
        dataSource.setUsername(mySQLConfig.getUsername());
        dataSource.setPassword(mySQLConfig.getPassword());
        dataSource.setMaxActive(mySQLConfig.getMaxActive());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LOGGER.debug("setApplicationContext");
    }
}
