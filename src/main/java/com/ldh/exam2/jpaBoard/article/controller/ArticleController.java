package com.ldh.exam2.jpaBoard.article.controller;

import com.ldh.exam2.jpaBoard.article.dao.ArticleRepository;
import com.ldh.exam2.jpaBoard.article.domain.Article;
import com.ldh.exam2.jpaBoard.user.dao.UserRepository;
import com.ldh.exam2.jpaBoard.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/menu/article")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    // 게시글 작성페이지 보기
    @RequestMapping("write")
    private String showWrite() {
        return "menu/article/write";
    }

    // 게시글 등록하기
    @RequestMapping("doWrite")
    @ResponseBody
    private String doWrite(String title, String body) {

        if (title == null || title.trim().length() == 0) {
            return "제목을 입력해주세요.";
        }
        title = title.trim();

        if (body == null || body.trim().length() == 0) {
            return "내용을 입력해주세요.";
        }
        body = body.trim();

        Article article = new Article();
        article.setRegDate(LocalDateTime.now());
        article.setUpdateDate(LocalDateTime.now());
        article.setTitle(title);
        article.setBody(body);

        // 게시글 작성자 표시
        User user = userRepository.findById(1L).get();
        article.setUser(user);

        articleRepository.save(article);
        return """
                <script>
                alert('%d번 게시물이 생성되었습니다.');
                location.replace('showList');
                </script>
                """.formatted(article.getId());
    }

    // 게시글 목록 보기
    @RequestMapping("showList")
    private String showList(Model model) {

        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);

        return "menu/article/list";
    }

    // 게시글 상세 보기
    @RequestMapping("detail")
    private String showDetail(long id, Model model) {

        Optional<Article> optionalArticle = articleRepository.findById(id);
        Article article = optionalArticle.get();

        model.addAttribute("article", article);

        return "menu/article/detail";
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
