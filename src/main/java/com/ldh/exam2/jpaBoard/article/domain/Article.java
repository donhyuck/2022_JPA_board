package com.ldh.exam2.jpaBoard.article.domain;

import com.ldh.exam2.jpaBoard.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private String title;
    private String body;
    @ManyToOne
    private User user;
}
