-- 脚本开始
DROP DATABASE
IF
    EXISTS `demo_xuehai`;

CREATE DATABASE `demo_xuehai` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `demo_xuehai`;
-- 表
-- 用户表
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR ( 64 ) NOT NULL UNIQUE,
    `password` VARCHAR ( 64 ) NOT NULL,
    `salt` VARCHAR ( 32 ) NOT NULL,
    `email` VARCHAR ( 64 ) NOT NULL UNIQUE,
    `authority` SMALLINT NOT NULL DEFAULT 0,
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `sex` TINYINT,
    `profile` VARCHAR ( 256 ),
    `school` VARCHAR ( 32 ),
    `major` VARCHAR ( 32 ) 
);
-- 问题类型表
CREATE TABLE `question_type` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR ( 10 ) NOT NULL UNIQUE,
    `parent` BIGINT,
    FOREIGN KEY ( `parent` ) REFERENCES `question_type` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE 
);
-- 问题表
CREATE TABLE `question` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user` BIGINT,
    `title` VARCHAR ( 64 ) NOT NULL,
    `content` TEXT NOT NULL,
    `type` BIGINT,
    `answer_num` INT NOT NULL DEFAULT 0,
    `collection_num` INT NOT NULL DEFAULT 0,
    `time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY ( `user` ) REFERENCES `user` ( `id` ) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY ( `type` ) REFERENCES `question_type` ( `id` ) ON DELETE SET NULL ON UPDATE CASCADE 
);
-- 回答表
CREATE TABLE `answer` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `content` text NOT NULL,
    `user` BIGINT,
    `like_num` INT NOT NULL DEFAULT 0,
    `comment_num` INT NOT NULL DEFAULT 0,
    `question` BIGINT NOT NULL,
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY ( `user` ) REFERENCES `user` ( `id` ) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY ( `question` ) REFERENCES `question` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE 
);
-- 评论表
CREATE TABLE `comment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `content` text NOT NULL,
    `user` BIGINT,
    `answer` BIGINT NOT NULL,
    `parent` BIGINT,
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY ( `user` ) REFERENCES `user` ( `id` ) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY ( `answer` ) REFERENCES `answer` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY ( `parent` ) REFERENCES `comment` ( `id` ) ON DELETE SET NULL ON UPDATE CASCADE 
);
-- 收藏表（问题与用户的关系）
CREATE TABLE `collection` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user` BIGINT NOT NULL,
    `question` BIGINT NOT NULL,
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY ( `user` ) REFERENCES `user` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY ( `question` ) REFERENCES `question` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE ( `user`, `question` ) 
);
-- 点赞表（回答与用户的关系）
CREATE TABLE `like` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user` BIGINT NOT NULL,
    `answer` BIGINT NOT NULL,
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY ( `user` ) REFERENCES `user` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY ( `answer` ) REFERENCES `answer` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE ( `user`, `answer` ) 
);
-- 关注表（用户与用户的关系）
CREATE TABLE `follow` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_from` BIGINT NOT NULL,
    `user_to` BIGINT NOT NULL,
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY ( `user_from` ) REFERENCES `user` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY ( `user_to` ) REFERENCES `user` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE ( `user_from`, `user_to` ) 
);
-- 消息表（可能不需要）
CREATE TABLE `message` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user` BIGINT NOT NULL,
    `content_type` SMALLINT NOT NULL,
    `content_id` BIGINT NOT NULL,
    `time` TIMESTAMP NOT NULL,
    FOREIGN KEY ( `user` ) REFERENCES `user` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE 
);

-- 视图
-- 关注数
CREATE VIEW `following` AS 
    SELECT
        `a`.`id` AS `id`,
        COUNT( * ) AS `num` 
    FROM
        ( `user` `a` JOIN `follow` `b` ON ( ( `a`.`id` = `b`.`user_from` ) ) ) 
    GROUP BY
        `a`.`id`;
-- 被关注数
CREATE VIEW `follower` AS 
    SELECT
        `a`.`id` AS `id`,
        COUNT( * ) AS `num` 
    FROM
        ( `user` `a` JOIN `follow` `b` ON ( ( `a`.`id` = `b`.`user_to` ) ) ) 
    GROUP BY
        `a`.`id`;
-- 问题收藏数
CREATE VIEW `collection_num` AS 
    SELECT
        `a`.`id` AS `id`,
        COUNT( * ) AS `num` 
    FROM
        ( `question` `a` JOIN `collection` `b` ON ( ( `a`.`id` = `b`.`question` ) ) ) 
    GROUP BY
        `a`.`id`;
-- 问题回答数
CREATE VIEW `answer_num` AS
    SELECT
        `a`.`id` AS `id`,
        COUNT( * ) AS `num` 
    FROM
        ( `question` `a` JOIN `answer` `b` ON ( ( `a`.`id` = `b`.`question` ) ) ) 
    GROUP BY
        `a`.`id`;
-- 回答点赞数
CREATE VIEW `like_num` AS 
    SELECT
        `a`.`id` AS `id`,
        COUNT( * ) AS `num` 
    FROM
        ( `answer` `a` JOIN `like` `b` ON ( ( `a`.`id` = `b`.`answer` ) ) ) 
    GROUP BY
        `a`.`id`;
-- 评论数
CREATE VIEW `comment_num` AS 
    SELECT
        `a`.`id` AS `id`,
        COUNT( * ) AS `num` 
    FROM
        ( `answer` `a` JOIN `comment` `b` ON ( ( `a`.`id` = `b`.`answer` ) ) ) 
    GROUP BY
        `a`.`id`;

-- 脚本结束