package com.creasy.blog.controller;

import com.creasy.blog.config.Constants;
import com.creasy.blog.pojo.Article;
import com.creasy.blog.pojo.ShowPageInfo;
import com.creasy.blog.service.IArticleService;
import com.creasy.blog.util.PageUtils;
import com.github.pagehelper.PageRowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private IArticleService iArticleService;

    @RequestMapping("/index")
    public String index(Integer pageNum, Model model) {
        if( null == pageNum || pageNum <= 0) {
            pageNum = 1;
        }
        PageRowBounds rowBounds = new PageRowBounds((pageNum-1)*Constants.PAGE_SIZE, Constants.PAGE_SIZE);
        List<Article> allArticle = iArticleService.getAllArticle(rowBounds);
        model.addAttribute("allArticle", allArticle);

        ShowPageInfo showPageInfo = PageUtils.getShowPageInfo(pageNum, rowBounds.getTotal());
        model.addAttribute("showPageInfo", showPageInfo);
//        allArticle.forEach(System.out::println);
        return "index";
    }

    @RequestMapping("/")
    public String root(Integer pageNum, Model model) {
        return index(pageNum, model);
    }

}
