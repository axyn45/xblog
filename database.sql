/*
 Navicat Premium Data Transfer

 Source Server         : 1
 Source Server Type    : MySQL
 Source Server Version : 101002
 Source Host           : 10.0.0.90:3306
 Source Schema         : xblog

 Target Server Type    : MySQL
 Target Server Version : 101002
 File Encoding         : 65001

 Date: 18/05/2023 11:54:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for xb_captchas
-- ----------------------------
DROP TABLE IF EXISTS `xb_captchas`;
CREATE TABLE `xb_captchas`  (
  `session` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Session ID',
  `cval` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Original CAPTCHA value',
  PRIMARY KEY (`session`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xb_logins
-- ----------------------------
DROP TABLE IF EXISTS `xb_logins`;
CREATE TABLE `xb_logins`  (
  `cookie` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'post ID, unique, auto-increment',
  `user` int NOT NULL COMMENT 'post author',
  `expire` datetime NULL DEFAULT NULL COMMENT 'post title',
  PRIMARY KEY (`cookie`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xb_posts
-- ----------------------------
DROP TABLE IF EXISTS `xb_posts`;
CREATE TABLE `xb_posts`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'post ID, unique, auto-increment',
  `author` int NOT NULL COMMENT 'post author',
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'post title',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'post content',
  `date` datetime NULL DEFAULT NULL COMMENT 'post first published date',
  `status` int NULL DEFAULT 0 COMMENT 'post status, {0:\"draft\",1:\"published\",2:\"suspended\"}',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xb_users
-- ----------------------------
DROP TABLE IF EXISTS `xb_users`;
CREATE TABLE `xb_users`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'user ID, unique, auto-increment',
  `uname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'username, unique',
  `pass` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT 'SHA256 of password',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'user email',
  `first_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'first name',
  `second_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'second name',
  `role` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'user' COMMENT 'user role(s),{admin,mod,dev,user}',
  `status` int NULL DEFAULT 0 COMMENT 'user status, {0:\"not activated\",1:\"normal\",2:\"suspended\"}',
  `activation` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Activation key sent in email',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
