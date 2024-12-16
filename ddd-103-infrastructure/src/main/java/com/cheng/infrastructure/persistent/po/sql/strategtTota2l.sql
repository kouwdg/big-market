/*
SQLyog Professional v13.1.1 (64 bit)
MySQL - 8.0.34 : Database - big_market
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`big_market` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `big_market`;

/*Table structure for table `award` */

DROP TABLE IF EXISTS `award`;

CREATE TABLE `award` (
                         `id` bigint NOT NULL COMMENT '自增Id',
                         `award_id` int NOT NULL COMMENT '奖品Id',
                         `award_key` varchar(32) NOT NULL COMMENT '奖品对接标识',
                         `award_config` varchar(32) NOT NULL COMMENT '奖品配置信息',
                         `award_desc` varbinary(128) NOT NULL COMMENT '奖品内容描述',
                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `award` */

insert  into `award`(`id`,`award_id`,`award_key`,`award_config`,`award_desc`,`create_time`,`update_time`) values
    (1,100,'user_credit_blacklist','1','黑名单积分','2024-12-04 16:15:56','2024-12-04 16:15:56');

/*Table structure for table `rule_tree` */

DROP TABLE IF EXISTS `rule_tree`;

CREATE TABLE `rule_tree` (
                             `id` bigint NOT NULL,
                             `tree_id` varchar(100) NOT NULL,
                             `tree_name` varchar(100) NOT NULL,
                             `tree_desc` varchar(255) NOT NULL,
                             `tree_node_rule_key` varchar(100) NOT NULL,
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `rule_tree` */

/*Table structure for table `rule_tree_node` */

DROP TABLE IF EXISTS `rule_tree_node`;

CREATE TABLE `rule_tree_node` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `tree_id` varchar(100) DEFAULT NULL,
                                  `rule_key` varchar(100) DEFAULT NULL,
                                  `rule_desc` varchar(255) DEFAULT NULL,
                                  `rule_value` varchar(100) DEFAULT NULL,
                                  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                                  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `rule_tree_node` */

/*Table structure for table `rule_tree_node_line` */

DROP TABLE IF EXISTS `rule_tree_node_line`;

CREATE TABLE `rule_tree_node_line` (
                                       `id` bigint NOT NULL,
                                       `tree_id` varchar(100) DEFAULT NULL,
                                       `rule_node_from` varchar(100) DEFAULT NULL,
                                       `rule_node_to` varchar(100) DEFAULT NULL,
                                       `rule_limit_type` varchar(100) DEFAULT 'EQUAL',
                                       `rule_limit_value` varchar(100) DEFAULT NULL,
                                       `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                                       `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `rule_tree_node_line` */

insert  into `rule_tree_node_line`(`id`,`tree_id`,`rule_node_from`,`rule_node_to`,`rule_limit_type`,`rule_limit_value`,`create_time`,`update_time`) values
    (1,'tree_lock','tree_lock','rule_stock','EQUAL','ALLOW','2024-12-12 16:47:41','2024-12-12 16:47:41');

/*Table structure for table `strategy` */

DROP TABLE IF EXISTS `strategy`;

CREATE TABLE `strategy` (
                            `id` bigint NOT NULL COMMENT '自增ID',
                            `strategy_id` bigint NOT NULL COMMENT '策略id',
                            `strategy_desc` varchar(128) NOT NULL COMMENT '抽奖策略描述',
                            `rule_models` varchar(200) DEFAULT NULL COMMENT '抽奖规则模型',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `strategy` */

insert  into `strategy`(`id`,`strategy_id`,`strategy_desc`,`rule_models`,`create_time`,`update_time`) values
                                                                                                          (1,10001,'抽奖策略A','rule_blacklist,rule_weight','2024-12-02 18:25:52','2024-12-02 18:25:56'),
                                                                                                          (2,10002,'抽奖策略B','rule_blacklist','2024-12-11 08:34:37','2024-12-11 08:34:37');

/*Table structure for table `strategy_award` */

DROP TABLE IF EXISTS `strategy_award`;

CREATE TABLE `strategy_award` (
                                  `id` bigint NOT NULL COMMENT '自增Id',
                                  `strategy_id` bigint NOT NULL COMMENT '抽奖策略Id',
                                  `award_id` int NOT NULL COMMENT '奖品Id',
                                  `award_title` varchar(128) NOT NULL COMMENT '抽奖奖品的标题',
                                  `award_subtitle` varbinary(128) DEFAULT NULL COMMENT '抽奖奖品的副标题',
                                  `award_count` int NOT NULL COMMENT '奖品的库存总量',
                                  `award_count_surplus` int NOT NULL COMMENT '奖品的库存',
                                  `award_rate` decimal(6,4) NOT NULL COMMENT '奖品的中奖概率',
                                  `rule_models` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '规则模型，rule里面的规则目录',
                                  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `strategy_award` */

insert  into `strategy_award`(`id`,`strategy_id`,`award_id`,`award_title`,`award_subtitle`,`award_count`,`award_count_surplus`,`award_rate`,`rule_models`,`sort`,`create_time`,`update_time`) values
                                                                                                                                                                                                  (1,10001,101,'随机积分',NULL,80000,80000,0.8000,'rule_random',1,'2024-12-02 18:28:44','2024-12-02 18:28:44'),
                                                                                                                                                                                                  (2,10001,102,'5次抽奖机会',NULL,10000,10000,0.1000,NULL,2,'2024-12-02 18:30:26','2024-12-02 18:30:26'),
                                                                                                                                                                                                  (3,10001,103,'10次抽奖机会',NULL,5000,5000,0.0500,NULL,3,'2024-12-02 18:31:20','2024-12-02 18:31:20'),
                                                                                                                                                                                                  (4,10001,105,'20次抽奖机会',NULL,4000,4000,0.0400,NULL,4,'2024-12-02 18:32:26','2024-12-02 18:32:26'),
                                                                                                                                                                                                  (5,10001,106,'鼠标','抽奖一次后解锁',100,100,0.0060,'rule_lock,rule_luck_award',5,'2024-12-02 18:36:04','2024-12-02 18:36:04'),
                                                                                                                                                                                                  (6,10001,107,'键盘','抽奖二次后解锁',100,100,0.0020,'rule_lock,rule_luck_award',6,'2024-12-02 18:36:51','2024-12-02 18:36:51'),
                                                                                                                                                                                                  (7,10001,108,'手机','抽奖四次后解锁',10,10,0.0019,'rule_lock,rule_luck_award',7,'2024-12-02 18:37:28','2024-12-02 18:37:28'),
                                                                                                                                                                                                  (8,10001,109,'万元现金','抽奖八次后解锁',1,1,0.0001,'rule_lock,rule_luck_award',8,'2024-12-02 18:37:56','2024-12-02 18:37:56'),
                                                                                                                                                                                                  (9,10002,101,'玩偶',NULL,100,100,0.5000,NULL,0,'2024-12-11 08:30:33','2024-12-11 08:30:33'),
                                                                                                                                                                                                  (10,10002,102,'鼠标',NULL,50,50,0.1000,'rule_lock,rule_luck_award',0,'2024-12-11 08:31:16','2024-12-11 08:31:16'),
                                                                                                                                                                                                  (11,10002,103,'U盘',NULL,20,20,0.4000,'rule_lock,rule_luck_award',0,'2024-12-11 08:32:01','2024-12-11 08:32:01');

/*Table structure for table `strategy_rule` */

DROP TABLE IF EXISTS `strategy_rule`;

CREATE TABLE `strategy_rule` (
                                 `id` bigint NOT NULL COMMENT '自增id',
                                 `strategy_id` bigint NOT NULL COMMENT '策略id',
                                 `award_id` int DEFAULT NULL COMMENT '抽奖奖品Id',
                                 `rule_type` int NOT NULL DEFAULT '0' COMMENT '抽奖规则类型 1策略规则 2 奖品规则',
                                 `rule_model` varchar(16) NOT NULL COMMENT '抽奖规则类型[role_lock]',
                                 `rule_value` varchar(128) NOT NULL COMMENT '抽奖规则比值',
                                 `rule_desc` varbinary(128) NOT NULL COMMENT '抽奖规则描述',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `strategy_rule` */

insert  into `strategy_rule`(`id`,`strategy_id`,`award_id`,`rule_type`,`rule_model`,`rule_value`,`rule_desc`,`create_time`,`update_time`) values
                                                                                                                                              (1,10001,101,2,'rule_random','1,1000','随机积分策略','2024-12-02 18:40:03','2024-12-02 18:40:03'),
                                                                                                                                              (2,10001,106,2,'rule_lock','1','加锁一次','2024-12-02 18:45:47','2024-12-02 18:45:47'),
                                                                                                                                              (3,10001,107,2,'rule_lock','2','加锁2次','2024-12-02 18:46:15','2024-12-02 18:46:15'),
                                                                                                                                              (4,10001,108,2,'rule_lock','4','加锁4次','2024-12-02 18:46:42','2024-12-02 18:46:42'),
                                                                                                                                              (5,10001,109,2,'rule_lock','8','加锁8次','2024-12-02 18:47:08','2024-12-02 18:47:08'),
                                                                                                                                              (6,10001,109,2,'rule_lunck_award','1,1000','幸运奖','2024-12-02 18:48:30','2024-12-02 18:48:30'),
                                                                                                                                              (9,10001,NULL,1,'rule_weight','4000:101,102,103,105 5000:106,107,108 6000:101,102,103,105,106,107,108,109 ','幸运积分，必中奖范围','2024-12-04 08:41:02','2024-12-04 08:41:02'),
                                                                                                                                              (10,10001,NULL,1,'rule_blacklist','100:user1,user2','黑名单用户，只能抽100','2024-12-04 16:11:27','2024-12-04 16:11:27'),
                                                                                                                                              (11,10001,107,2,'rule_luck_award','101','107的幸运奖是101','2024-12-10 21:08:20','2024-12-10 21:08:20'),
                                                                                                                                              (12,10002,102,2,'rule_lock','2','加锁两次','2024-12-11 08:32:46','2024-12-11 08:32:46'),
                                                                                                                                              (13,10002,103,2,'rule_lock','2','加锁两次','2024-12-11 08:33:13','2024-12-11 08:33:13'),
                                                                                                                                              (14,10002,NULL,1,'rule_blacklist','101:user1,user2','黑名单用户，只能抽100','2024-12-11 09:00:00','2024-12-11 09:00:00');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
