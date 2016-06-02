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
-- Table structure for table `headline`
--

DROP TABLE IF EXISTS `headline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `headline` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(8) NOT NULL,
  `link` varchar(45) NOT NULL,
  `title` varchar(45) NOT NULL,
  `image` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `headline`
--

LOCK TABLES `headline` WRITE;
/*!40000 ALTER TABLE `headline` DISABLE KEYS */;
INSERT INTO `headline` VALUES (1,0,'12','灯具的资料分析与标注','banner2.jpg'),(2,0,'11','Learning jQuery','11__learning-jquery-4th-ed.jpg'),(3,1,'x','618必领券码','573fc11eb6ff22169.png');
/*!40000 ALTER TABLE `headline` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='	';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `microtask`
--

LOCK TABLES `microtask` WRITE;
/*!40000 ALTER TABLE `microtask` DISABLE KEYS */;
INSERT INTO `microtask` VALUES (1,1,'[{\"label\":{\"id\":\"desc0\",\"text\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\"}},{\"image\":{\"id\":\"img\",\"src\":\"no-img.png\"}},{\"label\":{\"id\":\"desc1\",\"text\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后画出相关草图，产出设计方案\"}},{\"label\":{\"id\":\"q1\",\"for\":\"ans1\",\"text\":\"专业背景相关知识\"}},{\"text\":{\"id\":\"ans1\",\"multiline\":\"false\"}},{\"label\":{\"id\":\"q2\",\"for\":\"ans2\",\"text\":\"需求定义\"}},{\"text\":{\"id\":\"ans2\",\"multiline\":\"true\"}},{\"label\":{\"id\":\"q3\",\"for\":\"ans3\",\"text\":\"设计方案\"}},{\"text\":{\"id\":\"ans3\",\"multiline\":\"true\"}},{\"button\":{\"type\":\"int\",\"target\":\"submit\",\"text\":\"提交\"}}]','simple',NULL,NULL),(2,2,'[{\"label\":{\"id\":\"desc0\",\"text\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\"}},{\"image\":{\"id\":\"img\",\"src\":\"no-img.png\"}},{\"label\":{\"id\":\"desc1\",\"text\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后画出相关草图，产出设计方案\"}},{\"label\":{\"id\":\"q1\",\"for\":\"ans1\",\"text\":\"专业背景相关知识\"}},{\"text\":{\"id\":\"ans1\",\"multiline\":\"false\"}},{\"label\":{\"id\":\"q2\",\"for\":\"ans2\",\"text\":\"需求定义\"}},{\"text\":{\"id\":\"ans2\",\"multiline\":\"true\"}},{\"label\":{\"id\":\"q3\",\"for\":\"ans3\",\"text\":\"设计方案\"}},{\"text\":{\"id\":\"ans3\",\"multiline\":\"true\"}},{\"button\":{\"type\":\"int\",\"target\":\"submit\",\"text\":\"提交\"}}]','simple',NULL,NULL),(3,11,'[{\"label\":{\"id\":\"desc0\",\"text\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\"}},{\"image\":{\"id\":\"img\",\"src\":\"no-img.png\"}},{\"label\":{\"id\":\"desc1\",\"text\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后画出相关草图，产出设计方案\"}},{\"label\":{\"id\":\"q1\",\"for\":\"ans1\",\"text\":\"专业背景相关知识\"}},{\"text\":{\"id\":\"ans1\",\"multiline\":\"false\"}},{\"label\":{\"id\":\"q2\",\"for\":\"ans2\",\"text\":\"需求定义\"}},{\"text\":{\"id\":\"ans2\",\"multiline\":\"true\"}},{\"label\":{\"id\":\"q3\",\"for\":\"ans3\",\"text\":\"设计方案\"}},{\"text\":{\"id\":\"ans3\",\"multiline\":\"true\"}},{\"file\":{\"text\":\"草图上传\",\"accept\":\"image/gif, image/jpeg, image/png\"}},{\"button\":{\"type\":\"int\",\"target\":\"submit\",\"text\":\"提交\"}}]','simple',NULL,NULL),(4,12,'[{\"label\":{\"id\":\"desc0\",\"text\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\"}},{\"image\":{\"id\":\"img\",\"src\":\"no-img.png\"}},{\"label\":{\"id\":\"desc1\",\"text\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后画出相关草图，产出设计方案\"}},{\"label\":{\"id\":\"q1\",\"for\":\"ans1\",\"text\":\"专业背景相关知识\"}},{\"text\":{\"id\":\"ans1\",\"multiline\":\"false\"}},{\"label\":{\"id\":\"q2\",\"for\":\"ans2\",\"text\":\"需求定义\"}},{\"text\":{\"id\":\"ans2\",\"multiline\":\"true\"}},{\"label\":{\"id\":\"q3\",\"for\":\"ans3\",\"text\":\"设计方案\"}},{\"text\":{\"id\":\"ans3\",\"multiline\":\"true\"}},{\"file\":{\"text\":\"草图上传\",\"accept\":\"image/gif, image/jpeg, image/png\"}}]','simple',NULL,NULL),(5,13,'[{\"label\":{\"id\":\"desc0\",\"text\":\"1灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\"}},{\"image\":{\"id\":\"img\",\"src\":\"no-img.png\"}},{\"label\":{\"id\":\"desc1\",\"text\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后画出相关草图，产出设计方案\"}},{\"label\":{\"id\":\"q1\",\"for\":\"ans1\",\"text\":\"专业背景相关知识\"}},{\"text\":{\"id\":\"ans1\",\"multiline\":\"false\"}},{\"label\":{\"id\":\"q2\",\"for\":\"ans2\",\"text\":\"需求定义\"}},{\"text\":{\"id\":\"ans2\",\"multiline\":\"true\"}},{\"label\":{\"id\":\"q3\",\"for\":\"ans3\",\"text\":\"设计方案\"}},{\"text\":{\"id\":\"ans3\",\"multiline\":\"true\"}},{\"file\":{\"text\":\"草图上传\",\"accept\":\"image/gif, image/jpeg, image/png\"}}]','simple',NULL,NULL),(6,13,'[{\"label\":{\"id\":\"desc0\",\"text\":\"2灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\"}},{\"image\":{\"id\":\"img\",\"src\":\"no-img.png\"}},{\"label\":{\"id\":\"desc1\",\"text\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后画出相关草图，产出设计方案\"}},{\"label\":{\"id\":\"q1\",\"for\":\"ans1\",\"text\":\"专业背景相关知识\"}},{\"text\":{\"id\":\"ans1\",\"multiline\":\"false\"}},{\"label\":{\"id\":\"q2\",\"for\":\"ans2\",\"text\":\"需求定义\"}},{\"text\":{\"id\":\"ans2\",\"multiline\":\"true\"}},{\"label\":{\"id\":\"q3\",\"for\":\"ans3\",\"text\":\"设计方案\"}},{\"text\":{\"id\":\"ans3\",\"multiline\":\"true\"}},{\"file\":{\"text\":\"草图上传\",\"accept\":\"image/gif, image/jpeg, image/png\"}}]','simple',NULL,NULL),(7,13,'[{\"label\":{\"id\":\"desc0\",\"text\":\"3灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\"}},{\"image\":{\"id\":\"img\",\"src\":\"no-img.png\"}},{\"label\":{\"id\":\"desc1\",\"text\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后画出相关草图，产出设计方案\"}},{\"label\":{\"id\":\"q1\",\"for\":\"ans1\",\"text\":\"专业背景相关知识\"}},{\"text\":{\"id\":\"ans1\",\"multiline\":\"false\"}},{\"label\":{\"id\":\"q2\",\"for\":\"ans2\",\"text\":\"需求定义\"}},{\"text\":{\"id\":\"ans2\",\"multiline\":\"true\"}},{\"label\":{\"id\":\"q3\",\"for\":\"ans3\",\"text\":\"设计方案\"}},{\"text\":{\"id\":\"ans3\",\"multiline\":\"true\"}},{\"file\":{\"text\":\"草图上传\",\"accept\":\"image/gif, image/jpeg, image/png\"}}]','simple',NULL,NULL),(8,13,'[{\"label\":{\"id\":\"desc0\",\"text\":\"4灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\"}},{\"image\":{\"id\":\"img\",\"src\":\"no-img.png\"}},{\"label\":{\"id\":\"desc1\",\"text\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后画出相关草图，产出设计方案\"}},{\"label\":{\"id\":\"q1\",\"for\":\"ans1\",\"text\":\"专业背景相关知识\"}},{\"text\":{\"id\":\"ans1\",\"multiline\":\"false\"}},{\"label\":{\"id\":\"q2\",\"for\":\"ans2\",\"text\":\"需求定义\"}},{\"text\":{\"id\":\"ans2\",\"multiline\":\"true\"}},{\"label\":{\"id\":\"q3\",\"for\":\"ans3\",\"text\":\"设计方案\"}},{\"text\":{\"id\":\"ans3\",\"multiline\":\"true\"}},{\"file\":{\"text\":\"草图上传\",\"accept\":\"image/gif, image/jpeg, image/png\"}}]','simple',NULL,NULL),(9,13,'[{\"label\":{\"id\":\"desc0\",\"text\":\"5灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\"}},{\"image\":{\"id\":\"img\",\"src\":\"no-img.png\"}},{\"label\":{\"id\":\"desc1\",\"text\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后画出相关草图，产出设计方案\"}},{\"label\":{\"id\":\"q1\",\"for\":\"ans1\",\"text\":\"专业背景相关知识\"}},{\"text\":{\"id\":\"ans1\",\"multiline\":\"false\"}},{\"label\":{\"id\":\"q2\",\"for\":\"ans2\",\"text\":\"需求定义\"}},{\"text\":{\"id\":\"ans2\",\"multiline\":\"true\"}},{\"label\":{\"id\":\"q3\",\"for\":\"ans3\",\"text\":\"设计方案\"}},{\"text\":{\"id\":\"ans3\",\"multiline\":\"true\"}},{\"file\":{\"text\":\"草图上传\",\"accept\":\"image/gif, image/jpeg, image/png\"}}]','simple',NULL,NULL);
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
  `image` varchar(512) DEFAULT NULL,
  `tag` varchar(45) DEFAULT NULL,
  `repeatable` int(11) NOT NULL,
  `type` int(2) NOT NULL DEFAULT '0' COMMENT '0 - Normal;\n1 - MTurk;',
  `params` varchar(512) DEFAULT NULL,
  `time_limit` int(10) DEFAULT NULL,
  `optlock` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'创意众包平台',100,0,'{\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"info\":{\"预期任务形式\":\"文字+草图\",\"任务报酬\":\"5元\"},\"est_time\":\"10分钟\"}',0,1462896000,1463889600,5,100,'no-img.png','创意任务',0,0,NULL,NULL,0),(2,'d',100,0,'{\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"info\":{\"预期任务形式\":\"文字+草图\",\"任务报酬\":\"5元\"},\"est_time\":\"10分钟\"}',0,1462896000,1463889600,5,0,'','创意任务',0,0,NULL,NULL,0),(3,'创意众包平台3',100,0,'{\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"info\":{\"预期任务形式\":\"文字+草图\",\"任务报酬\":\"5元\"},\"est_time\":\"10分钟\"}',0,1462896000,1463889600,5,0,'no-img.png','创意任务',0,0,NULL,NULL,0),(4,'灯具的资料分析与标注1',100,0,'{\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"info\":{\"预期任务形式\":\"文字+草图\",\"任务报酬\":\"5元\"},\"est_time\":\"10分钟\"}',0,1462896000,1463889600,5,0,'no-img.png','标注任务',0,0,NULL,NULL,0),(5,'创意众包平台5',100,0,'{\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"info\":{\"预期任务形式\":\"文字+草图\",\"任务报酬\":\"5元\"},\"est_time\":\"10分钟\"}',0,1462896000,1463889600,5,0,'no-img.png','创意任务',0,0,NULL,NULL,0),(6,'创意众包平台6',100,0,'{\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"info\":{\"预期任务形式\":\"文字+草图\",\"任务报酬\":\"5元\"},\"est_time\":\"10分钟\"}',0,1462896000,1463889600,5,100,'no-img.png','创意任务',0,0,NULL,NULL,0),(7,'创意众包平台7',100,0,'{\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"info\":{\"预期任务形式\":\"文字+草图\",\"任务报酬\":\"5元\"},\"est_time\":\"10分钟\"}',0,1462896000,1463889600,5,0,'no-img.png','创意任务',0,0,NULL,NULL,0),(8,'创意众包平台8',100,0,'{\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"info\":{\"预期任务形式\":\"文字+草图\",\"任务报酬\":\"5元\"},\"est_time\":\"10分钟\"}',0,1462896000,1463889600,5,0,'no-img.png','创意任务',0,0,NULL,NULL,0),(9,'创意众包平台9',100,0,'{\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"info\":{\"预期任务形式\":\"文字+草图\",\"任务报酬\":\"5元\"},\"est_time\":\"10分钟\"}',0,1462896000,1463889600,5,0,'','创意任务',0,0,NULL,NULL,0),(10,'灯具的创意分析',100,0,'{\"est_time\":\"10分钟\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"info\":{\"任务报酬\":\"5元\",\"预期任务形式\":\"文字+草图\"}}',0,1462896000,NULL,5,4,'12__u=533482544,484081964&fm=21&gp=0.jpg','创意任务',0,0,NULL,NULL,0),(11,'灯具的创意设计',100,1,'{\"est_time\":\"10分钟\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"info\":{\"任务报酬\":\"5元\",\"预期任务形式\":\"文字+草图\"}}',0,1462896000,1463889600,5,3,'11__learning-jquery-4th-ed.jpg','创意任务',0,0,NULL,NULL,0),(12,'灯具的资料分析与标注',100,3,'{\"est_time\":\"10分钟\",\"desc_detail\":\"在本任务中，您需要首先提交专业背景中相关信息，然后提出需求，最后产出设计方案\",\"desc\":\"灯在生活中无处不在，具有各式各样的形态，功能，操作与交互方式。如日常的台灯、小夜灯、信号灯、聚光灯等。请以灯为主题，为日常中使用的某个灯具，设计一个新的概念方案。\",\"info\":{\"任务报酬\":\"5元\",\"预期任务形式\":\"文字+草图\"}}',0,1462896000,NULL,5,10,'banner2.jpg','标注任务',1,0,NULL,NULL,0),(13,'Sketch a lamp',100,7,'{\"est_time\":\"15mins\",\"desc_detail\":\"\",\"desc\":\"This is a MTurk task.\"}',1,NULL,NULL,5,8,'banner2.jpg','MTurk',0,1,'1',30,1);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tempfile`
