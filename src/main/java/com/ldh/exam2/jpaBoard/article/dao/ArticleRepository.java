package com.ldh.exam2.jpaBoard.article.dao;

import com.ldh.exam2.jpaBoard.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
