package com.creasy.blog.service;

import com.creasy.blog.pojo.Article;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface IArticleService {

    public List<Article> getAllArticle(RowBounds rowBounds);

}
