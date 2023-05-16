SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_books_info
-- ----------------------------
DROP TABLE IF EXISTS `xb_users`;
CREATE TABLE `xb_users`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'user ID, unique, auto-increment',
  `uname` varchar(20) NOT NULL COMMENT 'username, unique',
  `pass` varchar(256) NOT NULL default "0" COMMENT 'SHA256 of password',
  `email` varchar(50) DEFAULT NULL COMMENT 'user email',
  `first_name` varchar(20) DEFAULT NULL COMMENT 'first name',
  `second_name` varchar(20) DEFAULT NULL COMMENT 'second name',
  `role` varchar(15) DEFAULT "user" COMMENT 'user role(s),{admin,mod,dev,user}',
  `status` int(2) DEFAULT 0 COMMENT 'user status, {0:"not activated",1:"normal",2:"suspended"}',
--   `activation_key` varchar(256) DEFAULT NULL COMMENT 'email activation key',
  PRIMARY KEY (`id`) USING BTREE
)


DROP TABLE IF EXISTS `xb_captchas`;
CREATE TABLE `xb_captchas`  (
  `key` varchar(256) NOT NULL COMMENT 'SHA256 of CAPTCHA value with salt',
  `value` varchar(10) NOT NULL COMMENT 'Original CAPTCHA value',
  `expired` int(1) NOT NULL default 0 COMMENT 'CAPTCHA expiration status',
--   `activation_key` varchar(256) DEFAULT NULL COMMENT 'email activation key',
  PRIMARY KEY (`key`) USING BTREE
)
-- ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_books_info
-- ----------------------------
-- INSERT INTO `t_books_info` VALUES ('1001', 'Hadoop大数据技术与应用', 15.00, 20);
-- INSERT INTO `t_books_info` VALUES ('1002', 'Spark大数据技术与应用', 15.00, 18);
-- INSERT INTO `t_books_info` VALUES ('1003', 'Java Web程序设计', 12.00, 35);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `xb_options`;
CREATE TABLE `xb_options`  (
  `uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户学号，唯一',
  `uName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `uPassword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `uEmail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `uMajor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户专业',
  `uGroup` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户组',
  PRIMARY KEY (`uId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('2025020100', 'user1', '123456user1', 'user1@wtu.edu.cn', '大数据', 'user');
INSERT INTO `t_user` VALUES ('2025020126', 'lsq', 'lsq123456', 'sql147@qq.com', '大数据', 'admin');
INSERT INTO `t_user` VALUES ('2025020200', 'user2', '123456user2', 'user2@wtu.edu.cn', '人工智能', 'user');

SET FOREIGN_KEY_CHECKS = 1;
