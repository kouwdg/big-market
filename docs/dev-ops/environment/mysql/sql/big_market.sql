/*
SQLyog Community v13.3.0 (64 bit)
MySQL - 8.0.32 : Database - big_market
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
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `award_id` int NOT NULL COMMENT '抽奖奖品ID - 内部流转使用',
  `award_key` varchar(32) NOT NULL COMMENT '奖品对接标识 - 每一个都是一个对应的发奖策略',
  `award_config` varchar(32) NOT NULL COMMENT '奖品配置信息',
  `award_desc` varchar(128) NOT NULL COMMENT '奖品内容描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `award` */

insert  into `award`(`id`,`award_id`,`award_key`,`award_config`,`award_desc`,`create_time`,`update_time`) values 
(1,101,'user_credit_random','1,100','用户积分【优先透彻规则范围，如果没有则走配置】','2023-12-09 11:07:06','2023-12-09 11:21:31'),
(2,102,'openai_use_count','5','OpenAI 增加使用次数','2023-12-09 11:07:06','2023-12-09 11:12:59'),
(3,103,'openai_use_count','10','OpenAI 增加使用次数','2023-12-09 11:07:06','2023-12-09 11:12:59'),
(4,104,'openai_use_count','20','OpenAI 增加使用次数','2023-12-09 11:07:06','2023-12-09 11:12:58'),
(5,105,'openai_model','gpt-4','OpenAI 增加模型','2023-12-09 11:07:06','2023-12-09 11:12:01'),
(6,106,'openai_model','dall-e-2','OpenAI 增加模型','2023-12-09 11:07:06','2023-12-09 11:12:08'),
(7,107,'openai_model','dall-e-3','OpenAI 增加模型','2023-12-09 11:07:06','2023-12-09 11:12:10'),
(8,108,'openai_use_count','100','OpenAI 增加使用次数','2023-12-09 11:07:06','2023-12-09 11:12:55'),
(9,109,'openai_model','gpt-4,dall-e-2,dall-e-3','OpenAI 增加模型','2023-12-09 11:07:06','2023-12-09 11:12:39'),
(10,100,'user_credit_blacklist','1','黑名单积分','2024-01-06 12:30:40','2024-01-06 12:30:46');

/*Table structure for table `raffle_activity` */

DROP TABLE IF EXISTS `raffle_activity`;

