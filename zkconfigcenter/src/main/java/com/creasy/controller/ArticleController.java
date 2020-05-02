package com.creasy.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.creasy.pojo.Article;
import com.creasy.pojo.MySQLConfig;
import com.creasy.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private IArticleService iArticleService;


    @RequestMapping("/sqlconfig")
    public MySQLConfig getMySQLConfig(){
        MySQLConfig mySQLConfig = new MySQLConfig();
        if( dataSource instanceof DruidDataSource ){
            mySQLConfig.setUrl(((DruidDataSource) dataSource).getUrl());
            mySQLConfig.setUsername(((DruidDataSource) dataSource).getUsername());
            mySQLConfig.setPassword(((DruidDataSource) dataSource).getPassword());
            mySQLConfig.setMaxActive(((DruidDataSource) dataSource).getMaxActive());
        }
        return mySQLConfig;
    }

    @RequestMapping("/id/{id}")
    public Article queryArticle( @PathVariable("id") Integer id ){
        if( null != id ){
            return iArticleService.queryArticle(id);
        }
        return null;
    }

}
