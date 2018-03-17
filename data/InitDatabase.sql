DROP DATABASE
IF
	EXISTS `demo_Xuehai`;
CREATE DATABASE `demo` DEFAULT CHARACTER 
SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `demo`;
CREATE TABLE `user` (
`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
`username` VARCHAR ( 64 ) NOT NULL UNIQUE,
`password` VARCHAR ( 64 ) NOT NULL,
`salt` VARCHAR ( 64 ) NOT NULL,
`email` VARCHAR ( 64 ) NOT NULL UNIQUE,
`authority` SMALLINT NOT NULL,
`time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
`modified_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
`sex` TINYINT,
`profile` VARCHAR ( 256 ),
`school` VARCHAR ( 32 ),
`major` VARCHAR ( 32 ) 
);
CREATE TABLE `question_type` (
`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR ( 10 ) NOT NULL UNIQUE,
`parent` BIGINT,
FOREIGN KEY ( `parent` ) REFERENCES `question_type` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE 
);
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
CREATE TABLE `comment` (
`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
`content` text NOT NULL,
`user` BIGINT,
`answer` BIGINT NOT NULL,
`parent` BIGINT,
`time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY ( `user` ) REFERENCES `user` ( `id` ) ON DELETE 
SET NULL ON UPDATE CASCADE,
FOREIGN KEY ( `answer` ) REFERENCES `answer` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY ( `parent` ) REFERENCES `comment` ( `id` ) ON DELETE SET NULL ON UPDATE CASCADE 
);
CREATE TABLE `collection` (
`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
`user` BIGINT NOT NULL,
`question` BIGINT NOT NULL,
`time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY ( `user` ) REFERENCES `user` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY ( `question` ) REFERENCES `question` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
UNIQUE ( `user`, `question` ) 
);
CREATE TABLE `like` (
`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
`user` BIGINT NOT NULL,
`answer` BIGINT NOT NULL,
`time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY ( `user` ) REFERENCES `user` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY ( `answer` ) REFERENCES `answer` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
UNIQUE ( `user`, `answer` ) 
);
CREATE TABLE `follow` (
`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
`user_from` BIGINT NOT NULL,
`user_to` BIGINT NOT NULL,
`time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY ( `user_from` ) REFERENCES `user` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY ( `user_to` ) REFERENCES `user` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE,
UNIQUE ( `user_from`, `user_to` ) 
);
CREATE TABLE `message` (
`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
`user` BIGINT NOT NULL,
`content_type` SMALLINT NOT NULL,
`content_id` BIGINT NOT NULL,
`time` TIMESTAMP NOT NULL,
FOREIGN KEY ( `user` ) REFERENCES `user` ( `id` ) ON DELETE CASCADE ON UPDATE CASCADE 
);