--

DROP TABLE IF EXISTS `tempfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tempfile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usermicrotask_id` int(11) NOT NULL,
  `filename` varchar(64) DEFAULT NULL,
  `optlock` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tempfile`
--

LOCK TABLES `tempfile` WRITE;
/*!40000 ALTER TABLE `tempfile` DISABLE KEYS */;
INSERT INTO `tempfile` VALUES (8,9,'9__learning-jquery-4th-ed.jpg',NULL);
/*!40000 ALTER TABLE `tempfile` ENABLE KEYS */;
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
  `pay_method` varchar(32) DEFAULT NULL,
  `pay_account` varchar(128) DEFAULT NULL,
  `accept_rate` double NOT NULL DEFAULT '0.5',
  `nickname` varchar(45) DEFAULT NULL,
  `salt` varchar(45) DEFAULT NULL,
  `token_cookie` varchar(45) DEFAULT NULL,
  `optlock` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (5,'039195f42325681193bd7090c26eafa4','iorange@126.com','M',20,'15695714734','alipay_email',NULL,0.5,NULL,'whoknows1462772249954543251291',NULL,3),(6,'6236165990ab5ccf67bf03f790219c6d','test@test.com',NULL,NULL,NULL,NULL,NULL,0.5,NULL,'whoknows14634684397591628717812','fe15877e8bd04421bf1f90a688cd51e1',0);
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
  `optlock` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usermicrotask`
