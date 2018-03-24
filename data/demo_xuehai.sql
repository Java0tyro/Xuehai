/*
 Navicat Premium Data Transfer

 Source Server         : localhost_MySQL
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : demo_xuehai

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 24/03/2018 00:26:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user` bigint(20) NULL DEFAULT NULL,
  `question` bigint(20) NOT NULL,
  `time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collection
-- ----------------------------
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user` bigint(20) NOT NULL,
  `question` bigint(20) NOT NULL,
  `time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user`(`user`, `question`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user` bigint(20) NULL DEFAULT NULL,
  `answer` bigint(20) NOT NULL,
  `parent` bigint(20) NULL DEFAULT NULL,
  `time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_from` bigint(20) NOT NULL,
  `user_to` bigint(20) NOT NULL,
  `time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_from`(`user_from`, `user_to`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for like
-- ----------------------------
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user` bigint(20) NOT NULL,
  `answer` bigint(20) NOT NULL,
  `time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user`(`user`, `answer`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user` bigint(20) NULL DEFAULT NULL,
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` bigint(20) NULL DEFAULT NULL,
  `time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for question_type
-- ----------------------------
DROP TABLE IF EXISTS `question_type`;
CREATE TABLE `question_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `authority` smallint(6) NOT NULL DEFAULT 0,
  `sex` tinyint(4) NULL DEFAULT NULL,
  `profile` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `school` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `major` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Procedure structure for sp_DeleteUser
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_DeleteUser`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_DeleteUser`(IN user_id BIGINT)
BEGIN

	UPDATE answer SET `user` = NULL WHERE `user` = user_id;
	DELETE FROM collection WHERE `user` = user_id;
	UPDATE `comment` SET `user` = NULL WHERE `user` = user_id;
	DELETE FROM follow WHERE user_to = user_id OR user_from = user_id;
	DELETE FROM `like` WHERE `user` = user_id;
	UPDATE question SET `user` = NULL WHERE `user` = user_id;
	DELETE FROM `user` WHERE id = user_id;
	
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_GetTimeline
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_GetTimeline`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_GetTimeline`(
	IN user_id BIGINT,
	IN index_num INT,
	IN number INT
)
BEGIN


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
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


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
SELECT c.id, c.username, a.id, a.title, b.id, SUBSTRING(b.content, 1, 128), 1, b.time
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
SELECT c.id, c.username, a.id, SUBSTRING(a.content, 1, 128), b.id, SUBSTRING(b.content, 1, 128), 3, b.time
FROM `answer` `a` INNER JOIN `comment` `b` ON ( `a`.`id` = `b`.`answer`) INNER JOIN `user` `c` ON ( `b`.`user` = `c`.`id` )
WHERE a.`user` = user_id;


-- 插入数据：回答有了点赞
INSERT INTO `temp_timeline` (
	`content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT c.id, c.username, a.id, SUBSTRING(a.content, 1, 128), b.id, "", 4, b.time
FROM `answer` `a` INNER JOIN `like` `b` ON ( `a`.`id` = `b`.`answer`) INNER JOIN `user` `c` ON ( `b`.`user` = `c`.`id` )
WHERE a.`user` = user_id;


-- 插入数据：评论有了评论
INSERT INTO `temp_timeline` (
	`content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
)
SELECT c.id, c.username, a.id, SUBSTRING(a.content,1, 128), b.id, SUBSTRING(b.content,1, 128), 5, b.time
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
SELECT a.id, a.username, c.id, c.title, b.id, SUBSTRING(b.content,1, 128), 7, b.time
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
SELECT a.id, a.username, c.id, SUBSTRING(c.content, 1, 128), b.id, "", 9, b.time
FROM temp_following `a` INNER JOIN `like` `b` ON ( a.id = b.`user` ) INNER JOIN answer `c` ON ( b.answer = c.id );


-- 返回temp_timeline
SELECT `content1_id`, `content1`, `content2_id`, `content2`, `content3_id`, `content3`, `content_type`, `time`
FROM `temp_timeline`
ORDER BY `time` DESC
LIMIT index_num, number;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
