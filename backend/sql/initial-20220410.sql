-- MySQL dump 10.13  Distrib 8.0.28, for macos11 (arm64)
--
-- Host: 127.0.0.1    Database: we-master
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `gen_table`
--

DROP TABLE IF EXISTS `gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table` (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table`
--

LOCK TABLES `gen_table` WRITE;
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
INSERT INTO `gen_table` VALUES (2,'sys_config','参数配置表',NULL,NULL,'SysConfig','crud','org.wm.system','system','config','参数配置','wm','0','/',NULL,'admin','2022-04-09 12:38:58','',NULL,NULL);
/*!40000 ALTER TABLE `gen_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table_column`
--

DROP TABLE IF EXISTS `gen_table_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table_column` (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table_column`
--

LOCK TABLES `gen_table_column` WRITE;
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
INSERT INTO `gen_table_column` VALUES (11,'2','config_id','参数主键','int','Long','configId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2022-04-09 12:38:58','',NULL),(12,'2','config_name','参数名称','varchar(100)','String','configName','0','0',NULL,'1','1','1','1','LIKE','input','',2,'admin','2022-04-09 12:38:58','',NULL),(13,'2','config_key','参数键名','varchar(100)','String','configKey','0','0',NULL,'1','1','1','1','EQ','input','',3,'admin','2022-04-09 12:38:58','',NULL),(14,'2','config_value','参数键值','varchar(500)','String','configValue','0','0',NULL,'1','1','1','1','EQ','textarea','',4,'admin','2022-04-09 12:38:58','',NULL),(15,'2','config_type','系统内置（Y是 N否）','char(1)','String','configType','0','0',NULL,'1','1','1','1','EQ','select','',5,'admin','2022-04-09 12:38:58','',NULL),(16,'2','create_by','创建者','varchar(64)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',6,'admin','2022-04-09 12:38:58','',NULL),(17,'2','create_time','创建时间','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',7,'admin','2022-04-09 12:38:58','',NULL),(18,'2','update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',8,'admin','2022-04-09 12:38:58','',NULL),(19,'2','update_time','更新时间','datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',9,'admin','2022-04-09 12:38:58','',NULL),(20,'2','remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',10,'admin','2022-04-09 12:38:58','',NULL);
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2022-01-29 22:26:17','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2022-01-29 22:26:17','',NULL,'初始化密码 123456'),(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2022-01-29 22:26:17','',NULL,'深色主题theme-dark，浅色主题theme-light'),(4,'账号自助-验证码开关','sys.account.captchaOnOff','true','Y','admin','2022-01-29 22:26:17','',NULL,'是否开启验证码功能（true开启，false关闭）'),(5,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2022-01-29 22:26:17','',NULL,'是否开启注册用户功能（true开启，false关闭）'),(100,'','testA','testA','N','','2022-03-14 23:27:00','',NULL,NULL),(101,'','testB','testB','N','','2022-03-14 23:27:01','',NULL,NULL),(102,'','testA1','testA1','N','','2022-03-14 23:28:25','',NULL,NULL),(103,'','testB1','testB1','N','','2022-03-14 23:28:25','',NULL,NULL);
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (100,0,'0','若依科技',0,'若依','15888888888','ry@qq.com','0','0','admin','2022-01-29 22:26:14','',NULL),(101,100,'0,100','深圳总公司',1,'若依','15888888888','ry@qq.com','0','0','admin','2022-01-29 22:26:14','',NULL),(102,100,'0,100','长沙分公司',2,'若依','15888888888','ry@qq.com','0','0','admin','2022-01-29 22:26:14','',NULL),(103,101,'0,100,101','研发部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2022-01-29 22:26:14','',NULL),(104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2022-01-29 22:26:14','',NULL),(105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','0','0','admin','2022-01-29 22:26:14','',NULL),(106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','0','0','admin','2022-01-29 22:26:14','',NULL),(107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','0','0','admin','2022-01-29 22:26:14','',NULL),(108,102,'0,100,102','市场部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2022-01-29 22:26:14','',NULL),(109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2022-01-29 22:26:14','',NULL),(200,100,'0,100','测试部门一',3,'test','18909990991','test@gmail.com','0','2','admin','2022-03-28 23:15:46','',NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES (1,1,'男','0','sys_user_sex','','','Y','0','admin','2022-01-29 22:26:17','',NULL,'性别男'),(2,2,'女','1','sys_user_sex','','','N','0','admin','2022-01-29 22:26:17','',NULL,'性别女'),(3,3,'未知','2','sys_user_sex','','','N','0','admin','2022-01-29 22:26:17','',NULL,'性别未知'),(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2022-01-29 22:26:17','',NULL,'显示菜单'),(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2022-01-29 22:26:17','',NULL,'隐藏菜单'),(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2022-01-29 22:26:17','',NULL,'正常状态'),(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2022-01-29 22:26:17','',NULL,'停用状态'),(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2022-01-29 22:26:17','',NULL,'正常状态'),(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2022-01-29 22:26:17','',NULL,'停用状态'),(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2022-01-29 22:26:17','',NULL,'默认分组'),(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2022-01-29 22:26:17','',NULL,'系统分组'),(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2022-01-29 22:26:17','',NULL,'系统默认是'),(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2022-01-29 22:26:17','',NULL,'系统默认否'),(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2022-01-29 22:26:17','',NULL,'通知'),(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2022-01-29 22:26:17','',NULL,'公告'),(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2022-01-29 22:26:17','',NULL,'正常状态'),(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2022-01-29 22:26:17','',NULL,'关闭状态'),(18,1,'新增','1','sys_oper_type','','info','N','0','admin','2022-01-29 22:26:17','',NULL,'新增操作'),(19,2,'修改','2','sys_oper_type','','info','N','0','admin','2022-01-29 22:26:17','',NULL,'修改操作'),(20,3,'删除','3','sys_oper_type','','danger','N','0','admin','2022-01-29 22:26:17','',NULL,'删除操作'),(21,4,'授权','4','sys_oper_type','','primary','N','0','admin','2022-01-29 22:26:17','',NULL,'授权操作'),(22,5,'导出','5','sys_oper_type','','warning','N','0','admin','2022-01-29 22:26:17','',NULL,'导出操作'),(23,6,'导入','6','sys_oper_type','','warning','N','0','admin','2022-01-29 22:26:17','',NULL,'导入操作'),(24,7,'强退','7','sys_oper_type','','danger','N','0','admin','2022-01-29 22:26:17','',NULL,'强退操作'),(25,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2022-01-29 22:26:17','',NULL,'生成操作'),(26,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2022-01-29 22:26:17','',NULL,'清空操作'),(27,1,'成功','0','sys_common_status','','primary','N','0','admin','2022-01-29 22:26:17','',NULL,'正常状态'),(28,2,'失败','1','sys_common_status','','danger','N','0','admin','2022-01-29 22:26:17','',NULL,'停用状态');
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'用户性别','sys_user_sex','0','admin','2022-01-29 22:26:17','',NULL,'用户性别列表'),(2,'菜单状态','sys_show_hide','0','admin','2022-01-29 22:26:17','',NULL,'菜单状态列表'),(3,'系统开关','sys_normal_disable','0','admin','2022-01-29 22:26:17','',NULL,'系统开关列表'),(4,'任务状态','sys_job_status','0','admin','2022-01-29 22:26:17','',NULL,'任务状态列表'),(5,'任务分组','sys_job_group','0','admin','2022-01-29 22:26:17','',NULL,'任务分组列表'),(6,'系统是否','sys_yes_no','0','admin','2022-01-29 22:26:17','',NULL,'系统是否列表'),(7,'通知类型','sys_notice_type','0','admin','2022-01-29 22:26:17','',NULL,'通知类型列表'),(8,'通知状态','sys_notice_status','0','admin','2022-01-29 22:26:17','',NULL,'通知状态列表'),(9,'操作类型','sys_oper_type','0','admin','2022-01-29 22:26:17','',NULL,'操作类型列表'),(10,'系统状态','sys_common_status','0','admin','2022-01-29 22:26:17','',NULL,'登录状态列表');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job`
--

DROP TABLE IF EXISTS `sys_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job`
--

LOCK TABLES `sys_job` WRITE;
/*!40000 ALTER TABLE `sys_job` DISABLE KEYS */;
INSERT INTO `sys_job` VALUES (1,'系统默认（无参）','DEFAULT','ryTask.ryNoParams','0/10 * * * * ?','3','1','1','admin','2022-01-29 22:26:17','',NULL,''),(2,'系统默认（有参）','DEFAULT','ryTask.ryParams(\'ry\')','0/15 * * * * ?','3','1','1','admin','2022-01-29 22:26:17','',NULL,''),(3,'系统默认（多参）','DEFAULT','ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)','0/20 * * * * ?','3','1','1','admin','2022-01-29 22:26:17','',NULL,'');
/*!40000 ALTER TABLE `sys_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job_log`
--

DROP TABLE IF EXISTS `sys_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
  `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job_log`
--

LOCK TABLES `sys_job_log` WRITE;
/*!40000 ALTER TABLE `sys_job_log` DISABLE KEYS */;
INSERT INTO `sys_job_log` VALUES (1,'系统默认（无参）','DEFAULT','ryTask.ryNoParams','系统默认（无参） 总共耗时：4毫秒','0','','2022-04-05 00:27:42');
/*!40000 ALTER TABLE `sys_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_logininfor`
--

DROP TABLE IF EXISTS `sys_logininfor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统访问记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_logininfor`
--

LOCK TABLES `sys_logininfor` WRITE;
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
INSERT INTO `sys_logininfor` VALUES (100,'admin','127.0.0.1','内网IP','Unknown','Unknown','0','????','2022-03-15 22:34:42'),(101,'admin','127.0.0.1','内网IP','Unknown','Unknown','0','????','2022-03-15 22:47:43'),(102,'admin','127.0.0.1','内网IP','Unknown','Unknown','0','????','2022-03-15 23:29:16'),(103,'admin','127.0.0.1','内网IP','Unknown','Unknown','0','????','2022-03-16 22:37:23'),(104,'admin','127.0.0.1','内网IP','Unknown','Unknown','0','????','2022-03-16 23:08:54'),(105,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','?????/????','2022-03-16 23:13:09'),(106,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','?????/????','2022-03-16 23:19:53'),(107,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-16 23:20:15'),(108,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-17 00:13:34'),(109,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-17 00:18:46'),(110,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-17 00:19:10'),(111,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-17 00:19:33'),(112,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-17 21:21:34'),(113,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-17 23:47:46'),(114,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','?????/????','2022-03-18 00:26:58'),(115,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-18 00:27:17'),(116,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-03-18 00:27:26'),(117,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-18 00:29:14'),(118,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-03-18 00:29:19'),(119,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-18 00:30:12'),(120,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-03-18 00:31:17'),(121,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-19 12:33:25'),(122,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-20 12:51:50'),(123,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-20 17:00:17'),(124,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-03-20 17:51:43'),(125,'EMS0001','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-20 17:51:54'),(126,'EMS0001','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-03-20 17:52:17'),(127,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-20 17:52:24'),(128,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-20 22:04:02'),(129,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-21 21:21:19'),(130,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','?????/????','2022-03-21 22:40:15'),(131,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-21 22:40:24'),(132,'admmin','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','登录用户：admmin 不存在','2022-03-22 22:05:53'),(133,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-22 22:06:02'),(134,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-22 22:58:07'),(135,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','?????/????','2022-03-26 22:59:02'),(136,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-26 22:59:08'),(137,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-27 21:29:20'),(138,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-28 21:48:52'),(139,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-29 21:43:53'),(140,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-29 22:18:35'),(141,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-30 21:53:25'),(142,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-30 22:40:33'),(143,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-03-31 19:27:31'),(144,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-03 17:25:44'),(145,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-03 17:56:46'),(146,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-03 22:01:56'),(147,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-03 23:27:18'),(148,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-04 13:52:29'),(149,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-04 13:52:53'),(150,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-04 15:17:34'),(151,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-05 00:13:30'),(152,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-05 21:45:25'),(153,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-06 23:06:37'),(154,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-07 00:40:16'),(155,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-07 00:40:26'),(156,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-07 22:29:10'),(157,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-07 23:19:07'),(158,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-09 12:38:29'),(159,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-09 15:23:16'),(160,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-09 19:57:35'),(161,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-09 20:06:26'),(162,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-09 20:11:35'),(163,'admin12','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','登录用户：admin12 不存在','2022-04-09 20:26:47'),(164,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-09 20:27:01'),(165,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-09 20:27:14'),(166,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-09 20:47:21'),(167,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-09 20:47:28'),(168,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-09 20:48:47'),(169,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-09 20:49:25'),(170,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-09 20:54:49'),(171,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-09 20:54:55'),(172,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-09 20:55:47'),(173,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-09 20:56:05'),(174,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','????','2022-04-09 20:56:52'),(175,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-09 20:56:56'),(176,'18909990999','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','?????','2022-04-09 22:49:30'),(177,'18909990999','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','?????','2022-04-09 22:53:38'),(178,'18909990999','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','??????','2022-04-09 22:55:12'),(179,'18909990999','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','??????','2022-04-09 22:56:26'),(180,'18909990999','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','验证码已失效','2022-04-09 23:01:08'),(181,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','登录成功','2022-04-09 23:08:58'),(182,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-09 23:10:41'),(183,'18909990999','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','验证码错误','2022-04-09 23:59:10'),(184,'18909990999','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','验证码已失效','2022-04-09 23:59:24'),(185,'18909990999','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','验证码已失效','2022-04-10 00:00:47'),(186,'18909990999','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','验证码已失效','2022-04-10 00:01:06'),(187,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','登录成功','2022-04-10 00:30:55'),(188,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-10 00:56:33'),(189,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','登录成功','2022-04-10 00:56:49'),(190,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-10 01:10:26'),(191,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','登录成功','2022-04-10 01:10:31'),(192,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-10 01:11:13'),(193,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','登录成功','2022-04-10 01:11:18'),(194,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','登录成功','2022-04-10 01:43:58'),(195,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','登录成功','2022-04-10 12:17:54'),(196,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','登录成功','2022-04-10 13:26:39'),(197,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','验证码错误','2022-04-10 14:09:40'),(198,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','验证码错误','2022-04-10 14:09:57'),(199,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','验证码错误','2022-04-10 14:10:06'),(200,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','验证码错误','2022-04-10 14:10:24'),(201,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','登录成功','2022-04-10 14:10:46'),(202,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-10 15:01:09'),(203,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','1','验证码已失效','2022-04-10 15:33:16'),(204,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','登录成功','2022-04-10 15:33:31'),(205,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','退出成功','2022-04-10 15:39:06'),(206,'admin','127.0.0.1','内网IP','Chrome 9','Mac OS X','0','登录成功','2022-04-10 15:39:58');
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2007 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系统管理',0,2,'system',NULL,'',1,0,'M','0','0','','BookOutlined','admin','2022-01-29 22:26:14','admin','2022-04-09 13:51:03','系统管理目录'),(2,'系统监控',0,5,'monitor',NULL,'',1,0,'M','1','0','','LinkOutlined','admin','2022-01-29 22:26:14','admin','2022-04-09 23:09:13','系统监控目录'),(3,'系统工具',0,3,'tool',NULL,'',1,0,'M','0','0','','AppstoreOutlined','admin','2022-01-29 22:26:14','',NULL,'系统工具目录'),(4,'若依官网',0,4,'http://ruoyi.com','','',0,0,'M','1','0','','SmileOutlined','admin','2022-01-29 22:26:14','admin','2022-04-09 13:51:18','若依官网地址'),(100,'用户管理',1,1,'user','./TableList','',1,0,'C','0','0','system:user:list','TableOutlined','admin','2022-01-29 22:26:14','',NULL,'用户管理菜单'),(101,'角色管理',1,2,'role','system/role/index','',1,0,'C','0','0','system:role:list','SoundOutlined','admin','2022-01-29 22:26:14','',NULL,'角色管理菜单'),(102,'菜单管理',1,3,'menu','system/menu/index','',1,0,'C','0','0','system:menu:list','CrownOutlined','admin','2022-01-29 22:26:14','',NULL,'菜单管理菜单'),(103,'部门管理',1,4,'dept','system/dept/index','',1,0,'C','0','0','system:dept:list','BookOutlined','admin','2022-01-29 22:26:14','',NULL,'部门管理菜单'),(104,'岗位管理',1,5,'post','system/post/index','',1,0,'C','0','0','system:post:list','LinkOutlined','admin','2022-01-29 22:26:14','',NULL,'岗位管理菜单'),(105,'字典管理',1,6,'dict','system/dict/index','',1,0,'C','0','0','system:dict:list','AppstoreOutlined','admin','2022-01-29 22:26:14','',NULL,'字典管理菜单'),(106,'参数设置',1,7,'config','system/config/index','',1,0,'C','0','0','system:config:list','SmileOutlined','admin','2022-01-29 22:26:14','',NULL,'参数设置菜单'),(107,'通知公告',1,8,'notice','system/notice/index','',1,0,'C','0','0','system:notice:list','TableOutlined','admin','2022-01-29 22:26:14','',NULL,'通知公告菜单'),(108,'日志管理',1,10,'log','','',1,0,'M','0','0','','HistoryOutlined','admin','2022-01-29 22:26:14','admin','2022-04-09 13:55:20','日志管理菜单'),(109,'在线用户',2,1,'online','monitor/online/index','',1,0,'C','0','0','monitor:online:list','CrownOutlined','admin','2022-01-29 22:26:14','',NULL,'在线用户菜单'),(110,'定时任务',2,2,'job','monitor/job/index','',1,0,'C','0','0','monitor:job:list','BookOutlined','admin','2022-01-29 22:26:14','',NULL,'定时任务菜单'),(111,'数据监控',2,3,'druid','monitor/druid/index','',1,0,'C','0','0','monitor:druid:list','LinkOutlined','admin','2022-01-29 22:26:14','',NULL,'数据监控菜单'),(112,'服务监控',2,4,'server','monitor/server/index','',1,0,'C','0','0','monitor:server:list','AppstoreOutlined','admin','2022-01-29 22:26:14','',NULL,'服务监控菜单'),(113,'缓存监控',2,5,'cache','monitor/cache/index','',1,0,'C','0','0','monitor:cache:list','SmileOutlined','admin','2022-01-29 22:26:14','',NULL,'缓存监控菜单'),(115,'代码生成',3,2,'gen','tool/gen/index','',1,0,'C','0','0','tool:gen:list','SoundOutlined','admin','2022-01-29 22:26:14','',NULL,'代码生成菜单'),(116,'系统接口',3,3,'swagger','tool/swagger/index','',1,0,'C','0','0','tool:swagger:list','CrownOutlined','admin','2022-01-29 22:26:14','',NULL,'系统接口菜单'),(500,'操作日志',108,1,'operlog','monitor/operlog/index','',1,0,'C','0','0','monitor:operlog:list','BookOutlined','admin','2022-01-29 22:26:15','',NULL,'操作日志菜单'),(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','',1,0,'C','0','0','monitor:logininfor:list','LinkOutlined','admin','2022-01-29 22:26:15','',NULL,'登录日志菜单'),(1001,'用户查询',100,1,'','','',1,0,'F','0','0','system:user:query','AppstoreOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1002,'用户新增',100,2,'','','',1,0,'F','0','0','system:user:add','SmileOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1003,'用户修改',100,3,'','','',1,0,'F','0','0','system:user:edit','TableOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1004,'用户删除',100,4,'','','',1,0,'F','0','0','system:user:remove','SoundOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1005,'用户导出',100,5,'','','',1,0,'F','0','0','system:user:export','CrownOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1006,'用户导入',100,6,'','','',1,0,'F','0','0','system:user:import','BookOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1007,'重置密码',100,7,'','','',1,0,'F','0','0','system:user:resetPwd','LinkOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1008,'角色查询',101,1,'','','',1,0,'F','0','0','system:role:query','AppstoreOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1009,'角色新增',101,2,'','','',1,0,'F','0','0','system:role:add','SmileOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1010,'角色修改',101,3,'','','',1,0,'F','0','0','system:role:edit','TableOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1011,'角色删除',101,4,'','','',1,0,'F','0','0','system:role:remove','SoundOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1012,'角色导出',101,5,'','','',1,0,'F','0','0','system:role:export','CrownOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1013,'菜单查询',102,1,'','','',1,0,'F','0','0','system:menu:query','BookOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1014,'菜单新增',102,2,'','','',1,0,'F','0','0','system:menu:add','LinkOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1015,'菜单修改',102,3,'','','',1,0,'F','0','0','system:menu:edit','AppstoreOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1016,'菜单删除',102,4,'','','',1,0,'F','0','0','system:menu:remove','SmileOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1017,'部门查询',103,1,'','','',1,0,'F','0','0','system:dept:query','TableOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1018,'部门新增',103,2,'','','',1,0,'F','0','0','system:dept:add','SoundOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1019,'部门修改',103,3,'','','',1,0,'F','0','0','system:dept:edit','CrownOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1020,'部门删除',103,4,'','','',1,0,'F','0','0','system:dept:remove','BookOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1021,'岗位查询',104,1,'','','',1,0,'F','0','0','system:post:query','LinkOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1022,'岗位新增',104,2,'','','',1,0,'F','0','0','system:post:add','AppstoreOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1023,'岗位修改',104,3,'','','',1,0,'F','0','0','system:post:edit','SmileOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1024,'岗位删除',104,4,'','','',1,0,'F','0','0','system:post:remove','TableOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1025,'岗位导出',104,5,'','','',1,0,'F','0','0','system:post:export','SoundOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1026,'字典查询',105,1,'#','','',1,0,'F','0','0','system:dict:query','CrownOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1027,'字典新增',105,2,'#','','',1,0,'F','0','0','system:dict:add','BookOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1028,'字典修改',105,3,'#','','',1,0,'F','0','0','system:dict:edit','LinkOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1029,'字典删除',105,4,'#','','',1,0,'F','0','0','system:dict:remove','AppstoreOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1030,'字典导出',105,5,'#','','',1,0,'F','0','0','system:dict:export','SmileOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1031,'参数查询',106,1,'#','','',1,0,'F','0','0','system:config:query','TableOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1032,'参数新增',106,2,'#','','',1,0,'F','0','0','system:config:add','SoundOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1033,'参数修改',106,3,'#','','',1,0,'F','0','0','system:config:edit','CrownOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1034,'参数删除',106,4,'#','','',1,0,'F','0','0','system:config:remove','BookOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1035,'参数导出',106,5,'#','','',1,0,'F','0','0','system:config:export','LinkOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1036,'公告查询',107,1,'#','','',1,0,'F','0','0','system:notice:query','AppstoreOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1037,'公告新增',107,2,'#','','',1,0,'F','0','0','system:notice:add','SmileOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1038,'公告修改',107,3,'#','','',1,0,'F','0','0','system:notice:edit','TableOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1039,'公告删除',107,4,'#','','',1,0,'F','0','0','system:notice:remove','SoundOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1040,'操作查询',500,1,'#','','',1,0,'F','0','0','monitor:operlog:query','CrownOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1041,'操作删除',500,2,'#','','',1,0,'F','0','0','monitor:operlog:remove','BookOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1042,'日志导出',500,4,'#','','',1,0,'F','0','0','monitor:operlog:export','LinkOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1043,'登录查询',501,1,'#','','',1,0,'F','0','0','monitor:logininfor:query','AppstoreOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1044,'登录删除',501,2,'#','','',1,0,'F','0','0','monitor:logininfor:remove','SmileOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1045,'日志导出',501,3,'#','','',1,0,'F','0','0','monitor:logininfor:export','TableOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1046,'在线查询',109,1,'#','','',1,0,'F','0','0','monitor:online:query','SoundOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1047,'批量强退',109,2,'#','','',1,0,'F','0','0','monitor:online:batchLogout','CrownOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1048,'单条强退',109,3,'#','','',1,0,'F','0','0','monitor:online:forceLogout','BookOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1049,'任务查询',110,1,'#','','',1,0,'F','0','0','monitor:job:query','LinkOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1050,'任务新增',110,2,'#','','',1,0,'F','0','0','monitor:job:add','AppstoreOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1051,'任务修改',110,3,'#','','',1,0,'F','0','0','monitor:job:edit','SmileOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1052,'任务删除',110,4,'#','','',1,0,'F','0','0','monitor:job:remove','TableOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1053,'状态修改',110,5,'#','','',1,0,'F','0','0','monitor:job:changeStatus','SoundOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1054,'任务导出',110,7,'#','','',1,0,'F','0','0','monitor:job:export','CrownOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1055,'生成查询',115,1,'#','','',1,0,'F','0','0','tool:gen:query','BookOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1056,'生成修改',115,2,'#','','',1,0,'F','0','0','tool:gen:edit','LinkOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1057,'生成删除',115,3,'#','','',1,0,'F','0','0','tool:gen:remove','AppstoreOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1058,'导入代码',115,2,'#','','',1,0,'F','0','0','tool:gen:import','SmileOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1059,'预览代码',115,4,'#','','',1,0,'F','0','0','tool:gen:preview','TableOutlined','admin','2022-01-29 22:26:15','',NULL,''),(1060,'生成代码',115,5,'#','','',1,0,'F','0','0','tool:gen:code','SoundOutlined','admin','2022-01-29 22:26:15','',NULL,''),(2002,'123122222',100,2,'1231',NULL,NULL,1,0,'M','0','0',NULL,'teset','admin','2022-03-28 22:33:31','',NULL,''),(2003,'数据字典管理',1,22,'dictData','/system/DictList/data/DictDataList',NULL,1,0,'C','1','0',NULL,'#','admin','2022-04-03 18:53:15','',NULL,''),(2004,'任务调度',1,9,'sysJob','/system/Jobs/index',NULL,1,0,'C','0','0','sysJob','BorderBottomOutlined','admin','2022-04-04 14:06:34','admin','2022-04-09 13:54:26',''),(2005,'调度任务日志',1,11,'sysJobLog','/system/JobLogs',NULL,1,0,'C','1','0',NULL,'#','admin','2022-04-05 00:14:44','',NULL,''),(2006,'首页',0,1,'welcome','welcome/index',NULL,1,0,'M','0','0','','HomeOutlined','admin','2022-04-09 13:50:53','admin','2022-04-09 13:53:43','');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (1,'温馨提醒：2018-07-01 若依新版本发布啦','2',_binary '新版本内容','0','admin','2022-01-29 22:26:17','',NULL,'管理员'),(2,'维护通知：2018-07-01 若依系统凌晨维护','1',_binary '维护内容','0','admin','2022-01-29 22:26:17','',NULL,'管理员');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT INTO `sys_oper_log` VALUES (100,'岗位管理',2,'com.ruoyi.web.controller.system.SysPostController.edit()','PUT',1,'admin',NULL,'/system/post','127.0.0.1','内网IP','{\"postSort\":\"1\",\"flag\":false,\"remark\":\"teset\",\"postId\":1,\"params\":{},\"createBy\":\"admin\",\"createTime\":1648370082000,\"updateBy\":\"admin\",\"postName\":\"董事长\",\"postCode\":\"ceo\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 22:21:28'),(101,'岗位管理',2,'com.ruoyi.web.controller.system.SysPostController.edit()','PUT',1,'admin',NULL,'/system/post','127.0.0.1','内网IP','{\"postSort\":\"1\",\"flag\":false,\"remark\":\"teset11\",\"postId\":1,\"params\":{},\"createBy\":\"admin\",\"createTime\":1648370082000,\"updateBy\":\"admin\",\"postName\":\"董事长\",\"postCode\":\"ceo\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 22:21:38'),(102,'角色管理',1,'com.ruoyi.web.controller.system.SysRoleController.add()','POST',1,'admin',NULL,'/system/role','127.0.0.1','内网IP','{\"flag\":false,\"roleId\":100,\"admin\":false,\"params\":{},\"roleSort\":\"3\",\"deptCheckStrictly\":true,\"createBy\":\"admin\",\"menuCheckStrictly\":true,\"roleKey\":\"test\",\"roleName\":\"test\",\"deptIds\":[],\"menuIds\":[1,101,100,1001,1002,1003,1004,1005,1006,1007,1008],\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 22:51:49'),(103,'定时任务',2,'org.wm.controller.SysJobController.run()','PUT',1,'admin',NULL,'/monitor/job/run','127.0.0.1','内网IP','{\"jobGroup\":\"DEFAULT\",\"jobId\":1,\"misfirePolicy\":\"0\"}','{\"code\":200}',0,NULL,'2022-04-05 00:27:42'),(104,'代码生成',6,'org.wm.controller.GenController.importTableSave()','POST',1,'admin',NULL,'/tool/gen/importTable','127.0.0.1','内网IP','',NULL,1,'Cannot invoke \"String.split(String)\" because \"str\" is null','2022-04-05 22:57:27'),(105,'代码生成',6,'org.wm.controller.GenController.importTableSave()','POST',1,'admin',NULL,'/tool/gen/importTable','127.0.0.1','内网IP','',NULL,1,'Cannot invoke \"String.split(String)\" because \"str\" is null','2022-04-05 22:58:39'),(106,'代码生成',6,'org.wm.controller.GenController.importTableSave()','POST',1,'admin',NULL,'/tool/gen/importTable','127.0.0.1','内网IP','',NULL,1,'Cannot invoke \"String.split(String)\" because \"str\" is null','2022-04-05 22:59:26'),(107,'代码生成',6,'org.wm.controller.GenController.importTableSave()','POST',1,'admin',NULL,'/tool/gen/importTable','127.0.0.1','内网IP','',NULL,1,'Cannot invoke \"String.split(String)\" because \"str\" is null','2022-04-05 23:00:04'),(108,'代码生成',6,'org.wm.controller.GenController.importTableSave()','POST',1,'admin',NULL,'/tool/gen/importTable','127.0.0.1','内网IP','sys_config','{\"code\":200}',0,NULL,'2022-04-05 23:00:41'),(109,'代码生成',2,'org.wm.controller.GenController.editSave()','PUT',1,'admin',NULL,'/tool/gen','127.0.0.1','内网IP','{\"sub\":false,\"functionAuthor\":\"123\",\"columns\":[{\"capJavaField\":\"ConfigId\",\"usableColumn\":false,\"columnId\":1,\"isIncrement\":\"1\",\"increment\":true,\"insert\":true,\"dictType\":\"\",\"required\":false,\"superColumn\":false,\"updateBy\":\"\",\"isInsert\":\"1\",\"javaField\":\"configId\",\"htmlType\":\"input\",\"edit\":false,\"query\":false,\"columnComment\":\"参数主键\",\"sort\":1,\"list\":false,\"javaType\":\"Long\",\"queryType\":\"EQ\",\"columnType\":\"int\",\"createBy\":\"admin\",\"isPk\":\"1\",\"createTime\":1649170841000,\"tableId\":1,\"pk\":true,\"columnName\":\"config_id\"},{\"capJavaField\":\"ConfigName\",\"usableColumn\":false,\"columnId\":2,\"isIncrement\":\"0\",\"increment\":false,\"insert\":true,\"isList\":\"1\",\"dictType\":\"\",\"required\":false,\"superColumn\":false,\"updateBy\":\"\",\"isInsert\":\"1\",\"javaField\":\"configName\",\"htmlType\":\"input\",\"edit\":true,\"query\":true,\"columnComment\":\"参数名称\",\"isQuery\":\"1\",\"sort\":2,\"list\":true,\"javaType\":\"String\",\"queryType\":\"LIKE\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"isPk\":\"0\",\"createTime\":1649170841000,\"isEdit\":\"1\",\"tableId\":1,\"pk\":false,\"columnName\":\"config_name\"},{\"capJavaField\":\"ConfigKey\",\"usableColumn\":false,\"columnId\":3,\"isIncrement\":\"0\",\"increment\":false,\"insert\":true,\"isList\":\"1\",\"dictType\":\"\",\"required\":false,\"superColumn\":false,\"updateBy\":\"\",\"isInsert\":\"1\",\"javaField\":\"configKey\",\"htmlType\":\"input\",\"edit\":true,\"query\":true,\"columnComment\":\"参数键名\",\"isQuery\":\"1\",\"sort\":3,\"list\":true,\"javaType\":\"String\",\"queryType\":\"EQ\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"isPk\":\"0\",\"createTime\":1649170841000,\"isEdit\":\"1\",\"tableId\":1,\"pk\":false,\"columnName\":\"config_key\"},{\"capJavaField\":\"ConfigValue\",\"usableColumn\":false,\"columnId\":4,\"isIncrement\":\"0\",\"increment\":false,\"insert\":true,\"isList\":\"1\",\"dictType\":\"\",\"required\":false,\"superColumn\":false,\"updateBy\":\"\",\"isInsert\":\"1\",\"javaField\":\"configValue\",\"htmlType\":\"textarea\",\"edit\":true,\"query\":true,\"columnComment\":\"参数键值\",\"isQuery\":\"1\",\"sort\":4,\"list\":true,\"javaType\":\"String\",\"queryType\":\"EQ\",\"columnType\":\"varchar(500)\",\"createBy\":\"admin\",\"isPk\":\"0\",\"createTime\":1649170','{\"code\":200}',0,NULL,'2022-04-07 00:45:34'),(110,'代码生成',2,'org.wm.controller.GenController.synchDb()','GET',1,'admin',NULL,'/tool/gen/synchDb/sys_config','127.0.0.1','内网IP','{tableName=sys_config}','{\"code\":200}',0,NULL,'2022-04-07 22:32:56'),(111,'代码生成',8,'org.wm.controller.GenController.batchGenCode()','GET',1,'admin',NULL,'/tool/gen/batchGenCode','127.0.0.1','内网IP','{}',NULL,0,NULL,'2022-04-07 23:43:33'),(112,'代码生成',2,'org.wm.controller.GenController.editSave()','PUT',1,'admin',NULL,'/tool/gen','127.0.0.1','内网IP','{\"sub\":false,\"functionAuthor\":\"123\",\"columns\":[],\"businessName\":\"123\",\"moduleName\":\"123\",\"className\":\"123\",\"remark\":\"123\",\"tableName\":\"testte\",\"crud\":false,\"updateBy\":\"\",\"options\":\"{\\\"parentMenuId\\\":\\\"123\\\"}\",\"genType\":\"0\",\"packageName\":\"123\",\"functionName\":\"123\",\"tree\":false,\"tableComment\":\"1231\",\"params\":{\"parentMenuId\":\"123\"},\"tplCategory\":\"1231\",\"createBy\":\"admin\",\"parentMenuId\":\"123\",\"createTime\":1649170841000,\"tableId\":1,\"genPath\":\"12312312\"}','{\"code\":200}',0,NULL,'2022-04-07 23:47:03'),(113,'代码生成',3,'org.wm.controller.GenController.remove()','DELETE',1,'admin',NULL,'/tool/gen/1','127.0.0.1','内网IP','{tableIds=1}','{\"code\":200}',0,NULL,'2022-04-08 00:04:43'),(114,'代码生成',6,'org.wm.controller.GenController.importTableSave()','POST',1,'admin',NULL,'/tool/gen/importTable','127.0.0.1','内网IP','sys_config','{\"code\":200}',0,NULL,'2022-04-09 12:38:58');
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` VALUES (1,'ceo','董事长',1,'0','admin','2022-01-29 22:26:14','admin','2022-03-29 22:26:36','test1231'),(2,'se','项目经理',2,'0','admin','2022-01-29 22:26:14','',NULL,''),(3,'hr','人力资源',3,'0','admin','2022-01-29 22:26:14','',NULL,''),(4,'user','普通员工',4,'0','admin','2022-01-29 22:26:14','',NULL,'');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2022-01-29 22:26:14','',NULL,'超级管理员'),(2,'普通角色','common',2,'2',0,0,'0','0','admin','2022-01-29 22:26:14','admin','2022-04-03 23:27:48','普通角色'),(103,'测试一','ROLE_KEY1',3,'1',0,0,'0','2','admin','2022-03-22 23:14:03','admin','2022-03-22 23:28:05','test1231245'),(104,'test','teset',3,'1',0,0,'0','0','admin','2022-03-29 23:04:50','admin','2022-03-29 23:07:19','testt');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和部门关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_dept`
--

LOCK TABLES `sys_role_dept` WRITE;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
INSERT INTO `sys_role_dept` VALUES (2,100),(2,101),(2,105);
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (2,2),(2,4),(2,101),(2,102),(2,103),(2,104),(2,105),(2,106),(2,107),(2,108),(2,109),(2,110),(2,111),(2,112),(2,113),(2,115),(2,116),(2,500),(2,501),(2,1001),(2,1002),(2,1003),(2,1004),(2,1005),(2,1006),(2,1007),(2,1008),(2,1009),(2,1010),(2,1011),(2,1012),(2,1013),(2,1014),(2,1015),(2,1016),(2,1017),(2,1018),(2,1019),(2,1020),(2,1021),(2,1022),(2,1023),(2,1024),(2,1025),(2,1026),(2,1027),(2,1028),(2,1029),(2,1030),(2,1031),(2,1032),(2,1033),(2,1034),(2,1035),(2,1036),(2,1037),(2,1038),(2,1039),(2,1040),(2,1041),(2,1042),(2,1043),(2,1044),(2,1045),(2,1046),(2,1047),(2,1048),(2,1049),(2,1050),(2,1051),(2,1052),(2,1053),(2,1054),(2,1055),(2,1056),(2,1057),(2,1058),(2,1059),(2,1060),(104,1001),(104,1002),(104,1003),(104,1004),(104,1005),(104,1006),(104,2002);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,103,'admin','若依','00','ry@163.com','15888888888','1','/icons/icon-auatar.jpg','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2022-04-10 15:39:59','admin','2022-01-29 22:26:14','','2022-04-10 15:39:58','管理员'),(2,105,'ry','若依','00','ry@qq.com','15666666666','1','/icons/icon-auatar.jpg','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2022-01-29 22:26:14','admin','2022-01-29 22:26:14','',NULL,'测试员'),(100,101,'EMS0001','测试用户一','00','test@gmail.com','18909990999','1','/icons/icon-auatar.jpg','$2a$10$4WQsjT8hYq8kusMAPMx1HOoTxXRYtRDCljjOLZVr.ORLwllcq1KlW','0','2','127.0.0.1','2022-03-20 17:51:55','admin','2022-03-20 17:51:15','admin','2022-03-20 18:37:38','test1231');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与岗位关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_post`
--

LOCK TABLES `sys_user_post` WRITE;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-10 22:31:26
