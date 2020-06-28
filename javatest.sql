/*
 Navicat Premium Data Transfer

 Source Server         : 118.178.181.164_3306
 Source Server Type    : MySQL
 Source Server Version : 50645
 Source Host           : 118.178.181.164:3306
 Source Schema         : javatest

 Target Server Type    : MySQL
 Target Server Version : 50645
 File Encoding         : 65001

 Date: 28/06/2020 17:46:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gandu
-- ----------------------------
DROP TABLE IF EXISTS `gandu`;
CREATE TABLE `gandu`  (
  `userID` int(10) UNSIGNED NOT NULL,
  `groupID` int(10) NOT NULL,
  PRIMARY KEY (`userID`, `groupID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of gandu
-- ----------------------------
INSERT INTO `gandu` VALUES (1, 1);

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `createTime` date NULL DEFAULT NULL,
  `master` int(10) UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE,
  INDEX `Master`(`master`) USING BTREE,
  CONSTRAINT `Master` FOREIGN KEY (`master`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of groups
-- ----------------------------
INSERT INTO `groups` VALUES (1, '一号聊天室', '2020-06-02', 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 119 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '小米', 'a9378ba6243908d28b59f371363e57381450bfc74a6dc5d932eb5d40d22fa19df5ddef8be1c078a1');

SET FOREIGN_KEY_CHECKS = 1;
