package com.creasy.zkconfigcenter;

import com.alibaba.fastjson.JSON;
import com.creasy.pojo.MySQLConfig;
import org.junit.jupiter.api.Test;

public class TestJson {

    @Test
    public void printJson(){
        MySQLConfig mySQLConfig = new MySQLConfig();
        mySQLConfig.setUrl("jdbc:mysql://localhost:3306/blog_system");
        mySQLConfig.setUsername("root");
        mySQLConfig.setPassword("aq123456");
        mySQLConfig.setMaxActive(10);
        System.out.println(JSON.toJSONString(mySQLConfig));
    }

}
