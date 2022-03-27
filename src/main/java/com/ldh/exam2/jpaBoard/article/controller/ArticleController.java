package com.ldh.exam2.jpaBoard.article.controller;

import com.ldh.exam2.jpaBoard.article.dao.ArticleRepository;
import com.ldh.exam2.jpaBoard.article.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("user/article")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @RequestMapping("/list")
    @ResponseBody
    public List<Article> articles() {
        return articleRepository.findAll();
    }

    @RequestMapping("1")
    @ResponseBody
    public Article article() {
        Article article = new Article();
        return article;
    }
}