--

LOCK TABLES `usermicrotask` WRITE;
/*!40000 ALTER TABLE `usermicrotask` DISABLE KEYS */;
INSERT INTO `usermicrotask` VALUES (3,2,6,NULL,0),(4,3,10,'{\"ans3\":\"额额1\",\"ans2\":\"需求定义1\",\"file\":\"4__xymx.png\",\"ans1\":\"没有知识\"}',0),(8,4,18,'{\"ans3\":\"%e2\",\"ans2\":\"\'\'\",\"ans1\":\"什么鬼\"}',0),(11,4,21,'{\"ans3\":\"\'\\\"1324dqw12e \",\"ans2\":\"123不拉不拉\",\"file\":\"11__learning-jquery-4th-ed.jpg\",\"ans1\":\"%122就是这么屌\"}',0),(12,4,22,'{\"ans3\":\"噢噢噢\",\"ans2\":\"啊啊啊\",\"file\":\"12__u=533482544,484081964&fm=21&gp=0.jpg\",\"ans1\":\"呜呜呜\",\"tid\":\"12\"}',0),(13,4,23,NULL,0),(16,6,25,'{\"ans3\":\"vregvd\",\"ans2\":\"g24gh\",\"file\":\"16__b151f8198618367a86dae00029738bd4b21ce598.jpg\",\"ans1\":\"fewf\",\"tid\":\"13\"}',0),(17,9,25,'{\"ans3\":\"132414\",\"ans2\":\"t43r23\",\"file\":\"17__b151f8198618367a86dae00029738bd4b21ce598.jpg\",\"ans1\":\"g4\",\"tid\":\"13\"}',0),(18,7,27,'{\"ans3\":\"333\",\"ans2\":\"222\",\"file\":\"18__鐏叿.jpg\",\"ans1\":\"aaa\",\"tid\":\"13\"}',0),(19,6,27,'{\"ans3\":\"到位前请得起我12额的\",\"ans2\":\"1321 d2ed \",\"file\":\"19__xueyuangh.gif\",\"ans1\":\"fewfw\",\"tid\":\"13\"}',0),(20,8,27,'{\"ans3\":\"g432rf23d32d32cdsf23r23rfvf few\'\'\\\"\\\"\",\"ans2\":\"r32f23\",\"file\":\"20__xueyuangh.gif\",\"ans1\":\"个人\",\"tid\":\"13\"}',0),(21,9,28,'{\"ans3\":\"d31e12ew\",\"ans2\":\"d132e12e\",\"file\":\"21__xueyuangh.gif\",\"ans1\":\"fedf\",\"tid\":\"13\"}',0),(22,8,29,NULL,0),(23,5,30,NULL,0),(24,9,32,'{\"ans3\":\"412\",\"ans2\":\"22312\",\"ans1\":\"方法\",\"tid\":\"13\"}',1);
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
  `user_type` int(2) DEFAULT NULL COMMENT '0 - Normal;\n1- From MTurk;',
  `user_id` int(11) DEFAULT NULL,
  `mturk_id` varchar(45) DEFAULT NULL,
  `task_id` int(11) NOT NULL,
  `curr_usermicrotask_id` int(64) DEFAULT NULL,
  `state` int(2) DEFAULT NULL COMMENT '0 - Unfinished;\n1- Finished;',
  `ref_code` varchar(128) DEFAULT NULL,
  `start_time` int(16) NOT NULL,
  `optlock` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `uid` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usertask`
--

LOCK TABLES `usertask` WRITE;
/*!40000 ALTER TABLE `usertask` DISABLE KEYS */;
INSERT INTO `usertask` VALUES (1,NULL,5,NULL,1,NULL,0,NULL,0,0),(6,NULL,5,NULL,2,3,0,NULL,0,0),(10,NULL,5,NULL,11,NULL,1,NULL,0,0),(17,NULL,5,NULL,10,NULL,0,NULL,0,0),(18,NULL,5,NULL,12,NULL,1,NULL,0,0),(21,NULL,5,NULL,12,NULL,1,NULL,0,0),(22,NULL,5,NULL,12,NULL,1,NULL,0,0),(23,0,5,NULL,12,13,0,NULL,0,0),(25,1,NULL,'test123',13,NULL,1,'9c289781-75db-435f-8230-a17c4a534dbb',0,0),(27,1,NULL,'test456',13,NULL,1,'04132e1a-62d9-478f-aba0-cd35154a7835',0,0),(28,1,NULL,'test333',13,NULL,1,'060cde1f-414e-4bf3-9e46-53cfd130dc4c',0,0),(29,1,NULL,'m4apg7',13,22,0,NULL,0,0),(30,1,NULL,'aaa',13,23,0,NULL,0,0),(32,1,NULL,'test000',13,24,2,NULL,1464865840,2);
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

-- Dump completed on 2016-06-02 19:45:22
