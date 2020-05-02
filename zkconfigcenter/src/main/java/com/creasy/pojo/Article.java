package com.creasy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    private int id;//` int(11) NOT NULL AUTO_INCREMENT,
    private String title;//` varchar(50) NOT NULL COMMENT '文章标题',

}
