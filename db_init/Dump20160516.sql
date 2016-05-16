-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: creativecrowd
-- ------------------------------------------------------
-- Server version	5.7.12-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `adminuser`
--

DROP TABLE IF EXISTS `adminuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adminuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(256) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adminuser`
--

LOCK TABLES `adminuser` WRITE;
/*!40000 ALTER TABLE `adminuser` DISABLE KEYS */;
/*!40000 ALTER TABLE `adminuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `microtask`
--

DROP TABLE IF EXISTS `microtask`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `microtask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NOT NULL,
  `template` varchar(10240) NOT NULL,
  `handler` varchar(1024) NOT NULL,
  `prev_id` int(11) DEFAULT NULL,
  `next_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `microtask_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='	';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `microtask`
--

LOCK TABLES `microtask` WRITE;
/*!40000 ALTER TABLE `microtask` DISABLE KEYS */;
INSERT INTO `microtask` VALUES (1,1,'[{\"label\":{\"id\":\"desc0\",\"text\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\"}},{\"image\":{\"id\":\"img\",\"src\":\"no-img.png\"}},{\"label\":{\"id\":\"desc1\",\"text\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后画出相关草图，产出设计方案\"}},{\"label\":{\"id\":\"q1\",\"for\":\"ans1\",\"text\":\"专业背景相关知识\"}},{\"text\":{\"id\":\"ans1\",\"multiline\":\"false\"}},{\"label\":{\"id\":\"q2\",\"for\":\"ans2\",\"text\":\"需求定义\"}},{\"text\":{\"id\":\"ans2\",\"multiline\":\"true\"}},{\"label\":{\"id\":\"q3\",\"for\":\"ans3\",\"text\":\"设计方案\"}},{\"text\":{\"id\":\"ans3\",\"multiline\":\"true\"}},{\"button\":{\"type\":\"int\",\"target\":\"submit\",\"text\":\"提交\"}}]','simple',NULL,NULL),(2,2,'[{\"label\":{\"id\":\"desc0\",\"text\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\"}},{\"image\":{\"id\":\"img\",\"src\":\"no-img.png\"}},{\"label\":{\"id\":\"desc1\",\"text\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后画出相关草图，产出设计方案\"}},{\"label\":{\"id\":\"q1\",\"for\":\"ans1\",\"text\":\"专业背景相关知识\"}},{\"text\":{\"id\":\"ans1\",\"multiline\":\"false\"}},{\"label\":{\"id\":\"q2\",\"for\":\"ans2\",\"text\":\"需求定义\"}},{\"text\":{\"id\":\"ans2\",\"multiline\":\"true\"}},{\"label\":{\"id\":\"q3\",\"for\":\"ans3\",\"text\":\"设计方案\"}},{\"text\":{\"id\":\"ans3\",\"multiline\":\"true\"}},{\"button\":{\"type\":\"int\",\"target\":\"submit\",\"text\":\"提交\"}}]','simple',NULL,NULL);
/*!40000 ALTER TABLE `microtask` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) DEFAULT NULL,
  `quota` int(11) NOT NULL,
  `finished_count` int(11) NOT NULL DEFAULT '0',
  `desc_json` varchar(4096) NOT NULL,
  `mode` int(11) NOT NULL,
  `start_time` int(11) DEFAULT NULL,
  `end_time` int(11) DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `claimed_count` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'创意众包平台',100,0,'{\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"img\":\"no-img.png\",\"info\":{\"预期任务形式\":\"文字+草图\",\"任务报酬\":\"5元\"},\"est_time\":\"10分钟\"}',0,1462896000,1463889600,5,0),(2,'创意众包平台2',100,0,'{\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"img\":\"no-img.png\",\"info\":{\"预期任务形式\":\"文字+草图\",\"任务报酬\":\"5元\"},\"est_time\":\"10分钟\"}',0,1462896000,1463889600,5,0);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(128) NOT NULL,
  `email` varchar(128) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `pay_method` int(11) DEFAULT NULL,
  `pay_account` varchar(128) DEFAULT NULL,
  `accept_rate` double NOT NULL DEFAULT '0.5',
  `nickname` varchar(45) DEFAULT NULL,
  `salt` varchar(45) DEFAULT NULL,
  `token_cookie` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (5,'039195f42325681193bd7090c26eafa4','iorange@126.com',NULL,NULL,NULL,NULL,NULL,0.5,NULL,'whoknows1462772249954543251291','1b137775a54b4ffcb25724eb09ea1e23');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usermicrotask`
--

DROP TABLE IF EXISTS `usermicrotask`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usermicrotask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `microtask_id` int(11) NOT NULL,
  `usertask_id` int(11) NOT NULL,
  `results` varchar(10240) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usermicrotask`
--

LOCK TABLES `usermicrotask` WRITE;
/*!40000 ALTER TABLE `usermicrotask` DISABLE KEYS */;
INSERT INTO `usermicrotask` VALUES (3,2,6,NULL);
/*!40000 ALTER TABLE `usermicrotask` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usertask`
--

DROP TABLE IF EXISTS `usertask`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usertask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `curr_usermicrotask_id` int(64) DEFAULT NULL,
  `state` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `user_id_idx` (`user_id`),
  KEY `fk_tid` (`task_id`),
  CONSTRAINT `fk_uid` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `usertask_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usertask`
--

LOCK TABLES `usertask` WRITE;
/*!40000 ALTER TABLE `usertask` DISABLE KEYS */;
INSERT INTO `usertask` VALUES (1,5,1,NULL,0),(6,5,2,3,0);
/*!40000 ALTER TABLE `usertask` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-16 17:02:22