CREATE TABLE `raffle_activity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
  `activity_desc` varchar(128) NOT NULL COMMENT '活动描述',
  `begin_date_time` datetime NOT NULL COMMENT '开始时间',
  `end_date_time` datetime NOT NULL COMMENT '结束时间',
  `stock_count` int NOT NULL COMMENT '库存总量',
  `stock_count_surplus` int NOT NULL COMMENT '剩余库存',
  `activity_count_id` bigint NOT NULL COMMENT '活动参与次数配置',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `state` varchar(8) NOT NULL COMMENT '活动状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_activity_id` (`activity_id`),
  KEY `idx_begin_date_time` (`begin_date_time`),
  KEY `idx_end_date_time` (`end_date_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖活动表';

/*Data for the table `raffle_activity` */

insert  into `raffle_activity`(`id`,`activity_id`,`activity_name`,`activity_desc`,`begin_date_time`,`end_date_time`,`stock_count`,`stock_count_surplus`,`activity_count_id`,`strategy_id`,`state`,`create_time`,`update_time`) values 
(1,101,'pdd','pdd抽奖活动','2024-12-19 14:36:37','2024-12-28 14:36:40',100,100,101,100006,'open','2024-12-19 14:36:54','2024-12-23 17:13:28');

/*Table structure for table `raffle_activity_account` */

DROP TABLE IF EXISTS `raffle_activity_account`;

CREATE TABLE `raffle_activity_account` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `total_count` int NOT NULL COMMENT '总次数',
  `total_count_surplus` int NOT NULL COMMENT '剩余总次数',
  `day_count` int NOT NULL COMMENT '日次数',
  `day_count_surplus` int NOT NULL COMMENT '剩余日次数',
  `month_count_surplus` int NOT NULL COMMENT '剩余月次数',
  `month_count` int NOT NULL COMMENT '月次数',
  `flow_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '流水ID - 生成的唯一ID',
  `flow_channel` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'activity' COMMENT '流水渠道（activity-活动领取、sale-购买、redeem-兑换、free-免费赠送）',
  `biz_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务ID（外部透传，活动ID、订单ID）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_biz_id` (`biz_id`),
  UNIQUE KEY `uq_flow_id` (`flow_id`),
  KEY `idx_user_id_activity_id` (`user_id`,`activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖活动账户流水表';

/*Data for the table `raffle_activity_account` */

insert  into `raffle_activity_account`(`id`,`user_id`,`activity_id`,`total_count`,`total_count_surplus`,`day_count`,`day_count_surplus`,`month_count_surplus`,`month_count`,`flow_id`,`flow_channel`,`biz_id`,`create_time`,`update_time`) values 
(2,'chengyule',101,2,2,2,2,2,2,NULL,'activity',NULL,'2024-12-23 17:46:54','2024-12-23 17:48:57'),
(3,'cheng',101,39,36,39,36,36,39,NULL,'activity',NULL,'2024-12-25 17:39:18','2024-12-25 17:43:19');

/*Table structure for table `raffle_activity_account_day` */

DROP TABLE IF EXISTS `raffle_activity_account_day`;

CREATE TABLE `raffle_activity_account_day` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `day` varchar(10) NOT NULL COMMENT '日期（yyyy-mm-dd）',
  `day_count` int NOT NULL COMMENT '日次数',
  `day_count_surplus` int NOT NULL COMMENT '日次数-剩余',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_id_activity_id_day` (`user_id`,`activity_id`,`day`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖活动账户表-日次数';

/*Data for the table `raffle_activity_account_day` */

insert  into `raffle_activity_account_day`(`id`,`user_id`,`activity_id`,`day`,`day_count`,`day_count_surplus`,`create_time`,`update_time`) values 
(2,'cheng',101,'2024-12-26',39,36,'2024-12-26 10:36:29','2024-12-26 10:36:29');

/*Table structure for table `raffle_activity_account_month` */

DROP TABLE IF EXISTS `raffle_activity_account_month`;

CREATE TABLE `raffle_activity_account_month` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `month` varchar(7) NOT NULL COMMENT '月（yyyy-mm）',
  `month_count` int NOT NULL COMMENT '月次数',
  `month_count_surplus` int NOT NULL COMMENT '月次数-剩余',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_id_activity_id_month` (`user_id`,`activity_id`,`month`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖活动账户表-月次数';

/*Data for the table `raffle_activity_account_month` */

insert  into `raffle_activity_account_month`(`id`,`user_id`,`activity_id`,`month`,`month_count`,`month_count_surplus`,`create_time`,`update_time`) values 
(2,'cheng',101,'2024-12',39,36,'2024-12-26 02:36:30','2024-12-26 10:36:29');

/*Table structure for table `raffle_activity_count` */

DROP TABLE IF EXISTS `raffle_activity_count`;

CREATE TABLE `raffle_activity_count` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `activity_count_id` bigint NOT NULL COMMENT '活动次数编号',
  `total_count` int NOT NULL COMMENT '总次数',
  `day_count` int NOT NULL COMMENT '日次数',
  `month_count` int NOT NULL COMMENT '月次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_activity_count_id` (`activity_count_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖活动次数配置表';

/*Data for the table `raffle_activity_count` */

insert  into `raffle_activity_count`(`id`,`activity_count_id`,`total_count`,`day_count`,`month_count`,`create_time`,`update_time`) values 
(1,101,100,100,100,'2024-12-23 13:26:06','2024-12-23 16:49:08');

/*Table structure for table `raffle_activity_order` */

DROP TABLE IF EXISTS `raffle_activity_order`;

CREATE TABLE `raffle_activity_order` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `sku` bigint NOT NULL,
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单状态（not_used、used、expire）',
  `out_business_no` varchar(64) DEFAULT NULL COMMENT '业务防止重复Id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `sku_count` int NOT NULL COMMENT '充值数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `idx_out_busin` (`out_business_no`),
  KEY `idx_user_id_activity_id` (`user_id`,`activity_id`,`state`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖活动单';

/*Data for the table `raffle_activity_order` */

insert  into `raffle_activity_order`(`id`,`user_id`,`sku`,`activity_id`,`activity_name`,`strategy_id`,`order_id`,`order_time`,`state`,`out_business_no`,`create_time`,`update_time`,`sku_count`) values 
(13,'chengyule',101,101,'pdd',100006,'885333001827','2024-12-23 09:46:56','completed','700091009111','2024-12-23 17:46:54','2024-12-23 17:46:54',1),
(14,'chengyule',101,101,'pdd',100006,'934068633031','2024-12-23 09:48:59','completed','搲뢽??嚍?ᨃ','2024-12-23 17:48:57','2024-12-23 17:48:57',1),
(15,'cheng',101,101,'pdd',100006,'446020892825','2024-12-25 09:39:22','completed','22211797974','2024-12-25 17:39:18','2024-12-25 17:39:18',1),
(16,'cheng',101,101,'pdd',100006,'272454383895','2024-12-25 09:39:22','completed','29194462917','2024-12-25 17:39:18','2024-12-25 17:39:18',1),
(17,'cheng',101,101,'pdd',100006,'939604134082','2024-12-25 09:39:22','completed','84110187659','2024-12-25 17:39:19','2024-12-25 17:39:19',1),
(18,'cheng',101,101,'pdd',100006,'830530589599','2024-12-25 09:39:23','completed','36782914232','2024-12-25 17:39:19','2024-12-25 17:39:19',1),
(19,'cheng',101,101,'pdd',100006,'478050799242','2024-12-25 09:39:23','completed','57691617266','2024-12-25 17:39:19','2024-12-25 17:39:19',1),
(20,'cheng',101,101,'pdd',100006,'643624370944','2024-12-25 09:39:23','completed','26896509287','2024-12-25 17:39:19','2024-12-25 17:39:19',1),
(21,'cheng',101,101,'pdd',100006,'789253694621','2024-12-25 09:39:23','completed','33578856229','2024-12-25 17:39:19','2024-12-25 17:39:19',1),
(22,'cheng',101,101,'pdd',100006,'655432571746','2024-12-25 09:39:23','completed','10863907063','2024-12-25 17:39:19','2024-12-25 17:39:19',1),
(23,'cheng',101,101,'pdd',100006,'092664539724','2024-12-25 09:39:23','completed','83101397081','2024-12-25 17:39:19','2024-12-25 17:39:19',1),
(24,'cheng',101,101,'pdd',100006,'199583755638','2024-12-25 09:39:23','completed','22319937639','2024-12-25 17:39:20','2024-12-25 17:39:20',1),
(25,'cheng',101,101,'pdd',100006,'481369333968','2024-12-25 09:39:24','completed','70740749129','2024-12-25 17:39:20','2024-12-25 17:39:20',1),
(26,'cheng',101,101,'pdd',100006,'424532955699','2024-12-25 09:39:24','completed','55557573182','2024-12-25 17:39:20','2024-12-25 17:39:20',1),
(27,'cheng',101,101,'pdd',100006,'034077040813','2024-12-25 09:39:24','completed','53477104931','2024-12-25 17:39:20','2024-12-25 17:39:20',1),
(28,'cheng',101,101,'pdd',100006,'248080823680','2024-12-25 09:39:24','completed','59337888428','2024-12-25 17:39:20','2024-12-25 17:39:20',1),
(29,'cheng',101,101,'pdd',100006,'407634012950','2024-12-25 09:39:24','completed','63296739401','2024-12-25 17:39:20','2024-12-25 17:39:20',1),
(30,'cheng',101,101,'pdd',100006,'134551214413','2024-12-25 09:39:24','completed','86751141558','2024-12-25 17:39:20','2024-12-25 17:39:20',1),
(31,'cheng',101,101,'pdd',100006,'471133382325','2024-12-25 09:39:24','completed','71148343131','2024-12-25 17:39:21','2024-12-25 17:39:21',1),
(32,'cheng',101,101,'pdd',100006,'613196280303','2024-12-25 09:39:25','completed','60820963468','2024-12-25 17:39:21','2024-12-25 17:39:21',1),
(33,'cheng',101,101,'pdd',100006,'122545533779','2024-12-25 09:39:25','completed','97269920528','2024-12-25 17:39:21','2024-12-25 17:39:21',1),
(34,'cheng',101,101,'pdd',100006,'806735354735','2024-12-25 09:39:25','completed','45140627118','2024-12-25 17:39:21','2024-12-25 17:39:21',1),
(35,'cheng',101,101,'pdd',100006,'262050407550','2024-12-25 09:43:20','completed','16510113006','2024-12-25 17:43:16','2024-12-25 17:43:16',1),
(36,'cheng',101,101,'pdd',100006,'563741529542','2024-12-25 09:43:21','completed','09482850811','2024-12-25 17:43:17','2024-12-25 17:43:17',1),
(37,'cheng',101,101,'pdd',100006,'068360680815','2024-12-25 09:43:21','completed','69063960796','2024-12-25 17:43:17','2024-12-25 17:43:17',1),
(38,'cheng',101,101,'pdd',100006,'878098845568','2024-12-25 09:43:21','completed','38229689315','2024-12-25 17:43:17','2024-12-25 17:43:17',1),
(39,'cheng',101,101,'pdd',100006,'574972703625','2024-12-25 09:43:21','completed','78601417480','2024-12-25 17:43:17','2024-12-25 17:43:17',1),
(40,'cheng',101,101,'pdd',100006,'755370776693','2024-12-25 09:43:21','completed','49815242123','2024-12-25 17:43:17','2024-12-25 17:43:17',1),
(41,'cheng',101,101,'pdd',100006,'690312287852','2024-12-25 09:43:21','completed','87185020106','2024-12-25 17:43:17','2024-12-25 17:43:17',1),
(42,'cheng',101,101,'pdd',100006,'337012561656','2024-12-25 09:43:21','completed','56772947285','2024-12-25 17:43:17','2024-12-25 17:43:17',1),
(43,'cheng',101,101,'pdd',100006,'764482365606','2024-12-25 09:43:22','completed','41126335084','2024-12-25 17:43:18','2024-12-25 17:43:18',1),
(44,'cheng',101,101,'pdd',100006,'663664871381','2024-12-25 09:43:22','completed','86965744357','2024-12-25 17:43:18','2024-12-25 17:43:18',1),
(45,'cheng',101,101,'pdd',100006,'073508981039','2024-12-25 09:43:22','completed','25023014788','2024-12-25 17:43:18','2024-12-25 17:43:18',1),
(46,'cheng',101,101,'pdd',100006,'200722369709','2024-12-25 09:43:22','completed','97963618881','2024-12-25 17:43:18','2024-12-25 17:43:18',1),
(47,'cheng',101,101,'pdd',100006,'851215993108','2024-12-25 09:43:22','completed','59754273669','2024-12-25 17:43:18','2024-12-25 17:43:18',1),
(48,'cheng',101,101,'pdd',100006,'979396536837','2024-12-25 09:43:22','completed','96066784040','2024-12-25 17:43:18','2024-12-25 17:43:18',1),
(49,'cheng',101,101,'pdd',100006,'393067564766','2024-12-25 09:43:22','completed','03957242723','2024-12-25 17:43:19','2024-12-25 17:43:19',1),
(50,'cheng',101,101,'pdd',100006,'735276639748','2024-12-25 09:43:23','completed','26849390010','2024-12-25 17:43:19','2024-12-25 17:43:19',1),
(51,'cheng',101,101,'pdd',100006,'786247106889','2024-12-25 09:43:23','completed','24627155688','2024-12-25 17:43:19','2024-12-25 17:43:19',1),
(52,'cheng',101,101,'pdd',100006,'739994540787','2024-12-25 09:43:23','completed','19502773543','2024-12-25 17:43:19','2024-12-25 17:43:19',1),
(53,'cheng',101,101,'pdd',100006,'499726528859','2024-12-25 09:43:23','completed','04413414252','2024-12-25 17:43:19','2024-12-25 17:43:19',1);

/*Table structure for table `raffle_activity_sku` */

DROP TABLE IF EXISTS `raffle_activity_sku`;

CREATE TABLE `raffle_activity_sku` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `sku` bigint NOT NULL COMMENT '商品sku - 把每一个组合当做一个商品',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `activity_count_id` bigint NOT NULL COMMENT '活动个人参与次数ID',
  `stock_count` int NOT NULL COMMENT '商品库存',
  `stock_count_surplus` int NOT NULL COMMENT '剩余库存',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_sku` (`sku`),
  KEY `idx_activity_id_activity_count_id` (`activity_id`,`activity_count_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `raffle_activity_sku` */

insert  into `raffle_activity_sku`(`id`,`sku`,`activity_id`,`activity_count_id`,`stock_count`,`stock_count_surplus`,`create_time`,`update_time`) values 
(1,101,101,101,20,0,'2024-03-16 11:41:09','2024-12-25 17:43:20');

/*Table structure for table `rule_tree` */

DROP TABLE IF EXISTS `rule_tree`;

CREATE TABLE `rule_tree` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `tree_id` varchar(32) NOT NULL COMMENT '规则树ID',
  `tree_name` varchar(64) NOT NULL COMMENT '规则树名称',
  `tree_desc` varchar(128) DEFAULT NULL COMMENT '规则树描述',
  `tree_root_rule_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则树根入口规则',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_tree_id` (`tree_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `rule_tree` */

insert  into `rule_tree`(`id`,`tree_id`,`tree_name`,`tree_desc`,`tree_root_rule_key`,`create_time`,`update_time`) values 
(1,'tree_lock_1','规则树','规则树','rule_lock','2024-01-27 10:01:59','2024-02-15 07:49:59'),
(2,'tree_luck_award','规则树-兜底奖励','规则树-兜底奖励','rule_stock','2024-02-15 07:35:06','2024-02-15 07:50:20'),
(3,'tree_lock_2','规则树','规则树','rule_lock','2024-01-27 10:01:59','2024-02-15 07:49:59');

/*Table structure for table `rule_tree_node` */

DROP TABLE IF EXISTS `rule_tree_node`;

CREATE TABLE `rule_tree_node` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `tree_id` varchar(32) NOT NULL COMMENT '规则树ID',
  `rule_key` varchar(32) NOT NULL COMMENT '规则Key',
  `rule_desc` varchar(64) NOT NULL COMMENT '规则描述',
  `rule_value` varchar(128) DEFAULT NULL COMMENT '规则比值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `rule_tree_node` */

insert  into `rule_tree_node`(`id`,`tree_id`,`rule_key`,`rule_desc`,`rule_value`,`create_time`,`update_time`) values 
(1,'tree_lock_1','rule_lock','限定用户已完成N次抽奖后解锁','1','2024-01-27 10:03:09','2024-02-15 07:50:57'),
(2,'tree_lock_1','rule_luck_award','兜底奖品随机积分','101:1,100','2024-01-27 10:03:09','2024-02-15 07:51:00'),
(3,'tree_lock_1','rule_stock','库存扣减规则',NULL,'2024-01-27 10:04:43','2024-02-15 07:51:02'),
(4,'tree_luck_award','rule_stock','库存扣减规则',NULL,'2024-02-15 07:35:55','2024-02-15 07:39:19'),
(5,'tree_luck_award','rule_luck_award','兜底奖品随机积分','101:1,100','2024-02-15 07:35:55','2024-02-15 07:39:23'),
(6,'tree_lock_2','rule_lock','限定用户已完成N次抽奖后解锁','2','2024-01-27 10:03:09','2024-02-15 07:52:20'),
(7,'tree_lock_2','rule_luck_award','兜底奖品随机积分','101:1,100','2024-01-27 10:03:09','2024-02-08 19:59:43'),
(8,'tree_lock_2','rule_stock','库存扣减规则',NULL,'2024-01-27 10:04:43','2024-02-03 10:40:21');

/*Table structure for table `rule_tree_node_line` */

DROP TABLE IF EXISTS `rule_tree_node_line`;

CREATE TABLE `rule_tree_node_line` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `tree_id` varchar(32) NOT NULL COMMENT '规则树ID',
  `rule_node_from` varchar(32) NOT NULL COMMENT '规则Key节点 From',
  `rule_node_to` varchar(32) NOT NULL COMMENT '规则Key节点 To',
  `rule_limit_type` varchar(8) NOT NULL COMMENT '限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围];',
  `rule_limit_value` varchar(32) NOT NULL COMMENT '限定值（到下个节点）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `rule_tree_node_line` */

insert  into `rule_tree_node_line`(`id`,`tree_id`,`rule_node_from`,`rule_node_to`,`rule_limit_type`,`rule_limit_value`,`create_time`,`update_time`) values 
(1,'tree_lock_1','rule_lock','rule_stock','EQUAL','ALLOW','0000-00-00 00:00:00','2024-02-15 07:55:08'),
(2,'tree_lock_1','rule_lock','rule_luck_award','EQUAL','TAKE_OVER','0000-00-00 00:00:00','2024-02-15 07:55:11'),
(3,'tree_lock_1','rule_stock','rule_luck_award','EQUAL','ALLOW','0000-00-00 00:00:00','2024-02-15 07:55:13'),
(4,'tree_luck_award','rule_stock','rule_luck_award','EQUAL','TAKE_OVER','2024-02-15 07:37:31','2024-12-17 08:45:07'),
(5,'tree_lock_2','rule_lock','rule_stock','EQUAL','ALLOW','0000-00-00 00:00:00','2024-02-15 07:55:08'),
(6,'tree_lock_2','rule_lock','rule_luck_award','EQUAL','TAKE_OVER','0000-00-00 00:00:00','2024-02-15 07:55:11'),
(7,'tree_lock_2','rule_stock','rule_luck_award','EQUAL','ALLOW','0000-00-00 00:00:00','2024-02-15 07:55:13');

/*Table structure for table `strategy` */

DROP TABLE IF EXISTS `strategy`;

CREATE TABLE `strategy` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `strategy_desc` varchar(128) NOT NULL COMMENT '抽奖策略描述',
  `rule_models` varchar(256) DEFAULT NULL COMMENT '规则模型，rule配置的模型同步到此表，便于使用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_id` (`strategy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `strategy` */

insert  into `strategy`(`id`,`strategy_id`,`strategy_desc`,`rule_models`,`create_time`,`update_time`) values 
(1,100001,'抽奖策略','rule_blacklist,rule_weight','2023-12-09 09:37:19','2024-01-20 11:39:23'),
(2,100003,'抽奖策略-验证lock','rule_blacklist','2024-01-13 10:34:06','2024-01-20 15:03:19'),
(3,100002,'抽奖策略-非完整1概率',NULL,'2023-12-09 09:37:19','2024-02-03 10:14:17'),
(4,100004,'抽奖策略-随机抽奖',NULL,'2023-12-09 09:37:19','2024-01-20 19:21:03'),
(5,100005,'抽奖策略-测试概率计算',NULL,'2023-12-09 09:37:19','2024-01-21 21:54:58'),
(6,100006,'抽奖策略-规则树',NULL,'2024-02-03 09:53:40','2024-02-03 09:53:40');

/*Table structure for table `strategy_award` */

DROP TABLE IF EXISTS `strategy_award`;

CREATE TABLE `strategy_award` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `award_id` int NOT NULL COMMENT '抽奖奖品ID - 内部流转使用',
  `award_title` varchar(128) NOT NULL COMMENT '抽奖奖品标题',
  `award_subtitle` varchar(128) DEFAULT NULL COMMENT '抽奖奖品副标题',
  `award_count` int NOT NULL DEFAULT '0' COMMENT '奖品库存总量',
  `award_count_surplus` int NOT NULL DEFAULT '0' COMMENT '奖品库存剩余',
  `award_rate` decimal(6,4) NOT NULL COMMENT '奖品中奖概率',
  `rule_models` varchar(256) DEFAULT NULL COMMENT '规则模型，rule配置的模型同步到此表，便于使用',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_id_award_id` (`strategy_id`,`award_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `strategy_award` */

insert  into `strategy_award`(`id`,`strategy_id`,`award_id`,`award_title`,`award_subtitle`,`award_count`,`award_count_surplus`,`award_rate`,`rule_models`,`sort`,`create_time`,`update_time`) values 
(1,100001,101,'随机积分',NULL,80000,80000,0.3000,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:09'),
(2,100001,102,'5次使用',NULL,10000,10000,0.2000,'tree_luck_award',2,'2023-12-09 09:39:18','2024-02-15 07:42:11'),
(3,100001,103,'10次使用',NULL,5000,5000,0.2000,'tree_luck_award',3,'2023-12-09 09:42:36','2024-02-15 07:42:12'),
(4,100001,104,'20次使用',NULL,4000,4000,0.1000,'tree_luck_award',4,'2023-12-09 09:43:15','2024-02-15 07:42:12'),
(5,100001,105,'增加gpt-4对话模型',NULL,600,600,0.1000,'tree_luck_award',5,'2023-12-09 09:43:47','2024-02-15 07:42:13'),
(6,100001,106,'增加dall-e-2画图模型',NULL,200,200,0.0500,'tree_luck_award',6,'2023-12-09 09:44:20','2024-02-15 07:42:14'),
(7,100001,107,'增加dall-e-3画图模型','抽奖1次后解锁',200,200,0.0400,'tree_luck_award',7,'2023-12-09 09:45:38','2024-02-15 07:42:17'),
(8,100001,108,'增加100次使用','抽奖2次后解锁',199,199,0.0099,'tree_luck_award',8,'2023-12-09 09:46:02','2024-02-15 07:42:21'),
(9,100001,109,'解锁全部模型','抽奖6次后解锁',1,1,0.0001,'tree_luck_award',9,'2023-12-09 09:46:39','2024-02-15 07:42:26'),
(10,100002,101,'随机积分',NULL,1,1,0.5000,'tree_luck_award',1,'2023-12-09 09:46:39','2024-02-15 07:42:29'),
(11,100002,102,'5次使用',NULL,1,1,0.1000,'tree_luck_award',2,'2023-12-09 09:46:39','2024-02-15 07:42:32'),
(12,100002,106,'增加dall-e-2画图模型',NULL,1,1,0.0100,'tree_luck_award',3,'2023-12-09 09:46:39','2024-02-15 07:42:35'),
(13,100003,107,'增加dall-e-3画图模型','抽奖1次后解锁',200,200,0.0400,'tree_luck_award',7,'2023-12-09 09:45:38','2024-02-15 07:42:38'),
(14,100003,108,'增加100次使用','抽奖2次后解锁',199,199,0.0099,'tree_luck_award',8,'2023-12-09 09:46:02','2024-02-15 07:42:41'),
(15,100003,109,'解锁全部模型','抽奖6次后解锁',1,1,0.0001,'tree_luck_award',9,'2023-12-09 09:46:39','2024-02-15 07:42:44'),
(16,100004,109,'解锁全部模型','抽奖6次后解锁',1,1,1.0000,'tree_luck_award',9,'2023-12-09 09:46:39','2024-02-15 07:42:46'),
(17,100005,101,'随机积分',NULL,80000,80000,0.0300,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:47'),
(18,100005,102,'随机积分',NULL,80000,80000,0.0300,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:48'),
(19,100005,103,'随机积分',NULL,80000,80000,0.0300,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:50'),
(20,100005,104,'随机积分',NULL,80000,80000,0.0300,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:51'),
(21,100005,105,'随机积分',NULL,80000,80000,0.0010,'tree_luck_award',1,'2023-12-09 09:38:31','2024-02-15 07:42:52'),
(22,100006,101,'随机积分',NULL,100,97,0.0200,'tree_luck_award',1,'2023-12-09 09:38:31','2024-12-24 11:48:30'),
(23,100006,102,'7等奖',NULL,100,94,0.0300,'tree_luck_award',2,'2023-12-09 09:38:31','2024-12-18 18:42:15'),
(24,100006,103,'6等奖',NULL,100,89,0.0300,'tree_luck_award',3,'2023-12-09 09:38:31','2024-12-24 12:23:40'),
(25,100006,104,'5等奖',NULL,100,97,0.0300,'tree_luck_award',4,'2023-12-09 09:38:31','2024-12-18 18:40:18'),
(26,100006,105,'4等奖',NULL,100,92,0.0300,'tree_luck_award',5,'2023-12-09 09:38:31','2024-12-24 11:48:40'),
(27,100006,106,'3等奖','抽奖1次后解锁',100,59,0.0300,'tree_lock_1',6,'2023-12-09 09:38:31','2024-12-17 09:47:28'),
(28,100006,107,'2等奖','抽奖1次后解锁',100,60,0.0300,'tree_lock_1',7,'2023-12-09 09:38:31','2024-12-18 18:42:13'),
(29,100006,108,'1等奖','抽奖2次后解锁',100,64,0.0300,'tree_lock_2',8,'2023-12-09 09:38:31','2024-12-18 18:42:28'),
(30,100006,100,'test',NULL,100,100,0.0300,NULL,0,'2024-12-17 09:48:56','2024-12-17 09:48:56');

/*Table structure for table `strategy_rule` */

DROP TABLE IF EXISTS `strategy_rule`;

CREATE TABLE `strategy_rule` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `strategy_id` int NOT NULL COMMENT '抽奖策略ID',
  `award_id` int DEFAULT NULL COMMENT '抽奖奖品ID【规则类型为策略，则不需要奖品ID】',
  `rule_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '抽象规则类型；1-策略规则、2-奖品规则',
  `rule_model` varchar(16) NOT NULL COMMENT '抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】',
  `rule_value` varchar(256) NOT NULL COMMENT '抽奖规则比值',
  `rule_desc` varchar(128) NOT NULL COMMENT '抽奖规则描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_id_award_id` (`strategy_id`,`award_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `strategy_rule` */

insert  into `strategy_rule`(`id`,`strategy_id`,`award_id`,`rule_type`,`rule_model`,`rule_value`,`rule_desc`,`create_time`,`update_time`) values 
(1,100001,101,2,'rule_random','1,1000','随机积分策略','2023-12-09 10:05:30','2023-12-09 12:55:52'),
(2,100001,107,2,'rule_lock','1','抽奖1次后解锁','2023-12-09 10:16:41','2023-12-09 12:55:53'),
(3,100001,108,2,'rule_lock','2','抽奖2次后解锁','2023-12-09 10:17:43','2023-12-09 12:55:54'),
(4,100001,109,2,'rule_lock','6','抽奖6次后解锁','2023-12-09 10:17:43','2023-12-09 12:55:54'),
(5,100001,107,2,'rule_luck_award','1,100','兜底奖品100以内随机积分','2023-12-09 10:30:12','2023-12-09 12:55:55'),
(6,100001,108,2,'rule_luck_award','1,100','兜底奖品100以内随机积分','2023-12-09 10:30:43','2023-12-09 12:55:56'),
(7,100001,101,2,'rule_luck_award','1,10','兜底奖品10以内随机积分','2023-12-09 10:30:43','2023-12-09 12:55:57'),
(8,100001,102,2,'rule_luck_award','1,20','兜底奖品20以内随机积分','2023-12-09 10:30:43','2023-12-09 12:55:57'),
(9,100001,103,2,'rule_luck_award','1,30','兜底奖品30以内随机积分','2023-12-09 10:30:43','2023-12-09 12:55:58'),
(10,100001,104,2,'rule_luck_award','1,40','兜底奖品40以内随机积分','2023-12-09 10:30:43','2023-12-09 12:55:59'),
(11,100001,105,2,'rule_luck_award','1,50','兜底奖品50以内随机积分','2023-12-09 10:30:43','2023-12-09 12:56:00'),
(12,100001,106,2,'rule_luck_award','1,60','兜底奖品60以内随机积分','2023-12-09 10:30:43','2023-12-09 12:56:00'),
(13,100001,NULL,1,'rule_weight','4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109','消耗6000分，必中奖范围','2023-12-09 10:30:43','2023-12-31 14:51:50'),
(14,100001,NULL,1,'rule_blacklist','101:user001,user002,user003','黑名单抽奖，积分兜底','2023-12-09 12:59:45','2024-02-14 18:16:20'),
(15,100003,107,2,'rule_lock','1','抽奖1次后解锁','2023-12-09 10:16:41','2023-12-09 12:55:53'),
(16,100003,108,2,'rule_lock','2','抽奖2次后解锁','2023-12-09 10:17:43','2024-01-13 10:56:48'),
(17,100003,109,2,'rule_lock','6','抽奖6次后解锁','2023-12-09 10:17:43','2023-12-09 12:55:54');

/*Table structure for table `task` */

DROP TABLE IF EXISTS `task`;

CREATE TABLE `task` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(100) NOT NULL COMMENT '用户Id',
  `topic` varchar(32) NOT NULL COMMENT '消息主题',
  `message_id` varchar(32) NOT NULL COMMENT '消息ID',
  `message` varchar(512) NOT NULL COMMENT '消息主体',
  `state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '任务状态；create-创建、completed-完成、fail-失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务表，发送MQ';

/*Data for the table `task` */

/*Table structure for table `user_award_record` */

DROP TABLE IF EXISTS `user_award_record`;

CREATE TABLE `user_award_record` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_id` varchar(12) NOT NULL COMMENT '抽奖订单ID【作为幂等使用】',
  `award_id` int NOT NULL COMMENT '奖品ID',
  `award_title` varchar(128) NOT NULL COMMENT '奖品标题（名称）',
  `award_time` datetime NOT NULL COMMENT '中奖时间',
  `award_state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_award_id` (`strategy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户中奖记录表';

/*Data for the table `user_award_record` */

/*Table structure for table `user_raffle_order` */

DROP TABLE IF EXISTS `user_raffle_order`;

CREATE TABLE `user_raffle_order` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `order_state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  KEY `idx_user_id_activity_id` (`user_id`,`activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户抽奖订单表';

/*Data for the table `user_raffle_order` */

insert  into `user_raffle_order`(`id`,`user_id`,`activity_id`,`activity_name`,`strategy_id`,`order_id`,`order_time`,`order_state`,`create_time`,`update_time`) values 
(1,'cheng',101,'pdd',100006,'22154036641','2024-12-26 02:33:14','create','2024-12-26 10:33:13','2024-12-26 10:33:13');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
