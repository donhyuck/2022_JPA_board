# DB 생성
DROP DATABASE IF EXISTS jpaBoard;
CREATE DATABASE jpaBoard;
USE jpaBoard;

# 테이블 삭제
DROP TABLE `user`;
DROP TABLE `article`;

# 회원 테이블 생성
CREATE TABLE `user` (
    id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    reg_date DATETIME NOT NULL,
    update_date DATETIME NOT NULL,
    email VARCHAR(100) NOT NULL,
    `password` VARCHAR(150) NOT NULL,
    `name` CHAR(50) NOT NULL
);

# 회원데이터 추가
INSERT INTO `user`
SET reg_date = NOW(),
update_date = NOW(),
email = 'use1@test.com',
`password` = '1234',
`name` = '유저1';

INSERT INTO `user`
SET reg_date = NOW(),
update_date = NOW(),
email = 'use2@test.com',
`password` = '1234',
`name` = '유저2';

INSERT INTO `user`
SET reg_date = NOW(),
update_date = NOW(),
email = 'use3@test.com',
`password` = '1234',
`name` = '유저3';

SELECT * FROM `user`;

# 게시글 테이블 생성
CREATE TABLE `article` (
    id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    reg_date DATETIME NOT NULL,
    update_date DATETIME NOT NULL,
    title VARCHAR(30) NOT NULL,
    `body` TEXT NOT NULL,
    `user_id` BIGINT UNSIGNED NOT NULL
);

# 게시글 데이터 추가
INSERT INTO `article`
SET reg_date = NOW(),
update_date = NOW(),
title = 'test1',
`body` = 'content1',
`user_id` = 1;

INSERT INTO `article`
SET reg_date = NOW(),
update_date = NOW(),
title = 'test2',
`body` = 'content2',
`user_id` = 2;

INSERT INTO `article`
SET reg_date = NOW(),
update_date = NOW(),
title = 'test3',
`body` = 'content3',
`user_id` = 3;

SELECT * FROM `article`;