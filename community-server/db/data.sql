/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80036
 Source Host           : localhost:3306
 Source Schema         : db_community

 Target Server Type    : MySQL
 Target Server Version : 80036
 File Encoding         : 65001

 Date: 27/01/2026 23:26:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, '2025-08-08 23:21:33');
INSERT INTO `user_role` VALUES (3, 8, '2025-09-08 14:40:17');
INSERT INTO `user_role` VALUES (14, 2, '2025-08-11 11:35:19');
INSERT INTO `user_role` VALUES (15, 6, '2025-08-18 00:13:22');
INSERT INTO `user_role` VALUES (16, 3, '2025-09-08 03:04:32');
INSERT INTO `user_role` VALUES (17, 3, '2025-09-08 03:30:40');
INSERT INTO `user_role` VALUES (17, 7, '2025-09-08 03:30:40');
INSERT INTO `user_role` VALUES (18, 3, '2026-01-23 19:09:10');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户表id',
  `file_id` bigint NULL DEFAULT 1 COMMENT '头像id',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱，找回密码',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_active` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '0  账号被封  1账号激活',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像url',
  `is_del` int NULL DEFAULT 0 COMMENT '0 不删  1删',
  `dept_id` int NULL DEFAULT NULL COMMENT '所属部门ID',
  PRIMARY KEY (`user_id`, `username`, `email`) USING BTREE,
  INDEX `idx_user_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 1, '超级管理员', 'admin', '$2a$10$oB.JGHbd4QA9QnRSPuzzL.Fnd4JwuST5TXiz9fXNAKcJdlV7Ww2A6', 'admin@example.com', '15938522312', '1', '2025-04-23 11:15:31', '2025-11-13 17:35:01', '/uploads/202508/10cde446daaf45b2a07d8c52af72c4fc.png', 0, 1);
INSERT INTO `user` VALUES (2, 1, 'user01', 'user01', '$2a$10$oB.JGHbd4QA9QnRSPuzzL.Fnd4JwuST5TXiz9fXNAKcJdlV7Ww2A6', 'admin324@example.com', NULL, '1', '2025-04-23 11:15:31', '2025-11-13 17:35:01', NULL, 0, 1);
INSERT INTO `user` VALUES (3, 1, 'user02', 'user02', '$2a$10$oB.JGHbd4QA9QnRSPuzzL.Fnd4JwuST5TXiz9fXNAKcJdlV7Ww2A6', 'admin234@example.com', NULL, '0', '2025-04-23 11:15:31', '2026-01-25 00:28:52', NULL, 0, 1);
INSERT INTO `user` VALUES (4, 1, 'user03', 'user013', '$2a$10$oB.JGHbd4QA9QnRSPuzzL.Fnd4JwuST5TXiz9fXNAKcJdlV7Ww2A6', 'admi123n@example.com', NULL, '1', '2025-04-23 11:15:31', '2025-11-13 17:35:01', NULL, 0, 1);
INSERT INTO `user` VALUES (14, 1, '菲菲', 'user014', '$2a$10$oB.JGHbd4QA9QnRSPuzzL.Fnd4JwuST5TXiz9fXNAKcJdlV7Ww2A6', 'adm23in@example.com', NULL, '1', '2025-04-23 11:15:31', '2025-11-13 17:35:01', '/uploads/202507/a88240fb55244957ac71e5b3b2396ed8.jpg', 0, 1);
INSERT INTO `user` VALUES (15, 1, 'test', 'test', '$2a$10$oB.JGHbd4QA9QnRSPuzzL.Fnd4JwuST5TXiz9fXNAKcJdlV7Ww2A6', 'adm231in@example.com', NULL, '1', '2025-04-23 11:15:31', '2026-01-21 12:26:54', '/uploads/202510/895ffb4a4dae48a1825806126e561def.png', 0, 1);
INSERT INTO `user` VALUES (17, 1, '不好嘛', 'user015', '$2a$10$oB.JGHbd4QA9QnRSPuzzL.Fnd4JwuST5TXiz9fXNAKcJdlV7Ww2A6', 'admi3124nn@example.com', NULL, '1', '2025-04-23 11:15:31', '2025-11-13 17:35:01', 'https://www.gravatar.com/avatar/0b7b3fb1d6b5d5c3c3f85b625d18af6d?d=identicon', 0, NULL);
INSERT INTO `user` VALUES (18, 1, 'qwe', 'qwe', '$2a$10$j4b/hbJEsgF2SNE3u3UITuOqvx2SXJYOuzP8adFNTR3f50FlrHDFK', 'admi123nn@example.com', NULL, NULL, '2026-01-23 19:09:10', '2026-01-23 19:09:10', 'https://www.gravatar.com/avatar/462a2b4a4dc2a8b3f2d215bbba65ae1b?d=identicon', 0, NULL);

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1, 1, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 2, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 3, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 4, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 5, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 6, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 7, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 8, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 9, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 10, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 11, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 12, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 13, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 14, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 15, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 16, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 17, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 18, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 19, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 20, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 21, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 22, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 23, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 24, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 25, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 26, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 27, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 28, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 29, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 30, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 31, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 32, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 33, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 34, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 35, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 36, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 37, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (1, 38, '2025-08-19 14:46:03');
INSERT INTO `role_menu` VALUES (2, 1, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 2, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 3, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 4, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 6, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 8, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 9, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 10, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 11, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 12, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 13, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 14, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 15, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 16, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 22, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 23, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 26, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 27, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 28, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 29, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 30, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 31, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 32, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 33, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 34, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 35, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (2, 36, '2025-08-14 17:11:21');
INSERT INTO `role_menu` VALUES (3, 1, '2026-01-27 18:31:52');
INSERT INTO `role_menu` VALUES (3, 2, '2026-01-27 18:31:52');
INSERT INTO `role_menu` VALUES (3, 4, '2026-01-27 18:31:52');
INSERT INTO `role_menu` VALUES (3, 9, '2026-01-27 18:31:52');
INSERT INTO `role_menu` VALUES (3, 10, '2026-01-27 18:31:52');
INSERT INTO `role_menu` VALUES (3, 11, '2026-01-27 18:31:52');
INSERT INTO `role_menu` VALUES (3, 12, '2026-01-27 18:31:52');
INSERT INTO `role_menu` VALUES (3, 15, '2026-01-27 18:31:52');
INSERT INTO `role_menu` VALUES (3, 16, '2026-01-27 18:31:52');
INSERT INTO `role_menu` VALUES (3, 33, '2026-01-27 18:31:52');
INSERT INTO `role_menu` VALUES (5, 1, '2025-08-14 18:32:57');
INSERT INTO `role_menu` VALUES (5, 2, '2025-08-14 18:32:57');
INSERT INTO `role_menu` VALUES (5, 10, '2025-08-14 18:32:57');
INSERT INTO `role_menu` VALUES (5, 11, '2025-08-14 18:32:57');
INSERT INTO `role_menu` VALUES (5, 12, '2025-08-14 18:32:57');
INSERT INTO `role_menu` VALUES (6, 1, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 2, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 3, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 4, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 5, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 6, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 7, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 8, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 9, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 10, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 11, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 12, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 13, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 14, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 15, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 16, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 17, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 18, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 19, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 20, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 21, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 22, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 23, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 24, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 25, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 26, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 27, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 28, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 29, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 30, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 31, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 32, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 33, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 34, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 35, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 36, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (6, 37, '2025-08-17 16:44:39');
INSERT INTO `role_menu` VALUES (7, 1, '2025-09-08 03:30:15');
INSERT INTO `role_menu` VALUES (8, 1, '2025-09-08 14:39:57');

-- ----------------------------
-- Table structure for role_api
-- ----------------------------
DROP TABLE IF EXISTS `role_api`;
CREATE TABLE `role_api`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `api_id` bigint NOT NULL COMMENT '接口ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`role_id`, `api_id`) USING BTREE,
  INDEX `idx_api_id`(`api_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色API关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_api
