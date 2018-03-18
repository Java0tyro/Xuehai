-- 脚本开始



CREATE DATABASE IF NOT EXISTS `demo_xuehai` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `demo_xuehai`;



-- --------------------------------------------------------------------------
-- 表


-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR ( 64 ) NOT NULL UNIQUE,
    `password` VARCHAR ( 64 ) NOT NULL,
    `salt` VARCHAR ( 32 ) NOT NULL,
    `email` VARCHAR ( 64 ) NOT NULL UNIQUE,
    `authority` SMALLINT NOT NULL DEFAULT 0,
		`follower` INT NOT NULL DEFAULT 0,
		`following` INT NOT NULL DEFAULT 0,
		`question_num` INT NOT NULL DEFAULT 0,
		`answer_num` INT NOT NULL DEFAULT 0,
		`collection_num` INT NOT NULL DEFAULT 0,
		`like_num` INT NOT NULL DEFAULT 0,
		`comment_num` INT NOT NULL DEFAULT 0,
    `sex` TINYINT,
    `profile` VARCHAR ( 256 ),
    `school` VARCHAR ( 32 ),
    `major` VARCHAR ( 32 ),
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- 问题类型表
DROP TABLE IF EXISTS question_type;
CREATE TABLE question_type (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR ( 10 ) NOT NULL UNIQUE,
    `parent` BIGINT
);


