package com.creasy.blog.service.impl;

import com.creasy.blog.mapper.ArticleMapper;
import com.creasy.blog.pojo.Article;
import com.creasy.blog.service.IArticleService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("articleService")
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Article> getAllArticle(RowBounds rowBounds) {
        return articleMapper.getAllArticle(rowBounds);
    }

}