-- ----------------------------
INSERT INTO `role_api` VALUES (1, 1, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 2, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 3, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 4, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 5, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 6, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 7, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 8, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 9, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 10, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 11, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 12, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 13, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 14, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 15, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 16, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 17, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 18, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 19, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 20, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 21, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 22, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 23, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 24, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 25, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 26, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 27, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 28, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 29, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 30, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 31, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 32, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 33, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 34, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 35, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 36, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 37, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 38, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 39, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 40, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 41, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 42, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 43, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 44, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (1, 45, '2026-01-13 20:57:58');
INSERT INTO `role_api` VALUES (2, 1, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 2, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 3, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 4, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 5, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 6, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 7, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 8, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 9, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 10, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 11, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 12, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 13, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 14, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 15, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 16, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 17, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 18, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 19, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 20, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 21, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 22, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 23, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 24, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 25, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 26, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 27, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 28, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 29, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 30, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 31, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 32, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 33, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 34, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 35, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 36, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 37, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 38, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 39, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 40, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 41, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 42, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 43, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 44, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (2, 45, '2025-09-20 22:14:30');
INSERT INTO `role_api` VALUES (3, 1, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 2, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 3, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 4, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 5, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 6, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 7, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 8, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 9, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 10, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 11, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 12, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 13, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 14, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 15, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 16, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 17, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 18, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 19, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 20, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 21, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 22, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 23, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 24, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 25, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 26, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 27, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 28, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 29, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 30, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 31, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 32, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 33, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 34, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 35, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 36, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 37, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 38, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 39, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 40, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 41, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 42, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 43, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 44, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 45, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 46, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 47, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 48, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 49, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 50, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 51, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 52, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 53, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 54, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 55, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 56, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 57, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 58, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 59, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 60, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 61, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 62, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 63, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 64, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 65, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 66, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 67, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 68, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 69, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 70, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 71, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 72, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 73, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 74, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 75, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 76, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 77, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 78, '2026-01-27 18:31:35');
INSERT INTO `role_api` VALUES (3, 79, '2026-01-27 18:31:35');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色标识符',
  `role_sort` int NULL DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态:0-禁用,1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_system` tinyint NULL DEFAULT 0 COMMENT '是否系统内置:0-否,1-是',
  `is_del` int NULL DEFAULT 0 COMMENT '删除标志（0-存在，1-删除）',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `idx_role_key`(`role_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理员', 'super_admin', 1, 1, '2025-08-08 23:21:33', '2025-08-14 15:35:37', '拥有所有权限', 1, 0);
INSERT INTO `role` VALUES (2, '系统管理员', 'system_admin', 2, 1, '2025-08-08 23:21:33', '2025-08-14 15:31:08', '系统管理权限', 1, 0);
INSERT INTO `role` VALUES (3, '普通用户', 'common_user', 3, 1, '2025-08-08 23:21:33', '2025-09-20 21:54:02', '普通用户权限', 1, 0);
INSERT INTO `role` VALUES (4, '游客', 'guest', 4, 1, '2025-08-08 23:21:33', '2025-08-14 16:51:10', '只读权限', 1, 0);
INSERT INTO `role` VALUES (5, '小黑屋', 'test', 5, 1, '2025-08-13 15:38:59', '2025-08-14 16:51:07', '测试，禁止使用编辑功能', 0, 0);
INSERT INTO `role` VALUES (6, '展览测试角色', 'view_admin', 4, 1, '2025-08-17 16:44:16', '2025-08-18 15:12:52', '供给他人展示系统角色，只有观看数据的权力，后续添加系统指定数据', 0, 0);
INSERT INTO `role` VALUES (7, 'test001', 'test001', 1, 1, '2025-09-08 03:29:06', '2025-09-08 03:29:06', '', 0, 0);
INSERT INTO `role` VALUES (8, '123', '123', 0, 1, '2025-09-08 14:39:43', '2025-09-08 14:39:43', '', 0, 0);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由路径',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'M' COMMENT '菜单类型(M-目录,C-菜单,F-按钮,L-链接)',
  `visible` tinyint NULL DEFAULT 1 COMMENT '是否显示:0-隐藏,1-显示',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态:0-禁用,1-启用',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  `is_external` tinyint(1) NULL DEFAULT 0 COMMENT '是否外部链接:0-否,1-是',
  `target` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '_self' COMMENT '打开方式:_self-当前窗口,_blank-新窗口',
  `is_del` int NULL DEFAULT 0 COMMENT '删除标志（0-存在，1-删除）',
  PRIMARY KEY (`menu_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '控制台', 0, 1, '/admin/dashboard', 'admin/dashboard/index', 'C', 1, 1, 'admin:dashboard:view', 'monitor', '2025-08-11 11:11:10', '2026-01-23 19:24:30', '系统控制台', 0, '_self', 0);
INSERT INTO `menu` VALUES (2, '内容管理', 0, 2, '/admin/content', NULL, 'M', 1, 1, NULL, 'grid', '2025-08-11 11:11:10', '2025-08-17 14:37:45', '内容管理模块', 0, '_self', 0);
INSERT INTO `menu` VALUES (3, '社区管理', 0, 3, '/admin/community', NULL, 'M', 0, 0, NULL, 'connection', '2025-08-11 11:11:10', '2026-01-13 20:56:00', '社区管理模块', 0, '_self', 0);
INSERT INTO `menu` VALUES (4, '用户管理', 0, 4, '/admin/user', NULL, 'M', 1, 1, NULL, 'user', '2025-08-11 11:11:10', '2025-08-17 14:37:52', '用户管理模块', 0, '_self', 0);
INSERT INTO `menu` VALUES (5, '权限管理', 0, 5, '/admin/permission', NULL, 'M', 1, 1, NULL, 'lock', '2025-08-11 11:11:10', '2025-08-17 14:37:52', '权限管理模块', 0, '_self', 0);
INSERT INTO `menu` VALUES (6, '内容审核', 0, 6, '/admin/moderation', NULL, 'M', 1, 0, NULL, 'flag', '2025-08-11 11:11:10', '2025-09-08 02:48:34', '内容审核模块', 0, '_self', 0);
INSERT INTO `menu` VALUES (7, '系统设置', 0, 7, '/admin/system', NULL, 'M', 1, 0, NULL, 'setting', '2025-08-11 11:11:10', '2025-09-08 02:50:37', '系统设置模块', 0, '_self', 0);
INSERT INTO `menu` VALUES (8, '积分管理', 0, 8, '/admin/points', NULL, 'M', 1, 1, NULL, 'TrendCharts', '2025-08-11 11:11:10', '2025-08-17 14:37:52', '积分管理模块', 0, '_self', 0);
INSERT INTO `menu` VALUES (9, '消息通知', 0, 9, '/admin/notification', NULL, 'M', 1, 1, NULL, 'Bell', '2025-08-11 11:11:10', '2025-08-17 20:12:38', '消息通知模块', 0, '_self', 0);
INSERT INTO `menu` VALUES (10, '文章管理', 2, 1, '/admin/articles', 'admin/content/article/index', 'C', 1, 1, 'admin:content:article:view', 'document', '2025-08-11 11:11:10', NULL, '文章管理', 0, '_self', 0);
INSERT INTO `menu` VALUES (11, '标签管理', 2, 2, '/admin/tags', 'admin/content/tag/index', 'C', 1, 1, 'admin:content:tag:view', 'collection', '2025-08-11 11:11:10', NULL, '标签管理', 0, '_self', 0);
INSERT INTO `menu` VALUES (12, '评论管理', 2, 3, '/admin/comments', 'admin/content/comment/index', 'C', 1, 1, 'admin:content:comment:view', 'ChatDotRound', '2025-08-11 11:11:10', '2025-08-13 23:30:41', '评论管理', 0, '_self', 0);
INSERT INTO `menu` VALUES (13, '话题管理', 3, 1, '/admin/topics', 'admin/community/topic/index', 'C', 0, 0, 'admin:community:topic:view', 'collection', '2025-08-11 11:11:10', '2025-09-08 02:47:37', '话题管理', 0, '_self', 0);
INSERT INTO `menu` VALUES (14, '活动管理', 3, 2, '/admin/activities', 'admin/community/activity/index', 'C', 1, 1, 'admin:community:activity:view', 'TrendCharts', '2025-08-11 11:11:10', '2025-08-13 23:31:04', '活动管理', 0, '_self', 0);
INSERT INTO `menu` VALUES (15, '用户列表', 4, 1, '/admin/userList', 'admin/user/list/index', 'C', 1, 1, 'admin:user:list:view', 'user', '2025-08-11 11:11:10', NULL, '用户列表', 0, '_self', 0);
INSERT INTO `menu` VALUES (16, '用户审核', 4, 2, '/admin/userAudit', 'admin/user/audit/index', 'C', 1, 0, 'admin:user:audit:view', 'warning', '2025-08-11 11:11:10', '2025-09-08 15:00:45', '用户审核', 0, '_self', 0);
INSERT INTO `menu` VALUES (17, '角色管理', 5, 1, '/admin/permissionRole', 'admin/permission/role/index', 'C', 1, 1, 'admin:permission:role:view', 'key', '2025-08-11 11:11:10', NULL, '角色管理', 0, '_self', 0);
INSERT INTO `menu` VALUES (18, '用户角色权限', 5, 2, '/admin/permissionUser', 'admin/permission/user/index', 'C', 1, 1, 'admin:permission:user:view', 'user', '2025-08-11 11:11:10', '2025-08-17 19:43:34', '用户角色权限', 0, '_self', 0);
INSERT INTO `menu` VALUES (19, '菜单权限', 5, 3, '/admin/menu', 'admin/permission/menu/index', 'C', 1, 1, 'admin:permission:menu:view', 'list', '2025-08-11 11:11:10', '2025-08-17 01:30:07', '菜单权限', 0, '_self', 0);
INSERT INTO `menu` VALUES (20, '权限操作日志', 5, 4, '/admin/permissionLog', 'admin/permission/log/index', 'C', 1, 0, 'admin:permission:log:view', 'notebook', '2025-08-11 11:11:10', '2025-09-08 02:49:21', '权限操作日志', 0, '_self', 0);
INSERT INTO `menu` VALUES (21, '权限设置', 5, 5, '/admin/permissionSetting', 'admin/permission/setting/index', 'C', 1, 0, 'admin:permission:setting:view', 'setting', '2025-08-11 11:11:10', '2025-09-08 02:49:49', '权限设置', 0, '_self', 0);
INSERT INTO `menu` VALUES (22, '举报处理', 6, 1, '/admin/report', 'admin/moderation/report/index', 'C', 1, 1, 'admin:moderation:report:view', 'flag', '2025-08-11 11:11:10', '2025-08-19 15:04:14', '举报处理', 0, '_self', 0);
INSERT INTO `menu` VALUES (23, '敏感词过滤', 6, 2, '/admin/moderationSensitive', 'admin/moderation/sensitive/index', 'C', 1, 1, 'admin:moderation:sensitive:view', 'warning', '2025-08-11 11:11:10', NULL, '敏感词过滤', 0, '_self', 0);
INSERT INTO `menu` VALUES (24, '基础配置', 7, 1, '/admin/systemBasic', 'admin/system/basic/index', 'C', 1, 1, 'admin:system:basic:view', 'setting', '2025-08-11 11:11:10', NULL, '基础配置', 0, '_self', 0);
INSERT INTO `menu` VALUES (25, '系统操作日志', 7, 2, '/admin/systemLogs', 'admin/system/log/index', 'C', 1, 0, 'admin:system:log:view', 'document', '2025-08-11 11:11:10', '2025-09-08 02:49:26', '系统操作日志', 0, '_self', 0);
INSERT INTO `menu` VALUES (26, '积分账户', 8, 1, '/admin/pointAccount', 'admin/points/account/index', 'C', 1, 1, 'admin:points:account:view', 'collection', '2025-08-11 11:11:10', '2025-08-19 19:50:52', '积分账户', 0, '_self', 0);
INSERT INTO `menu` VALUES (27, '积分规则', 8, 2, '/admin/pointRules', 'admin/points/rule/index', 'C', 1, 0, 'admin:points:rule:view', 'key', '2025-08-11 11:11:10', '2025-09-08 02:52:09', '积分规则', 0, '_self', 0);
INSERT INTO `menu` VALUES (28, '等级设置', 8, 3, '/admin/pointLevels', 'admin/points/level/index', 'C', 1, 0, 'admin:points:level:view', 'list', '2025-08-11 11:11:10', '2025-09-08 02:53:00', '等级设置', 0, '_self', 0);
INSERT INTO `menu` VALUES (29, '积分商品', 8, 4, '/admin/pointGoods', 'admin/points/goods/index', 'C', 1, 0, 'admin:points:goods:view', 'document', '2025-08-11 11:11:10', '2025-09-08 02:52:18', '积分商品', 0, '_self', 0);
INSERT INTO `menu` VALUES (30, '积分订单', 8, 5, '/admin/pointOrders', 'admin/points/order/index', 'C', 1, 0, 'admin:points:order:view', 'connection', '2025-08-11 11:11:10', '2025-09-08 02:52:25', '积分订单', 0, '_self', 0);
INSERT INTO `menu` VALUES (31, '积分日志', 8, 6, '/admin/pointLogs', 'admin/points/log/index', 'C', 1, 0, 'admin:points:log:view', 'notebook', '2025-08-11 11:11:10', '2025-09-08 02:49:32', '积分日志', 0, '_self', 0);
INSERT INTO `menu` VALUES (32, '积分设置', 8, 7, '/admin/pointSettings', 'admin/points/setting/index', 'C', 1, 0, 'admin:points:setting:view', 'setting', '2025-08-11 11:11:10', '2025-09-08 02:52:42', '积分设置', 0, '_self', 0);
INSERT INTO `menu` VALUES (33, '系统公告', 9, 1, '/admin/announcement', 'admin/notification/announcement/index', 'C', 1, 1, 'admin:notification:announcement:view', 'message', '2025-08-11 11:11:10', NULL, '系统公告', 0, '_self', 0);
INSERT INTO `menu` VALUES (34, '用户消息', 9, 2, '/admin/notificationUser', 'admin/notification/user/index', 'C', 1, 0, 'admin:notification:user:view', 'user', '2025-08-11 11:11:10', '2025-09-08 02:53:37', '用户消息', 0, '_self', 0);
INSERT INTO `menu` VALUES (35, '消息模板', 9, 3, '/admin/notificationTemplate', 'admin/notification/template/index', 'C', 1, 0, 'admin:notification:template:view', 'ChatLineRound', '2025-08-11 11:11:10', '2025-09-08 02:53:45', '消息模板', 0, '_self', 0);
INSERT INTO `menu` VALUES (36, '通知设置', 9, 4, '/admin/notificationSettings', 'admin/notification/setting/index', 'C', 1, 0, 'admin:notification:setting:view', 'setting', '2025-08-11 11:11:10', '2025-09-08 02:53:57', '通知设置', 0, '_self', 0);
INSERT INTO `menu` VALUES (37, '分类管理', 5, 9, '/admin/category', 'admin/content/category/index', 'C', 1, 1, 'admin:content:category:view', 'FolderOpened', '2025-08-17 16:09:22', '2025-08-18 22:48:39', '文章类别分类', 0, '_self', 0);
INSERT INTO `menu` VALUES (38, '用户违规', 6, 40, '/admin/userViolations', 'admin/violations/userViolations/index', 'C', 1, 1, 'admin:violations:user:view', 'Warning', '2025-08-19 14:44:15', '2025-08-19 14:44:15', '用户违规', 0, '_self', 0);

-- ----------------------------
-- Table structure for api_permission
-- ----------------------------
DROP TABLE IF EXISTS `api_permission`;
CREATE TABLE `api_permission`  (
  `api_id` bigint NOT NULL AUTO_INCREMENT COMMENT '接口ID',
  `api_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口名称',
  `api_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口路径(支持通配符)',
  `http_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'HTTP方法',
  `perms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限标识符，格式: 系统:模块:操作',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态:0-禁用,1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`api_id`) USING BTREE,
  UNIQUE INDEX `idx_perms`(`perms` ASC) USING BTREE,
  INDEX `idx_api_path`(`api_path` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'API权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of api_permission
-- ----------------------------
INSERT INTO `api_permission` VALUES (1, '删除文章', 'admin/articledelete/{id}', 'POST', 'admin:article:delete:{id}:post', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (2, '获取文章列表', 'admin/articlelist', 'GET', 'admin:article:list:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (3, '获取控制台数据', 'dashboarddata', 'GET', 'dashboard:data:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (4, '更新标签', 'admin/tagsupdateTag/{id}', 'PUT', 'admin:tags:updateTag:{id}:put', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (5, '创建标签', 'admin/tags', 'POST', 'admin:tags:post', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (6, '所有标签', 'admin/tags', 'GET', 'admin:tags:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (7, '批量删除', 'admin/tagsbatch', 'DELETE', 'admin:tags:batch:delete', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (8, '删除', 'admin/tags{id}', 'DELETE', 'admin:tags:{id}:delete', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (9, '审批', 'admin/tags', 'PUT', 'admin:tags:put', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (10, '添加用户', 'admin/users/addUser', 'PUT', 'admin:users:addUser:put', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (11, '获取用户列表', 'admin/users', 'GET', 'admin:users:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (12, '获取用户列表', 'admin/userslist', 'GET', 'admin:users:list:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (13, '批量删除用户', 'admin/users/batch-delete', 'POST', 'admin:users:batch-delete:post', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (14, '删除用户', 'admin/users/{userId}', 'DELETE', 'admin:users:{id}:delete', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (15, '改变激活用户状态', 'admin/users/active-change', 'POST', 'admin:users:active-change:post', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (16, '更新用户信息', 'admin/users/{userId}', 'PUT', 'admin:users:{id}:put', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (17, '删除公告', '/system/announcement/{id}', 'DELETE', 'system:announcement:{id}:delete', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (18, '获取公告列表', '/system/announcement/list', 'GET', 'system:announcement:list:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (19, '发布公告', '/system/announcement/publish/{id}', 'POST', 'system:announcement:publish:{id}:post', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (20, '下线公告', '/system/announcement/offline/{id}', 'POST', 'system:announcement:offline:{id}:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (21, '获取公告详情', '/system/announcement{id}', 'GET', 'system:announcement:{id}:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (22, '新增公告', '/system/announcement', 'POST', 'system:announcement:post', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (23, '修改公告', '/system/announcement', 'PUT', 'system:announcement:put', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (24, '更新文章评论区状态', '/posts{articleId}/comment-status', 'PUT', 'posts:{id}:comment-status:put', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (25, '删除评论', '/comments{commentId}', 'DELETE', 'comments:{id}:delete', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (26, '创建支付宝支付', '/api/payment/alipay/create', 'POST', 'api:payment:alipay:create:post', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (27, '支付宝异步通知处理', '/api/payment/alipay/notify', 'POST', 'api:payment:alipay:notify:post', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (28, '支付宝同步返回处理', '/api/payment/alipay/return', 'GET', 'api:payment:alipay:return:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (29, '获取用户积分账户列表', 'pointsAccount', 'GET', 'pointsAccount:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (30, '获取举报详情', 'report/{id}', 'GET', 'report:id:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (31, '获取举报列表', 'report', 'GET', 'report:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (32, '提交举报', 'reportsubmit', 'POST', 'report:submit:post', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (33, '忽略举报', 'report/{id}/ignore', 'POST', 'report:ignore:post', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (34, '获取单个用户积分账户详情', 'pointsAccount{accountId}', 'GET', 'pointsAccount:{id}:get', 1, '2025-09-20 18:11:38', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (35, '获取API树', 'apiPermissiontree', 'GET', 'apiPermission:tree:get', 1, '2025-09-20 20:34:04', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (36, '获取角色API权限', 'apiPermission{roleId}', 'GET', 'apiPermission:role:{id}:get', 1, '2025-09-20 21:09:12', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (37, '删除角色', 'role', 'DELETE', 'role:delete:delete', 1, '2025-09-20 21:09:12', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (38, '修改角色状态', 'rolestatus', 'PUT', 'role:status:put', 1, '2025-09-20 21:09:12', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (39, '获取角色列表（不分页）', 'rolegetRoleList', 'GET', 'role:getRoleList:get', 1, '2025-09-20 21:09:12', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (40, '获取角色列表(分页)', 'rolelist', 'GET', 'role:list:get', 1, '2025-09-20 21:09:12', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (41, '更新角色', 'role', 'PUT', 'role:update:put', 1, '2025-09-20 21:09:12', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (42, '创建角色', 'role', 'POST', 'role:create:post', 1, '2025-09-20 21:09:12', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (43, '更新角色菜单权限', 'rolemenu', 'PUT', 'role:menu:put', 1, '2025-09-20 21:09:12', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (44, '获取角色详情', 'role{roleId}', 'GET', 'role:detail:{id}:get', 1, '2025-09-20 21:09:12', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (45, '更新角色API权限', 'roleapiPermission', 'PUT', 'role:apiPermission:put', 1, '2025-09-20 21:31:47', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (46, '获取评论', '/comments/getCommentsById', 'GET', 'comments:getCommentsById:get', 1, '2025-10-15 01:16:57', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (47, '添加新的评论', '/comments/addComment', 'POST', 'comments:addComment:post', 1, '2025-10-15 01:16:57', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (48, '获取评论管理列表', '/comments/admin', 'GET', 'comments:admin:get', 1, '2025-10-15 01:16:57', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (49, '获取分类列表', '/category', 'GET', 'category:get', 1, '2025-10-15 01:16:57', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (50, '获取所有分类', '/category/list', 'GET', 'category:list:get', 1, '2025-10-15 01:16:57', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (51, '获取分类列表', '/category{categoryId}', 'DELETE', 'category:{categoryId}:delete', 1, '2025-10-15 01:16:57', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (52, '添加分类', '/category', 'POST', 'category:add', 1, '2025-10-15 01:16:57', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (53, '获取所有分类树', '/category/listTree', 'GET', 'category:listTree:get', 1, '2025-10-15 01:16:57', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (54, '更新分类', '/category', 'PUT', 'category:update', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (55, '获取文章的分类', '/category/{articleId}/categories', 'GET', 'category:{articleId}:categories:get', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (56, 'ai对话，生成文本', '/api/deepseek/generate', 'POST', 'api:deepseek:generate:post', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (57, '新建收藏夹', '/favorite/folders', 'POST', 'favorite:folders:post', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (58, '删除收藏夹', '/favorite/folders/{folderId}', 'DELETE', 'favorite:folders:delete', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (59, '收藏文章', '/favorite', 'POST', 'favorite:post', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (60, '获取收藏夹列表', '/favorite/folders', 'GET', 'favorite:folders:get', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (61, '修改收藏夹名称', '/favorite/folders/{folderId}', 'PUT', 'favorite:folders:put', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (62, '获取收藏的文章列表', '/favorite/articles', 'GET', 'favorite:articles:get', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (63, '取消文章收藏', '/favorite/{articleId}', 'DELETE', 'favorite:delete', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (64, '移动收藏文章', '/favorite/move', 'PUT', 'favorite:move:put', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:15');
INSERT INTO `api_permission` VALUES (65, '通过文件id删除文件', '/files/delFileById/{id}', 'POST', 'files:delFileById:post', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (66, '单文件上传', '/files/upload', 'POST', 'files:upload:post', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (67, '获取菜单列表', 'menulist', 'GET', 'menu:list:get', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (68, '获取角色管理菜单', 'menugetUserMenuTree', 'GET', 'menu:getUserMenuTree:get', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (69, '获取角色管理菜单', 'menutree', 'GET', 'menu:tree:get', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (70, '删除菜单', 'menu{menuId}', 'DELETE', 'menu:{menuId}:delete', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (71, '添加菜单', 'menu', 'POST', 'menu:add:post', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (72, '更新菜单', 'menu', 'PUT', 'menu:update:put', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (73, '获取角色菜单权限id', 'menu{roleId}', 'GET', 'menu:{roleId}:get', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (74, '创建新标签', '/tag/createTag', 'POST', 'tag:createTag:post', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (75, '所有标签', '/tag/getAllTags', 'GET', 'tag:getAllTags:get', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (76, '热门标签', '/tag/getPopularTags', 'GET', 'tag:getPopularTags:get', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (77, '修改用户信息', '/user/updateUserProfile', 'POST', 'user:updateUserProfile:post', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (78, '更换用户头像', '/user/updateUserCoverId', 'POST', 'user:updateUserCoverId:post', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');
INSERT INTO `api_permission` VALUES (79, '更新用户角色', '/userroles', 'PUT', 'user:roles:put', 1, '2025-10-15 01:16:58', '2026-01-27 17:51:16');

-- ----------------------------
-- Triggers structure for table user
-- ----------------------------
DROP TRIGGER IF EXISTS `after_user_insert`;
delimiter ;;
CREATE TRIGGER `after_user_insert` AFTER INSERT ON `user` FOR EACH ROW BEGIN
    INSERT INTO user_role (user_id, role_id) VALUES (NEW.user_id, 3);
		INSERT INTO user_points_account(user_id) VALUES (NEW.user_id);
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table role
-- ----------------------------
DROP TRIGGER IF EXISTS `before_role_delete`;
delimiter ;;
CREATE TRIGGER `before_role_delete` BEFORE DELETE ON `role` FOR EACH ROW BEGIN
    DELETE FROM user_role WHERE role_id = OLD.role_id;
    DELETE FROM role_menu WHERE role_id = OLD.role_id;
    DELETE FROM data_scope WHERE role_id = OLD.role_id;
		DELETE FROM role_api WHERE role_id = OLD.role_id;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table menu
-- ----------------------------
DROP TRIGGER IF EXISTS `before_menu_delete`;
delimiter ;;
CREATE TRIGGER `before_menu_delete` BEFORE DELETE ON `menu` FOR EACH ROW BEGIN
    DELETE FROM role_menu WHERE menu_id = OLD.menu_id;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
