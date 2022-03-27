package com.ldh.exam2.jpaBoard.article.controller;

import com.ldh.exam2.jpaBoard.article.dao.ArticleRepository;
import com.ldh.exam2.jpaBoard.article.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("user/article")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @RequestMapping("showList")
    @ResponseBody
    public List<Article> showList() {
        return articleRepository.findAll();
    }

    @RequestMapping("1")
    @ResponseBody
    public Article article() {
        Article article = new Article();
        return article;
    }

    @RequestMapping("showDetail")
    @ResponseBody
    public Article showDetail(long id) {
        Optional<Article> article = articleRepository.findById(id);
        return article.get();
    }

    @RequestMapping("doDelete")
    @ResponseBody
    public String doDelete(long id) {

        if (articleRepository.existsById(id) == false) {
            return "%d번 게시글을 찾을 수 없습니다.".formatted(id);
        }

        articleRepository.deleteById(id);
        return "%d번 게시글이 삭제되었습니다.".formatted(id);
    }

    @RequestMapping("doModify")
    @ResponseBody
    public Article doModify(long id, String title, String body) {
        
        Article article = articleRepository.findById(id).get();

        if (title != null) {
            article.setTitle(title);
        }

        if (body != null) {
            article.setBody(body);
        }

        article.setUpdateDate(LocalDateTime.now());

        articleRepository.save(article);

        return article;
    }
}