-- 问题表
DROP TABLE IF EXISTS question;
CREATE TABLE question (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user` BIGINT,
    `title` VARCHAR ( 64 ) NOT NULL,
    `content` TEXT NOT NULL,
    `type` BIGINT,
    `answer_num` INT NOT NULL DEFAULT 0,
    `collection_num` INT NOT NULL DEFAULT 0,
    `time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- 回答表
DROP TABLE IF EXISTS answer;
CREATE TABLE answer (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `content` text NOT NULL,
    `user` BIGINT,
    `like_num` INT NOT NULL DEFAULT 0,
    `comment_num` INT NOT NULL DEFAULT 0,
    `question` BIGINT NOT NULL,
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- 评论表
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `content` text NOT NULL,
    `user` BIGINT,
    `answer` BIGINT NOT NULL,
    `parent` BIGINT,
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- 收藏表（问题与用户的关系）
DROP TABLE IF EXISTS collection;
CREATE TABLE collection (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user` BIGINT NOT NULL,
    `question` BIGINT NOT NULL,
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE ( `user`, `question` ) 
);


-- 点赞表（回答与用户的关系）
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user` BIGINT NOT NULL,
    `answer` BIGINT NOT NULL,
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE ( `user`, `answer` ) 
);


-- 关注表（用户与用户的关系）
DROP TABLE IF EXISTS follow;
CREATE TABLE follow (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_from` BIGINT NOT NULL,
    `user_to` BIGINT NOT NULL,
    `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE ( `user_from`, `user_to` ) 
);



-- --------------------------------------------------------------------------
-- 触发器


-- 添加回答时：问题回答数+1，回答者回答数+1
DROP TRIGGER IF EXISTS add_answer;
DELIMITER $$
CREATE TRIGGER add_answer AFTER INSERT ON `answer` FOR EACH ROW
BEGIN
	UPDATE `question` SET `answer_num` = `answer_num` + 1 WHERE `id` = NEW.`question`;
	UPDATE `user` SET `answer_num` = `answer_num` + 1 WHERE `id` = NEW.`user`;
END;
$$
DELIMITER ;
-- 删除回答时：删除回答的评论，删除回答的赞，问题回答数-1，回答者回答数-1，
DROP TRIGGER IF EXISTS del_answer;
DELIMITER $$
CREATE TRIGGER del_answer BEFORE DELETE ON `answer` FOR EACH ROW
BEGIN
	DELETE FROM `comment` WHERE answer = OLD.id;
	DELETE FROM `like` WHERE answer = OLD.id;
	UPDATE `question` SET `answer_num` = `answer_num` - 1 WHERE `id` = OLD.`question`;
	UPDATE `user` SET `answer_num` = `answer_num` - 1 WHERE `id` = OLD.`user`;
END;
$$
DELIMITER ;


-- 添加收藏时：问题收藏数+1，用户收藏数+1
DROP TRIGGER IF EXISTS add_collection;
DELIMITER $$
CREATE TRIGGER `add_collection` AFTER INSERT ON `collection` FOR EACH ROW
BEGIN
	UPDATE `question` SET collection_num = collection_num + 1 WHERE `id` = NEW.question;
	UPDATE `user` SET collection_num = collection_num + 1 WHERE `id` = NEW.`user`;
END;
$$
DELIMITER ;
-- 删除收藏时：问题收藏数-1，用户收藏数-1
DROP TRIGGER IF EXISTS del_collection;
DELIMITER $$
CREATE TRIGGER `del_collection` BEFORE DELETE ON `collection` FOR EACH ROW
BEGIN
	UPDATE `question` SET collection_num = collection_num - 1 WHERE `id` = OLD.question;
	UPDATE `user` SET collection_num = collection_num - 1 WHERE `id` = OLD.`user`;
END;
$$
DELIMITER ;


-- 添加评论时：回答评论数+1，评论者评论数+1
DROP TRIGGER IF EXISTS add_comment;
DELIMITER $$
CREATE TRIGGER `add_comment` AFTER INSERT ON `comment` FOR EACH ROW
BEGIN
	UPDATE `answer` SET `comment_num` = `comment_num` + 1 WHERE `id` = NEW.`answer`;
	UPDATE `user` SET `comment_num`  = `comment_num` + 1 WHERE `id` = NEW.`user`;
END;
$$
DELIMITER ;
-- 删除评论时：引用改评论的parent字段置为NULL，回答评论数-1，评论者评论数-1
DROP TRIGGER IF EXISTS del_comment;
DELIMITER $$
CREATE TRIGGER `del_comment` BEFORE DELETE ON `comment` FOR EACH ROW
BEGIN
	UPDATE `comment` SET parent = NULL WHERE parent = OLD.id;
	UPDATE `answer` SET `comment_num` = `comment_num` - 1 WHERE `id` = OLD.`answer`;
	UPDATE `user` SET `comment_num`  = `comment_num` - 1 WHERE `id` = OLD.`user`;
END;
$$
DELIMITER ;


-- 添加关注时：following+1， 被关注人follower+1
DROP TRIGGER IF EXISTS add_follow;
DELIMITER $$
CREATE TRIGGER `add_follow` AFTER INSERT ON `follow` FOR EACH ROW
BEGIN
	UPDATE `user` SET following = following + 1 WHERE `id` = NEW.user_from;
	UPDATE `user` SET follower = follower + 1 WHERE `id` = NEW.user_to;
END;
$$
DELIMITER ;
-- 删除关注时：folloing-1，被关注人follower-1
DROP TRIGGER IF EXISTS del_follow;
DELIMITER $$
CREATE TRIGGER `del_follow` BEFORE DELETE ON `follow` FOR EACH ROW
BEGIN
	UPDATE `user` SET following = following - 1 WHERE `id` = OLD.user_from;
	UPDATE `user` SET follower = follower - 1 WHERE `id` = OLD.user_to;
END;
$$
DELIMITER ;


-- 添加赞时：回答赞数+1，回答者赞数+1
DROP TRIGGER IF EXISTS add_like;
DELIMITER $$
CREATE TRIGGER `add_like` AFTER INSERT ON `like` FOR EACH ROW
BEGIN
	UPDATE `answer` SET like_num = like_num + 1 WHERE `id` = NEW.answer;
	UPDATE `user` SET like_num = like_num + 1 WHERE `id` IN ( SELECT `user` FROM `answer` WHERE `id` = NEW.answer ) ;
END;
$$
DELIMITER ;
-- 添加赞时：回答赞数-1，回答者赞数-1
DROP TRIGGER IF EXISTS del_like;
DELIMITER $$
CREATE TRIGGER `del_like` BEFORE DELETE ON `like` FOR EACH ROW
BEGIN
	UPDATE `answer` SET like_num = like_num - 1 WHERE `id` = OLD.answer;
	UPDATE `user` SET like_num = like_num - 1 WHERE `id` IN ( SELECT `user` FROM `answer` WHERE `id` = OLD.answer ) ;
END;
$$
DELIMITER ;



-- 添加问题时：提问人问题数+1
DROP TRIGGER IF EXISTS add_question;
DELIMITER $$
CREATE TRIGGER `add_question` AFTER INSERT ON `question` FOR EACH ROW
BEGIN
	UPDATE `user` SET `question_num` = `question_num` + 1 WHERE `id` = NEW.`user`;
END;
$$
DELIMITER ;
-- 删除问题时：对应回答的question字段置为NULL，提问人问题数-1
DROP TRIGGER IF EXISTS del_question;
DELIMITER $$
CREATE TRIGGER `del_question` BEFORE DELETE ON `question` FOR EACH ROW
BEGIN
	UPDATE answer SET question = NULL WHERE question = OLD.id;
	UPDATE `user` SET `question_num` = `question_num` - 1 WHERE `id` = OLD.`user`;
END;
$$
DELIMITER ;


-- 删除用户时：answer表user字段置为NULL，collection表删除对应记录，
-- comment表user字段置为NULL，follow表删除对应记录，like表删除对应记录
-- question表user字段置为NULL
DROP TRIGGER IF EXISTS del_user;
DELIMITER $$
CREATE TRIGGER del_user BEFORE DELETE ON `user` FOR EACH ROW
BEGIN
	UPDATE answer SET `user` = NULL WHERE `user` = OLD.id;
	UPDATE `comment` SET `user` = NULL WHERE `user` = OLD.id;
	UPDATE question SET `user` = NULL WHERE `user` = OLD.id;
	DELETE FROM collection WHERE `user` = OLD.id;
	DELETE FROM follow WHERE `user_to` = OLD.id OR `user_from` = OLD.id;
	DELETE FROM `like` WHERE `user` = OLD.id;
END;
$$
DELIMITER ;


-- --------------------------------------------------------------------------
-- 存储过程
DROP PROCEDURE IF EXISTS sp_GetTimeline;
DELIMITER $$
CREATE PROCEDURE sp_GetTimeline (
	IN user_id BIGINT,
	IN index_num INT,
	IN number INT
) BEGIN
DROP TABLE IF EXISTS `temp_timeline`;
CREATE TEMPORARY TABLE `temp_timeline` (
	`content1_id` BIGINT,
	`content1` VARCHAR(128),
	`content2_id` BIGINT,
	`content2` VARCHAR(128),
	`content3_id` BIGINT,
	`content3` VARCHAR(128),
	`content_type` INT,
	`time` TIMESTAMP
);

-- 获取关注列表
DROP TABLE IF EXISTS `temp_following`;
CREATE TEMPORARY TABLE `temp_following` LIKE `user`;
INSERT INTO `temp_following`
SELECT *
FROM `user`
WHERE `id` IN (
    SELECT `user_to` FROM `follow` WHERE `user_from` = user_id
);


-- 有了新的关注

INSERT INTO `temp_timeline` (
    `content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT b.id, b.username, user_id, "", a.id, "", 0, a.time
FROM `follow` `a` INNER JOIN `user` `b` ON ( a.user_from = b.id )
WHERE a.user_to = user_id;




-- 插入数据：提问有了回答
INSERT INTO `temp_timeline` (
	`content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT c.id, c.username, a.id, a.title, b.id, b.content, 1, b.time
FROM `question` `a` INNER JOIN `answer` `b` ON ( `a`.`id` = `b`.`question`) INNER JOIN `user` `c` ON ( `b`.`user` = `c`.`id` )
WHERE a.`user` = user_id;

-- 插入数据：提问有了收藏
INSERT INTO `temp_timeline` (
	`content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT c.id, c.username, a.id, a.title, b.id, "", 2, b.time
FROM `question` `a` INNER JOIN `collection` `b` ON ( `a`.`id` = `b`.`question`) INNER JOIN `user` `c` ON ( `b`.`user` = `c`.`id` )
WHERE a.`user` = user_id;




-- 插入数据：回答有了评论
INSERT INTO `temp_timeline` (
	`content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT c.id, c.username, a.id, a.content, b.id, b.content, 3, b.time
FROM `answer` `a` INNER JOIN `comment` `b` ON ( `a`.`id` = `b`.`answer`) INNER JOIN `user` `c` ON ( `b`.`user` = `c`.`id` )
WHERE a.`user` = user_id;

-- 插入数据：回答有了点赞
INSERT INTO `temp_timeline` (
	`content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT c.id, c.username, a.id, a.content, b.id, "", 4, b.time
FROM `answer` `a` INNER JOIN `like` `b` ON ( `a`.`id` = `b`.`answer`) INNER JOIN `user` `c` ON ( `b`.`user` = `c`.`id` )
WHERE a.`user` = user_id;





-- 插入数据：评论有了评论
INSERT INTO `temp_timeline` (
	`content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT c.id, c.username, a.id, a.content, b.id, b.content, 5, b.time
FROM `comment` `a` INNER JOIN `comment` `b` ON ( `a`.`id` = `b`.`parent`) INNER JOIN `user` `c` ON ( `b`.`user` = `c`.`id` )
WHERE a.`user` = user_id;




-- Following：提问
INSERT INTO `temp_timeline` (
	`content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT a.id, a.username, 0, "", b.id, b.title, 6, b.time
FROM temp_following `a` INNER JOIN question `b` ON ( a.id = b.`user` );


-- Following：回答
INSERT INTO `temp_timeline` (
	`content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT a.id, a.username, c.id, c.title, b.id, b.content, 7, b.time
FROM temp_following `a` INNER JOIN answer `b` ON ( a.id = b.`user` ) INNER JOIN question `c` ON ( c.id = b.question ) ;


-- Following：收藏
INSERT INTO temp_timeline (
	`content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT a.id, a.username, c.id, c.title, b.id, "", 8, b.time
FROM temp_following `a` INNER JOIN collection `b` ON ( a.id = b.`user` ) INNER JOIN question `c` ON ( b.question = c.id );


-- Following：点赞
INSERT INTO temp_timeline (
	`content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT a.id, a.username, c.id, c.content, b.id, "", 9, b.time
FROM temp_following `a` INNER JOIN `like` `b` ON ( a.id = b.`user` ) INNER JOIN answer `c` ON ( b.answer = c.id );



-- 返回temp_timeline
SELECT `content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
FROM `temp_timeline`
ORDER BY `time` DESC
LIMIT index_num, number;
END;
$$
DELIMITER ;





-- --------------------------------------------------------------------------
-- 脚本结束