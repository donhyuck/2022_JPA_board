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
@RequestMapping("/user/article")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    // 게시글 목록 보기
    @RequestMapping("showList")
    @ResponseBody
    private List<Article> showList() {
        return articleRepository.findAll();
    }

    // 게시글 상세 보기
    @RequestMapping("detail")
    @ResponseBody
    private Article showDetail(long id) {
        Optional<Article> article = articleRepository.findById(id);
        return article.get();
    }

    // 게시글 수정하기
    @RequestMapping("doModify")
    @ResponseBody
    private Article doModify(long id, String title, String body) {

        // 수정할 게시글 가져오기
        Article article = articleRepository.findById(id).get();

        // 수정하기
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

    // 게시글 삭제 보기
    @RequestMapping("doDelete")
    @ResponseBody
    private String doDelete(long id) {

        if (articleRepository.existsById(id) == false) {
            return "%d번 게시글을 찾을 수 없습니다.".formatted(id);
        }

        articleRepository.deleteById(id);
        return "%d번 게시글이 삭제되었습니다.".formatted(id);
    }

}
