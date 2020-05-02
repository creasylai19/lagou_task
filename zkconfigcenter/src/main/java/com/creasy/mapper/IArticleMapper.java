package com.creasy.mapper;

import com.creasy.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IArticleMapper {

    @Select("select * from t_article where id = #{id}")
    Article queryArticle(int id);
}
