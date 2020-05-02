package com.creasy.service.impl;

import com.creasy.mapper.IArticleMapper;
import com.creasy.pojo.Article;
import com.creasy.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService implements IArticleService {

    @Autowired
    private IArticleMapper iArticleMapper;

    @Override
    public Article queryArticle(int id) {
        return iArticleMapper.queryArticle(id);
    }
}
