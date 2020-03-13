package com.creasy.blog.mapper;

import com.creasy.blog.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface ArticleMapper {

    public List<Article> getAllArticle(RowBounds rowBounds);

}
