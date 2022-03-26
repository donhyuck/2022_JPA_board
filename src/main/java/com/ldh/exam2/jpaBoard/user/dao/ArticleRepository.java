package com.ldh.exam2.jpaBoard.user.dao;

import com.ldh.exam2.jpaBoard.user.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
