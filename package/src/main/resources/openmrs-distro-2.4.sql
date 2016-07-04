-- MySQL dump 10.13  Distrib 5.6.30, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: openmrs-refappp2
-- ------------------------------------------------------
-- Server version	5.6.30-0ubuntu0.14.04.1

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
-- Table structure for table `active_list`
--

DROP TABLE IF EXISTS `active_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `active_list` (
  `active_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `active_list_type_id` int(11) NOT NULL,
  `person_id` int(11) NOT NULL,
  `concept_id` int(11) NOT NULL,
  `start_obs_id` int(11) DEFAULT NULL,
  `stop_obs_id` int(11) DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`active_list_id`),
  KEY `user_who_voided_active_list` (`voided_by`),
  KEY `user_who_created_active_list` (`creator`),
  KEY `active_list_type_of_active_list` (`active_list_type_id`),
  KEY `person_of_active_list` (`person_id`),
  KEY `concept_active_list` (`concept_id`),
  KEY `start_obs_active_list` (`start_obs_id`),
  KEY `stop_obs_active_list` (`stop_obs_id`),
  CONSTRAINT `active_list_type_of_active_list` FOREIGN KEY (`active_list_type_id`) REFERENCES `active_list_type` (`active_list_type_id`),
  CONSTRAINT `concept_active_list` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `person_of_active_list` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`),
  CONSTRAINT `start_obs_active_list` FOREIGN KEY (`start_obs_id`) REFERENCES `obs` (`obs_id`),
  CONSTRAINT `stop_obs_active_list` FOREIGN KEY (`stop_obs_id`) REFERENCES `obs` (`obs_id`),
  CONSTRAINT `user_who_created_active_list` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_active_list` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `active_list`
--

LOCK TABLES `active_list` WRITE;
/*!40000 ALTER TABLE `active_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `active_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `active_list_allergy`
--

DROP TABLE IF EXISTS `active_list_allergy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `active_list_allergy` (
  `active_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `allergy_type` varchar(50) DEFAULT NULL,
  `reaction_concept_id` int(11) DEFAULT NULL,
  `severity` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`active_list_id`),
  KEY `reaction_allergy` (`reaction_concept_id`),
  CONSTRAINT `reaction_allergy` FOREIGN KEY (`reaction_concept_id`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `active_list_allergy`
--

LOCK TABLES `active_list_allergy` WRITE;
/*!40000 ALTER TABLE `active_list_allergy` DISABLE KEYS */;
/*!40000 ALTER TABLE `active_list_allergy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `active_list_problem`
--

DROP TABLE IF EXISTS `active_list_problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `active_list_problem` (
  `active_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(50) DEFAULT NULL,
  `sort_weight` double DEFAULT NULL,
  PRIMARY KEY (`active_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `active_list_problem`
--

LOCK TABLES `active_list_problem` WRITE;
/*!40000 ALTER TABLE `active_list_problem` DISABLE KEYS */;
/*!40000 ALTER TABLE `active_list_problem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `active_list_type`
--

DROP TABLE IF EXISTS `active_list_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `active_list_type` (
  `active_list_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`active_list_type_id`),
  KEY `user_who_retired_active_list_type` (`retired_by`),
  KEY `user_who_created_active_list_type` (`creator`),
  CONSTRAINT `user_who_created_active_list_type` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_active_list_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `active_list_type`
--

LOCK TABLES `active_list_type` WRITE;
/*!40000 ALTER TABLE `active_list_type` DISABLE KEYS */;
INSERT INTO `active_list_type` VALUES (1,'Allergy','An Allergy the Patient may have',1,'2010-05-28 00:00:00',0,NULL,NULL,NULL,'96f4f603-6a99-11df-a648-37a07f9c90fb'),(2,'Problem','A Problem the Patient may have',1,'2010-05-28 00:00:00',0,NULL,NULL,NULL,'a0c7422b-6a99-11df-a648-37a07f9c90fb');
/*!40000 ALTER TABLE `active_list_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `allergy`
--

DROP TABLE IF EXISTS `allergy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `allergy` (
  `allergy_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL,
  `severity_concept_id` int(11) DEFAULT NULL,
  `coded_allergen` int(11) NOT NULL,
  `non_coded_allergen` varchar(255) DEFAULT NULL,
  `allergen_type` varchar(50) NOT NULL,
  `comment` varchar(1024) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(4) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) DEFAULT NULL,
  PRIMARY KEY (`allergy_id`),
  UNIQUE KEY `allergy_id` (`allergy_id`),
  KEY `allergy_patient_id_fk` (`patient_id`),
  KEY `allergy_coded_allergen_fk` (`coded_allergen`),
  KEY `allergy_severity_concept_id_fk` (`severity_concept_id`),
  KEY `allergy_creator_fk` (`creator`),
  KEY `allergy_changed_by_fk` (`changed_by`),
  KEY `allergy_voided_by_fk` (`voided_by`),
  CONSTRAINT `allergy_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `allergy_coded_allergen_fk` FOREIGN KEY (`coded_allergen`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `allergy_creator_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `allergy_patient_id_fk` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `allergy_severity_concept_id_fk` FOREIGN KEY (`severity_concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `allergy_voided_by_fk` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `allergy`
--

LOCK TABLES `allergy` WRITE;
/*!40000 ALTER TABLE `allergy` DISABLE KEYS */;
/*!40000 ALTER TABLE `allergy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `allergy_reaction`
--

DROP TABLE IF EXISTS `allergy_reaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `allergy_reaction` (
  `allergy_reaction_id` int(11) NOT NULL AUTO_INCREMENT,
  `allergy_id` int(11) NOT NULL,
  `reaction_concept_id` int(11) NOT NULL,
  `reaction_non_coded` varchar(255) DEFAULT NULL,
  `uuid` char(38) DEFAULT NULL,
  PRIMARY KEY (`allergy_reaction_id`),
  UNIQUE KEY `allergy_reaction_id` (`allergy_reaction_id`),
  KEY `allergy_reaction_allergy_id_fk` (`allergy_id`),
  KEY `allergy_reaction_reaction_concept_id_fk` (`reaction_concept_id`),
  CONSTRAINT `allergy_reaction_allergy_id_fk` FOREIGN KEY (`allergy_id`) REFERENCES `allergy` (`allergy_id`),
  CONSTRAINT `allergy_reaction_reaction_concept_id_fk` FOREIGN KEY (`reaction_concept_id`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `allergy_reaction`
--

LOCK TABLES `allergy_reaction` WRITE;
/*!40000 ALTER TABLE `allergy_reaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `allergy_reaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appframework_component_state`
--

DROP TABLE IF EXISTS `appframework_component_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appframework_component_state` (
  `component_state_id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `component_id` varchar(255) NOT NULL,
  `component_type` varchar(50) NOT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`component_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appframework_component_state`
--

LOCK TABLES `appframework_component_state` WRITE;
/*!40000 ALTER TABLE `appframework_component_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `appframework_component_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appframework_user_app`
--

DROP TABLE IF EXISTS `appframework_user_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appframework_user_app` (
  `app_id` varchar(50) NOT NULL,
  `json` mediumtext NOT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appframework_user_app`
--

LOCK TABLES `appframework_user_app` WRITE;
/*!40000 ALTER TABLE `appframework_user_app` DISABLE KEYS */;
/*!40000 ALTER TABLE `appframework_user_app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointmentscheduling_appointment`
--

DROP TABLE IF EXISTS `appointmentscheduling_appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointmentscheduling_appointment` (
  `appointment_id` int(11) NOT NULL AUTO_INCREMENT,
  `time_slot_id` int(11) NOT NULL,
  `visit_id` int(11) DEFAULT NULL,
  `patient_id` int(11) NOT NULL,
  `appointment_type_id` int(11) NOT NULL,
  `status` varchar(255) NOT NULL,
  `reason` varchar(1024) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(4) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `cancel_reason` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`appointment_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `appointment_creator` (`creator`),
  KEY `appointment_changed_by` (`changed_by`),
  KEY `appointment_voided_by` (`voided_by`),
  KEY `appointment_time_slot_id` (`time_slot_id`),
  KEY `appointment_appointment_type_id` (`appointment_type_id`),
  KEY `appointment_visit_id` (`visit_id`),
  KEY `appointment_patient_id` (`patient_id`),
  CONSTRAINT `appointment_appointment_type_id` FOREIGN KEY (`appointment_type_id`) REFERENCES `appointmentscheduling_appointment_type` (`appointment_type_id`),
  CONSTRAINT `appointment_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `appointment_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `appointment_patient_id` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `appointment_time_slot_id` FOREIGN KEY (`time_slot_id`) REFERENCES `appointmentscheduling_time_slot` (`time_slot_id`),
  CONSTRAINT `appointment_visit_id` FOREIGN KEY (`visit_id`) REFERENCES `visit` (`visit_id`),
  CONSTRAINT `appointment_voided_by` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointmentscheduling_appointment`
--

LOCK TABLES `appointmentscheduling_appointment` WRITE;
/*!40000 ALTER TABLE `appointmentscheduling_appointment` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointmentscheduling_appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointmentscheduling_appointment_block`
--

DROP TABLE IF EXISTS `appointmentscheduling_appointment_block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointmentscheduling_appointment_block` (
  `appointment_block_id` int(11) NOT NULL AUTO_INCREMENT,
  `location_id` int(11) NOT NULL,
  `provider_id` int(11) DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(4) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`appointment_block_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `appointment_block_creator` (`creator`),
  KEY `appointment_block_changed_by` (`changed_by`),
  KEY `appointment_block_voided_by` (`voided_by`),
  KEY `appointment_block_location_id` (`location_id`),
  KEY `appointment_block_provider_id` (`provider_id`),
  CONSTRAINT `appointment_block_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `appointment_block_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `appointment_block_location_id` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `appointment_block_provider_id` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`provider_id`),
  CONSTRAINT `appointment_block_voided_by` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointmentscheduling_appointment_block`
--

LOCK TABLES `appointmentscheduling_appointment_block` WRITE;
/*!40000 ALTER TABLE `appointmentscheduling_appointment_block` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointmentscheduling_appointment_block` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointmentscheduling_appointment_request`
--

DROP TABLE IF EXISTS `appointmentscheduling_appointment_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointmentscheduling_appointment_request` (
  `appointment_request_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL,
  `appointment_type_id` int(11) NOT NULL,
  `status` varchar(255) NOT NULL,
  `provider_id` int(11) DEFAULT NULL,
  `requested_by` int(11) DEFAULT NULL,
  `requested_on` datetime NOT NULL,
  `min_time_frame_value` int(11) DEFAULT NULL,
  `min_time_frame_units` varchar(255) DEFAULT NULL,
  `max_time_frame_value` int(11) DEFAULT NULL,
  `max_time_frame_units` varchar(255) DEFAULT NULL,
  `notes` varchar(1024) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(4) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`appointment_request_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `appointment_request_creator` (`creator`),
  KEY `appointment_request_changed_by` (`changed_by`),
  KEY `appointment_request_voided_by` (`voided_by`),
  KEY `appointment_request_appointment_type_id` (`appointment_type_id`),
  KEY `appointment_request_patient_id` (`patient_id`),
  KEY `appointment_request_provider_id` (`provider_id`),
  KEY `appointment_request_requested_by` (`requested_by`),
  CONSTRAINT `appointment_request_appointment_type_id` FOREIGN KEY (`appointment_type_id`) REFERENCES `appointmentscheduling_appointment_type` (`appointment_type_id`),
  CONSTRAINT `appointment_request_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `appointment_request_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `appointment_request_patient_id` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `appointment_request_provider_id` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`provider_id`),
  CONSTRAINT `appointment_request_requested_by` FOREIGN KEY (`requested_by`) REFERENCES `provider` (`provider_id`),
  CONSTRAINT `appointment_request_voided_by` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointmentscheduling_appointment_request`
--

LOCK TABLES `appointmentscheduling_appointment_request` WRITE;
/*!40000 ALTER TABLE `appointmentscheduling_appointment_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointmentscheduling_appointment_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointmentscheduling_appointment_status_history`
--

DROP TABLE IF EXISTS `appointmentscheduling_appointment_status_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointmentscheduling_appointment_status_history` (
  `appointment_status_history_id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_id` int(11) NOT NULL,
  `status` varchar(255) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  PRIMARY KEY (`appointment_status_history_id`),
  KEY `appointment_status_history_appointment` (`appointment_id`),
  CONSTRAINT `appointment_status_history_appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointmentscheduling_appointment` (`appointment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointmentscheduling_appointment_status_history`
--

LOCK TABLES `appointmentscheduling_appointment_status_history` WRITE;
/*!40000 ALTER TABLE `appointmentscheduling_appointment_status_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointmentscheduling_appointment_status_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointmentscheduling_appointment_type`
--

DROP TABLE IF EXISTS `appointmentscheduling_appointment_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointmentscheduling_appointment_type` (
  `appointment_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `duration` int(11) NOT NULL,
  `uuid` char(38) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(4) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `confidential` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`appointment_type_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `appointment_type_creator` (`creator`),
  KEY `appointment_type_changed_by` (`changed_by`),
  KEY `appointment_type_retired_by` (`retired_by`),
  CONSTRAINT `appointment_type_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `appointment_type_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `appointment_type_retired_by` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointmentscheduling_appointment_type`
--

LOCK TABLES `appointmentscheduling_appointment_type` WRITE;
/*!40000 ALTER TABLE `appointmentscheduling_appointment_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointmentscheduling_appointment_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointmentscheduling_block_type_map`
--

DROP TABLE IF EXISTS `appointmentscheduling_block_type_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointmentscheduling_block_type_map` (
  `appointment_type_id` int(11) NOT NULL,
  `appointment_block_id` int(11) NOT NULL,
  PRIMARY KEY (`appointment_type_id`,`appointment_block_id`),
  KEY `appointment_block_type_map_appointment_block_id` (`appointment_block_id`),
  CONSTRAINT `appointment_block_type_map_appointment_block_id` FOREIGN KEY (`appointment_block_id`) REFERENCES `appointmentscheduling_appointment_block` (`appointment_block_id`),
  CONSTRAINT `appointment_block_type_map_appointment_type_id` FOREIGN KEY (`appointment_type_id`) REFERENCES `appointmentscheduling_appointment_type` (`appointment_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointmentscheduling_block_type_map`
--

LOCK TABLES `appointmentscheduling_block_type_map` WRITE;
/*!40000 ALTER TABLE `appointmentscheduling_block_type_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointmentscheduling_block_type_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointmentscheduling_time_slot`
--

DROP TABLE IF EXISTS `appointmentscheduling_time_slot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointmentscheduling_time_slot` (
  `time_slot_id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_block_id` int(11) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(4) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`time_slot_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `appointment_slot_creator` (`creator`),
  KEY `appointment_slot__changed_by` (`changed_by`),
  KEY `appointment_slot_voided_by` (`voided_by`),
  KEY `appointment_slot_appointment_block_id` (`appointment_block_id`),
  CONSTRAINT `appointment_slot__changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `appointment_slot_appointment_block_id` FOREIGN KEY (`appointment_block_id`) REFERENCES `appointmentscheduling_appointment_block` (`appointment_block_id`),
  CONSTRAINT `appointment_slot_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `appointment_slot_voided_by` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointmentscheduling_time_slot`
--

LOCK TABLES `appointmentscheduling_time_slot` WRITE;
/*!40000 ALTER TABLE `appointmentscheduling_time_slot` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointmentscheduling_time_slot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calculation_registration`
--

DROP TABLE IF EXISTS `calculation_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calculation_registration` (
  `calculation_registration_id` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `provider_class_name` varchar(512) NOT NULL,
  `calculation_name` varchar(512) NOT NULL,
  `configuration` text,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`calculation_registration_id`),
  UNIQUE KEY `uuid` (`uuid`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calculation_registration`
--

LOCK TABLES `calculation_registration` WRITE;
/*!40000 ALTER TABLE `calculation_registration` DISABLE KEYS */;
/*!40000 ALTER TABLE `calculation_registration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `care_setting`
--

DROP TABLE IF EXISTS `care_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `care_setting` (
  `care_setting_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `care_setting_type` varchar(50) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`care_setting_id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `care_setting_creator` (`creator`),
  KEY `care_setting_retired_by` (`retired_by`),
  KEY `care_setting_changed_by` (`changed_by`),
  CONSTRAINT `care_setting_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `care_setting_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `care_setting_retired_by` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `care_setting`
--

LOCK TABLES `care_setting` WRITE;
/*!40000 ALTER TABLE `care_setting` DISABLE KEYS */;
INSERT INTO `care_setting` VALUES (1,'Outpatient','Out-patient care setting','OUTPATIENT',1,'2013-12-27 00:00:00',0,NULL,NULL,NULL,NULL,NULL,'6f0c9a92-6f24-11e3-af88-005056821db0'),(2,'Inpatient','In-patient care setting','INPATIENT',1,'2013-12-27 00:00:00',0,NULL,NULL,NULL,NULL,NULL,'c365e560-c3ec-11e3-9c1a-0800200c9a66');
/*!40000 ALTER TABLE `care_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chartsearch_bookmark`
--

DROP TABLE IF EXISTS `chartsearch_bookmark`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chartsearch_bookmark` (
  `bookmark_id` int(11) NOT NULL AUTO_INCREMENT,
  `bookmark_name` text NOT NULL,
  `search_phrase` text NOT NULL,
  `selected_categories` text,
  `uuid` char(38) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `default_search` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`bookmark_id`),
  KEY `bookmark_owner-fk` (`user_id`),
  KEY `bookmark_patient-fk` (`patient_id`),
  CONSTRAINT `bookmark_owner-fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `bookmark_patient-fk` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chartsearch_bookmark`
--

LOCK TABLES `chartsearch_bookmark` WRITE;
/*!40000 ALTER TABLE `chartsearch_bookmark` DISABLE KEYS */;
/*!40000 ALTER TABLE `chartsearch_bookmark` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chartsearch_categories`
--

DROP TABLE IF EXISTS `chartsearch_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chartsearch_categories` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `filter_query` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chartsearch_categories`
--

LOCK TABLES `chartsearch_categories` WRITE;
/*!40000 ALTER TABLE `chartsearch_categories` DISABLE KEYS */;
INSERT INTO `chartsearch_categories` VALUES (1,'Test','concept_class_name:Test','Category item for filtering Test','3918-2910-4245-84f4-a9f38648ac60'),(2,'Procedure','concept_class_name:Procedure','Category item for filtering Procedure','66b0e726-a280-4d42-ada3-42bc111f68d6'),(3,'Drug','concept_class_name:Drug','Category item for filtering Drug','35cdafb5-5f6b-4c79-89e4-7226bea70ba9'),(4,'Diagnosis','concept_class_name:Diagnosis','Category item for filtering Diagnosis','a458cbba-fb5d-4e5f-b0da-bfab122860a8'),(5,'Finding','concept_class_name:Finding','Category item for filtering Finding','fc29ada4-0e00-4450-905c-e2982a242df2'),(6,'Anatomy','concept_class_name:Anatomy','Category item for filtering Anatomy','290c0ddd-3aad-4718-addf-3d1d33f7ae5e'),(7,'Question','concept_class_name:Question','Category item for filtering Question','0717136a-5c5f-4d68-b099-cbf1ad820363'),(8,'LabSet','concept_class_name:LabSet','Category item for filtering LabSet','e68bb6ff-8b11-48e0-b259-703ffbdf4123'),(9,'MedSet','concept_class_name:MedSet','Category item for filtering MedSet','d90cb961-42c3-4dd8-93f1-d9d3bff09866'),(10,'ConvSet','concept_class_name:ConvSet','Category item for filtering ConvSet','c7cd56a5-62a3-4da5-a2ec-7b2390b31c7b'),(11,'Misc','concept_class_name:Misc','Category item for filtering Misc','9159716d-120b-48c7-9d83-a954430b4362'),(12,'Symptom','concept_class_name:Symptom','Category item for filtering Symptom','113f71f4-e1c0-4548-97df-1fca64f4b011'),(13,'Symptom/Finding','concept_class_name:\"Symptom/Finding\"','Category item for filtering Symptom/Finding','b5611ebc-4ceb-40ed-8142-61d42967b87f'),(14,'Specimen','concept_class_name:Specimen','Category item for filtering Specimen','4d280790-c856-4ebb-974b-4b1bd0684302'),(15,'Misc Order','concept_class_name:\"Misc Order\"','Category item for filtering Misc Order','952ae740-b9d4-49ed-856c-500bc8e2a381'),(16,'Frequency','concept_class_name:Frequency','Category item for filtering Frequency','21ac0fec-947f-4a76-aeee-2e17d9d0cb13'),(17,'Pharmacology','concept_class_name:\"Pharmacologic Drug Class\"','Category item for filtering medications based on pharmacologic properties as opposed to therapeutic properties','663f91a0-24bd-11e5-b345-feff819cdc9f'),(18,'Units','concept_class_name:\"Units of Measure\"','Category item for filtering prescription and dispensation units','663f961e-24bd-11e5-b345-feff819cdc9f'),(19,'Allergies','none','Category filter item for Patient Allergies','dc33c2c0-3910-11e5-a151-feff819cdc9f'),(20,'Appointments','none','Category filter item for Patient Appointments','dc33c630-3910-11e5-a151-feff819cdc9f');
/*!40000 ALTER TABLE `chartsearch_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chartsearch_category_displayname`
--

DROP TABLE IF EXISTS `chartsearch_category_displayname`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chartsearch_category_displayname` (
  `displayname_id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `diplay_name` varchar(15) NOT NULL,
  `preference_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`displayname_id`),
  KEY `categtory_displayname-fk` (`preference_id`),
  KEY `displayname_owner-fk` (`category_id`),
  CONSTRAINT `categtory_displayname-fk` FOREIGN KEY (`preference_id`) REFERENCES `chartsearch_preference` (`preference_id`),
  CONSTRAINT `displayname_owner-fk` FOREIGN KEY (`category_id`) REFERENCES `chartsearch_categories` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chartsearch_category_displayname`
--

LOCK TABLES `chartsearch_category_displayname` WRITE;
/*!40000 ALTER TABLE `chartsearch_category_displayname` DISABLE KEYS */;
/*!40000 ALTER TABLE `chartsearch_category_displayname` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chartsearch_history`
--

DROP TABLE IF EXISTS `chartsearch_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chartsearch_history` (
  `search_id` int(11) NOT NULL AUTO_INCREMENT,
  `search_phrase` text NOT NULL,
  `last_searched_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `uuid` char(38) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  PRIMARY KEY (`search_id`),
  KEY `history_owner-fk` (`user_id`),
  KEY `history_patient-fk` (`patient_id`),
  CONSTRAINT `history_owner-fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `history_patient-fk` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chartsearch_history`
--

LOCK TABLES `chartsearch_history` WRITE;
/*!40000 ALTER TABLE `chartsearch_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `chartsearch_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chartsearch_note`
--

DROP TABLE IF EXISTS `chartsearch_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chartsearch_note` (
  `note_id` int(11) NOT NULL AUTO_INCREMENT,
  `comment` text NOT NULL,
  `search_phrase` text NOT NULL,
  `priority` text NOT NULL,
  `created_or_last_modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `uuid` char(38) DEFAULT NULL,
  `display_color` char(10) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  PRIMARY KEY (`note_id`),
  KEY `note_owner-fk` (`user_id`),
  KEY `note_patient-fk` (`patient_id`),
  CONSTRAINT `note_owner-fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `note_patient-fk` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chartsearch_note`
--

LOCK TABLES `chartsearch_note` WRITE;
/*!40000 ALTER TABLE `chartsearch_note` DISABLE KEYS */;
/*!40000 ALTER TABLE `chartsearch_note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chartsearch_preference`
--

DROP TABLE IF EXISTS `chartsearch_preference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chartsearch_preference` (
  `preference_id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `enable_history` tinyint(1) NOT NULL DEFAULT '1',
  `enable_bookmark` tinyint(1) NOT NULL DEFAULT '1',
  `enable_notes` tinyint(1) NOT NULL DEFAULT '1',
  `enable_duplicateresults` tinyint(1) NOT NULL DEFAULT '1',
  `enable_multiplefiltering` tinyint(1) NOT NULL DEFAULT '1',
  `enable_quicksearches` tinyint(1) NOT NULL DEFAULT '1',
  `enable_defaultsearch` tinyint(1) NOT NULL DEFAULT '1',
  `personalnotes_colors` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`preference_id`),
  KEY `preference_owner-fk` (`user_id`),
  CONSTRAINT `preference_owner-fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chartsearch_preference`
--

LOCK TABLES `chartsearch_preference` WRITE;
/*!40000 ALTER TABLE `chartsearch_preference` DISABLE KEYS */;
INSERT INTO `chartsearch_preference` VALUES (1,'949e76d2-3571-11e5-a151-feff819cdc9f',1,1,1,0,0,1,1,'',2);
/*!40000 ALTER TABLE `chartsearch_preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chartsearch_synonym_groups`
--

DROP TABLE IF EXISTS `chartsearch_synonym_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chartsearch_synonym_groups` (
  `group_id` int(11) NOT NULL,
  `group_name` varchar(255) NOT NULL,
  `is_category` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chartsearch_synonym_groups`
--

LOCK TABLES `chartsearch_synonym_groups` WRITE;
/*!40000 ALTER TABLE `chartsearch_synonym_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `chartsearch_synonym_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chartsearch_synonyms`
--

DROP TABLE IF EXISTS `chartsearch_synonyms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chartsearch_synonyms` (
  `synonym_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`synonym_id`),
  KEY `fk_synonymgroups_synonyms` (`group_id`),
  CONSTRAINT `fk_synonymgroups_synonyms` FOREIGN KEY (`group_id`) REFERENCES `chartsearch_synonym_groups` (`group_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chartsearch_synonyms`
--

LOCK TABLES `chartsearch_synonyms` WRITE;
/*!40000 ALTER TABLE `chartsearch_synonyms` DISABLE KEYS */;
/*!40000 ALTER TABLE `chartsearch_synonyms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clob_datatype_storage`
--

DROP TABLE IF EXISTS `clob_datatype_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clob_datatype_storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `value` longtext NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`),
  UNIQUE KEY `clob_datatype_storage_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clob_datatype_storage`
--

LOCK TABLES `clob_datatype_storage` WRITE;
/*!40000 ALTER TABLE `clob_datatype_storage` DISABLE KEYS */;
/*!40000 ALTER TABLE `clob_datatype_storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cohort`
--

DROP TABLE IF EXISTS `cohort`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cohort` (
  `cohort_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`cohort_id`),
  UNIQUE KEY `cohort_uuid_index` (`uuid`),
  KEY `user_who_changed_cohort` (`changed_by`),
  KEY `cohort_creator` (`creator`),
  KEY `user_who_voided_cohort` (`voided_by`),
  CONSTRAINT `cohort_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_changed_cohort` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_cohort` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cohort`
--

LOCK TABLES `cohort` WRITE;
/*!40000 ALTER TABLE `cohort` DISABLE KEYS */;
/*!40000 ALTER TABLE `cohort` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cohort_member`
--

DROP TABLE IF EXISTS `cohort_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cohort_member` (
  `cohort_id` int(11) NOT NULL DEFAULT '0',
  `patient_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`cohort_id`,`patient_id`),
  KEY `member_patient` (`patient_id`),
  CONSTRAINT `member_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON UPDATE CASCADE,
  CONSTRAINT `parent_cohort` FOREIGN KEY (`cohort_id`) REFERENCES `cohort` (`cohort_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cohort_member`
--

LOCK TABLES `cohort_member` WRITE;
/*!40000 ALTER TABLE `cohort_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `cohort_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept`
--

DROP TABLE IF EXISTS `concept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept` (
  `concept_id` int(11) NOT NULL AUTO_INCREMENT,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `short_name` varchar(255) DEFAULT NULL,
  `description` text,
  `form_text` text,
  `datatype_id` int(11) NOT NULL DEFAULT '0',
  `class_id` int(11) NOT NULL DEFAULT '0',
  `is_set` tinyint(1) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_id`),
  UNIQUE KEY `concept_uuid_index` (`uuid`),
  KEY `user_who_changed_concept` (`changed_by`),
  KEY `concept_classes` (`class_id`),
  KEY `concept_creator` (`creator`),
  KEY `concept_datatypes` (`datatype_id`),
  KEY `user_who_retired_concept` (`retired_by`),
  CONSTRAINT `concept_classes` FOREIGN KEY (`class_id`) REFERENCES `concept_class` (`concept_class_id`),
  CONSTRAINT `concept_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `concept_datatypes` FOREIGN KEY (`datatype_id`) REFERENCES `concept_datatype` (`concept_datatype_id`),
  CONSTRAINT `user_who_changed_concept` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_concept` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept`
--

LOCK TABLES `concept` WRITE;
/*!40000 ALTER TABLE `concept` DISABLE KEYS */;
INSERT INTO `concept` VALUES (1,0,'','',NULL,4,11,0,1,'2016-07-04 10:37:58',NULL,NULL,NULL,NULL,NULL,NULL,'e3e3b199-ae51-4829-bac7-ce8c85f39e69'),(2,0,'','',NULL,4,11,0,1,'2016-07-04 10:37:58',NULL,NULL,NULL,NULL,NULL,NULL,'d51fb1ab-b45b-41ed-8b27-e3866a4e9a37');
/*!40000 ALTER TABLE `concept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_answer`
--

DROP TABLE IF EXISTS `concept_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_answer` (
  `concept_answer_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `answer_concept` int(11) DEFAULT NULL,
  `answer_drug` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `sort_weight` double DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_answer_id`),
  UNIQUE KEY `concept_answer_uuid_index` (`uuid`),
  KEY `answer` (`answer_concept`),
  KEY `answers_for_concept` (`concept_id`),
  KEY `answer_creator` (`creator`),
  KEY `answer_answer_drug_fk` (`answer_drug`),
  CONSTRAINT `answer` FOREIGN KEY (`answer_concept`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `answer_answer_drug_fk` FOREIGN KEY (`answer_drug`) REFERENCES `drug` (`drug_id`),
  CONSTRAINT `answer_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `answers_for_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_answer`
--

LOCK TABLES `concept_answer` WRITE;
/*!40000 ALTER TABLE `concept_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_class`
--

DROP TABLE IF EXISTS `concept_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_class` (
  `concept_class_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_class_id`),
  UNIQUE KEY `concept_class_uuid_index` (`uuid`),
  KEY `concept_class_retired_status` (`retired`),
  KEY `concept_class_creator` (`creator`),
  KEY `user_who_retired_concept_class` (`retired_by`),
  KEY `concept_class_name_index` (`name`),
  CONSTRAINT `concept_class_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_concept_class` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_class`
--

LOCK TABLES `concept_class` WRITE;
/*!40000 ALTER TABLE `concept_class` DISABLE KEYS */;
INSERT INTO `concept_class` VALUES (1,'Test','Acq. during patient encounter (vitals, labs, etc.)',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4907b2-c2cc-11de-8d13-0010c6dffd0f'),(2,'Procedure','Describes a clinical procedure',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d490bf4-c2cc-11de-8d13-0010c6dffd0f'),(3,'Drug','Drug',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d490dfc-c2cc-11de-8d13-0010c6dffd0f'),(4,'Diagnosis','Conclusion drawn through findings',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4918b0-c2cc-11de-8d13-0010c6dffd0f'),(5,'Finding','Practitioner observation/finding',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d491a9a-c2cc-11de-8d13-0010c6dffd0f'),(6,'Anatomy','Anatomic sites / descriptors',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d491c7a-c2cc-11de-8d13-0010c6dffd0f'),(7,'Question','Question (eg, patient history, SF36 items)',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d491e50-c2cc-11de-8d13-0010c6dffd0f'),(8,'LabSet','Term to describe laboratory sets',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d492026-c2cc-11de-8d13-0010c6dffd0f'),(9,'MedSet','Term to describe medication sets',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4923b4-c2cc-11de-8d13-0010c6dffd0f'),(10,'ConvSet','Term to describe convenience sets',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d492594-c2cc-11de-8d13-0010c6dffd0f'),(11,'Misc','Terms which don\'t fit other categories',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d492774-c2cc-11de-8d13-0010c6dffd0f'),(12,'Symptom','Patient-reported observation',1,'2004-10-04 00:00:00',0,NULL,NULL,NULL,'8d492954-c2cc-11de-8d13-0010c6dffd0f'),(13,'Symptom/Finding','Observation that can be reported from patient or found on exam',1,'2004-10-04 00:00:00',0,NULL,NULL,NULL,'8d492b2a-c2cc-11de-8d13-0010c6dffd0f'),(14,'Specimen','Body or fluid specimen',1,'2004-12-02 00:00:00',0,NULL,NULL,NULL,'8d492d0a-c2cc-11de-8d13-0010c6dffd0f'),(15,'Misc Order','Orderable items which aren\'t tests or drugs',1,'2005-02-17 00:00:00',0,NULL,NULL,NULL,'8d492ee0-c2cc-11de-8d13-0010c6dffd0f'),(16,'Frequency','A class for order frequencies',1,'2014-03-06 00:00:00',0,NULL,NULL,NULL,'8e071bfe-520c-44c0-a89b-538e9129b42a');
/*!40000 ALTER TABLE `concept_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_complex`
--

DROP TABLE IF EXISTS `concept_complex`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_complex` (
  `concept_id` int(11) NOT NULL,
  `handler` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`concept_id`),
  CONSTRAINT `concept_attributes` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_complex`
--

LOCK TABLES `concept_complex` WRITE;
/*!40000 ALTER TABLE `concept_complex` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_complex` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_datatype`
--

DROP TABLE IF EXISTS `concept_datatype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_datatype` (
  `concept_datatype_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `hl7_abbreviation` varchar(3) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_datatype_id`),
  UNIQUE KEY `concept_datatype_uuid_index` (`uuid`),
  KEY `concept_datatype_retired_status` (`retired`),
  KEY `concept_datatype_creator` (`creator`),
  KEY `user_who_retired_concept_datatype` (`retired_by`),
  KEY `concept_datatype_name_index` (`name`),
  CONSTRAINT `concept_datatype_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_concept_datatype` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_datatype`
--

LOCK TABLES `concept_datatype` WRITE;
/*!40000 ALTER TABLE `concept_datatype` DISABLE KEYS */;
INSERT INTO `concept_datatype` VALUES (1,'Numeric','NM','Numeric value, including integer or float (e.g., creatinine, weight)',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4a4488-c2cc-11de-8d13-0010c6dffd0f'),(2,'Coded','CWE','Value determined by term dictionary lookup (i.e., term identifier)',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4a48b6-c2cc-11de-8d13-0010c6dffd0f'),(3,'Text','ST','Free text',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4a4ab4-c2cc-11de-8d13-0010c6dffd0f'),(4,'N/A','ZZ','Not associated with a datatype (e.g., term answers, sets)',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4a4c94-c2cc-11de-8d13-0010c6dffd0f'),(5,'Document','RP','Pointer to a binary or text-based document (e.g., clinical document, RTF, XML, EKG, image, etc.) stored in complex_obs table',1,'2004-04-15 00:00:00',0,NULL,NULL,NULL,'8d4a4e74-c2cc-11de-8d13-0010c6dffd0f'),(6,'Date','DT','Absolute date',1,'2004-07-22 00:00:00',0,NULL,NULL,NULL,'8d4a505e-c2cc-11de-8d13-0010c6dffd0f'),(7,'Time','TM','Absolute time of day',1,'2004-07-22 00:00:00',0,NULL,NULL,NULL,'8d4a591e-c2cc-11de-8d13-0010c6dffd0f'),(8,'Datetime','TS','Absolute date and time',1,'2004-07-22 00:00:00',0,NULL,NULL,NULL,'8d4a5af4-c2cc-11de-8d13-0010c6dffd0f'),(10,'Boolean','BIT','Boolean value (yes/no, true/false)',1,'2004-08-26 00:00:00',0,NULL,NULL,NULL,'8d4a5cca-c2cc-11de-8d13-0010c6dffd0f'),(11,'Rule','ZZ','Value derived from other data',1,'2006-09-11 00:00:00',0,NULL,NULL,NULL,'8d4a5e96-c2cc-11de-8d13-0010c6dffd0f'),(12,'Structured Numeric','SN','Complex numeric values possible (ie, <5, 1-10, etc.)',1,'2005-08-06 00:00:00',0,NULL,NULL,NULL,'8d4a606c-c2cc-11de-8d13-0010c6dffd0f'),(13,'Complex','ED','Complex value.  Analogous to HL7 Embedded Datatype',1,'2008-05-28 12:25:34',0,NULL,NULL,NULL,'8d4a6242-c2cc-11de-8d13-0010c6dffd0f');
/*!40000 ALTER TABLE `concept_datatype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_description`
--

DROP TABLE IF EXISTS `concept_description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_description` (
  `concept_description_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `description` text NOT NULL,
  `locale` varchar(50) NOT NULL DEFAULT '',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_description_id`),
  UNIQUE KEY `concept_description_uuid_index` (`uuid`),
  KEY `user_who_changed_description` (`changed_by`),
  KEY `description_for_concept` (`concept_id`),
  KEY `user_who_created_description` (`creator`),
  CONSTRAINT `description_for_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `user_who_changed_description` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_description` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_description`
--

LOCK TABLES `concept_description` WRITE;
/*!40000 ALTER TABLE `concept_description` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_description` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_map_type`
--

DROP TABLE IF EXISTS `concept_map_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_map_type` (
  `concept_map_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `is_hidden` tinyint(1) NOT NULL DEFAULT '0',
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_map_type_id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `mapped_user_creator_concept_map_type` (`creator`),
  KEY `mapped_user_changed_concept_map_type` (`changed_by`),
  KEY `mapped_user_retired_concept_map_type` (`retired_by`),
  CONSTRAINT `mapped_user_changed_concept_map_type` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `mapped_user_creator_concept_map_type` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `mapped_user_retired_concept_map_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_map_type`
--

LOCK TABLES `concept_map_type` WRITE;
/*!40000 ALTER TABLE `concept_map_type` DISABLE KEYS */;
INSERT INTO `concept_map_type` VALUES (1,'SAME-AS',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'35543629-7d8c-11e1-909d-c80aa9edcf4e'),(2,'NARROWER-THAN',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'43ac5109-7d8c-11e1-909d-c80aa9edcf4e'),(3,'BROADER-THAN',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'4b9d9421-7d8c-11e1-909d-c80aa9edcf4e'),(4,'Associated finding',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'55e02065-7d8c-11e1-909d-c80aa9edcf4e'),(5,'Associated morphology',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'605f4a61-7d8c-11e1-909d-c80aa9edcf4e'),(6,'Associated procedure',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'6eb1bfce-7d8c-11e1-909d-c80aa9edcf4e'),(7,'Associated with',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'781bdc8f-7d8c-11e1-909d-c80aa9edcf4e'),(8,'Causative agent',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'808f9e19-7d8c-11e1-909d-c80aa9edcf4e'),(9,'Finding site',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'889c3013-7d8c-11e1-909d-c80aa9edcf4e'),(10,'Has specimen',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'929600b9-7d8c-11e1-909d-c80aa9edcf4e'),(11,'Laterality',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'999c6fc0-7d8c-11e1-909d-c80aa9edcf4e'),(12,'Severity',NULL,1,'2016-07-04 00:00:00',NULL,NULL,0,0,NULL,NULL,NULL,'a0e52281-7d8c-11e1-909d-c80aa9edcf4e'),(13,'Access',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'f9e90b29-7d8c-11e1-909d-c80aa9edcf4e'),(14,'After',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'01b60e29-7d8d-11e1-909d-c80aa9edcf4e'),(15,'Clinical course',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'5f7c3702-7d8d-11e1-909d-c80aa9edcf4e'),(16,'Component',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'67debecc-7d8d-11e1-909d-c80aa9edcf4e'),(17,'Direct device',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'718c00da-7d8d-11e1-909d-c80aa9edcf4e'),(18,'Direct morphology',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'7b9509cb-7d8d-11e1-909d-c80aa9edcf4e'),(19,'Direct substance',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'82bb495d-7d8d-11e1-909d-c80aa9edcf4e'),(20,'Due to',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'8b77f7d3-7d8d-11e1-909d-c80aa9edcf4e'),(21,'Episodicity',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'94a81179-7d8d-11e1-909d-c80aa9edcf4e'),(22,'Finding context',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'9d23c22e-7d8d-11e1-909d-c80aa9edcf4e'),(23,'Finding informer',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'a4524368-7d8d-11e1-909d-c80aa9edcf4e'),(24,'Finding method',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'af089254-7d8d-11e1-909d-c80aa9edcf4e'),(25,'Has active ingredient',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'b65aa605-7d8d-11e1-909d-c80aa9edcf4e'),(26,'Has definitional manifestation',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'c2b7b2fa-7d8d-11e1-909d-c80aa9edcf4'),(27,'Has dose form',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'cc3878e6-7d8d-11e1-909d-c80aa9edcf4e'),(28,'Has focus',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'d67c5840-7d8d-11e1-909d-c80aa9edcf4e'),(29,'Has intent',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'de2fb2c5-7d8d-11e1-909d-c80aa9edcf4e'),(30,'Has interpretation',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'e758838b-7d8d-11e1-909d-c80aa9edcf4e'),(31,'Indirect device',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'ee63c142-7d8d-11e1-909d-c80aa9edcf4e'),(32,'Indirect morphology',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'f4f36681-7d8d-11e1-909d-c80aa9edcf4e'),(33,'Interprets',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'fc7f5fed-7d8d-11e1-909d-c80aa9edcf4e'),(34,'Measurement method',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'06b11d79-7d8e-11e1-909d-c80aa9edcf4e'),(35,'Method',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'0efb4753-7d8e-11e1-909d-c80aa9edcf4e'),(36,'Occurrence',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'16e7b617-7d8e-11e1-909d-c80aa9edcf4e'),(37,'Part of',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'1e82007b-7d8e-11e1-909d-c80aa9edcf4e'),(38,'Pathological process',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'2969915e-7d8e-11e1-909d-c80aa9edcf4e'),(39,'Priority',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'32d57796-7d8e-11e1-909d-c80aa9edcf4e'),(40,'Procedure context',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'3f11904c-7d8e-11e1-909d-c80aa9edcf4e'),(41,'Procedure device',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'468c4aa3-7d8e-11e1-909d-c80aa9edcf4e'),(42,'Procedure morphology',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'5383e889-7d8e-11e1-909d-c80aa9edcf4e'),(43,'Procedure site',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'5ad2655d-7d8e-11e1-909d-c80aa9edcf4e'),(44,'Procedure site - Direct',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'66085196-7d8e-11e1-909d-c80aa9edcf4e'),(45,'Procedure site - Indirect',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'7080e843-7d8e-11e1-909d-c80aa9edcf4e'),(46,'Property',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'76bfb796-7d8e-11e1-909d-c80aa9edcf4e'),(47,'Recipient category',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'7e7d00e4-7d8e-11e1-909d-c80aa9edcf4e'),(48,'Revision status',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'851e14c1-7d8e-11e1-909d-c80aa9edcf4e'),(49,'Route of administration',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'8ee5b13d-7d8e-11e1-909d-c80aa9edcf4e'),(50,'Scale type',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'986acf48-7d8e-11e1-909d-c80aa9edcf4e'),(51,'Specimen procedure',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'a6937642-7d8e-11e1-909d-c80aa9edcf4e'),(52,'Specimen source identity',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'b1d6941e-7d8e-11e1-909d-c80aa9edcf4e'),(53,'Specimen source morphology',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'b7c793c1-7d8e-11e1-909d-c80aa9edcf4e'),(54,'Specimen source topography',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'be9f9eb8-7d8e-11e1-909d-c80aa9edcf4e'),(55,'Specimen substance',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'c8f2bacb-7d8e-11e1-909d-c80aa9edcf4e'),(56,'Subject of information',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'d0664c4f-7d8e-11e1-909d-c80aa9edcf4e'),(57,'Subject relationship context',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'dace9d13-7d8e-11e1-909d-c80aa9edcf4e'),(58,'Surgical approach',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'e3cd666d-7d8e-11e1-909d-c80aa9edcf4e'),(59,'Temporal context',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'ed96447d-7d8e-11e1-909d-c80aa9edcf4e'),(60,'Time aspect',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'f415bcce-7d8e-11e1-909d-c80aa9edcf4e'),(61,'Using access device',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'fa9538a9-7d8e-11e1-909d-c80aa9edcf4e'),(62,'Using device',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'06588655-7d8f-11e1-909d-c80aa9edcf4e'),(63,'Using energy',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'0c2ae0bc-7d8f-11e1-909d-c80aa9edcf4e'),(64,'Using substance',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'13d2c607-7d8f-11e1-909d-c80aa9edcf4e'),(65,'IS A',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'1ce7a784-7d8f-11e1-909d-c80aa9edcf4e'),(66,'MAY BE A',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'267812a3-7d8f-11e1-909d-c80aa9edcf4e'),(67,'MOVED FROM',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'2de3168e-7d8f-11e1-909d-c80aa9edcf4e'),(68,'MOVED TO',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'32f0fd99-7d8f-11e1-909d-c80aa9edcf4e'),(69,'REPLACED BY',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'3b3b9a7d-7d8f-11e1-909d-c80aa9edcf4e'),(70,'WAS A',NULL,1,'2016-07-04 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'41a034da-7d8f-11e1-909d-c80aa9edcf4e');
/*!40000 ALTER TABLE `concept_map_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_name`
--

DROP TABLE IF EXISTS `concept_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_name` (
  `concept_name_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `locale` varchar(50) NOT NULL DEFAULT '',
  `locale_preferred` tinyint(1) DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `concept_name_type` varchar(50) DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_name_id`),
  UNIQUE KEY `concept_name_uuid_index` (`uuid`),
  KEY `name_of_concept` (`name`),
  KEY `name_for_concept` (`concept_id`),
  KEY `user_who_created_name` (`creator`),
  KEY `user_who_voided_this_name` (`voided_by`),
  CONSTRAINT `name_for_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `user_who_created_name` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_this_name` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_name`
--

LOCK TABLES `concept_name` WRITE;
/*!40000 ALTER TABLE `concept_name` DISABLE KEYS */;
INSERT INTO `concept_name` VALUES (1,1,'Verdadeiro','pt',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'f9bfaf56-9fec-4f07-a8e9-f1f55318beb7'),(2,1,'Sim','pt',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'0829c5a1-e603-44e7-b931-6c21a4008fcc'),(3,1,'True','en',1,1,'2016-07-04 10:37:58','FULLY_SPECIFIED',0,NULL,NULL,NULL,'00ead318-6b84-473e-a7d9-a826d8373b71'),(4,1,'Yes','en',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'12e64b4f-9746-4167-b0fd-e0b276dfe8b9'),(5,1,'Vero','it',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'6fb0bfb3-af47-4747-ba36-c97e87b08a5e'),(6,1,'Sì','it',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'b03ec342-7dde-4700-9f6f-c873c768c515'),(7,1,'Vrai','fr',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'f5363eb2-ee0d-45a3-af42-0ed304105aa1'),(8,1,'Oui','fr',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'a96741bd-31a1-4aad-a940-da09ab507733'),(9,1,'Verdadero','es',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'0845ccf5-34a5-4979-8e3e-c837407970d2'),(10,1,'Sí','es',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'9813b008-e566-425a-9213-f3eed6aeed7c'),(11,2,'Falso','pt',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'4e7fcd2b-df28-415f-9fef-0f595301961c'),(12,2,'Não','pt',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'259ba4d6-0ad4-4fc9-8d53-12654c000f9c'),(13,2,'False','en',1,1,'2016-07-04 10:37:58','FULLY_SPECIFIED',0,NULL,NULL,NULL,'e033606e-4db4-4cb0-a767-0f32b60a3989'),(14,2,'No','en',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'06ef1bee-c375-48e6-808c-2143b84f8c9d'),(15,2,'Falso','it',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'15493258-f9b7-4446-a9fd-00d795f35d69'),(16,2,'No','it',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'835f195e-ec50-4f87-9375-b403f54c97a5'),(17,2,'Faux','fr',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'25260995-b583-4e73-8524-bf675055aa73'),(18,2,'Non','fr',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'7cbe9625-ed71-4828-8f58-c7ca352f184d'),(19,2,'Falso','es',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'6ac06c11-2d2b-4c22-962b-236522a2e6aa'),(20,2,'No','es',0,1,'2016-07-04 10:37:58',NULL,0,NULL,NULL,NULL,'70f98125-8d28-4ca4-9ee2-95d7873456a4');
/*!40000 ALTER TABLE `concept_name` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_name_tag`
--

DROP TABLE IF EXISTS `concept_name_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_name_tag` (
  `concept_name_tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(50) NOT NULL,
  `description` text,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_name_tag_id`),
  UNIQUE KEY `concept_name_tag_unique_tags` (`tag`),
  UNIQUE KEY `concept_name_tag_uuid_index` (`uuid`),
  KEY `user_who_created_name_tag` (`creator`),
  KEY `user_who_voided_name_tag` (`voided_by`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_name_tag`
--

LOCK TABLES `concept_name_tag` WRITE;
/*!40000 ALTER TABLE `concept_name_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_name_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_name_tag_map`
--

DROP TABLE IF EXISTS `concept_name_tag_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_name_tag_map` (
  `concept_name_id` int(11) NOT NULL,
  `concept_name_tag_id` int(11) NOT NULL,
  KEY `mapped_concept_name` (`concept_name_id`),
  KEY `mapped_concept_name_tag` (`concept_name_tag_id`),
  CONSTRAINT `mapped_concept_name` FOREIGN KEY (`concept_name_id`) REFERENCES `concept_name` (`concept_name_id`),
  CONSTRAINT `mapped_concept_name_tag` FOREIGN KEY (`concept_name_tag_id`) REFERENCES `concept_name_tag` (`concept_name_tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_name_tag_map`
--

LOCK TABLES `concept_name_tag_map` WRITE;
/*!40000 ALTER TABLE `concept_name_tag_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_name_tag_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_numeric`
--

DROP TABLE IF EXISTS `concept_numeric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_numeric` (
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `hi_absolute` double DEFAULT NULL,
  `hi_critical` double DEFAULT NULL,
  `hi_normal` double DEFAULT NULL,
  `low_absolute` double DEFAULT NULL,
  `low_critical` double DEFAULT NULL,
  `low_normal` double DEFAULT NULL,
  `units` varchar(50) DEFAULT NULL,
  `precise` tinyint(1) NOT NULL DEFAULT '0',
  `display_precision` int(11) DEFAULT NULL,
  PRIMARY KEY (`concept_id`),
  CONSTRAINT `numeric_attributes` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_numeric`
--

LOCK TABLES `concept_numeric` WRITE;
/*!40000 ALTER TABLE `concept_numeric` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_numeric` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_proposal`
--

DROP TABLE IF EXISTS `concept_proposal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_proposal` (
  `concept_proposal_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) DEFAULT NULL,
  `encounter_id` int(11) DEFAULT NULL,
  `original_text` varchar(255) NOT NULL DEFAULT '',
  `final_text` varchar(255) DEFAULT NULL,
  `obs_id` int(11) DEFAULT NULL,
  `obs_concept_id` int(11) DEFAULT NULL,
  `state` varchar(32) NOT NULL DEFAULT 'UNMAPPED',
  `comments` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `locale` varchar(50) NOT NULL DEFAULT '',
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_proposal_id`),
  UNIQUE KEY `concept_proposal_uuid_index` (`uuid`),
  KEY `user_who_changed_proposal` (`changed_by`),
  KEY `concept_for_proposal` (`concept_id`),
  KEY `user_who_created_proposal` (`creator`),
  KEY `encounter_for_proposal` (`encounter_id`),
  KEY `proposal_obs_concept_id` (`obs_concept_id`),
  KEY `proposal_obs_id` (`obs_id`),
  CONSTRAINT `concept_for_proposal` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `encounter_for_proposal` FOREIGN KEY (`encounter_id`) REFERENCES `encounter` (`encounter_id`),
  CONSTRAINT `proposal_obs_concept_id` FOREIGN KEY (`obs_concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `proposal_obs_id` FOREIGN KEY (`obs_id`) REFERENCES `obs` (`obs_id`),
  CONSTRAINT `user_who_changed_proposal` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_proposal` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_proposal`
--

LOCK TABLES `concept_proposal` WRITE;
/*!40000 ALTER TABLE `concept_proposal` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_proposal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_proposal_tag_map`
--

DROP TABLE IF EXISTS `concept_proposal_tag_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_proposal_tag_map` (
  `concept_proposal_id` int(11) NOT NULL,
  `concept_name_tag_id` int(11) NOT NULL,
  KEY `mapped_concept_proposal_tag` (`concept_name_tag_id`),
  KEY `mapped_concept_proposal` (`concept_proposal_id`),
  CONSTRAINT `mapped_concept_proposal` FOREIGN KEY (`concept_proposal_id`) REFERENCES `concept_proposal` (`concept_proposal_id`),
  CONSTRAINT `mapped_concept_proposal_tag` FOREIGN KEY (`concept_name_tag_id`) REFERENCES `concept_name_tag` (`concept_name_tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_proposal_tag_map`
--

LOCK TABLES `concept_proposal_tag_map` WRITE;
/*!40000 ALTER TABLE `concept_proposal_tag_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_proposal_tag_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_reference_map`
--

DROP TABLE IF EXISTS `concept_reference_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_reference_map` (
  `concept_map_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_reference_term_id` int(11) NOT NULL,
  `concept_map_type_id` int(11) NOT NULL DEFAULT '1',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_map_id`),
  KEY `map_for_concept` (`concept_id`),
  KEY `map_creator` (`creator`),
  KEY `mapped_concept_map_type` (`concept_map_type_id`),
  KEY `mapped_user_changed_ref_term` (`changed_by`),
  KEY `mapped_concept_reference_term` (`concept_reference_term_id`),
  CONSTRAINT `map_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `map_for_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `mapped_concept_map_type` FOREIGN KEY (`concept_map_type_id`) REFERENCES `concept_map_type` (`concept_map_type_id`),
  CONSTRAINT `mapped_concept_reference_term` FOREIGN KEY (`concept_reference_term_id`) REFERENCES `concept_reference_term` (`concept_reference_term_id`),
  CONSTRAINT `mapped_user_changed_ref_term` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_reference_map`
--

LOCK TABLES `concept_reference_map` WRITE;
/*!40000 ALTER TABLE `concept_reference_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_reference_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_reference_source`
--

DROP TABLE IF EXISTS `concept_reference_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_reference_source` (
  `concept_source_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `description` text NOT NULL,
  `hl7_code` varchar(50) DEFAULT '',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_source_id`),
  UNIQUE KEY `concept_source_unique_hl7_codes` (`hl7_code`),
  KEY `unique_hl7_code` (`hl7_code`),
  KEY `concept_source_creator` (`creator`),
  KEY `user_who_retired_concept_source` (`retired_by`),
  CONSTRAINT `concept_source_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_concept_source` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_reference_source`
--

LOCK TABLES `concept_reference_source` WRITE;
/*!40000 ALTER TABLE `concept_reference_source` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_reference_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_reference_term`
--

DROP TABLE IF EXISTS `concept_reference_term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_reference_term` (
  `concept_reference_term_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_source_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `version` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_changed` datetime DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_reference_term_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `mapped_user_creator` (`creator`),
  KEY `mapped_user_changed` (`changed_by`),
  KEY `mapped_user_retired` (`retired_by`),
  KEY `mapped_concept_source` (`concept_source_id`),
  KEY `idx_code_concept_reference_term` (`code`),
  CONSTRAINT `mapped_concept_source` FOREIGN KEY (`concept_source_id`) REFERENCES `concept_reference_source` (`concept_source_id`),
  CONSTRAINT `mapped_user_changed` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `mapped_user_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `mapped_user_retired` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_reference_term`
--

LOCK TABLES `concept_reference_term` WRITE;
/*!40000 ALTER TABLE `concept_reference_term` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_reference_term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_reference_term_map`
--

DROP TABLE IF EXISTS `concept_reference_term_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_reference_term_map` (
  `concept_reference_term_map_id` int(11) NOT NULL AUTO_INCREMENT,
  `term_a_id` int(11) NOT NULL,
  `term_b_id` int(11) NOT NULL,
  `a_is_to_b_id` int(11) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_reference_term_map_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `mapped_term_a` (`term_a_id`),
  KEY `mapped_term_b` (`term_b_id`),
  KEY `mapped_concept_map_type_ref_term_map` (`a_is_to_b_id`),
  KEY `mapped_user_creator_ref_term_map` (`creator`),
  KEY `mapped_user_changed_ref_term_map` (`changed_by`),
  CONSTRAINT `mapped_concept_map_type_ref_term_map` FOREIGN KEY (`a_is_to_b_id`) REFERENCES `concept_map_type` (`concept_map_type_id`),
  CONSTRAINT `mapped_term_a` FOREIGN KEY (`term_a_id`) REFERENCES `concept_reference_term` (`concept_reference_term_id`),
  CONSTRAINT `mapped_term_b` FOREIGN KEY (`term_b_id`) REFERENCES `concept_reference_term` (`concept_reference_term_id`),
  CONSTRAINT `mapped_user_changed_ref_term_map` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `mapped_user_creator_ref_term_map` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_reference_term_map`
--

LOCK TABLES `concept_reference_term_map` WRITE;
/*!40000 ALTER TABLE `concept_reference_term_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_reference_term_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_set`
--

DROP TABLE IF EXISTS `concept_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_set` (
  `concept_set_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `concept_set` int(11) NOT NULL DEFAULT '0',
  `sort_weight` double DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_set_id`),
  UNIQUE KEY `concept_set_uuid_index` (`uuid`),
  KEY `idx_concept_set_concept` (`concept_id`),
  KEY `has_a` (`concept_set`),
  KEY `user_who_created` (`creator`),
  CONSTRAINT `has_a` FOREIGN KEY (`concept_set`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `user_who_created` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_set`
--

LOCK TABLES `concept_set` WRITE;
/*!40000 ALTER TABLE `concept_set` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_state_conversion`
--

DROP TABLE IF EXISTS `concept_state_conversion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_state_conversion` (
  `concept_state_conversion_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) DEFAULT '0',
  `program_workflow_id` int(11) DEFAULT '0',
  `program_workflow_state_id` int(11) DEFAULT '0',
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_state_conversion_id`),
  UNIQUE KEY `concept_state_conversion_uuid_index` (`uuid`),
  UNIQUE KEY `unique_workflow_concept_in_conversion` (`program_workflow_id`,`concept_id`),
  KEY `concept_triggers_conversion` (`concept_id`),
  KEY `conversion_to_state` (`program_workflow_state_id`),
  CONSTRAINT `concept_triggers_conversion` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `conversion_involves_workflow` FOREIGN KEY (`program_workflow_id`) REFERENCES `program_workflow` (`program_workflow_id`),
  CONSTRAINT `conversion_to_state` FOREIGN KEY (`program_workflow_state_id`) REFERENCES `program_workflow_state` (`program_workflow_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_state_conversion`
--

LOCK TABLES `concept_state_conversion` WRITE;
/*!40000 ALTER TABLE `concept_state_conversion` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_state_conversion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_stop_word`
--

DROP TABLE IF EXISTS `concept_stop_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_stop_word` (
  `concept_stop_word_id` int(11) NOT NULL AUTO_INCREMENT,
  `word` varchar(50) NOT NULL,
  `locale` varchar(50) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_stop_word_id`),
  UNIQUE KEY `Unique_StopWord_Key` (`word`,`locale`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_stop_word`
--

LOCK TABLES `concept_stop_word` WRITE;
/*!40000 ALTER TABLE `concept_stop_word` DISABLE KEYS */;
INSERT INTO `concept_stop_word` VALUES (1,'A','en','f5f45540-e2a7-11df-87ae-18a905e044dc'),(2,'AND','en','f5f469ae-e2a7-11df-87ae-18a905e044dc'),(3,'AT','en','f5f47070-e2a7-11df-87ae-18a905e044dc'),(4,'BUT','en','f5f476c4-e2a7-11df-87ae-18a905e044dc'),(5,'BY','en','f5f47d04-e2a7-11df-87ae-18a905e044dc'),(6,'FOR','en','f5f4834e-e2a7-11df-87ae-18a905e044dc'),(7,'HAS','en','f5f48a24-e2a7-11df-87ae-18a905e044dc'),(8,'OF','en','f5f49064-e2a7-11df-87ae-18a905e044dc'),(9,'THE','en','f5f496ae-e2a7-11df-87ae-18a905e044dc'),(10,'TO','en','f5f49cda-e2a7-11df-87ae-18a905e044dc');
/*!40000 ALTER TABLE `concept_stop_word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conditions`
--

DROP TABLE IF EXISTS `conditions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conditions` (
  `condition_id` int(11) NOT NULL AUTO_INCREMENT,
  `previous_condition_id` int(11) DEFAULT NULL,
  `patient_id` int(11) NOT NULL,
  `status` varchar(255) NOT NULL,
  `concept_id` int(11) NOT NULL,
  `condition_non_coded` varchar(1024) DEFAULT NULL,
  `onset_date` datetime DEFAULT NULL,
  `additional_detail` varchar(1024) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `end_reason` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `voided` tinyint(1) NOT NULL,
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`condition_id`),
  UNIQUE KEY `uuid` (`uuid`),
  UNIQUE KEY `condition_uuid_index` (`uuid`),
  KEY `conditions_previous_condition_id_fk` (`previous_condition_id`),
  KEY `conditions_patient_fk` (`patient_id`),
  KEY `conditions_concept_fk` (`concept_id`),
  KEY `conditions_end_reason_fk` (`end_reason`),
  KEY `conditions_created_by_fk` (`creator`),
  KEY `conditions_voided_by_fk` (`voided_by`),
  CONSTRAINT `conditions_concept_fk` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `conditions_created_by_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `conditions_end_reason_fk` FOREIGN KEY (`end_reason`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `conditions_patient_fk` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON UPDATE CASCADE,
  CONSTRAINT `conditions_previous_condition_id_fk` FOREIGN KEY (`previous_condition_id`) REFERENCES `conditions` (`condition_id`),
  CONSTRAINT `conditions_voided_by_fk` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conditions`
--

LOCK TABLES `conditions` WRITE;
/*!40000 ALTER TABLE `conditions` DISABLE KEYS */;
/*!40000 ALTER TABLE `conditions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug`
--

DROP TABLE IF EXISTS `drug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug` (
  `drug_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `combination` tinyint(1) NOT NULL DEFAULT '0',
  `dosage_form` int(11) DEFAULT NULL,
  `maximum_daily_dose` double DEFAULT NULL,
  `minimum_daily_dose` double DEFAULT NULL,
  `route` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `strength` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`drug_id`),
  UNIQUE KEY `drug_uuid_index` (`uuid`),
  KEY `primary_drug_concept` (`concept_id`),
  KEY `drug_creator` (`creator`),
  KEY `drug_changed_by` (`changed_by`),
  KEY `dosage_form_concept` (`dosage_form`),
  KEY `drug_retired_by` (`retired_by`),
  KEY `route_concept` (`route`),
  CONSTRAINT `dosage_form_concept` FOREIGN KEY (`dosage_form`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `drug_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `drug_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `drug_retired_by` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `primary_drug_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `route_concept` FOREIGN KEY (`route`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug`
--

LOCK TABLES `drug` WRITE;
/*!40000 ALTER TABLE `drug` DISABLE KEYS */;
/*!40000 ALTER TABLE `drug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug_ingredient`
--

DROP TABLE IF EXISTS `drug_ingredient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug_ingredient` (
  `drug_id` int(11) NOT NULL,
  `ingredient_id` int(11) NOT NULL,
  `uuid` char(38) NOT NULL,
  `strength` double DEFAULT NULL,
  `units` int(11) DEFAULT NULL,
  PRIMARY KEY (`drug_id`,`ingredient_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `drug_ingredient_units_fk` (`units`),
  KEY `drug_ingredient_ingredient_id_fk` (`ingredient_id`),
  CONSTRAINT `drug_ingredient_drug_id_fk` FOREIGN KEY (`drug_id`) REFERENCES `drug` (`drug_id`),
  CONSTRAINT `drug_ingredient_ingredient_id_fk` FOREIGN KEY (`ingredient_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `drug_ingredient_units_fk` FOREIGN KEY (`units`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug_ingredient`
--

LOCK TABLES `drug_ingredient` WRITE;
/*!40000 ALTER TABLE `drug_ingredient` DISABLE KEYS */;
/*!40000 ALTER TABLE `drug_ingredient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug_order`
--

DROP TABLE IF EXISTS `drug_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug_order` (
  `order_id` int(11) NOT NULL DEFAULT '0',
  `drug_inventory_id` int(11),
  `dose` double DEFAULT NULL,
  `as_needed` tinyint(1) DEFAULT NULL,
  `dosing_type` varchar(255) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `as_needed_condition` varchar(255) DEFAULT NULL,
  `num_refills` int(11) DEFAULT NULL,
  `dosing_instructions` text,
  `duration` int(11) DEFAULT NULL,
  `duration_units` int(11) DEFAULT NULL,
  `quantity_units` int(11) DEFAULT NULL,
  `route` int(11) DEFAULT NULL,
  `dose_units` int(11) DEFAULT NULL,
  `frequency` int(11) DEFAULT NULL,
  `brand_name` varchar(255) DEFAULT NULL,
  `dispense_as_written` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`order_id`),
  KEY `inventory_item` (`drug_inventory_id`),
  KEY `drug_order_duration_units_fk` (`duration_units`),
  KEY `drug_order_quantity_units` (`quantity_units`),
  KEY `drug_order_route_fk` (`route`),
  KEY `drug_order_dose_units` (`dose_units`),
  KEY `drug_order_frequency_fk` (`frequency`),
  CONSTRAINT `drug_order_dose_units` FOREIGN KEY (`dose_units`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `drug_order_duration_units_fk` FOREIGN KEY (`duration_units`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `drug_order_frequency_fk` FOREIGN KEY (`frequency`) REFERENCES `order_frequency` (`order_frequency_id`),
  CONSTRAINT `drug_order_quantity_units` FOREIGN KEY (`quantity_units`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `drug_order_route_fk` FOREIGN KEY (`route`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `extends_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `inventory_item` FOREIGN KEY (`drug_inventory_id`) REFERENCES `drug` (`drug_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug_order`
--

LOCK TABLES `drug_order` WRITE;
/*!40000 ALTER TABLE `drug_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `drug_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug_reference_map`
--

DROP TABLE IF EXISTS `drug_reference_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug_reference_map` (
  `drug_reference_map_id` int(11) NOT NULL AUTO_INCREMENT,
  `drug_id` int(11) NOT NULL,
  `term_id` int(11) NOT NULL,
  `concept_map_type` int(11) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`drug_reference_map_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `drug_for_drug_reference_map` (`drug_id`),
  KEY `concept_reference_term_for_drug_reference_map` (`term_id`),
  KEY `concept_map_type_for_drug_reference_map` (`concept_map_type`),
  KEY `user_who_changed_drug_reference_map` (`changed_by`),
  KEY `drug_reference_map_creator` (`creator`),
  KEY `user_who_retired_drug_reference_map` (`retired_by`),
  CONSTRAINT `concept_map_type_for_drug_reference_map` FOREIGN KEY (`concept_map_type`) REFERENCES `concept_map_type` (`concept_map_type_id`),
  CONSTRAINT `concept_reference_term_for_drug_reference_map` FOREIGN KEY (`term_id`) REFERENCES `concept_reference_term` (`concept_reference_term_id`),
  CONSTRAINT `drug_for_drug_reference_map` FOREIGN KEY (`drug_id`) REFERENCES `drug` (`drug_id`),
  CONSTRAINT `drug_reference_map_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_changed_drug_reference_map` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_drug_reference_map` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug_reference_map`
--

LOCK TABLES `drug_reference_map` WRITE;
/*!40000 ALTER TABLE `drug_reference_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `drug_reference_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encounter`
--

DROP TABLE IF EXISTS `encounter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `encounter` (
  `encounter_id` int(11) NOT NULL AUTO_INCREMENT,
  `encounter_type` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL DEFAULT '0',
  `location_id` int(11) DEFAULT NULL,
  `form_id` int(11) DEFAULT NULL,
  `encounter_datetime` datetime NOT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `visit_id` int(11) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`encounter_id`),
  UNIQUE KEY `encounter_uuid_index` (`uuid`),
  KEY `encounter_datetime_idx` (`encounter_datetime`),
  KEY `encounter_ibfk_1` (`creator`),
  KEY `encounter_type_id` (`encounter_type`),
  KEY `encounter_form` (`form_id`),
  KEY `encounter_location` (`location_id`),
  KEY `encounter_patient` (`patient_id`),
  KEY `user_who_voided_encounter` (`voided_by`),
  KEY `encounter_changed_by` (`changed_by`),
  KEY `encounter_visit_id_fk` (`visit_id`),
  CONSTRAINT `encounter_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `encounter_form` FOREIGN KEY (`form_id`) REFERENCES `form` (`form_id`),
  CONSTRAINT `encounter_ibfk_1` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `encounter_location` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `encounter_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON UPDATE CASCADE,
  CONSTRAINT `encounter_type_id` FOREIGN KEY (`encounter_type`) REFERENCES `encounter_type` (`encounter_type_id`),
  CONSTRAINT `encounter_visit_id_fk` FOREIGN KEY (`visit_id`) REFERENCES `visit` (`visit_id`),
  CONSTRAINT `user_who_voided_encounter` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encounter`
--

LOCK TABLES `encounter` WRITE;
/*!40000 ALTER TABLE `encounter` DISABLE KEYS */;
/*!40000 ALTER TABLE `encounter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encounter_provider`
--

DROP TABLE IF EXISTS `encounter_provider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `encounter_provider` (
  `encounter_provider_id` int(11) NOT NULL AUTO_INCREMENT,
  `encounter_id` int(11) NOT NULL,
  `provider_id` int(11) NOT NULL,
  `encounter_role_id` int(11) NOT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `date_voided` datetime DEFAULT NULL,
  `voided_by` int(11) DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`encounter_provider_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `encounter_id_fk` (`encounter_id`),
  KEY `provider_id_fk` (`provider_id`),
  KEY `encounter_role_id_fk` (`encounter_role_id`),
  KEY `encounter_provider_creator` (`creator`),
  KEY `encounter_provider_changed_by` (`changed_by`),
  KEY `encounter_provider_voided_by` (`voided_by`),
  CONSTRAINT `encounter_id_fk` FOREIGN KEY (`encounter_id`) REFERENCES `encounter` (`encounter_id`),
  CONSTRAINT `encounter_provider_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `encounter_provider_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `encounter_provider_voided_by` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `encounter_role_id_fk` FOREIGN KEY (`encounter_role_id`) REFERENCES `encounter_role` (`encounter_role_id`),
  CONSTRAINT `provider_id_fk` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`provider_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encounter_provider`
--

LOCK TABLES `encounter_provider` WRITE;
/*!40000 ALTER TABLE `encounter_provider` DISABLE KEYS */;
/*!40000 ALTER TABLE `encounter_provider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encounter_role`
--

DROP TABLE IF EXISTS `encounter_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `encounter_role` (
  `encounter_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`encounter_role_id`),
  UNIQUE KEY `uuid` (`uuid`),
  UNIQUE KEY `encounter_role_unique_name` (`name`),
  KEY `encounter_role_creator_fk` (`creator`),
  KEY `encounter_role_changed_by_fk` (`changed_by`),
  KEY `encounter_role_retired_by_fk` (`retired_by`),
  CONSTRAINT `encounter_role_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `encounter_role_creator_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `encounter_role_retired_by_fk` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encounter_role`
--

LOCK TABLES `encounter_role` WRITE;
/*!40000 ALTER TABLE `encounter_role` DISABLE KEYS */;
INSERT INTO `encounter_role` VALUES (1,'Unknown','Unknown encounter role for legacy providers with no encounter role set',1,'2011-08-18 14:00:00',NULL,NULL,0,NULL,NULL,NULL,'a0b03050-c99b-11e0-9572-0800200c9a66');
/*!40000 ALTER TABLE `encounter_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encounter_type`
--

DROP TABLE IF EXISTS `encounter_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `encounter_type` (
  `encounter_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `description` text,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `edit_privilege` varchar(255) DEFAULT NULL,
  `view_privilege` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`encounter_type_id`),
  UNIQUE KEY `encounter_type_unique_name` (`name`),
  UNIQUE KEY `encounter_type_uuid_index` (`uuid`),
  KEY `encounter_type_retired_status` (`retired`),
  KEY `user_who_created_type` (`creator`),
  KEY `user_who_retired_encounter_type` (`retired_by`),
  KEY `privilege_which_can_view_encounter_type` (`view_privilege`),
  KEY `privilege_which_can_edit_encounter_type` (`edit_privilege`),
  CONSTRAINT `privilege_which_can_edit_encounter_type` FOREIGN KEY (`edit_privilege`) REFERENCES `privilege` (`privilege`),
  CONSTRAINT `privilege_which_can_view_encounter_type` FOREIGN KEY (`view_privilege`) REFERENCES `privilege` (`privilege`),
  CONSTRAINT `user_who_created_type` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_encounter_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encounter_type`
--

LOCK TABLES `encounter_type` WRITE;
/*!40000 ALTER TABLE `encounter_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `encounter_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field`
--

DROP TABLE IF EXISTS `field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field` (
  `field_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` text,
  `field_type` int(11) DEFAULT NULL,
  `concept_id` int(11) DEFAULT NULL,
  `table_name` varchar(50) DEFAULT NULL,
  `attribute_name` varchar(50) DEFAULT NULL,
  `default_value` text,
  `select_multiple` tinyint(1) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`field_id`),
  UNIQUE KEY `field_uuid_index` (`uuid`),
  KEY `field_retired_status` (`retired`),
  KEY `user_who_changed_field` (`changed_by`),
  KEY `concept_for_field` (`concept_id`),
  KEY `user_who_created_field` (`creator`),
  KEY `type_of_field` (`field_type`),
  KEY `user_who_retired_field` (`retired_by`),
  CONSTRAINT `concept_for_field` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `type_of_field` FOREIGN KEY (`field_type`) REFERENCES `field_type` (`field_type_id`),
  CONSTRAINT `user_who_changed_field` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_field` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_field` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field`
--

LOCK TABLES `field` WRITE;
/*!40000 ALTER TABLE `field` DISABLE KEYS */;
/*!40000 ALTER TABLE `field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_answer`
--

DROP TABLE IF EXISTS `field_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_answer` (
  `field_id` int(11) NOT NULL DEFAULT '0',
  `answer_id` int(11) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`field_id`,`answer_id`),
  UNIQUE KEY `field_answer_uuid_index` (`uuid`),
  KEY `field_answer_concept` (`answer_id`),
  KEY `user_who_created_field_answer` (`creator`),
  CONSTRAINT `answers_for_field` FOREIGN KEY (`field_id`) REFERENCES `field` (`field_id`),
  CONSTRAINT `field_answer_concept` FOREIGN KEY (`answer_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `user_who_created_field_answer` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_answer`
--

LOCK TABLES `field_answer` WRITE;
/*!40000 ALTER TABLE `field_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_type`
--

DROP TABLE IF EXISTS `field_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_type` (
  `field_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` text,
  `is_set` tinyint(1) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`field_type_id`),
  UNIQUE KEY `field_type_uuid_index` (`uuid`),
  KEY `user_who_created_field_type` (`creator`),
  CONSTRAINT `user_who_created_field_type` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_type`
--

LOCK TABLES `field_type` WRITE;
/*!40000 ALTER TABLE `field_type` DISABLE KEYS */;
INSERT INTO `field_type` VALUES (1,'Concept','',0,1,'2005-02-22 00:00:00','8d5e7d7c-c2cc-11de-8d13-0010c6dffd0f'),(2,'Database element','',0,1,'2005-02-22 00:00:00','8d5e8196-c2cc-11de-8d13-0010c6dffd0f'),(3,'Set of Concepts','',1,1,'2005-02-22 00:00:00','8d5e836c-c2cc-11de-8d13-0010c6dffd0f'),(4,'Miscellaneous Set','',1,1,'2005-02-22 00:00:00','8d5e852e-c2cc-11de-8d13-0010c6dffd0f'),(5,'Section','',1,1,'2005-02-22 00:00:00','8d5e86fa-c2cc-11de-8d13-0010c6dffd0f');
/*!40000 ALTER TABLE `field_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form`
--

DROP TABLE IF EXISTS `form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form` (
  `form_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `version` varchar(50) NOT NULL DEFAULT '',
  `build` int(11) DEFAULT NULL,
  `published` tinyint(1) NOT NULL DEFAULT '0',
  `xslt` text,
  `template` text,
  `description` text,
  `encounter_type` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retired_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`form_id`),
  UNIQUE KEY `form_uuid_index` (`uuid`),
  KEY `form_published_index` (`published`),
  KEY `form_retired_index` (`retired`),
  KEY `form_published_and_retired_index` (`published`,`retired`),
  KEY `user_who_last_changed_form` (`changed_by`),
  KEY `user_who_created_form` (`creator`),
  KEY `form_encounter_type` (`encounter_type`),
  KEY `user_who_retired_form` (`retired_by`),
  CONSTRAINT `form_encounter_type` FOREIGN KEY (`encounter_type`) REFERENCES `encounter_type` (`encounter_type_id`),
  CONSTRAINT `user_who_created_form` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_last_changed_form` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_form` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form`
--

LOCK TABLES `form` WRITE;
/*!40000 ALTER TABLE `form` DISABLE KEYS */;
/*!40000 ALTER TABLE `form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_field`
--

DROP TABLE IF EXISTS `form_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_field` (
  `form_field_id` int(11) NOT NULL AUTO_INCREMENT,
  `form_id` int(11) NOT NULL DEFAULT '0',
  `field_id` int(11) NOT NULL DEFAULT '0',
  `field_number` int(11) DEFAULT NULL,
  `field_part` varchar(5) DEFAULT NULL,
  `page_number` int(11) DEFAULT NULL,
  `parent_form_field` int(11) DEFAULT NULL,
  `min_occurs` int(11) DEFAULT NULL,
  `max_occurs` int(11) DEFAULT NULL,
  `required` tinyint(1) NOT NULL DEFAULT '0',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `sort_weight` double DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`form_field_id`),
  UNIQUE KEY `form_field_uuid_index` (`uuid`),
  KEY `user_who_last_changed_form_field` (`changed_by`),
  KEY `user_who_created_form_field` (`creator`),
  KEY `field_within_form` (`field_id`),
  KEY `form_containing_field` (`form_id`),
  KEY `form_field_hierarchy` (`parent_form_field`),
  CONSTRAINT `field_within_form` FOREIGN KEY (`field_id`) REFERENCES `field` (`field_id`),
  CONSTRAINT `form_containing_field` FOREIGN KEY (`form_id`) REFERENCES `form` (`form_id`),
  CONSTRAINT `form_field_hierarchy` FOREIGN KEY (`parent_form_field`) REFERENCES `form_field` (`form_field_id`),
  CONSTRAINT `user_who_created_form_field` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_last_changed_form_field` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_field`
--

LOCK TABLES `form_field` WRITE;
/*!40000 ALTER TABLE `form_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `form_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_resource`
--

DROP TABLE IF EXISTS `form_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_resource` (
  `form_resource_id` int(11) NOT NULL AUTO_INCREMENT,
  `form_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value_reference` text NOT NULL,
  `datatype` varchar(255) DEFAULT NULL,
  `datatype_config` text,
  `preferred_handler` varchar(255) DEFAULT NULL,
  `handler_config` text,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`form_resource_id`),
  UNIQUE KEY `uuid` (`uuid`),
  UNIQUE KEY `unique_form_and_name` (`form_id`,`name`),
  CONSTRAINT `form_resource_form_fk` FOREIGN KEY (`form_id`) REFERENCES `form` (`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_resource`
--

LOCK TABLES `form_resource` WRITE;
/*!40000 ALTER TABLE `form_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `form_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `global_property`
--

DROP TABLE IF EXISTS `global_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `global_property` (
  `property` varchar(255) NOT NULL DEFAULT '',
  `property_value` text,
  `description` text,
  `uuid` char(38) NOT NULL,
  `datatype` varchar(255) DEFAULT NULL,
  `datatype_config` text,
  `preferred_handler` varchar(255) DEFAULT NULL,
  `handler_config` text,
  PRIMARY KEY (`property`),
  UNIQUE KEY `global_property_uuid_index` (`uuid`),
  KEY `global_property_property_index` (`property`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `global_property`
--

LOCK TABLES `global_property` WRITE;
/*!40000 ALTER TABLE `global_property` DISABLE KEYS */;
INSERT INTO `global_property` VALUES ('adminui.mandatory','false','true/false whether or not the adminui module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','e4e56a2d-f945-4a63-9ba7-4e53021fcf11',NULL,NULL,NULL,NULL),('adminui.started','true','DO NOT MODIFY. true/false whether or not the adminui module has been started.  This is used to make sure modules that were running  prior to a restart are started again','ff638c27-6716-4196-8c99-9f02d3e6008c',NULL,NULL,NULL,NULL),('allergy.allergen.ConceptClasses','Drug,MedSet','A comma-separated list of the allowed concept classes for the allergen field of the allergy dialog','717afa17-6c43-413b-9022-c32dacbcac24',NULL,NULL,NULL,NULL),('allergy.concept.allergen.drug','162552AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA','UUID for the drug allergens concept','376131e2-d721-4c58-9f36-7fe20225f944',NULL,NULL,NULL,NULL),('allergy.concept.allergen.environment','162554AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA','UUID for the environment allergens concept','9048adc0-7a54-429a-b7ca-86b9aee3777b',NULL,NULL,NULL,NULL),('allergy.concept.allergen.food','162553AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA','UUID for the food allergens concept','6b77d5ae-49d5-4ba5-8a13-3ed8fcf71419',NULL,NULL,NULL,NULL),('allergy.concept.otherNonCoded','5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA','UUID for the allergy other non coded concept','65772efe-4856-462a-986e-b654d1216e94',NULL,NULL,NULL,NULL),('allergy.concept.reactions','162555AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA','UUID for the allergy reactions concept','da19a4e0-5081-4cda-95ac-55d54722273e',NULL,NULL,NULL,NULL),('allergy.concept.severity.mild','1498AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA','UUID for the MILD severity concept','39444778-bc1b-4f4b-9a62-938370a20578',NULL,NULL,NULL,NULL),('allergy.concept.severity.moderate','1499AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA','UUID for the MODERATE severity concept','906f4725-404f-4874-b8c7-4f56ac9c08ff',NULL,NULL,NULL,NULL),('allergy.concept.severity.severe','1500AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA','UUID for the SEVERE severity concept','6e885546-d285-402b-9127-6c1f85c767f8',NULL,NULL,NULL,NULL),('allergy.concept.unknown','1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA','UUID for the allergy unknown concept','fae5a650-f0ce-47fa-a282-e8617d91e67c',NULL,NULL,NULL,NULL),('allergy.reaction.ConceptClasses','Symptom','A comma-separated list of the allowed concept classes for the reaction field of the allergy dialog','cfb446e2-9725-4730-b2e1-b0ba46758c88',NULL,NULL,NULL,NULL),('allergyapi.mandatory','false','true/false whether or not the allergyapi module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','8db469d2-1d25-4cc1-968a-4362d62d6daf',NULL,NULL,NULL,NULL),('allergyapi.started','true','DO NOT MODIFY. true/false whether or not the allergyapi module has been started.  This is used to make sure modules that were running  prior to a restart are started again','f440739c-1c03-4171-bc84-381aaa35b4e5',NULL,NULL,NULL,NULL),('allergyui.mandatory','false','true/false whether or not the allergyui module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','1d2eb223-921e-4283-97d6-cc10cc2e1055',NULL,NULL,NULL,NULL),('allergyui.started','true','DO NOT MODIFY. true/false whether or not the allergyui module has been started.  This is used to make sure modules that were running  prior to a restart are started again','5532780c-da3d-4438-81f7-5b696e9eff4a',NULL,NULL,NULL,NULL),('appframework.mandatory','false','true/false whether or not the appframework module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','54bfe6a4-3507-495d-9ee0-f3159e227db2',NULL,NULL,NULL,NULL),('appframework.started','true','DO NOT MODIFY. true/false whether or not the appframework module has been started.  This is used to make sure modules that were running  prior to a restart are started again','cbcce6f8-9ada-4950-a7b5-d8615d359a44',NULL,NULL,NULL,NULL),('application.name','OpenMRS','The name of this application, as presented to the user, for example on the login and welcome pages.','9993d3f4-2b36-4ce8-a2fe-fad53549b22e',NULL,NULL,NULL,NULL),('appointmentscheduling.defaultTimeSlotDuration','60','Default Time Slot Duration. (In Minutes)','038e5b7f-9567-4295-a362-2a0643f52cac',NULL,NULL,NULL,NULL),('appointmentscheduling.defaultVisitType','1','Default Visit Type for the in consultation state change.','088dad9e-dd3c-456e-8e5e-5edd120ec6e7',NULL,NULL,NULL,NULL),('appointmentscheduling.hideEndVisitButtons','true','Hide \"End Visit\" buttons on the patient dashboard.','2d6e5bc1-909d-43bf-b6cf-cd4a15aa9201',NULL,NULL,NULL,NULL),('appointmentscheduling.manageAppointmentsFormTimout','60','Refresh the manage appointments page after this number of seconds. (60 Seconds Minimum,-1 to Disable auto Refresh)','4fd570e7-9859-4895-a73e-38cf4b647b1d',NULL,NULL,NULL,NULL),('appointmentscheduling.mandatory','false','true/false whether or not the appointmentscheduling module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','dfc69f43-83f8-426e-b8ea-8e25b583d00e',NULL,NULL,NULL,NULL),('appointmentscheduling.phoneNumberPersonAttributeTypeId','8','Person attribute type id for the phone number.','c017c824-182e-4a40-be86-6bcbace25cf1',NULL,NULL,NULL,NULL),('appointmentscheduling.started','true','DO NOT MODIFY. true/false whether or not the appointmentscheduling module has been started.  This is used to make sure modules that were running  prior to a restart are started again','4b81e0fc-6c0e-48d5-859f-3dc0c7ec8e5a',NULL,NULL,NULL,NULL),('appointmentschedulingui.mandatory','false','true/false whether or not the appointmentschedulingui module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','498d3473-25dc-498b-b3f0-f3656f7926d3',NULL,NULL,NULL,NULL),('appointmentschedulingui.started','true','DO NOT MODIFY. true/false whether or not the appointmentschedulingui module has been started.  This is used to make sure modules that were running  prior to a restart are started again','05e68eb7-c76d-40aa-af6d-82e0627deda3',NULL,NULL,NULL,NULL),('appui.mandatory','false','true/false whether or not the appui module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','6aa9312f-6953-4fe6-b894-ca5e885a8fa7',NULL,NULL,NULL,NULL),('appui.started','true','DO NOT MODIFY. true/false whether or not the appui module has been started.  This is used to make sure modules that were running  prior to a restart are started again','88e8ed5f-d774-47d9-a0c3-3a91009335dc',NULL,NULL,NULL,NULL),('atlas.mandatory','false','true/false whether or not the atlas module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','eca5c179-b4b8-4e26-86d2-d03b701407d1',NULL,NULL,NULL,NULL),('atlas.numberOfEncounters','?','Stores the last calculated count of non-voided encounters in the system (\"?\" if never calculated).','ea88d8dd-0cea-4b48-9ee6-073ceae8cc3b',NULL,NULL,NULL,NULL),('atlas.numberOfObservations','?','Stores the last calculated count of non-voided observations in the system (\"?\" if never calculated).','fc8f84d8-2cfc-491f-9d6a-34fac0dbde6d',NULL,NULL,NULL,NULL),('atlas.numberOfPatients','?','Stores the last calculated count of non-voided patients in the system (\"?\" if never calculated).','4b2564a2-79c0-4c1f-952c-d35ebf2b9e5e',NULL,NULL,NULL,NULL),('atlas.sendCounts','false','Send counts to OpenMRS Atlas.','5e117c24-336b-4568-92b8-2665137cc512',NULL,NULL,NULL,NULL),('atlas.started','true','DO NOT MODIFY. true/false whether or not the atlas module has been started.  This is used to make sure modules that were running  prior to a restart are started again','8e44cf0d-8050-4d5b-8785-e661de6907a6',NULL,NULL,NULL,NULL),('atlas.stopAskingToConfigure','false','When set to \'true\', we stop pestering admins to configure the atlas.','62baadfc-454e-4e3c-a67c-42c04c1e7946',NULL,NULL,NULL,NULL),('calculation.mandatory','false','true/false whether or not the calculation module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','5a37caa1-d45d-4500-81c8-c93cc16e957a',NULL,NULL,NULL,NULL),('calculation.started','true','DO NOT MODIFY. true/false whether or not the calculation module has been started.  This is used to make sure modules that were running  prior to a restart are started again','dffa4b2b-9125-4ad1-8746-b14af0c6cfe8',NULL,NULL,NULL,NULL),('chartsearch.httpSolrUrl','http://localhost','Url to HTTP SOLR. Uses only if \'Use Dedicated SOLR set to\ntrue\'','7db1b9cd-e63a-4117-8cd5-c8cb1c22af54',NULL,NULL,NULL,NULL),('chartsearch.mandatory','false','true/false whether or not the chartsearch module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','168b82f2-fb7a-4459-8f34-fd3d216bf0f4',NULL,NULL,NULL,NULL),('chartsearch.started','true','DO NOT MODIFY. true/false whether or not the chartsearch module has been started.  This is used to make sure modules that were running  prior to a restart are started again','bbe3fe04-f12f-4db2-b14f-f46aef81dd64',NULL,NULL,NULL,NULL),('chartsearch.useDedicatedSolrServer','false','Use dedicated SOLR server instead of embedded SOLR (true,\nfalse).\nYou will need to specify SOLR url. Requires module restart.','ef09d321-e09f-4644-8fb3-0b3c03e11717',NULL,NULL,NULL,NULL),('concept.causeOfDeath','5002','Concept id of the concept defining the CAUSE OF DEATH concept','53cbfde6-1fac-4c01-9f07-076c2f58f694',NULL,NULL,NULL,NULL),('concept.defaultConceptMapType','NARROWER-THAN','Default concept map type which is used when no other is set','ac46e1c6-6112-49c7-b7fc-f96588526e44',NULL,NULL,NULL,NULL),('concept.false','2','Concept id of the concept defining the FALSE boolean concept','fac678da-3c69-4a7e-a2b9-e42a1d8ead01',NULL,NULL,NULL,NULL),('concept.height','5090','Concept id of the concept defining the HEIGHT concept','222db48f-d223-4705-8950-6b8c1b3b5a1e',NULL,NULL,NULL,NULL),('concept.medicalRecordObservations','1238','The concept id of the MEDICAL_RECORD_OBSERVATIONS concept.  This concept_id is presumed to be the generic grouping (obr) concept in hl7 messages.  An obs_group row is not created for this concept.','20672167-4886-48c3-bc41-c7676e05fd2a',NULL,NULL,NULL,NULL),('concept.none','1107','Concept id of the concept defining the NONE concept','b41c2d36-8867-44c1-826f-f3ea0e0a0bf2',NULL,NULL,NULL,NULL),('concept.otherNonCoded','5622','Concept id of the concept defining the OTHER NON-CODED concept','5e8b7b0e-75d4-48ae-9ee1-d0ab796c97ef',NULL,NULL,NULL,NULL),('concept.patientDied','1742','Concept id of the concept defining the PATIENT DIED concept','2e5bd45e-4b75-406b-bcd4-300572fd0040',NULL,NULL,NULL,NULL),('concept.problemList','1284','The concept id of the PROBLEM LIST concept.  This concept_id is presumed to be the generic grouping (obr) concept in hl7 messages.  An obs_group row is not created for this concept.','ba653a62-9e30-445d-ac01-f88157f81cf5',NULL,NULL,NULL,NULL),('concept.reasonExitedCare',NULL,'Concept id of the concept defining the REASON EXITED CARE concept','1925034c-0924-4f76-adbc-ed0f7486a4f2',NULL,NULL,NULL,NULL),('concept.reasonOrderStopped','1812','Concept id of the concept defining the REASON ORDER STOPPED concept','b63893fd-12f9-4ac5-aec7-233c7420202c',NULL,NULL,NULL,NULL),('concept.true','1','Concept id of the concept defining the TRUE boolean concept','e98edc0c-0e87-4af1-8c4e-bb0f0625ae67',NULL,NULL,NULL,NULL),('concept.weight','5089','Concept id of the concept defining the WEIGHT concept','1334cf8f-0b3a-4ce9-8cdf-e7ecd06fb44f',NULL,NULL,NULL,NULL),('conceptDrug.dosageForm.conceptClasses',NULL,'A comma-separated list of the allowed concept classes for the dosage form field of the concept drug management form.','dd7155eb-122e-4968-97b2-fa94751f031c',NULL,NULL,NULL,NULL),('conceptDrug.route.conceptClasses',NULL,'A comma-separated list of the allowed concept classes for the route field of the concept drug management form.','6405b584-2e2a-438e-bd8a-ee4c29e43d08',NULL,NULL,NULL,NULL),('concepts.locked','false','if true, do not allow editing concepts','dcde5b44-494f-40d9-8c39-6cd2899b10fe','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('concept_map_type_management.enable','false','Enables or disables management of concept map types','93e43e25-ab84-405a-8a26-c6f3d1c47684','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('conditionList.endReasonConceptSetUuid',NULL,'UUID of end reason concept set','04e69e10-d3ac-40b0-afce-70840babfeca',NULL,NULL,NULL,NULL),('conditionList.nonCodedUuid',NULL,'UUID of non coded concept','337936b1-cd28-4487-9660-9bba9552e5d5',NULL,NULL,NULL,NULL),('coreapps.dashboardUrl','/coreapps/clinicianfacing/patient.page?patientId={{patientId}}&app=pih.app.clinicianDashboard','Allows one to override the default dashboard url','9f2444fe-6d14-4334-932c-cb47d4b1537e',NULL,NULL,NULL,NULL),('coreapps.defaultPatientIdentifierLocation',NULL,'When adding a new patient identifier via the patient dashboard, the location to use if not specified (and the identifier type requires a location)','356d866b-2c0c-4d08-b48f-458fee8ee228',NULL,NULL,NULL,NULL),('coreapps.mandatory','false','true/false whether or not the coreapps module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','c2e6b381-abae-4c2a-ba4c-7f40f35f2b5b',NULL,NULL,NULL,NULL),('coreapps.recentDiagnosisPeriodInDays',NULL,'Number of days to consider diagnosis as recent','4bc4cfea-d985-43c2-92e0-321c0c81b281',NULL,NULL,NULL,NULL),('coreapps.searchDelayLong','1000','Time, in milliseconds, that the patient search waits for an additional keystroke if only 1 or 2 characters have been entered','996345c1-468c-482f-a274-14677bdcd1e0',NULL,NULL,NULL,NULL),('coreapps.searchDelayShort','300','Default time, in milliseconds, that the patient search waits for an additional keystroke before performed a search','d2b380be-6368-444b-80cc-cb46466d0eb4',NULL,NULL,NULL,NULL),('coreapps.started','true','DO NOT MODIFY. true/false whether or not the coreapps module has been started.  This is used to make sure modules that were running  prior to a restart are started again','6065f63f-8a8d-4811-b761-852715e632fc',NULL,NULL,NULL,NULL),('coreapps.visitsPageUrl','/coreapps/patientdashboard/patientDashboard.page?patientId={{patientId}}#visits','Allows one to override the default general patient visits page','72a1f9fe-268e-4064-8292-83d1fd4dd11b',NULL,NULL,NULL,NULL),('coreapps.visitsPageWithSpecificVisitUrl','/coreapps/patientdashboard/patientDashboard.page?patientId={{patientId}}&visitId={{visit.id}}#','Allows one to override the default page that displays the details of a specific patient visit','ea350bae-ec6a-4c66-9068-bd31e1da0f78',NULL,NULL,NULL,NULL),('dashboard.encounters.maximumNumberToShow','3','An integer which, if specified, would determine the maximum number of encounters to display on the encounter tab of the patient dashboard.','e4f2f132-16e7-463b-b5c5-ec9949c4799c',NULL,NULL,NULL,NULL),('dashboard.encounters.providerDisplayRoles',NULL,'A comma-separated list of encounter roles (by name or id). Providers with these roles in an encounter will be displayed on the encounter tab of the patient dashboard.','d994cf9a-c83f-4e3a-9c61-446114e8da94',NULL,NULL,NULL,NULL),('dashboard.encounters.showEditLink','true','true/false whether or not to show the \'Edit Encounter\' link on the patient dashboard','d708865d-f6d1-44e7-808a-592a2af33506','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('dashboard.encounters.showEmptyFields','true','true/false whether or not to show empty fields on the \'View Encounter\' window','9710e850-8bad-48e9-b924-9c19427eb1cc','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('dashboard.encounters.showViewLink','true','true/false whether or not to show the \'View Encounter\' link on the patient dashboard','fd76b933-60e9-47ef-a9c4-4ae4e303f70c','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('dashboard.encounters.usePages','smart','true/false/smart on how to show the pages on the \'View Encounter\' window.  \'smart\' means that if > 50% of the fields have page numbers defined, show data in pages','feebd529-42f4-4343-bd05-035d51de1610',NULL,NULL,NULL,NULL),('dashboard.header.programs_to_show',NULL,'List of programs to show Enrollment details of in the patient header. (Should be an ordered comma-separated list of program_ids or names.)','a8384b6e-f08d-427f-a2c5-d30b4efa7d5e',NULL,NULL,NULL,NULL),('dashboard.header.showConcept','5497','Comma delimited list of concepts ids to show on the patient header overview','ee89fac7-65bd-4c3a-b48c-70208097684f',NULL,NULL,NULL,NULL),('dashboard.header.workflows_to_show',NULL,'List of programs to show Enrollment details of in the patient header. List of workflows to show current status of in the patient header. These will only be displayed if they belong to a program listed above. (Should be a comma-separated list of program_workflow_ids.)','3572179c-a7c9-4065-a1a6-d9d0079ee2d4',NULL,NULL,NULL,NULL),('dashboard.metadata.caseConversion',NULL,'Indicates which type automatic case conversion is applied to program/workflow/state in the patient dashboard. Valid values: lowercase, uppercase, capitalize. If empty no conversion is applied.','459b3829-3f47-456b-aedc-6a0e2fd2ae3e',NULL,NULL,NULL,NULL),('dashboard.overview.showConcepts',NULL,'Comma delimited list of concepts ids to show on the patient dashboard overview tab','556486b0-b866-48a9-9d54-b2252013288f',NULL,NULL,NULL,NULL),('dashboard.regimen.displayDrugSetIds','ANTIRETROVIRAL DRUGS,TUBERCULOSIS TREATMENT DRUGS','Drug sets that appear on the Patient Dashboard Regimen tab. Comma separated list of name of concepts that are defined as drug sets.','a77f764a-3e59-4b2a-b664-cf93bd665dcb',NULL,NULL,NULL,NULL),('dashboard.regimen.displayFrequencies','7 days/week,6 days/week,5 days/week,4 days/week,3 days/week,2 days/week,1 days/week','Frequency of a drug order that appear on the Patient Dashboard. Comma separated list of name of concepts that are defined as drug frequencies.','4202dd04-f2a9-4256-8b40-2541fa353b93',NULL,NULL,NULL,NULL),('dashboard.regimen.standardRegimens','<list>  <regimenSuggestion>    <drugComponents>      <drugSuggestion>        <drugId>2</drugId>        <dose>1</dose>        <units>tab(s)</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>    </drugComponents>    <displayName>3TC + d4T(30) + NVP (Triomune-30)</displayName>    <codeName>standardTri30</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion>  <regimenSuggestion>    <drugComponents>      <drugSuggestion>        <drugId>3</drugId>        <dose>1</dose>        <units>tab(s)</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>    </drugComponents>    <displayName>3TC + d4T(40) + NVP (Triomune-40)</displayName>    <codeName>standardTri40</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion>  <regimenSuggestion>    <drugComponents>      <drugSuggestion>        <drugId>39</drugId>        <dose>1</dose>        <units>tab(s)</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>      <drugSuggestion>        <drugId>22</drugId>        <dose>200</dose>        <units>mg</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>    </drugComponents>    <displayName>AZT + 3TC + NVP</displayName>    <codeName>standardAztNvp</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion>  <regimenSuggestion>    <drugComponents>      <drugSuggestion reference=\"../../../regimenSuggestion[3]/drugComponents/drugSuggestion\"/>      <drugSuggestion>        <drugId>11</drugId>        <dose>600</dose>        <units>mg</units>        <frequency>1/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>    </drugComponents>    <displayName>AZT + 3TC + EFV(600)</displayName>    <codeName>standardAztEfv</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion>  <regimenSuggestion>    <drugComponents>      <drugSuggestion>        <drugId>5</drugId>        <dose>30</dose>        <units>mg</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>      <drugSuggestion>        <drugId>42</drugId>        <dose>150</dose>        		<units>mg</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>      <drugSuggestion reference=\"../../../regimenSuggestion[4]/drugComponents/drugSuggestion[2]\"/>    </drugComponents>    <displayName>d4T(30) + 3TC + EFV(600)</displayName>    <codeName>standardD4t30Efv</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion>  <regimenSuggestion>    <drugComponents>      <drugSuggestion>        <drugId>6</drugId>        <dose>40</dose>        <units>mg</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>      <drugSuggestion reference=\"../../../regimenSuggestion[5]/drugComponents/drugSuggestion[2]\"/>      <drugSuggestion reference=\"../../../regimenSuggestion[4]/drugComponents/drugSuggestion[2]\"/>    </drugComponents>    <displayName>d4T(40) + 3TC + EFV(600)</displayName>    <codeName>standardD4t40Efv</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion></list>','XML description of standard drug regimens, to be shown as shortcuts on the dashboard regimen entry tab','a7127f0b-156d-49d1-a9a2-64021c59bf6d',NULL,NULL,NULL,NULL),('dashboard.relationships.show_types',NULL,'Types of relationships separated by commas.  Doctor/Patient,Parent/Child','7b3a7e6a-6f92-4c84-b7de-bc6ce9c9638b',NULL,NULL,NULL,NULL),('dashboard.showPatientName','false','Whether or not to display the patient name in the patient dashboard title. Note that enabling this could be security risk if multiple users operate on the same computer.','502514cd-057c-4586-84ef-fcfa1899f53f','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('dataexchange.mandatory','false','true/false whether or not the dataexchange module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','4d22fdbb-c441-49b5-9966-7704cfa69213',NULL,NULL,NULL,NULL),('dataexchange.started','true','DO NOT MODIFY. true/false whether or not the dataexchange module has been started.  This is used to make sure modules that were running  prior to a restart are started again','6e7058d6-8934-4e66-b669-ff7e8681ad47',NULL,NULL,NULL,NULL),('datePicker.weekStart','0','First day of the week in the date picker. Domingo/Dimanche/Sunday:0  Lunes/Lundi/Monday:1','178f9798-f932-45c0-af31-485dac4968f5',NULL,NULL,NULL,NULL),('default_locale','en_GB','Specifies the default locale. You can specify both the language code(ISO-639) and the country code(ISO-3166), e.g. \'en_GB\' or just country: e.g. \'en\'','4df85c53-405b-4792-9b8c-b15ec9034e85',NULL,NULL,NULL,NULL),('default_location','Unknown Location','The name of the location to use as a system default','f036c018-e65d-4979-a81f-aa735438d3c2',NULL,NULL,NULL,NULL),('default_theme',NULL,'Default theme for users.  OpenMRS ships with themes of \'green\', \'orange\', \'purple\', and \'legacy\'','30943a37-0e85-4a1d-805b-a63192d574c1',NULL,NULL,NULL,NULL),('drugOrder.requireDrug','false','Set to value true if you need to specify a formulation(Drug) when creating a drug order.','5ab894db-0358-4362-aae0-08ca1d3a0097',NULL,NULL,NULL,NULL),('emr.admissionEncounterType',NULL,'UUID of the encounter type for admitting a patient','46de9e77-ead9-40ec-9c01-6cebe77df306',NULL,NULL,NULL,NULL),('emr.admissionForm',NULL,'UUID of the Admission Form (not required)','efa7e1c9-a866-4fd0-863f-80e602adeeb2',NULL,NULL,NULL,NULL),('emr.atFacilityVisitType',NULL,'UUID of the VisitType that we use for newly-created visits','6dfc11ba-a806-4851-98c5-62df04f6a388',NULL,NULL,NULL,NULL),('emr.encounterMatcher',NULL,'Class name of a custom encounter matcher (e.g. org.openmrs.module.example.customEncounterMatcher)','7ba7b1f8-83ea-41e2-ae1d-7a05f62ea3a0',NULL,NULL,NULL,NULL),('emr.exitFromInpatientEncounterType',NULL,'UUID of the encounter type for exiting a patient from inpatient care','2cf8e9c6-d39d-4fc9-a213-572ce1a9c68d',NULL,NULL,NULL,NULL),('emr.exitFromInpatientForm',NULL,'UUID of the Discharge Form (not required)','ad951096-89ae-4d00-9fc1-3c14af6d3d99',NULL,NULL,NULL,NULL),('emr.extraPatientIdentifierTypes',NULL,'A list of UUIDs indicating extra Patient Identifier Types that should be displayed','89331304-b3be-4d45-95a6-48a84900d623',NULL,NULL,NULL,NULL),('emr.primaryIdentifierType',NULL,'Primary identifier type for looking up patients, generating barcodes, etc','902afb81-8abd-40a6-980d-c71e167d47ad',NULL,NULL,NULL,NULL),('emr.transferWithinHospitalEncounterType',NULL,'UUID of the encounter type for transferring a patient within the hospital','128d461f-74c7-4266-b6f1-92272e1b843d',NULL,NULL,NULL,NULL),('emr.transferWithinHospitalForm',NULL,'UUID of the Transfer Form (not required)','62b0bb99-4a92-4c8c-8815-6709a1c62e81',NULL,NULL,NULL,NULL),('emr.unknownLocation',NULL,'UUID of the Location that represents an Unknown Location','7dd69949-9286-4505-bd11-1613ba32ed88',NULL,NULL,NULL,NULL),('emr.unknownProvider',NULL,'UUID of the Provider that represents an Unknown Provider','eb64edc4-1fe1-4eea-9a36-36a6af9fdded',NULL,NULL,NULL,NULL),('emr.visitNoteEncounterType',NULL,'UUID of the encounter type for a visit note','f31b4741-d580-4866-b1cb-5284fe860afb',NULL,NULL,NULL,NULL),('emrapi.lastViewedPatientSizeLimit','50','Specifies the system wide number of patients to store as last viewed for a single user,\n            defaults to 50 if not specified','58609eaa-638a-49c5-9c97-3f215940035e',NULL,NULL,NULL,NULL),('emrapi.mandatory','false','true/false whether or not the emrapi module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','2122fd03-5498-4385-8ca9-221d62437788',NULL,NULL,NULL,NULL),('emrapi.nonDiagnosisConceptSets',NULL,'UUIDs or mapping of non diagnosis concept sets','d1e4d56d-e86e-48bb-862f-cd0c78db5233',NULL,NULL,NULL,NULL),('emrapi.started','true','DO NOT MODIFY. true/false whether or not the emrapi module has been started.  This is used to make sure modules that were running  prior to a restart are started again','4345cc57-cb44-4deb-baee-32465dcbaaa8',NULL,NULL,NULL,NULL),('emrapi.suppressedDiagnosisConcepts',NULL,'UUIDs or mappings of suppressed diagnosis concepts','ad3ea289-a054-4740-8871-958c170ef2e8',NULL,NULL,NULL,NULL),('encounterForm.obsSortOrder','number','The sort order for the obs listed on the encounter edit form.  \'number\' sorts on the associated numbering from the form schema.  \'weight\' sorts on the order displayed in the form schema.','4d600f5e-13d3-4dec-88d1-4798514648c0',NULL,NULL,NULL,NULL),('EncounterType.encounterTypes.locked','false','saving, retiring or deleting an Encounter Type is not permitted, if true','a175cd65-5ffb-4579-a550-004ce5e6d4aa','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('event.mandatory','false','true/false whether or not the event module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','2e4cab72-f0d5-4f51-9d93-e9b16ee87166',NULL,NULL,NULL,NULL),('event.started','true','DO NOT MODIFY. true/false whether or not the event module has been started.  This is used to make sure modules that were running  prior to a restart are started again','486a7941-aa50-4e90-8949-9453755ffdc4',NULL,NULL,NULL,NULL),('Form.forms.locked','false','Set to a value of true if you do not want any changes to be made on forms, else set to false.','d0c70878-ad8c-4503-8a11-6504e37cd5b9',NULL,NULL,NULL,NULL),('FormEntry.enableDashboardTab','true','true/false whether or not to show a Form Entry tab on the patient dashboard','a9cdff22-f457-45fb-aa2c-c237f6276cdd','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('FormEntry.enableOnEncounterTab','false','true/false whether or not to show a Enter Form button on the encounters tab of the patient dashboard','7a6530c7-cf88-4272-ad94-091c510fd7bb','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('formentryapp.mandatory','false','true/false whether or not the formentryapp module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','3895e3c6-9ab5-4404-aafc-0cffd07d0c57',NULL,NULL,NULL,NULL),('formentryapp.started','true','DO NOT MODIFY. true/false whether or not the formentryapp module has been started.  This is used to make sure modules that were running  prior to a restart are started again','f7ee2271-39fc-4965-aa47-5f7bd59cdd8d',NULL,NULL,NULL,NULL),('graph.color.absolute','rgb(20,20,20)','Color of the \'invalid\' section of numeric graphs on the patient dashboard.','a007a3f5-86e8-4901-b6b4-cf9389d09a7d',NULL,NULL,NULL,NULL),('graph.color.critical','rgb(200,0,0)','Color of the \'critical\' section of numeric graphs on the patient dashboard.','1520ff94-d711-432c-a154-5fcd761c0865',NULL,NULL,NULL,NULL),('graph.color.normal','rgb(255,126,0)','Color of the \'normal\' section of numeric graphs on the patient dashboard.','b379b7e4-6026-4f6c-aefd-15284ddc97a7',NULL,NULL,NULL,NULL),('gzip.enabled','false','Set to \'true\' to turn on OpenMRS\'s gzip filter, and have the webapp compress data before sending it to any client that supports it. Generally use this if you are running Tomcat standalone. If you are running Tomcat behind Apache, then you\'d want to use Apache to do gzip compression.','e05e521a-d396-4dd3-aff0-5216bc988fea','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('hl7_archive.dir','hl7_archives','The default name or absolute path for the folder where to write the hl7_in_archives.','80b280ef-d160-4e54-8ba9-870ae83e93b0',NULL,NULL,NULL,NULL),('hl7_processor.ignore_missing_patient_non_local','false','If true, hl7 messages for patients that are not found and are non-local will silently be dropped/ignored','f394237a-91cb-4223-a7f5-7f8133d3fadc','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('htmlformentry.archiveDir','htmlformentry/archive/%Y/%m','Used to specify the directory used to serialize data as a blob submitted via html forms before propagation to the database.\nIf a relative is specified then the directory is created in the default application data directory otherwise the absolute\npath is used.The %Y and %M are replaced with 4 digit year and 2 digit month respectively. If this property is empty nothing then nothing is saved.','8f566b2b-17df-4686-b1df-0b49aacc6ef0',NULL,NULL,NULL,NULL),('htmlformentry.archiveHtmlForms','False','Set to True if you want to archive the submitted html forms and False otherwise','17bafa6c-89cd-4faa-9e66-f041efde576c',NULL,NULL,NULL,NULL),('htmlformentry.dateFormat',NULL,'Always display dates in HTML Forms in this (Java) date format. E.g. \"dd/MMM/yyyy\" for 31/Jan/2012.','471b458e-233f-4467-9bd2-ad7096a453a6',NULL,NULL,NULL,NULL),('htmlformentry.datePickerYearsRange','110,20','datePickerYearsRange parameter can be  set here Eg:\'110,20\' meaning that the possible years that appear in the datepicker dropdown range from  20 years past the current year, and 110 years prior to the current year.','f357714f-9cb2-42c5-9fc6-9e33902fc04d',NULL,NULL,NULL,NULL),('htmlformentry.mandatory','false','true/false whether or not the htmlformentry module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','3c076688-952b-41e1-b49e-9ef49fc45822',NULL,NULL,NULL,NULL),('htmlformentry.showDateFormat','true','Set to true if you want static text for the date format to be displayed next to date widgets, else set to false.','755f1b77-4e44-4884-80b7-1420ceb3a87e',NULL,NULL,NULL,NULL),('htmlformentry.started','true','DO NOT MODIFY. true/false whether or not the htmlformentry module has been started.  This is used to make sure modules that were running  prior to a restart are started again','4a617632-82a6-41f4-a34b-9beefbead64f',NULL,NULL,NULL,NULL),('htmlformentry.timeFormat','HH:mm','Always display times in HTML Forms in this (Java) date format. E.g. \"HH:mm\" for 14:45.','42c386b0-cda9-46af-870c-9364750cfd5d',NULL,NULL,NULL,NULL),('htmlformentry19ext.mandatory','false','true/false whether or not the htmlformentry19ext module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','544693b2-0af3-4350-a163-11c3ad7dd497',NULL,NULL,NULL,NULL),('htmlformentry19ext.started','true','DO NOT MODIFY. true/false whether or not the htmlformentry19ext module has been started.  This is used to make sure modules that were running  prior to a restart are started again','2ce1410f-d32c-43eb-ad9b-ef0ab3f6bca7',NULL,NULL,NULL,NULL),('htmlformentryui.mandatory','false','true/false whether or not the htmlformentryui module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','53b30ef9-9ede-42ad-84eb-ac41ff428556',NULL,NULL,NULL,NULL),('htmlformentryui.started','true','DO NOT MODIFY. true/false whether or not the htmlformentryui module has been started.  This is used to make sure modules that were running  prior to a restart are started again','7e81bc65-6d04-4ce7-9f66-ce26bb3a7ee6',NULL,NULL,NULL,NULL),('htmlwidgets.mandatory','false','true/false whether or not the htmlwidgets module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','9b872144-5ba4-4ae3-861c-14f9d6075b15',NULL,NULL,NULL,NULL),('htmlwidgets.started','true','DO NOT MODIFY. true/false whether or not the htmlwidgets module has been started.  This is used to make sure modules that were running  prior to a restart are started again','42d7a575-acd0-4fb2-a675-59276dd9da75',NULL,NULL,NULL,NULL),('idgen.database_version','2.5.1','DO NOT MODIFY.  Current database version number for the idgen module.','f66343ba-7b6a-47f4-89b9-4a1b2576407a',NULL,NULL,NULL,NULL),('idgen.mandatory','false','true/false whether or not the idgen module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','47204a31-2675-413c-859b-f1aa1276a174',NULL,NULL,NULL,NULL),('idgen.started','true','DO NOT MODIFY. true/false whether or not the idgen module has been started.  This is used to make sure modules that were running  prior to a restart are started again','b9989796-45c0-44d6-adc5-154019e52f2d',NULL,NULL,NULL,NULL),('layout.address.format','<org.openmrs.layout.web.address.AddressTemplate>\n    <nameMappings class=\"properties\">\n      <property name=\"postalCode\" value=\"Location.postalCode\"/>\n      <property name=\"address2\" value=\"Location.address2\"/>\n      <property name=\"address1\" value=\"Location.address1\"/>\n      <property name=\"country\" value=\"Location.country\"/>\n      <property name=\"stateProvince\" value=\"Location.stateProvince\"/>\n      <property name=\"cityVillage\" value=\"Location.cityVillage\"/>\n    </nameMappings>\n    <sizeMappings class=\"properties\">\n      <property name=\"postalCode\" value=\"10\"/>\n      <property name=\"address2\" value=\"40\"/>\n      <property name=\"address1\" value=\"40\"/>\n      <property name=\"country\" value=\"10\"/>\n      <property name=\"stateProvince\" value=\"10\"/>\n      <property name=\"cityVillage\" value=\"10\"/>\n    </sizeMappings>\n    <lineByLineFormat>\n      <string>address1</string>\n      <string>address2</string>\n      <string>cityVillage stateProvince country postalCode</string>\n    </lineByLineFormat>\n  </org.openmrs.layout.web.address.AddressTemplate>','XML description of address formats','ec41b7ff-95bd-45ab-bc61-fd6ab0c49953',NULL,NULL,NULL,NULL),('layout.name.format','short','Format in which to display the person names.  Valid values are short, long','4f71a767-fc74-4cdb-ae70-0687bc6b1b51',NULL,NULL,NULL,NULL),('locale.allowed.list','en, es, fr, it, pt','Comma delimited list of locales allowed for use on system','4c3b3326-d567-4e2c-85f5-5e18af33b356',NULL,NULL,NULL,NULL),('location.field.style','default','Type of widget to use for location fields','5f68249b-3a99-478e-9cc9-ee98e0fbfbf3',NULL,NULL,NULL,NULL),('log.layout','%p - %C{1}.%M(%L) |%d{ISO8601}| %m%n','A log layout pattern which is used by the OpenMRS file appender.','25860c14-4ad4-453e-b466-00be38899a89',NULL,NULL,NULL,NULL),('log.level','org.openmrs.api:info','Logging levels for log4j.xml. Valid format is class:level,class:level. If class not specified, \'org.openmrs.api\' presumed. Valid levels are trace, debug, info, warn, error or fatal','bc0f1801-ef27-4dda-a75e-8d21eea0cec7',NULL,NULL,NULL,NULL),('log.location',NULL,'A directory where the OpenMRS log file appender is stored. The log file name is \'openmrs.log\'.','d304ae49-f901-422d-aa79-7163a4ac54c7',NULL,NULL,NULL,NULL),('mail.debug','false','true/false whether to print debugging information during mailing','2dfe8108-dd62-468e-8fe6-da925b3ecad4',NULL,NULL,NULL,NULL),('mail.default_content_type','text/plain','Content type to append to the mail messages','0ae519da-8106-4261-9ae0-63eafdbc8cdf',NULL,NULL,NULL,NULL),('mail.from','info@openmrs.org','Email address to use as the default from address','0a16cc93-d998-4f76-b5c5-cd654f3a690d',NULL,NULL,NULL,NULL),('mail.password','test','Password for the SMTP user (if smtp_auth is enabled)','4cb99db5-659c-42a4-9467-4ed2b9e23a63',NULL,NULL,NULL,NULL),('mail.smtp.starttls.enable','false','Set to true to enable TLS encryption, else set to false','7cab0cc3-7b42-4fd9-8b35-43e4ec06aed2',NULL,NULL,NULL,NULL),('mail.smtp_auth','false','true/false whether the smtp host requires authentication','f88f5ceb-db71-4e24-b6cf-1afd678875fa',NULL,NULL,NULL,NULL),('mail.smtp_host','localhost','SMTP host name','48f9d9d5-3894-4200-ad34-ca7f911ac66f',NULL,NULL,NULL,NULL),('mail.smtp_port','25','SMTP port','85d020ea-c1b5-4841-a7c8-6e306aaa064e',NULL,NULL,NULL,NULL),('mail.transport_protocol','smtp','Transport protocol for the messaging engine. Valid values: smtp','0e14d1ad-5062-4928-ab6f-4aa0e536ec22',NULL,NULL,NULL,NULL),('mail.user','test','Username of the SMTP user (if smtp_auth is enabled)','949da097-fb45-47db-9e92-59723d8ed3ce',NULL,NULL,NULL,NULL),('metadatadeploy.mandatory','false','true/false whether or not the metadatadeploy module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','dd3a074b-a491-4731-9a23-b9ec103ed73c',NULL,NULL,NULL,NULL),('metadatadeploy.started','true','DO NOT MODIFY. true/false whether or not the metadatadeploy module has been started.  This is used to make sure modules that were running  prior to a restart are started again','364ec697-6a6a-47b0-b883-6d6aad633121',NULL,NULL,NULL,NULL),('metadatamapping.addLocalMappings',NULL,'Specifies whether the concept mappings to the local dictionary should be created when exporting concepts','2a7b5a5c-3758-40cb-b64e-685d88b6be50',NULL,NULL,NULL,NULL),('metadatamapping.mandatory','false','true/false whether or not the metadatamapping module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','ac5d03d2-f9ab-4011-997d-8f6f5c8cac9f',NULL,NULL,NULL,NULL),('metadatamapping.started','true','DO NOT MODIFY. true/false whether or not the metadatamapping module has been started.  This is used to make sure modules that were running  prior to a restart are started again','33de647e-8719-4295-aac3-9dd13a5edbcf',NULL,NULL,NULL,NULL),('metadatasharing.enableOnTheFlyPackages','false','Specifies whether metadata packages can be exported on the fly','48c35af0-f443-471b-8ebb-503ec4d3674b',NULL,NULL,NULL,NULL),('metadatasharing.mandatory','false','true/false whether or not the metadatasharing module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','242c2b7c-768e-4baf-992a-c9ed222dcf46',NULL,NULL,NULL,NULL),('metadatasharing.persistIdsForClasses',NULL,'A comma separated list of class package/names that denotes classes to try and persist ids for. Common options: org.openmrs.Concept,org.openmrs.Form,org.openmrs.ConceptDatatype,org.openmrs.ConceptClass,org.openmrs.EncounterType,org.openmrs.IdentifierType,org.openmrs.RelationshipType,org.openmrs.Location','a8ff7ef4-4382-4154-b31e-4d2a07ac2add',NULL,NULL,NULL,NULL),('metadatasharing.preferredConceptSourceIds',NULL,'Comma-separated list of concept source Ids for preferred sources, in case an incoming concept \nhas duplicate mappings to any of these sources, no confirmation will be required unless its \ndatatype or concept class differs from that of the existing concept','dfb71b14-cbbe-43fa-b3a0-40f79bb1a15c',NULL,NULL,NULL,NULL),('metadatasharing.started','true','DO NOT MODIFY. true/false whether or not the metadatasharing module has been started.  This is used to make sure modules that were running  prior to a restart are started again','48ab61d2-7ac5-4e7c-ab1a-e4fd9e9fddc0',NULL,NULL,NULL,NULL),('metadatasharing.webservicesKey',NULL,'Key to grant access to remote systems to consume module webservices RESTfully','c2c1a4b7-14e9-445b-bdb6-195cfe9fb49a',NULL,NULL,NULL,NULL),('minSearchCharacters','2','Number of characters user must input before searching is started.','391812c0-72df-4c79-840d-fa732b0c9c85',NULL,NULL,NULL,NULL),('module_repository_folder','modules','Name of the folder in which to store the modules','5cba1371-2b35-49bf-b01c-31ad4dadc129',NULL,NULL,NULL,NULL),('newPatientForm.relationships',NULL,'Comma separated list of the RelationshipTypes to show on the new/short patient form.  The list is defined like \'3a, 4b, 7a\'.  The number is the RelationshipTypeId and the \'a\' vs \'b\' part is which side of the relationship is filled in by the user.','45e8f64f-a49b-457e-b92a-08fb8a60d204',NULL,NULL,NULL,NULL),('new_patient_form.showRelationships','false','true/false whether or not to show the relationship editor on the addPatient.htm screen','0c1ae0e3-5314-4f84-8477-f2cd3fe65ba6','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('obs.complex_obs_dir','complex_obs','Default directory for storing complex obs.','1eeddfa1-cac2-48e2-863f-11a20b047241',NULL,NULL,NULL,NULL),('order.drugDispensingUnitsConceptUuid',NULL,'Specifies the uuid of the concept set where its members represent the possible drug dispensing units','45e08baf-0c8e-4efa-9763-52feb1ba146c',NULL,NULL,NULL,NULL),('order.drugDosingUnitsConceptUuid',NULL,'Specifies the uuid of the concept set where its members represent the possible drug dosing units','d3aec2f1-432e-467d-bd82-4f6622b5985e',NULL,NULL,NULL,NULL),('order.drugRoutesConceptUuid',NULL,'Specifies the uuid of the concept set where its members represent the possible drug routes','b18cf2b0-50c1-4f31-b242-5a9f1d063d1b',NULL,NULL,NULL,NULL),('order.durationUnitsConceptUuid',NULL,'Specifies the uuid of the concept set where its members represent the possible duration units','3794bdb0-5325-4011-8b9b-0101c22ce80d',NULL,NULL,NULL,NULL),('order.nextOrderNumberSeed','1','The next order number available for assignment','9851013e-1b30-40ed-a2c3-ee3aab08c5f1',NULL,NULL,NULL,NULL),('order.orderNumberGeneratorBeanId',NULL,'Specifies spring bean id of the order generator to use when assigning order numbers','acfabf75-8d54-42d3-9f48-0b23bed46c34',NULL,NULL,NULL,NULL),('order.testSpecimenSourcesConceptUuid',NULL,'Specifies the uuid of the concept set where its members represent the possible test specimen sources','703c81b6-965e-4527-bd3a-662960b5e180',NULL,NULL,NULL,NULL),('patient.defaultPatientIdentifierValidator','org.openmrs.patient.impl.LuhnIdentifierValidator','This property sets the default patient identifier validator.  The default validator is only used in a handful of (mostly legacy) instances.  For example, it\'s used to generate the isValidCheckDigit calculated column and to append the string \"(default)\" to the name of the default validator on the editPatientIdentifierType form.','0a9524e7-620b-4204-9648-4a83743d41d4',NULL,NULL,NULL,NULL),('patient.headerAttributeTypes',NULL,'A comma delimited list of PersonAttributeType names that will be shown on the patient dashboard','38b72a3d-7919-498e-810d-08a1dbfd965f',NULL,NULL,NULL,NULL),('patient.identifierPrefix',NULL,'This property is only used if patient.identifierRegex is empty.  The string here is prepended to the sql indentifier search string.  The sql becomes \"... where identifier like \'<PREFIX><QUERY STRING><SUFFIX>\';\".  Typically this value is either a percent sign (%) or empty.','2ab98cbe-2531-461d-a5f0-fcc5f0a2d53f',NULL,NULL,NULL,NULL),('patient.identifierRegex',NULL,'WARNING: Using this search property can cause a drop in mysql performance with large patient sets.  A MySQL regular expression for the patient identifier search strings.  The @SEARCH@ string is replaced at runtime with the user\'s search string.  An empty regex will cause a simply \'like\' sql search to be used. Example: ^0*@SEARCH@([A-Z]+-[0-9])?$','3cce3bcb-b40b-420b-a077-eb906332fcc2',NULL,NULL,NULL,NULL),('patient.identifierSearchPattern',NULL,'If this is empty, the regex or suffix/prefix search is used.  Comma separated list of identifiers to check.  Allows for faster searching of multiple options rather than the slow regex. e.g. @SEARCH@,0@SEARCH@,@SEARCH-1@-@CHECKDIGIT@,0@SEARCH-1@-@CHECKDIGIT@ would turn a request for \"4127\" into a search for \"in (\'4127\',\'04127\',\'412-7\',\'0412-7\')\"','a89dafdc-fc15-4c90-b9cf-f49d9ee0f712',NULL,NULL,NULL,NULL),('patient.identifierSuffix',NULL,'This property is only used if patient.identifierRegex is empty.  The string here is prepended to the sql indentifier search string.  The sql becomes \"... where identifier like \'<PREFIX><QUERY STRING><SUFFIX>\';\".  Typically this value is either a percent sign (%) or empty.','f41cdb16-10e2-4c50-8513-645720f2f44b',NULL,NULL,NULL,NULL),('patient.listingAttributeTypes',NULL,'A comma delimited list of PersonAttributeType names that should be displayed for patients in _lists_','1dbfb5bc-752d-4622-a5bb-db169b462343',NULL,NULL,NULL,NULL),('patient.nameValidationRegex','^[a-zA-Z \\-]+$','Names of the patients must pass this regex. Eg : ^[a-zA-Z \\-]+$ contains only english alphabet letters, spaces, and hyphens. A value of .* or the empty string means no validation is done.','91765d13-f730-4449-b99a-9793cfbc8e2a',NULL,NULL,NULL,NULL),('patient.viewingAttributeTypes',NULL,'A comma delimited list of PersonAttributeType names that should be displayed for patients when _viewing individually_','2b78ed52-bb7c-4575-b68a-1bb6509b9b3e',NULL,NULL,NULL,NULL),('PatientIdentifierType.locked','false','Set to a value of true if you do not want allow editing patient identifier types, else set to false.','96b48a74-838c-4c86-a8d3-1ffd0a80973f',NULL,NULL,NULL,NULL),('patientSearch.matchMode','START','Specifies how patient names are matched while searching patient. Valid values are \'ANYWHERE\' or \'START\'. Defaults to start if missing or invalid value is present.','4e73aa35-a26d-4f82-b584-68954ec6d89a',NULL,NULL,NULL,NULL),('patient_identifier.importantTypes',NULL,'A comma delimited list of PatientIdentifier names : PatientIdentifier locations that will be displayed on the patient dashboard.  E.g.: TRACnet ID:Rwanda,ELDID:Kenya','f8ab04c0-34ca-4178-b0a4-d59c4dc6387a',NULL,NULL,NULL,NULL),('person.attributeSearchMatchMode','EXACT','Specifies how person attributes are matched while searching person. Valid values are \'ANYWHERE\' or \'EXACT\'. Defaults to exact if missing or invalid value is present.','e3d69167-593f-43ce-a4a9-a63dae762de6',NULL,NULL,NULL,NULL),('person.searchMaxResults','1000','The maximum number of results returned by patient searches','9ed143ef-b731-46a1-8b4f-b3150e76093d',NULL,NULL,NULL,NULL),('PersonAttributeType.locked','false','Set to a value of true if you do not want allow editing person attribute types, else set to false.','9749b6db-4216-4626-8862-6d61d5590e12',NULL,NULL,NULL,NULL),('provider.unknownProviderUuid',NULL,'Specifies the uuid of the Unknown Provider account','69fe37d3-241f-4c29-95d7-3c22dc4683e2',NULL,NULL,NULL,NULL),('providermanagement.addressWidget','personAddress','Address widget to use throughout the module','ecaf634d-e35d-4f3d-8bb1-c4c5ffee749d',NULL,NULL,NULL,NULL),('providermanagement.advancedSearchPersonAttributeType',NULL,'Person attribute type, specified by uuid, to use as a search field on the advanced search page','59883852-dd8e-4437-ad77-18ce957e2903',NULL,NULL,NULL,NULL),('providermanagement.database_version','1.0','DO NOT MODIFY.  Current database version number for the providermanagement module.','0570f6aa-a529-4290-af49-08a684ea7c97',NULL,NULL,NULL,NULL),('providermanagement.historicalPatientListDisplayFields','Identifier:patient.patientIdentifier.identifier|Given Name:patient.personName.givenName|Family Name:patient.personName.familyName|Age:patient.age|Gender:patient.gender|Start Date:relationship.startDate|End Date:relationship.endDate','Fields to display in the historical patient lists; specified as a pipe-delimited list of label/field pairs','9ead992d-adb9-4640-a9e4-1a3c94f2c839',NULL,NULL,NULL,NULL),('providermanagement.historicalProviderListDisplayFields','Identifier:provider.identifier|Given Name:provider.person.personName.givenName|Family Name:provider.person.personName.familyName|Role:provider.providerRole|Gender:provider.person.gender|Start Date:relationship.startDate|End Date:relationship.endDate','Fields to display in the historical provider lists; specified as a pipe-delimited list of label/field pairs','d3b03333-b83a-4521-9030-0b0b68f8c8b6',NULL,NULL,NULL,NULL),('providermanagement.mandatory','false','true/false whether or not the providermanagement module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','b1eb9dec-7d9a-465a-9ed5-121e4697f509',NULL,NULL,NULL,NULL),('providermanagement.patientListDisplayFields','Identifier:patient.patientIdentifier.identifier|Given Name:patient.personName.givenName|Family Name:patient.personName.familyName|Age:patient.age|Gender:patient.gender|Start Date:relationship.startDate','Fields to display in the patient lists; specified as a pipe-delimited list of label/field pairs','d3b98b43-9139-4ada-8950-c0583efbe0ed',NULL,NULL,NULL,NULL),('providermanagement.patientSearchDisplayFields','Identifier:patient.patientIdentifier.identifier|Given Name:patient.personName.givenName|Family Name:patient.personName.familyName|Age:patient.age|Gender:patient.gender','Fields to display in the patient search results; specified as a pipe-delimited list of label/field pairs','c878cd94-8d45-4e1e-8c46-3aad77ff3565',NULL,NULL,NULL,NULL),('providermanagement.personAttributeTypes',NULL,'Person attributes to display on the provider dashboard; specified as a pipe-delimited list of person attribute type uuids','5c7479df-82c2-45c7-814b-76161fb676fd',NULL,NULL,NULL,NULL),('providermanagement.personSearchDisplayFields','Given Name:person.personName.givenName|Family Name:person.personName.familyName|Age:person.age|Gender:person.gender','Fields to display in the person search results; specified as a pipe-delimited list of label/field pairs','9cad962e-4fd2-468c-a3e1-7e3214ed4807',NULL,NULL,NULL,NULL),('providermanagement.providerListDisplayFields','Identifier:provider.identifier|Given Name:provider.person.personName.givenName|Family Name:provider.person.personName.familyName|Role:provider.providerRole|Gender:provider.person.gender|Start Date:relationship.startDate','Fields to display in the provider lists; specified as a pipe-delimited list of label/field pairs','d9067459-8a3e-4d29-a509-7a7244bcabe2',NULL,NULL,NULL,NULL),('providermanagement.providerSearchDisplayFields','Identifier:provider.identifier|Given Name:provider.person.personName.givenName|Family Name:provider.person.personName.familyName|Role:provider.providerRole|Gender:provider.person.gender','Fields to display in the provider search results; specified as a pipe-delimited list of label/field pairs','aa74e04e-57ef-46e4-8159-d99bcf326624',NULL,NULL,NULL,NULL),('providermanagement.restrictSearchToProvidersWithProviderRoles','false','True/false whether to restrict providers to those with roles','cf015af2-3576-44de-be99-0a0d67b5c204',NULL,NULL,NULL,NULL),('providermanagement.started','true','DO NOT MODIFY. true/false whether or not the providermanagement module has been started.  This is used to make sure modules that were running  prior to a restart are started again','97e28763-b767-4b2f-92ae-dcd063692c1b',NULL,NULL,NULL,NULL),('providerSearch.matchMode','EXACT','Specifies how provider identifiers are matched while searching for providers. Valid values are START,EXACT, END or ANYWHERE','5c4aa61d-944b-4348-9239-6fc3f49bcb3f',NULL,NULL,NULL,NULL),('referenceapplication.mandatory','false','true/false whether or not the referenceapplication module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','538c86cf-30c0-4b12-98b0-9cba54d0c0f2',NULL,NULL,NULL,NULL),('referenceapplication.started','true','DO NOT MODIFY. true/false whether or not the referenceapplication module has been started.  This is used to make sure modules that were running  prior to a restart are started again','9aefd5ef-0b2b-472a-bcef-885b0e10caaf',NULL,NULL,NULL,NULL),('referencedemodata.mandatory','false','true/false whether or not the referencedemodata module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','b46adf59-049c-4efd-9eee-2a0a1e1bd481',NULL,NULL,NULL,NULL),('referencedemodata.started','true','DO NOT MODIFY. true/false whether or not the referencedemodata module has been started.  This is used to make sure modules that were running  prior to a restart are started again','1d8d31d9-ff45-4d82-b79f-40a64751bcba',NULL,NULL,NULL,NULL),('referencemetadata.mandatory','false','true/false whether or not the referencemetadata module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','449d0a89-5537-44ad-8152-275c4bdac156',NULL,NULL,NULL,NULL),('referencemetadata.started','true','DO NOT MODIFY. true/false whether or not the referencemetadata module has been started.  This is used to make sure modules that were running  prior to a restart are started again','16bc6c6f-23da-475f-8096-9b443f2c3643',NULL,NULL,NULL,NULL),('registrationapp.mandatory','false','true/false whether or not the registrationapp module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','d7388f60-268f-4fd2-b554-4edc4afcfbb6',NULL,NULL,NULL,NULL),('registrationapp.started','true','DO NOT MODIFY. true/false whether or not the registrationapp module has been started.  This is used to make sure modules that were running  prior to a restart are started again','336f5df7-b02d-48fd-b089-129613b81def',NULL,NULL,NULL,NULL),('registrationcore.familyNameAutoSuggestList',NULL,'A comma separated list of common names to auto suggest for the family name field, when registering patients.','a65f10fc-ad96-42fb-be2d-b8fd5bdd7542',NULL,NULL,NULL,NULL),('registrationcore.fastSimilarPatientSearchAlgorithm','registrationcore.BasicSimilarPatientSearchAlgorithm','Specifies a bean used for the fast similar patient search algorithm.','864c26a3-bff4-4872-9ddc-23cbcae46b51',NULL,NULL,NULL,NULL),('registrationcore.givenNameAutoSuggestList',NULL,'A comma separated list of common names to auto suggest for the given name field, when registering patients.','3d985146-6c9c-40b5-9a31-bdf9df52dfcc',NULL,NULL,NULL,NULL),('registrationcore.identifierSourceId',NULL,'Specifies the identifier source to use when generating patient identifiers','abc508b9-236f-4e84-a7b1-85fafd499c6b',NULL,NULL,NULL,NULL),('registrationcore.mandatory','false','true/false whether or not the registrationcore module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','f67165ff-74fa-4aac-b273-626082ef027b',NULL,NULL,NULL,NULL),('registrationcore.mpi.implementation',NULL,'Which MPI to should we connect to? Specify a Spring bean name, or leave blank to disable MPI integration','1462f779-debc-4b60-a346-a6c69116cb72',NULL,NULL,NULL,NULL),('registrationcore.mpi.personIdentifierId',NULL,'Specifies id of identifier type which will be used for Mpi Person Id','c8a01800-91d2-44d3-b009-1ad4a87a4ddb',NULL,NULL,NULL,NULL),('registrationcore.mpi.url',NULL,'Specifies url to MPI server','f90134b8-2377-4a40-87b5-50589cf5f1cd',NULL,NULL,NULL,NULL),('registrationcore.mpi.username',NULL,'Specifies username for authentication to MPI server','22d6d1d0-e78a-4f5b-a2f8-6b34052cbc5f',NULL,NULL,NULL,NULL),('registrationcore.openempi.enableProbabilisticMatching','false','Specifies if probably matching should be used','c2eecec5-8a74-4e32-a102-58d248715b00',NULL,NULL,NULL,NULL),('registrationcore.openempi.globalIdentifierDomainId',NULL,'Specifies main identifier on MPI server (e.g. \"xyz\" for OpenEMPI)','9766a5ba-24ab-4a1b-909c-79bacd8b4e3f',NULL,NULL,NULL,NULL),('registrationcore.patientNameSearch','registrationcore.BasicPatientNameSearch','Specifies a bean used for the auto suggest name feature.','a50644a0-ed3d-4530-855d-079bb91950ef',NULL,NULL,NULL,NULL),('registrationcore.preciseSimilarPatientSearchAlgorithm','registrationcore.BasicExactPatientSearchAlgorithm','Specifies a bean used for the precise similar patient search algorithm.','6f8b149b-5034-43e7-8560-17814d825ee8',NULL,NULL,NULL,NULL),('registrationcore.started','true','DO NOT MODIFY. true/false whether or not the registrationcore module has been started.  This is used to make sure modules that were running  prior to a restart are started again','18972402-7e0d-4cff-8999-2c9046b7d743',NULL,NULL,NULL,NULL),('report.deleteReportsAgeInHours','72','Reports that are not explicitly saved are deleted automatically when they are this many hours old. (Values less than or equal to zero means do not delete automatically)','b228baaf-13ec-42e3-9a28-8a92c5426e58',NULL,NULL,NULL,NULL),('report.xmlMacros',NULL,'Macros that will be applied to Report Schema XMLs when they are interpreted. This should be java.util.properties format.','53fba62e-0b12-4cb0-8222-2583fd17ff74',NULL,NULL,NULL,NULL),('reporting.dataEvaluationBatchSize','-1','This determines whether to run evaluators for Data in batches and what the size of those batches should be.\nA value of less than or equal to 0 indicates that no batching is desired.','7bd60964-9dad-4591-8a21-8e3c1991cf5b',NULL,NULL,NULL,NULL),('reporting.defaultDateFormat','dd/MMM/yyyy','Default date format to use when formatting report data','6e2d5abb-224a-4a3d-a95c-8b4ed1dbae3d',NULL,NULL,NULL,NULL),('reporting.defaultLocale','en','Default locale to use when formatting report data','3110e861-9b32-40f8-b8cf-5554af6619ca',NULL,NULL,NULL,NULL),('reporting.evaluationLoggerEnabled','false','If false, will disable the built in use of the evaluation logger to log evaluation information for performance diagnostics','8d0d5390-0e2c-41f7-b4fb-306da6415711',NULL,NULL,NULL,NULL),('reporting.includeDataExportsAsDataSetDefinitions','false','If reportingcompatibility is installed, this indicates whether data exports should be exposed as Dataset Definitions','cef686cd-8880-4f14-a405-057acbbbd73b',NULL,NULL,NULL,NULL),('reporting.mandatory','false','true/false whether or not the reporting module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','c1cf7e53-7ffa-46fa-ac31-8962bbca9370',NULL,NULL,NULL,NULL),('reporting.maxCachedReports','10','The maximum number of reports whose underlying data and output should be kept in the cache at any one time','6cfcb1a7-a90a-4368-901f-1128ebfa4453',NULL,NULL,NULL,NULL),('reporting.maxReportsToRun','1','The maximum number of reports that should be processed at any one time','e9ac1d51-bd60-48aa-afa4-f27ab32bd142',NULL,NULL,NULL,NULL),('reporting.preferredIdentifierTypes',NULL,'Pipe-separated list of patient identifier type names, which should be displayed on default patient datasets','27bd8377-8e8a-45e1-bee6-a2fa24e73a70',NULL,NULL,NULL,NULL),('reporting.runReportCohortFilterMode','showIfNull','Supports the values hide,showIfNull,show which determine whether the cohort selector should be available in the run report page','fa7e6086-4d3f-4393-b910-9acaf601d135',NULL,NULL,NULL,NULL),('reporting.started','true','DO NOT MODIFY. true/false whether or not the reporting module has been started.  This is used to make sure modules that were running  prior to a restart are started again','fc17837d-7878-481c-9699-3f771165b8ba',NULL,NULL,NULL,NULL),('reporting.testPatientsCohortDefinition',NULL,'Points to a cohort definition representing all test/fake patients that you want to exclude from all queries and reports. You may set this to the UUID of a saved cohort definition, or to \"library:keyInADefinitionLibrary\"','ac396182-9de4-47ca-8a95-a0b9dd66ccde',NULL,NULL,NULL,NULL),('reportingrest.mandatory','false','true/false whether or not the reportingrest module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','486883c7-f622-41c1-92c9-d43d21c6bdb7',NULL,NULL,NULL,NULL),('reportingrest.started','true','DO NOT MODIFY. true/false whether or not the reportingrest module has been started.  This is used to make sure modules that were running  prior to a restart are started again','d033eadc-5eb6-45bc-9802-cdb96c17f989',NULL,NULL,NULL,NULL),('reportProblem.url','http://errors.openmrs.org/scrap','The openmrs url where to submit bug reports','86350659-851d-4b8c-8c22-2cab60a60a45',NULL,NULL,NULL,NULL),('scheduler.password','test','Password for the OpenMRS user that will perform the scheduler activities','56872322-1d16-4182-a670-732165a9a9dc',NULL,NULL,NULL,NULL),('scheduler.username','admin','Username for the OpenMRS user that will perform the scheduler activities','59bfb2f5-c365-4c44-80f3-d032b07dcb5a',NULL,NULL,NULL,NULL),('search.caseSensitiveDatabaseStringComparison','true','Indicates whether database string comparison is case sensitive or not. Setting this to false for MySQL with a case insensitive collation improves search performance.','a8208853-e563-485f-9c3e-685b676c77fb',NULL,NULL,NULL,NULL),('search.indexVersion','3','Indicates the index version. If it is blank, the index needs to be rebuilt.','79d7d8b7-cca7-40d2-920f-db7608633c99',NULL,NULL,NULL,NULL),('searchWidget.batchSize','200','The maximum number of search results that are returned by an ajax call','28dfc696-a137-4372-b81c-4b194d374c32',NULL,NULL,NULL,NULL),('searchWidget.dateDisplayFormat',NULL,'Date display format to be used to display the date somewhere in the UI i.e the search widgets and autocompletes','ceae5dc6-2f32-4b69-a354-c8516317a86c',NULL,NULL,NULL,NULL),('searchWidget.maximumResults','2000','Specifies the maximum number of results to return from a single search in the search widgets','24e9e450-cc55-46ca-bba3-804c08ef9901',NULL,NULL,NULL,NULL),('searchWidget.runInSerialMode','false','Specifies whether the search widgets should make ajax requests in serial or parallel order, a value of true is appropriate for implementations running on a slow network connection and vice versa','4d75eea4-9067-44a3-99f0-63e3a8bec441','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('searchWidget.searchDelayInterval','300','Specifies time interval in milliseconds when searching, between keyboard keyup event and triggering the search off, should be higher if most users are slow when typing so as to minimise the load on the server','1e4c3c5c-1a81-4cba-9445-f0fbc038cef3',NULL,NULL,NULL,NULL),('security.allowedFailedLoginsBeforeLockout','7','Maximum number of failed logins allowed after which username is locked out','f2705105-1e84-43c3-accd-e741c7d3d5af',NULL,NULL,NULL,NULL),('security.passwordCannotMatchUsername','true','Configure whether passwords must not match user\'s username or system id','ff312625-91c1-462a-962e-33fd1f93b419','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('security.passwordCustomRegex',NULL,'Configure a custom regular expression that a password must match','a165229d-9bf6-4d8b-a8f9-9c87a413a876',NULL,NULL,NULL,NULL),('security.passwordMinimumLength','8','Configure the minimum length required of all passwords','aaff07d9-741a-4aed-9593-261956ae800a',NULL,NULL,NULL,NULL),('security.passwordRequiresDigit','true','Configure whether passwords must contain at least one digit','a30944a9-268c-494f-b1a9-58603d771268','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('security.passwordRequiresNonDigit','true','Configure whether passwords must contain at least one non-digit','ac4dff95-9e43-4df1-96ab-7f1e73bd7034','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('security.passwordRequiresUpperAndLowerCase','true','Configure whether passwords must contain both upper and lower case characters','68006d47-84f6-437d-abd6-2b69d7f2d061','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('serialization.xstream.mandatory','false','true/false whether or not the serialization.xstream module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','c82ef288-89e9-43d5-a3cd-b5719d82fa1f',NULL,NULL,NULL,NULL),('serialization.xstream.started','true','DO NOT MODIFY. true/false whether or not the serialization.xstream module has been started.  This is used to make sure modules that were running  prior to a restart are started again','6b198b9b-cb9a-4720-9688-ce109a03d45c',NULL,NULL,NULL,NULL),('uicommons.mandatory','false','true/false whether or not the uicommons module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','2df4d82c-bd61-43a9-abab-bc14f4c5e699',NULL,NULL,NULL,NULL),('uicommons.started','true','DO NOT MODIFY. true/false whether or not the uicommons module has been started.  This is used to make sure modules that were running  prior to a restart are started again','d5461bdf-d65b-4078-9eb1-88e68d240c13',NULL,NULL,NULL,NULL),('uiframework.formatter.dateAndTimeFormat','dd.MMM.yyyy, HH:mm:ss','Format used by UiUtils.format for dates that have a time component','b83a5465-d95f-499d-8eb6-eae2c4e22da3',NULL,NULL,NULL,NULL),('uiframework.formatter.dateFormat','dd.MMM.yyyy','Format used by UiUtils.format for dates that do not have a time component','c2325983-290e-40fd-a06c-0bc17b36010e',NULL,NULL,NULL,NULL),('uiframework.mandatory','false','true/false whether or not the uiframework module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','585173cf-9b86-4be0-96f3-7650dde67196',NULL,NULL,NULL,NULL),('uiframework.started','true','DO NOT MODIFY. true/false whether or not the uiframework module has been started.  This is used to make sure modules that were running  prior to a restart are started again','7ee73301-3aef-400c-a1d2-af81d6c353d5',NULL,NULL,NULL,NULL),('uilibrary.mandatory','false','true/false whether or not the uilibrary module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','c8f7ee07-6aa7-4e14-8ec0-b44f22a04529',NULL,NULL,NULL,NULL),('uilibrary.started','true','DO NOT MODIFY. true/false whether or not the uilibrary module has been started.  This is used to make sure modules that were running  prior to a restart are started again','a8b257ad-2558-4000-aa17-31499e60462f',NULL,NULL,NULL,NULL),('user.headerAttributeTypes',NULL,'A comma delimited list of PersonAttributeType names that will be shown on the user dashboard. (not used in v1.5)','686640e8-66fd-4578-a6a4-b3253513cc79',NULL,NULL,NULL,NULL),('user.listingAttributeTypes',NULL,'A comma delimited list of PersonAttributeType names that should be displayed for users in _lists_','148cb1d6-8ec0-4e34-8df8-ed0e9898a7c7',NULL,NULL,NULL,NULL),('user.requireEmailAsUsername','false','Indicates whether a username must be a valid e-mail or not.','bf3df9a8-a6e5-497e-9de5-bee1c11dab57','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('user.viewingAttributeTypes',NULL,'A comma delimited list of PersonAttributeType names that should be displayed for users when _viewing individually_','4f9e4f31-01d0-4ba0-98b9-267ccdea99fd',NULL,NULL,NULL,NULL),('use_patient_attribute.healthCenter','false','Indicates whether or not the \'health center\' attribute is shown when viewing/searching for patients','ed3c733d-36da-4a9e-b79e-d0f36fe91c16','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('use_patient_attribute.mothersName','false','Indicates whether or not mother\'s name is able to be added/viewed for a patient','b218c72b-f94b-4ed8-b82b-913a57622382','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('visits.allowOverlappingVisits','true','true/false whether or not to allow visits of a given patient to overlap','d5a3363d-be95-441d-bce6-196d7a03de37','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('visits.assignmentHandler','org.openmrs.api.handler.ExistingVisitAssignmentHandler','Set to the name of the class responsible for assigning encounters to visits.','d4603187-0af2-4d14-811f-09238e431f32',NULL,NULL,NULL,NULL),('visits.autoCloseVisitType',NULL,'comma-separated list of the visit type(s) to automatically close','ce04b3f4-a38d-413c-98d7-67bceebc1d75',NULL,NULL,NULL,NULL),('visits.enabled','true','Set to true to enable the Visits feature. This will replace the \'Encounters\' tab with a \'Visits\' tab on the dashboard.','f2c413fb-2594-40cd-b128-bf069f882245','org.openmrs.customdatatype.datatype.BooleanDatatype',NULL,NULL,NULL),('visits.encounterTypeToVisitTypeMapping',NULL,'Specifies how encounter types are mapped to visit types when automatically assigning encounters to visits. e.g 1:1, 2:1, 3:2 in the format encounterTypeId:visitTypeId or encounterTypeUuid:visitTypeUuid or a combination of encounter/visit type uuids and ids e.g 1:759799ab-c9a5-435e-b671-77773ada74e4','fb691b2e-d250-4168-a957-4e774517de13',NULL,NULL,NULL,NULL),('webservices.rest.allowedips',NULL,'A comma-separate list of IP addresses that are allowed to access the web services. An empty string allows everyone to access all ws. \n        IPs can be declared with bit masks e.g. 10.0.0.0/30 matches 10.0.0.0 - 10.0.0.3 and 10.0.0.0/24 matches 10.0.0.0 - 10.0.0.255.','55a1c2f0-623e-4a99-abc6-45044a492739',NULL,NULL,NULL,NULL),('webservices.rest.mandatory','false','true/false whether or not the webservices.rest module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','e9d73632-9d68-4ecb-8ebb-a4cb0d4742a5',NULL,NULL,NULL,NULL),('webservices.rest.maxResultsAbsolute','100','The absolute max results limit. If the client requests a larger number of results, then will get an error','484d83f7-c38b-4586-b771-f5771c7b775e',NULL,NULL,NULL,NULL),('webservices.rest.maxResultsDefault','50','The default max results limit if the user does not provide a maximum when making the web service call.','76f838a4-7904-4d1f-adf7-7a2fbcec55cb',NULL,NULL,NULL,NULL),('webservices.rest.quietDocs','true','If the value of this setting is \"true\", then nothing is logged while the Swagger specification is being generated.','75ed5970-8914-4165-9938-8437753fad67',NULL,NULL,NULL,NULL),('webservices.rest.started','true','DO NOT MODIFY. true/false whether or not the webservices.rest module has been started.  This is used to make sure modules that were running  prior to a restart are started again','207da62c-baf9-42e9-ae69-c8198af5cf95',NULL,NULL,NULL,NULL),('webservices.rest.uriPrefix',NULL,'The URI prefix through which clients consuming web services will connect to the web application, should be of the form http://{ipAddress}:{port}/{contextPath}','0fb6f713-8d5e-4182-b214-5a88d025ae1f',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `global_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hl7_in_archive`
--

DROP TABLE IF EXISTS `hl7_in_archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hl7_in_archive` (
  `hl7_in_archive_id` int(11) NOT NULL AUTO_INCREMENT,
  `hl7_source` int(11) NOT NULL DEFAULT '0',
  `hl7_source_key` varchar(255) DEFAULT NULL,
  `hl7_data` text NOT NULL,
  `date_created` datetime NOT NULL,
  `message_state` int(11) DEFAULT '2',
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`hl7_in_archive_id`),
  UNIQUE KEY `hl7_in_archive_uuid_index` (`uuid`),
  KEY `hl7_in_archive_message_state_idx` (`message_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hl7_in_archive`
--

LOCK TABLES `hl7_in_archive` WRITE;
/*!40000 ALTER TABLE `hl7_in_archive` DISABLE KEYS */;
/*!40000 ALTER TABLE `hl7_in_archive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hl7_in_error`
--

DROP TABLE IF EXISTS `hl7_in_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hl7_in_error` (
  `hl7_in_error_id` int(11) NOT NULL AUTO_INCREMENT,
  `hl7_source` int(11) NOT NULL DEFAULT '0',
  `hl7_source_key` text,
  `hl7_data` text NOT NULL,
  `error` varchar(255) NOT NULL DEFAULT '',
  `error_details` mediumtext,
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`hl7_in_error_id`),
  UNIQUE KEY `hl7_in_error_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hl7_in_error`
--

LOCK TABLES `hl7_in_error` WRITE;
/*!40000 ALTER TABLE `hl7_in_error` DISABLE KEYS */;
/*!40000 ALTER TABLE `hl7_in_error` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hl7_in_queue`
--

DROP TABLE IF EXISTS `hl7_in_queue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hl7_in_queue` (
  `hl7_in_queue_id` int(11) NOT NULL AUTO_INCREMENT,
  `hl7_source` int(11) NOT NULL DEFAULT '0',
  `hl7_source_key` text,
  `hl7_data` text NOT NULL,
  `message_state` int(11) NOT NULL DEFAULT '0',
  `date_processed` datetime DEFAULT NULL,
  `error_msg` text,
  `date_created` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`hl7_in_queue_id`),
  UNIQUE KEY `hl7_in_queue_uuid_index` (`uuid`),
  KEY `hl7_source_with_queue` (`hl7_source`),
  CONSTRAINT `hl7_source_with_queue` FOREIGN KEY (`hl7_source`) REFERENCES `hl7_source` (`hl7_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hl7_in_queue`
--

LOCK TABLES `hl7_in_queue` WRITE;
/*!40000 ALTER TABLE `hl7_in_queue` DISABLE KEYS */;
/*!40000 ALTER TABLE `hl7_in_queue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hl7_source`
--

DROP TABLE IF EXISTS `hl7_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hl7_source` (
  `hl7_source_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` text,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`hl7_source_id`),
  UNIQUE KEY `hl7_source_uuid_index` (`uuid`),
  KEY `user_who_created_hl7_source` (`creator`),
  CONSTRAINT `user_who_created_hl7_source` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hl7_source`
--

LOCK TABLES `hl7_source` WRITE;
/*!40000 ALTER TABLE `hl7_source` DISABLE KEYS */;
INSERT INTO `hl7_source` VALUES (1,'LOCAL','',1,'2006-09-01 00:00:00','8d6b8bb6-c2cc-11de-8d13-0010c6dffd0f');
/*!40000 ALTER TABLE `hl7_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `htmlformentry_html_form`
--

DROP TABLE IF EXISTS `htmlformentry_html_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `htmlformentry_html_form` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `form_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `xml_data` mediumtext NOT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL DEFAULT '0002-11-30 00:00:00',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `uuid` char(38) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `htmlformentry_html_form_uuid_index` (`uuid`),
  KEY `User who created htmlformentry_htmlform` (`creator`),
  KEY `Form with which this htmlform is related` (`form_id`),
  KEY `User who changed htmlformentry_htmlform` (`changed_by`),
  KEY `user_who_retired_html_form` (`retired_by`),
  CONSTRAINT `Form with which this htmlform is related` FOREIGN KEY (`form_id`) REFERENCES `form` (`form_id`),
  CONSTRAINT `User who changed htmlformentry_htmlform` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `User who created htmlformentry_htmlform` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_html_form` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `htmlformentry_html_form`
--

LOCK TABLES `htmlformentry_html_form` WRITE;
/*!40000 ALTER TABLE `htmlformentry_html_form` DISABLE KEYS */;
/*!40000 ALTER TABLE `htmlformentry_html_form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idgen_auto_generation_option`
--

DROP TABLE IF EXISTS `idgen_auto_generation_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idgen_auto_generation_option` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `identifier_type` int(11) NOT NULL,
  `source` int(11) NOT NULL,
  `manual_entry_enabled` tinyint(1) NOT NULL DEFAULT '1',
  `automatic_generation_enabled` tinyint(1) NOT NULL DEFAULT '1',
  `location` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `source for idgen_auto_generation_option` (`source`),
  KEY `location_for_auto_generation_option` (`location`),
  KEY `identifier_type for idgen_auto_generation_option` (`identifier_type`),
  CONSTRAINT `identifier_type for idgen_auto_generation_option` FOREIGN KEY (`identifier_type`) REFERENCES `patient_identifier_type` (`patient_identifier_type_id`),
  CONSTRAINT `location_for_auto_generation_option` FOREIGN KEY (`location`) REFERENCES `location` (`location_id`),
  CONSTRAINT `source for idgen_auto_generation_option` FOREIGN KEY (`source`) REFERENCES `idgen_identifier_source` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idgen_auto_generation_option`
--

LOCK TABLES `idgen_auto_generation_option` WRITE;
/*!40000 ALTER TABLE `idgen_auto_generation_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `idgen_auto_generation_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idgen_id_pool`
--

DROP TABLE IF EXISTS `idgen_id_pool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idgen_id_pool` (
  `id` int(11) NOT NULL,
  `source` int(11) DEFAULT NULL,
  `batch_size` int(11) DEFAULT NULL,
  `min_pool_size` int(11) DEFAULT NULL,
  `sequential` tinyint(1) NOT NULL DEFAULT '0',
  `refill_with_scheduled_task` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `source for idgen_id_pool` (`source`),
  CONSTRAINT `id for idgen_id_pool` FOREIGN KEY (`id`) REFERENCES `idgen_identifier_source` (`id`),
  CONSTRAINT `source for idgen_id_pool` FOREIGN KEY (`source`) REFERENCES `idgen_identifier_source` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idgen_id_pool`
--

LOCK TABLES `idgen_id_pool` WRITE;
/*!40000 ALTER TABLE `idgen_id_pool` DISABLE KEYS */;
/*!40000 ALTER TABLE `idgen_id_pool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idgen_identifier_source`
--

DROP TABLE IF EXISTS `idgen_identifier_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idgen_identifier_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `identifier_type` int(11) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id for idgen_identifier_source` (`id`),
  KEY `identifier_type for idgen_identifier_source` (`identifier_type`),
  KEY `creator for idgen_identifier_source` (`creator`),
  KEY `changed_by for idgen_identifier_source` (`changed_by`),
  KEY `retired_by for idgen_identifier_source` (`retired_by`),
  CONSTRAINT `changed_by for idgen_identifier_source` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `creator for idgen_identifier_source` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `identifier_type for idgen_identifier_source` FOREIGN KEY (`identifier_type`) REFERENCES `patient_identifier_type` (`patient_identifier_type_id`),
  CONSTRAINT `retired_by for idgen_identifier_source` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idgen_identifier_source`
--

LOCK TABLES `idgen_identifier_source` WRITE;
/*!40000 ALTER TABLE `idgen_identifier_source` DISABLE KEYS */;
/*!40000 ALTER TABLE `idgen_identifier_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idgen_log_entry`
--

DROP TABLE IF EXISTS `idgen_log_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idgen_log_entry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source` int(11) NOT NULL,
  `identifier` varchar(50) NOT NULL,
  `date_generated` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `generated_by` int(11) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id for idgen_log` (`id`),
  KEY `source for idgen_log` (`source`),
  KEY `generated_by for idgen_log` (`generated_by`),
  CONSTRAINT `generated_by for idgen_log` FOREIGN KEY (`generated_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `source for idgen_log` FOREIGN KEY (`source`) REFERENCES `idgen_identifier_source` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idgen_log_entry`
--

LOCK TABLES `idgen_log_entry` WRITE;
/*!40000 ALTER TABLE `idgen_log_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `idgen_log_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idgen_pooled_identifier`
--

DROP TABLE IF EXISTS `idgen_pooled_identifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idgen_pooled_identifier` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `pool_id` int(11) NOT NULL,
  `identifier` varchar(50) NOT NULL,
  `date_used` datetime DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pool_id for idgen_pooled_identifier` (`pool_id`),
  CONSTRAINT `pool_id for idgen_pooled_identifier` FOREIGN KEY (`pool_id`) REFERENCES `idgen_id_pool` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idgen_pooled_identifier`
--

LOCK TABLES `idgen_pooled_identifier` WRITE;
/*!40000 ALTER TABLE `idgen_pooled_identifier` DISABLE KEYS */;
/*!40000 ALTER TABLE `idgen_pooled_identifier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idgen_remote_source`
--

DROP TABLE IF EXISTS `idgen_remote_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idgen_remote_source` (
  `id` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `user` varchar(50) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `id for idgen_remote_source` FOREIGN KEY (`id`) REFERENCES `idgen_identifier_source` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idgen_remote_source`
--

LOCK TABLES `idgen_remote_source` WRITE;
/*!40000 ALTER TABLE `idgen_remote_source` DISABLE KEYS */;
/*!40000 ALTER TABLE `idgen_remote_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idgen_reserved_identifier`
--

DROP TABLE IF EXISTS `idgen_reserved_identifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idgen_reserved_identifier` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source` int(11) NOT NULL,
  `identifier` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id for idgen_reserved_identifier` (`id`),
  KEY `source for idgen_reserved_identifier` (`source`),
  CONSTRAINT `source for idgen_reserved_identifier` FOREIGN KEY (`source`) REFERENCES `idgen_identifier_source` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idgen_reserved_identifier`
--

LOCK TABLES `idgen_reserved_identifier` WRITE;
/*!40000 ALTER TABLE `idgen_reserved_identifier` DISABLE KEYS */;
/*!40000 ALTER TABLE `idgen_reserved_identifier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idgen_seq_id_gen`
--

DROP TABLE IF EXISTS `idgen_seq_id_gen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idgen_seq_id_gen` (
  `id` int(11) NOT NULL,
  `next_sequence_value` int(11) NOT NULL DEFAULT '-1',
  `base_character_set` varchar(255) NOT NULL,
  `first_identifier_base` varchar(50) NOT NULL,
  `prefix` varchar(20) DEFAULT NULL,
  `suffix` varchar(20) DEFAULT NULL,
  `min_length` int(11) DEFAULT NULL,
  `max_length` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `id for idgen_seq_id_gen` FOREIGN KEY (`id`) REFERENCES `idgen_identifier_source` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idgen_seq_id_gen`
--

LOCK TABLES `idgen_seq_id_gen` WRITE;
/*!40000 ALTER TABLE `idgen_seq_id_gen` DISABLE KEYS */;
/*!40000 ALTER TABLE `idgen_seq_id_gen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `liquibasechangelog`
--

DROP TABLE IF EXISTS `liquibasechangelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `liquibasechangelog` (
  `ID` varchar(63) NOT NULL,
  `AUTHOR` varchar(63) NOT NULL,
  `FILENAME` varchar(200) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`AUTHOR`,`FILENAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liquibasechangelog`
--

LOCK TABLES `liquibasechangelog` WRITE;
/*!40000 ALTER TABLE `liquibasechangelog` DISABLE KEYS */;
INSERT INTO `liquibasechangelog` VALUES ('0','bwolfe','liquibase-update-to-latest.xml','2011-09-20 00:00:00',10016,'MARK_RAN','3:ccc4741ff492cb385f44e714053920af',NULL,NULL,NULL,NULL),('02232009-1141','nribeka','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10061,'EXECUTED','3:b5921fb42deb90fe52e042838d0638a0','Modify Column','Modify the password column to fit the output of SHA-512 function',NULL,'2.0.5'),('1','Eli','liquibase.xml','2016-07-04 10:38:33',10683,'EXECUTED','3:ffa64ac1eaf591a3f0b3b252669f0b9a','Create Table','added synonym table',NULL,'2.0.5'),('1','upul','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10042,'MARK_RAN','3:7fbc03c45bb69cd497b096629d32c3f5','Add Column','Add the column to person_attribute type to connect each type to a privilege',NULL,'2.0.5'),('1-grant-new-dashboard-overview-tab-app-privileges','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10483,'EXECUTED','3:6af3c30685c99d96ad1cd577719b1600','Custom SQL','Granting the new patient overview tab application privileges',NULL,'2.0.5'),('1-increase-privilege-col-size-privilege','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10485,'EXECUTED','3:6ecff8787eca17532e310087cfd65a06','Drop Foreign Key Constraint (x2), Modify Column, Add Foreign Key Constraint (x2)','Increasing the size of the privilege column in the privilege table',NULL,'2.0.5'),('10-insert-new-app-privileges','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10481,'EXECUTED','3:6ca60c3d5202a79f2e43367215cb447b','Custom SQL','Inserting the new application privileges',NULL,'2.0.5'),('10000000-1000-appointment-appointment','yony258','liquibase.xml','2016-07-04 10:38:30',10669,'EXECUTED','3:3dd6540810cbe6effb18ef106756bcf9','Create Table, Add Foreign Key Constraint (x7)','Create the appointment table',NULL,'2.0.5'),('10000000-1000-appointment-block','yony258','liquibase.xml','2016-07-04 10:38:30',10666,'EXECUTED','3:fc3edaed1b54e11dbe639b41e2fc3b04','Create Table, Add Foreign Key Constraint (x5)','Create the appointment block table',NULL,'2.0.5'),('10000000-1000-appointment-block-type-map','dkayiwa','liquibase.xml','2016-07-04 10:38:30',10668,'EXECUTED','3:278bb50a18f3cfdbabb7274f78b64d04','Create Table, Add Foreign Key Constraint (x2)','Create the link table \"appointmentscheduling_block_type_map\" that links appointmentscheduling_appointment_block table and appointmentscheduling_appointment_type table',NULL,'2.0.5'),('10000000-1000-appointment-status','yony258','liquibase.xml','2016-07-04 10:38:30',10670,'EXECUTED','3:75e91c0cb474fc6dfc211ed88074ec3c','Create Table, Add Foreign Key Constraint','Create appointment status history table',NULL,'2.0.5'),('10000000-1000-appointment-time-slot','yony258','liquibase.xml','2016-07-04 10:38:30',10667,'EXECUTED','3:444794d6c7637eda96851852e5872898','Create Table, Add Foreign Key Constraint (x4)','Create the time slot table',NULL,'2.0.5'),('10000000-1000-appointment-type','dkayiwa','liquibase.xml','2016-07-04 10:38:30',10665,'EXECUTED','3:58a9440e09242029258de5a5e0030d57','Create Table, Add Foreign Key Constraint (x3)','Create appointment type table',NULL,'2.0.5'),('11-insert-new-api-privileges','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10482,'EXECUTED','3:fe15eb2a97dd397b15fb5c4174fabe05','Custom SQL','Inserting the new API privileges',NULL,'2.0.5'),('1226348923233-12','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10022,'EXECUTED','3:7efb7ed5267126e1e44c9f344e35dd7d','Insert Row (x12)','',NULL,'2.0.5'),('1226348923233-13','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10023,'EXECUTED','3:8b9e14aa00a4382aa2623b39400c9110','Insert Row (x2)','',NULL,'2.0.5'),('1226348923233-14','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10027,'EXECUTED','3:8910082a3b369438f86025e4006b7538','Insert Row (x4)','',NULL,'2.0.5'),('1226348923233-15','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10028,'EXECUTED','3:8485e0ebef4dc368ab6b87de939f8e82','Insert Row (x15)','',NULL,'2.0.5'),('1226348923233-16','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10019,'EXECUTED','3:5778f109b607f882cc274750590d5004','Insert Row','',NULL,'2.0.5'),('1226348923233-17','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10030,'EXECUTED','3:3c324233bf1f386dcc4a9be55401c260','Insert Row (x2)','',NULL,'2.0.5'),('1226348923233-18','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10031,'EXECUTED','3:40ad1a506929811955f4d7d4753d576e','Insert Row (x2)','',NULL,'2.0.5'),('1226348923233-2','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10020,'EXECUTED','3:35613fc962f41ed143c46e578fd64a70','Insert Row (x5)','',NULL,'2.0.5'),('1226348923233-20','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10032,'EXECUTED','3:0ce5c5b83b4754b44f4bcda8eb866f3a','Insert Row','',NULL,'2.0.5'),('1226348923233-21','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10033,'EXECUTED','3:51c90534135f429c1bcde82be0f6157d','Insert Row','',NULL,'2.0.5'),('1226348923233-22','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10018,'EXECUTED','3:2d4897a84ce4408d8fcec69767a5c563','Insert Row','',NULL,'2.0.5'),('1226348923233-23','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10034,'EXECUTED','3:19f78a07a33a5efc28b4712a07b02a29','Insert Row','',NULL,'2.0.5'),('1226348923233-6','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10026,'EXECUTED','3:a947f43a1881ac56186039709a4a0ac8','Insert Row (x13)','',NULL,'2.0.5'),('1226348923233-8','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10021,'EXECUTED','3:dceb0cc19be3545af8639db55785d66e','Insert Row (x7)','',NULL,'2.0.5'),('1226412230538-24','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10024,'EXECUTED','3:0b77e92c0d1482c1bef7ca1add6b233b','Insert Row (x2)','',NULL,'2.0.5'),('1226412230538-7','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10025,'EXECUTED','3:c189f41d824649ef72dc3cef74d3580b','Insert Row (x106)','',NULL,'2.0.5'),('1226412230538-9a','ben (generated)','liquibase-core-data.xml','2016-07-04 10:37:37',10035,'EXECUTED','3:73c2b426be208fb50f088ad4ee76c8d6','Insert Row (x4)','',NULL,'2.0.5'),('1227123456789-100','dkayiwa','liquibase-schema-only.xml','2016-07-04 10:37:33',178,'EXECUTED','3:24751e1218f5fff3d2abf8e281e557c5','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-1','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',1,'EXECUTED','3:a851046bb3eb5b0daccb6e69ef8a9a00','Create Table','',NULL,'2.0.5'),('1227303685425-10','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',8,'EXECUTED','3:3fb7e78555ddf8d8014ba336bb8b5402','Create Table','',NULL,'2.0.5'),('1227303685425-100','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',114,'EXECUTED','3:8d20fc37ce4266cba349eeef66951688','Create Index','',NULL,'2.0.5'),('1227303685425-101','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',115,'EXECUTED','3:cc9b2ad0c2ff9ad6fcfd2f56b52d795f','Create Index','',NULL,'2.0.5'),('1227303685425-102','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',116,'EXECUTED','3:97d1301e8ab7f35e109c733fdedde10f','Create Index','',NULL,'2.0.5'),('1227303685425-103','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',117,'EXECUTED','3:2447e4abc7501a18f401594e4c836fff','Create Index','',NULL,'2.0.5'),('1227303685425-104','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',118,'EXECUTED','3:8d6c644eaf9f696e3fee1362863c26ec','Create Index','',NULL,'2.0.5'),('1227303685425-105','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',119,'EXECUTED','3:fb3838f818387718d9b4cbf410d653cd','Create Index','',NULL,'2.0.5'),('1227303685425-106','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',120,'EXECUTED','3:a644de1082a85ab7a0fc520bb8fc23d7','Create Index','',NULL,'2.0.5'),('1227303685425-107','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',121,'EXECUTED','3:f11eb4e4bc4a5192b7e52622965aacb2','Create Index','',NULL,'2.0.5'),('1227303685425-108','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',135,'EXECUTED','3:07fc6fd2c0086f941aed0b2c95c89dc8','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-109','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',136,'EXECUTED','3:c2911be31587bbc868a55f13fcc3ba5e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-11','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',9,'EXECUTED','3:1cef06ece4f56bfbe205ec03b75a129f','Create Table','',NULL,'2.0.5'),('1227303685425-110','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',137,'EXECUTED','3:32c42fa39fe81932aa02974bb19567ed','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-111','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',138,'EXECUTED','3:f35c8159ca7f84ae551bdb988b833760','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-112','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',139,'EXECUTED','3:df0a45bc276e7484f183e3190cff8394','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-115','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',140,'EXECUTED','3:d3b13502ef9794718d68bd0697fd7c2b','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-116','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',141,'EXECUTED','3:6014d91cadbbfc05bd364619d94a4f18','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-117','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',142,'EXECUTED','3:0841471be0ebff9aba768017b9a9717b','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-118','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',143,'EXECUTED','3:c73351f905761c3cee7235b526eff1a0','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-119','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',144,'EXECUTED','3:cd72c79bfd3c807ba5451d8ca5cb2612','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-12','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',10,'EXECUTED','3:b9726f1c78d0cba40d5ee61e721350f7','Create Table','',NULL,'2.0.5'),('1227303685425-120','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',145,'EXECUTED','3:b07d718d9d2b64060584d4c460ffc277','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-121','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',146,'EXECUTED','3:be141d71df248fba87a322b35f13b4db','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-122','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',147,'EXECUTED','3:7bcc45dda3aeea4ab3916701483443d3','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-123','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',148,'EXECUTED','3:031e4dcf20174b92bbbb07323b86d569','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-124','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',149,'EXECUTED','3:7d277181a4e9d5e14f9cb1220c6c4c57','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-125','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',150,'EXECUTED','3:7172ef61a904cd7ae765f0205d9e66dd','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-126','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',151,'EXECUTED','3:0d9a3ffc816c3e4e8649df3de01a8ff6','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-128','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',152,'EXECUTED','3:3a9357d6283b2bb97c1423825d6d57eb','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-129','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',153,'EXECUTED','3:e3923913d6f34e0e8bc7333834884419','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-13','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',11,'EXECUTED','3:982544aff0ae869f5ac9691d5c93a7e4','Create Table','',NULL,'2.0.5'),('1227303685425-130','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',154,'EXECUTED','3:dae7a98f3643acfe9db5c3b4b9e8f4ea','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-131','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',155,'EXECUTED','3:44a4e4791a0f727fffc96b9dab0a3fa8','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-132','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',156,'EXECUTED','3:16dcc5a95708dbdaff07ed27507d8e29','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-134','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',157,'EXECUTED','3:c37757ed38ace0bb94d8455a49e3049a','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-135','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',158,'EXECUTED','3:ab40ab94ab2f86a0013ecbf9dd034de4','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-136','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',159,'EXECUTED','3:3878ab735b369d778a7feb2b92746352','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-137','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',160,'EXECUTED','3:a7bf99f775c2f07b534a4df4e5c5c20b','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-139','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',161,'EXECUTED','3:f0a1690648292d939876bdeefa74792a','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-14','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',12,'EXECUTED','3:8122a19068fb1fd7b2c4d54e398ba507','Create Table','',NULL,'2.0.5'),('1227303685425-140','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',162,'EXECUTED','3:7ba4860a1e0a00ff49a93d4e86929691','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-141','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',163,'EXECUTED','3:5b176976d808cf8b1b8fae7d2b19e059','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-142','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',164,'EXECUTED','3:5538db250e63d70a79dce2c5a74ee528','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-143','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',165,'EXECUTED','3:a981ac9be845bf6c6098aa98cd4d8456','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-144','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',166,'EXECUTED','3:84428d9dce773758f73616129935d888','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-145','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',167,'EXECUTED','3:6b8af6c242f1d598591478897feed2d8','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-146','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',168,'EXECUTED','3:a8cddf3b63050686248e82a3b6de781f','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-147','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',169,'EXECUTED','3:e8b5350ad40fa006c088f08fae4d3141','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-149','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',170,'EXECUTED','3:b6c44ee5824ae261a9a87b8ac60fe23d','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-15','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',13,'EXECUTED','3:18dd3e507ecbe5212bcdf0a0a8012d88','Create Table','',NULL,'2.0.5'),('1227303685425-150','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',171,'EXECUTED','3:f8c495d78d68c9fc701271a8e5d1f102','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-151','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',172,'EXECUTED','3:94dc5b8d27f275fb06cc230eb313e430','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-153','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',173,'EXECUTED','3:2a04930665fe64516765263d1a9b0775','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-154','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',174,'EXECUTED','3:adfef8b8dd7b774b268b0968b7400f42','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-155','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',175,'EXECUTED','3:540b0422c733b464a33ca937348d8b4c','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-158','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',176,'EXECUTED','3:d1d73e19bab5821f256c01a83e2d945f','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-159','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',177,'EXECUTED','3:c23d0cd0eec5f20385b4182af18fc835','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-16','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',14,'EXECUTED','3:4c19b5c980b58e54af005e1fa50359ae','Create Table','',NULL,'2.0.5'),('1227303685425-160','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',179,'EXECUTED','3:cc64f6e676cb6a448f73599d8149490c','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-161','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',180,'EXECUTED','3:f48e300a3439d90fa2d518b3d6e145a5','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-162','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',181,'EXECUTED','3:467e31995c41be55426a5256d99312c4','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-163','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',182,'EXECUTED','3:45a4519c252f7e42d649292b022ec158','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-164','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',183,'EXECUTED','3:dfd51e701b716c07841c2e4ea6f59f3e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-165','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',184,'EXECUTED','3:0d69b82ce833ea585e95a6887f800108','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-166','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',185,'EXECUTED','3:2070f44c444e1e6efdbe7dfb9f7b846d','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-167','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',186,'EXECUTED','3:9196c0f9792007c72233649cc7c2ac58','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-168','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',187,'EXECUTED','3:33d644a49e92a4bbd4cb653d6554c8d0','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-169','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',188,'EXECUTED','3:e75c37cbd9aa22cf95b2dc89fdb2c831','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-17','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',15,'EXECUTED','3:250e3f0d4bb139fa751d0875fd1d5b89','Create Table','',NULL,'2.0.5'),('1227303685425-170','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',189,'EXECUTED','3:ac31515d8822caa0c87705cb0706e52f','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-171','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',190,'EXECUTED','3:b40823e1322acea52497f43033e72e5e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-173','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',191,'EXECUTED','3:b37c0b43a23ad3b072e34055875f7dcc','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-174','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',192,'EXECUTED','3:f8c5737a51f0f040e9fac3060c246e46','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-175','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',193,'EXECUTED','3:87cc15f9622b014d01d4df512a3f835e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-176','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',194,'EXECUTED','3:f57273dfbaba02ea785a0e165994f74b','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-177','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',195,'EXECUTED','3:1555790e99827ded259d5ec7860eb1b1','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-178','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',196,'EXECUTED','3:757206752885a07d1eea5585ad9e2dce','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-179','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',197,'EXECUTED','3:018efd7d7a5e84f7c2c9cec7299d596e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-18','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',16,'EXECUTED','3:2eb2063d3e1233e7ebc23f313da5bff6','Create Table','',NULL,'2.0.5'),('1227303685425-180','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',198,'EXECUTED','3:9ba52b3b7059674e881b7611a3428bde','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-181','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',199,'EXECUTED','3:ffee79a7426d7e41cf65889c2a5064f2','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-182','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',200,'EXECUTED','3:380743d4f027534180d818f5c507fae9','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-183','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',201,'EXECUTED','3:6882a4cf798e257af34753a8b5e7a157','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-184','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',202,'EXECUTED','3:93473623db4b6e7ca7813658da5b6771','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-185','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',203,'EXECUTED','3:f19a46800a4695266f3372aa709650b2','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-186','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',204,'EXECUTED','3:adfd7433de8d3b196d1166f62e497f8d','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-187','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',205,'EXECUTED','3:88b09239d29fbddd8bf4640df9f3e235','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-188','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',206,'EXECUTED','3:777e5970e09a3a1608bf7c40ef1ea1db','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-189','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',207,'EXECUTED','3:40dab4e434aa06340ba046fbd1382c6d','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-19','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',17,'EXECUTED','3:b5acf27abca6fec3533e081a6c57c415','Create Table','',NULL,'2.0.5'),('1227303685425-190','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',208,'EXECUTED','3:8766098ff9779a913d5642862955eaff','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-191','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',209,'EXECUTED','3:2a52afd1df6dcf64ec21f3c6ffe1d022','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-192','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',210,'EXECUTED','3:fa5de48b1490faa157e1977529034169','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-193','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',211,'EXECUTED','3:1213cb90fa9bd1561a371cc53c262d0f','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-194','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',212,'EXECUTED','3:2eb07e7388ff8d68d36cf2f3552c1a7c','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-195','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',213,'EXECUTED','3:d6ec9bb7b3bab333dcea4a3c18083616','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-196','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',214,'EXECUTED','3:df03afd5ef34e472fd6d43ef74a859e1','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-197','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',215,'EXECUTED','3:a0811395501a4423ca66de08fcf53895','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-198','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',216,'EXECUTED','3:e46a78b95280d9082557ed991af8dbe7','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-199','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',217,'EXECUTED','3:e57afffae0d6a439927e45cde4393363','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-2','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',2,'EXECUTED','3:d90246bb4d8342608e818a872d3335f1','Create Table','',NULL,'2.0.5'),('1227303685425-20','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',18,'EXECUTED','3:e0a8a4978c536423320f1ff44520169a','Create Table','',NULL,'2.0.5'),('1227303685425-200','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',218,'EXECUTED','3:667c2308fcf366f47fab8d9df3a3b2ae','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-201','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',219,'EXECUTED','3:104750a2b7779fa43e8457071e0bc33e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-202','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',220,'EXECUTED','3:4b813b03362a54d89a28ed1b10bc9069','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-203','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',221,'EXECUTED','3:526893ceedd67d8a26747e314a15f501','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-204','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',222,'EXECUTED','3:d0dbb7cc972e73f6a429b273ef63132e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-205','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',223,'EXECUTED','3:44244a3065d9a5531a081d176aa4e93d','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-207','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',224,'EXECUTED','3:77ade071c48615dbb39cbf9f01610c0e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-208','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',225,'EXECUTED','3:04495bf48c23d0fe56133da87c4e9a66','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-209','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',226,'EXECUTED','3:ff99d75d98ce0428a57100aeb558a529','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-21','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',19,'EXECUTED','3:f2353036e6382f45f91af5d8024fb04c','Create Table','',NULL,'2.0.5'),('1227303685425-210','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',227,'EXECUTED','3:537b2e8f88277a6276bcdac5d1493e4e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-211','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',228,'EXECUTED','3:3da39190692480b0a610b5c66fd056b8','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-212','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',229,'EXECUTED','3:347f20b32a463b73f0a93de13731a3a3','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-213','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',230,'EXECUTED','3:b89ee7f6dd678737268566b7e7d0d5d3','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-214','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',231,'EXECUTED','3:5f4b3400ecb50d46e04a18b6b57821c8','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-215','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',232,'EXECUTED','3:3dfa6664ca6b77eee492af73908f7312','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-216','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',233,'EXECUTED','3:7f7dbdcdaf3914e33458c0d67bc326db','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-217','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',234,'EXECUTED','3:1f03c97bd9b3ee1c2726656c6a0db795','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-218','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',235,'EXECUTED','3:e0a67bb4f3ea4b44de76fd73ca02ddb3','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-219','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',236,'EXECUTED','3:9a347c93be3356b84358ada2264ed201','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-22','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',20,'EXECUTED','3:5ff0caa8c8497fd681f111bf2842baca','Create Table','',NULL,'2.0.5'),('1227303685425-220','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',237,'EXECUTED','3:8a744c0020e6c6ae519ed0a04d79f82d','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-221','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',238,'EXECUTED','3:cc3f5b38ea88221efe32bc99be062edf','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-222','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',239,'EXECUTED','3:a90ffe3cbe9ddd1704e702e71ba5a216','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-223','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:34',240,'EXECUTED','3:ee4b79223897197c46c79d6ed2e68538','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-224','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',241,'EXECUTED','3:56d7625e53d13008ae7a31d09ba7dab8','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-225','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',242,'EXECUTED','3:b94477e4e6ecf22bc973408d2d01a868','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-226','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',243,'EXECUTED','3:d7a0cde832f1f557f0f42710645c1b50','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-227','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',244,'EXECUTED','3:6873a8454254a783dbcbe608828c0bd0','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-228','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',245,'EXECUTED','3:6ffcdbbe70b8f8e096785a3c2fe83318','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-229','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',246,'EXECUTED','3:ef5d7095407e0df6a1fcaf7c3c55872b','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-23','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',21,'EXECUTED','3:c3aa4ad35ead35e805a99083a95a1c86','Create Table','',NULL,'2.0.5'),('1227303685425-230','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',247,'EXECUTED','3:306710c2acba2e689bf1121d577f449b','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-231','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',248,'EXECUTED','3:71f320edc9221ce73876d80077b7b94d','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-232','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',249,'EXECUTED','3:8dfba47fb6719dc743b231cc645a8378','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-234','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',250,'EXECUTED','3:9b12808a0fe62d6951bcd61f9cbff3f8','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-235','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',251,'EXECUTED','3:a3e9822b106a9bb42f5b9d28dc70335c','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-236','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',252,'EXECUTED','3:388be0f658f8bf6df800fe3efd4dadb3','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-237','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',253,'EXECUTED','3:eee4cb65598835838fd6deb8e4043693','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-239','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',254,'EXECUTED','3:3b38e45410dd1d02530d012a12b6b03c','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-24','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',22,'EXECUTED','3:f4f4e3a5fa3d93bb50d2004c6976cc12','Create Table','',NULL,'2.0.5'),('1227303685425-240','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',255,'EXECUTED','3:095316f05dac21b4a33a141e5781d99d','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-241','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',256,'EXECUTED','3:2cffae7a53d76f19e5194778cff75a4f','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-242','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',257,'EXECUTED','3:828732619d67fa932631e18827d74463','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-243','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',258,'EXECUTED','3:730166a1b0c3162e8ce882e0c8f308c5','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-244','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',259,'EXECUTED','3:77c03c05576961f7efebdfa10ae68119','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-245','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',260,'EXECUTED','3:d7d8dcaceb9793b0801c87eb2c94cd11','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-246','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',261,'EXECUTED','3:d395ea3ef18817dc23e750a1048cb4e1','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-247','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',262,'EXECUTED','3:4546bff7866082946f19e5d82ffc4d2e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-248','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',263,'EXECUTED','3:4e1071a7c1047f2d3b49778ce2f8bc40','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-249','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',264,'EXECUTED','3:fd6b7823929af9fded1f213d319eae13','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-25','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',23,'EXECUTED','3:2a720a7c4302c435c06c045abcce3b4d','Create Table','',NULL,'2.0.5'),('1227303685425-250','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',265,'EXECUTED','3:955129e5e6adf3723583c047eb33583d','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-251','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',266,'EXECUTED','3:7a12c926e69a3d1a7e31da2b8d7123e5','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-252','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',267,'EXECUTED','3:eb8c61b5b792346af3d3f8732278260b','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-253','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',268,'EXECUTED','3:761e6c7fb13a82c6ab671039e5dc5646','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-254','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',269,'EXECUTED','3:9a2401574c95120e1f90d18fde428d10','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-255','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',270,'EXECUTED','3:cda3d0c5c91b85d9f4610554eabb331e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-256','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',271,'EXECUTED','3:ebf6243c66261bf0168e72ceccd0fdb8','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-257','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',272,'EXECUTED','3:8dc087b963a10a52a22312c3c995cec2','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-258','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',273,'EXECUTED','3:489d5e366070f6b2424b8e5a20d0118f','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-259','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',274,'EXECUTED','3:d8d2a1cfddf07123a8e6f52b1e71705d','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-26','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',24,'EXECUTED','3:7f6992f21c5541316bce526edb78a949','Create Table','',NULL,'2.0.5'),('1227303685425-260','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',275,'EXECUTED','3:741f0c9e309b8515b713decb56ed6cb2','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-261','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',276,'EXECUTED','3:7034f7db7864956b7ca13ceb70cc8a92','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-262','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',277,'EXECUTED','3:e3a5995253a29723231b0912b971fb5a','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-263','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',278,'EXECUTED','3:b1d3718c15765d4a3bf89cb61376d3af','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-264','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',279,'EXECUTED','3:973683323e2ce886f07ef53a6836ad1e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-265','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',280,'EXECUTED','3:eff44c0cd530b852864042134ebccb47','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-266','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',281,'EXECUTED','3:2493248589e21293811c01cdb6c2fb87','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-267','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',282,'EXECUTED','3:ee636bdbc5839d7de0914648e1f07431','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-268','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',283,'EXECUTED','3:96710f10538b24f39e74ebc13eb6a3fc','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-269','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',284,'EXECUTED','3:5be60bacdaf2ab2d8a3103e36b32f6b9','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-27','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',25,'EXECUTED','3:6d359a7595e5b9a61ac1101aa7df59e7','Create Table','',NULL,'2.0.5'),('1227303685425-270','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',285,'EXECUTED','3:35a1f2d06c31af2f02df3e7aea4d05a5','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-271','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',286,'EXECUTED','3:64d94dfba329b70a842a09b01b952850','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-272','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',287,'EXECUTED','3:0e3787e31b95815106e7e051b9c4a79a','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-273','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',288,'EXECUTED','3:00949a7bd184ed4ee994eabf4b98a41f','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-274','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',289,'EXECUTED','3:b63e3786d661815d2b7c63b277796fc9','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-275','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',290,'EXECUTED','3:7e0ac267990b953ff9efe8fece53b4dd','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-276','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',291,'EXECUTED','3:d66f7691a19406c215b3b4b4c5330775','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-277','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',292,'EXECUTED','3:9b7c8b6ab0b9f8ffde5e7853efa40db5','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-278','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',293,'EXECUTED','3:e5f95724ac551e5905c604e59af444f9','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-279','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',294,'EXECUTED','3:6b4a4c9072a92897562aa595c27aaae4','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-28','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',26,'EXECUTED','3:7264098c8ccfa42b12ffb13056f77383','Create Table','',NULL,'2.0.5'),('1227303685425-280','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',295,'EXECUTED','3:8fb315815532eb73c13fac2dac763f69','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-281','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',296,'EXECUTED','3:1bed6131408c745505800d96130d3b30','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-282','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',297,'EXECUTED','3:ecf4138f4fd2d1e8be720381ac401623','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-283','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',298,'EXECUTED','3:00b414c29dcc3be9683f28ff3f2d9b20','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-284','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',299,'EXECUTED','3:b71dcc206a323ffa6ac4cd658de7b435','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-285','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',300,'EXECUTED','3:17423acbed3db4325d48d91e9f0e7147','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-286','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',301,'EXECUTED','3:2d8576bdbc9dd67137ba462b563022d8','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-287','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',302,'EXECUTED','3:6798c27ddf8ca72952030d6005422c1e','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-288','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',303,'EXECUTED','3:b28ec2579454fa7a13fd3896420ad1ff','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-289','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',304,'EXECUTED','3:cbfff99e22305c5570cdc8fdb33f3542','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-29','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',27,'EXECUTED','3:750f7e9a5f0898408626a11ce7f34ee4','Create Table','',NULL,'2.0.5'),('1227303685425-290','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',305,'EXECUTED','3:66957ec2b3211869a1ad777de33e7983','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-291','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',306,'EXECUTED','3:18b7da760f632dc6baf910fe5001212d','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-292','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',307,'EXECUTED','3:a1a914015e07b1637a9c655a9be3cfcd','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-293','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',308,'EXECUTED','3:5fedacb04729210c4a27bbfa2a3704f1','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-294','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',309,'EXECUTED','3:cf53101d520adb79fd1827819bcf0401','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-295','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',310,'EXECUTED','3:22b93c390cd6054f3dc8b62814d143cf','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-296','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',311,'EXECUTED','3:8b71fc2ae6be26a1ddc499cfc6e2cdba','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-297','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',312,'EXECUTED','3:d10fb06a37b1433a248b549ebae31e63','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-298','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',313,'EXECUTED','3:a3008458deed3c8c95f475395df6d788','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-299','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',314,'EXECUTED','3:b380ee7cce1b82a2f983d242b45c63b3','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-30','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',28,'EXECUTED','3:0951dbc13f8f4c6fd4ccc8a7ddb9d77c','Create Table','',NULL,'2.0.5'),('1227303685425-300','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',315,'EXECUTED','3:01f02c28d4f52e712aad87873aaa40f8','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-301','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:35',316,'EXECUTED','3:adf03ccc09e8f37f827b8ffbf3afff83','Add Foreign Key Constraint','',NULL,'2.0.5'),('1227303685425-31','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',29,'EXECUTED','3:5669760a2908489a63a69c3d34dd5c54','Create Table','',NULL,'2.0.5'),('1227303685425-32','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',30,'EXECUTED','3:c8b2b1bb1eb7b3885c89f436210cc2d5','Create Table','',NULL,'2.0.5'),('1227303685425-33','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',31,'EXECUTED','3:7b55b4b34bca59cf0d0b7271a2906568','Create Table','',NULL,'2.0.5'),('1227303685425-34','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',32,'EXECUTED','3:05a6b514927868471d71b334502d0e85','Create Table','',NULL,'2.0.5'),('1227303685425-35','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',33,'EXECUTED','3:528a3f364b7acce00fcc4d49153a5626','Create Table','',NULL,'2.0.5'),('1227303685425-36','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',34,'EXECUTED','3:2f8eb6548a5d935d7df6f68b329b7685','Create Table','',NULL,'2.0.5'),('1227303685425-37','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',35,'EXECUTED','3:fb11397b44997fe8fef33f8ae86d4044','Create Table','',NULL,'2.0.5'),('1227303685425-39','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',36,'EXECUTED','3:3efa037ecd0227f935b601f0bda28692','Create Table','',NULL,'2.0.5'),('1227303685425-4','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',3,'EXECUTED','3:27c254248b84b163e54161c6f14c2104','Create Table','',NULL,'2.0.5'),('1227303685425-40','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',37,'EXECUTED','3:934d8b580b8b3572b3795a92496783a2','Create Table','',NULL,'2.0.5'),('1227303685425-41','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',38,'EXECUTED','3:a65a25558c348c19863a0088ae031ad7','Create Table','',NULL,'2.0.5'),('1227303685425-42','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',39,'EXECUTED','3:1264d39b6cb1fa81263df8f7a0819a5e','Create Table','',NULL,'2.0.5'),('1227303685425-43','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',40,'EXECUTED','3:bbdbc4ae1631b83db687f4a92453ba3e','Create Table','',NULL,'2.0.5'),('1227303685425-44','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',41,'EXECUTED','3:98d047deb448c37f252eca7c035bf158','Create Table','',NULL,'2.0.5'),('1227303685425-45','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',42,'EXECUTED','3:a8a1d8af033a6e76c1e2060727bf4ebe','Create Table','',NULL,'2.0.5'),('1227303685425-46','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',43,'EXECUTED','3:b18b944837dd60d5d82996b8b1b57833','Create Table','',NULL,'2.0.5'),('1227303685425-47','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',44,'EXECUTED','3:7ae5af2d7f0b3e0bfaf9f243f56851eb','Create Table','',NULL,'2.0.5'),('1227303685425-48','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',45,'EXECUTED','3:fdbf71c7035399d628225af885c63114','Create Table','',NULL,'2.0.5'),('1227303685425-49','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',46,'EXECUTED','3:0c00abd4429f2072f360915eb2eec3de','Create Table','',NULL,'2.0.5'),('1227303685425-5','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',4,'EXECUTED','3:63b45eb5e407f1aa4e6569392ca957ca','Create Table','',NULL,'2.0.5'),('1227303685425-50','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',47,'EXECUTED','3:0e2c0cecd82166846a869f8ecce32bc2','Create Table','',NULL,'2.0.5'),('1227303685425-51','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',48,'EXECUTED','3:308c259886e442254ca616d365db78a2','Create Table','',NULL,'2.0.5'),('1227303685425-52','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',49,'EXECUTED','3:c11b85565b591de38d518fe60411507d','Create Table','',NULL,'2.0.5'),('1227303685425-53','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',50,'EXECUTED','3:2860fbf54a18e959646882325eb4dd87','Create Table','',NULL,'2.0.5'),('1227303685425-54','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',51,'EXECUTED','3:f09d17e1a43189fc59b1c5d7c6c7d692','Create Table','',NULL,'2.0.5'),('1227303685425-55','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',52,'EXECUTED','3:2318a41398b0583575e358aa79813cc6','Create Table','',NULL,'2.0.5'),('1227303685425-56','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',53,'EXECUTED','3:9d78f818f482903055f79b2fb7081e0f','Create Table','',NULL,'2.0.5'),('1227303685425-57','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',54,'EXECUTED','3:401395592f65e4216b0b0e8a756ae9b8','Create Table','',NULL,'2.0.5'),('1227303685425-58','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',55,'EXECUTED','3:fcb459a88d234d6556ab5f5aff73a926','Create Table','',NULL,'2.0.5'),('1227303685425-59','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',56,'EXECUTED','3:ab774554467ea7818601acb495c57e5a','Create Table','',NULL,'2.0.5'),('1227303685425-6','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',5,'EXECUTED','3:570a29d5b7f6b945d80de91fce59c0f6','Create Table','',NULL,'2.0.5'),('1227303685425-60','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',57,'EXECUTED','3:54282635bb2bf4218be532ed940ac4b0','Create Table','',NULL,'2.0.5'),('1227303685425-61','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',58,'EXECUTED','3:920daa80f4f9db2cacee8dca6ca4f971','Create Table','',NULL,'2.0.5'),('1227303685425-62','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',59,'EXECUTED','3:55daf6d077eac0ef7e30e6395bc4bc68','Create Table','',NULL,'2.0.5'),('1227303685425-63','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',60,'EXECUTED','3:cdc470c39dadd7cb1a1527a82ff737d3','Create Table','',NULL,'2.0.5'),('1227303685425-64','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',61,'EXECUTED','3:e3eb66044ea03e417837e9c1668f28e3','Create Table','',NULL,'2.0.5'),('1227303685425-65','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',62,'EXECUTED','3:5b25f91c273cc6b58b25385d24bc4f12','Create Table','',NULL,'2.0.5'),('1227303685425-66','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',63,'EXECUTED','3:93e2d359d5f6c38b95dfd47dce687c9c','Create Table','',NULL,'2.0.5'),('1227303685425-67','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',64,'EXECUTED','3:7c991af945580bbcb08e8d288c327525','Create Table','',NULL,'2.0.5'),('1227303685425-68','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',65,'EXECUTED','3:40096bd3e62db8377ce4f0a1fcea444e','Create Table','',NULL,'2.0.5'),('1227303685425-7','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',6,'EXECUTED','3:13a412a8d9125f657594bc96f742dd1b','Create Table','',NULL,'2.0.5'),('1227303685425-70','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',66,'EXECUTED','3:0df5ce250df07062c43119d18fc2a85b','Create Table','',NULL,'2.0.5'),('1227303685425-71','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',67,'EXECUTED','3:06e7ba94af07838a3d2ebb98816412a3','Create Table','',NULL,'2.0.5'),('1227303685425-72','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',68,'EXECUTED','3:01610d6974df470c653bc34953a31335','Create Table','',NULL,'2.0.5'),('1227303685425-73','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',89,'EXECUTED','3:33d08000805c4b9d7db06556961553b1','Add Primary Key','',NULL,'2.0.5'),('1227303685425-75','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',90,'EXECUTED','3:f2b0a95b4015b54d38c721906abc1fdb','Add Primary Key','',NULL,'2.0.5'),('1227303685425-77','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',91,'EXECUTED','3:bdde9c0d7374a3468a94426199b0d930','Add Primary Key','',NULL,'2.0.5'),('1227303685425-78','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',92,'EXECUTED','3:6fb4014a9a3ecc6ed09a896936b8342d','Add Primary Key','',NULL,'2.0.5'),('1227303685425-79','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',93,'EXECUTED','3:77e1d7c49e104435d10d90cc70e006e3','Add Primary Key','',NULL,'2.0.5'),('1227303685425-81','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',94,'EXECUTED','3:a5871abe4cdc3d8d9390a9b4ab0d0776','Add Primary Key','',NULL,'2.0.5'),('1227303685425-82','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',95,'EXECUTED','3:2f7eab1e485fd5a653af8799a84383b4','Add Primary Key','',NULL,'2.0.5'),('1227303685425-83','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',96,'EXECUTED','3:60ca763d5ac940b3bc189e2f28270bd8','Add Primary Key','',NULL,'2.0.5'),('1227303685425-84','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',97,'EXECUTED','3:901f48ab4c9e3a702fc0b38c5e724a5e','Add Primary Key','',NULL,'2.0.5'),('1227303685425-85','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',99,'EXECUTED','3:5544801862c8f21461acf9a22283ccab','Create Index','',NULL,'2.0.5'),('1227303685425-86','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',100,'EXECUTED','3:70591fc2cd8ce2e7bda36b407bbcaa86','Create Index','',NULL,'2.0.5'),('1227303685425-87','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',101,'EXECUTED','3:35c206a147d28660ffee5f87208f1f6b','Create Index','',NULL,'2.0.5'),('1227303685425-88','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',102,'EXECUTED','3:d399797580e14e7d67c1c40637314476','Create Index','',NULL,'2.0.5'),('1227303685425-89','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',103,'EXECUTED','3:138fa4373fe05e63fe5f923cf3c17e69','Create Index','',NULL,'2.0.5'),('1227303685425-9','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:32',7,'EXECUTED','3:ac6886dbc0c811bda6909908fb2d30a2','Create Table','',NULL,'2.0.5'),('1227303685425-90','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',104,'EXECUTED','3:4b60e13b8e209c2b5b1f981f4c28fc1b','Create Index','',NULL,'2.0.5'),('1227303685425-91','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',105,'EXECUTED','3:f9c13df6f50d1e7c1fad36faa020d7a6','Create Index','',NULL,'2.0.5'),('1227303685425-92','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',106,'EXECUTED','3:c24d9e0d28b3a208dbe2fc1cfaf23720','Create Index','',NULL,'2.0.5'),('1227303685425-93','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',107,'EXECUTED','3:ae9beae273f9502bc01580754e0f2bdf','Create Index','',NULL,'2.0.5'),('1227303685425-94','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',108,'EXECUTED','3:39d98e23d1480b677bc8f2341711756b','Create Index','',NULL,'2.0.5'),('1227303685425-95','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',109,'EXECUTED','3:16ece63cd24c4c5048356cc2854235e1','Create Index','',NULL,'2.0.5'),('1227303685425-96','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',110,'EXECUTED','3:de9943f6a1500bd3f94cb7e0c1d3bde7','Create Index','',NULL,'2.0.5'),('1227303685425-97','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',111,'EXECUTED','3:c0fac38fa4928378abe6f47bd78926b1','Create Index','',NULL,'2.0.5'),('1227303685425-98','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',112,'EXECUTED','3:4c8938f3ea457f5f4f4936e9cbaf898b','Create Index','',NULL,'2.0.5'),('1227303685425-99','ben (generated)','liquibase-schema-only.xml','2016-07-04 10:37:33',113,'EXECUTED','3:d331ce5f04aca9071c5b897396d81098','Create Index','',NULL,'2.0.5'),('2','Eli','liquibase.xml','2016-07-04 10:38:33',10684,'EXECUTED','3:b5e51af4099a150627b3ffff370b9b44','Create Table, Add Foreign Key Constraint','added synonym groups table and a foreign key',NULL,'2.0.5'),('2','upul','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10043,'MARK_RAN','3:b1811e5e43321192b275d6e2fe2fa564','Add Foreign Key Constraint','Create the foreign key from the privilege required for to edit\n			a person attribute type and the privilege.privilege column',NULL,'2.0.5'),('2-increase-privilege-col-size-rol-privilege','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10486,'EXECUTED','3:6fc0247ae054fedeb32a4af3775046f4','Drop Foreign Key Constraint, Modify Column, Add Foreign Key Constraint','Increasing the size of the privilege column in the role_privilege table',NULL,'2.0.5'),('2-role-assign-new-api-privileges-to-renamed-ones','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10484,'EXECUTED','3:4eadbd161bf0f7e15eafb4a52b01b625','Custom SQL','Assigning the new API-level privileges to roles that used to have the renamed privileges',NULL,'2.0.5'),('200805281223','bmckown','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10045,'MARK_RAN','3:b1fc37f9ec96eac9203f0808c2f4ac26','Create Table, Add Foreign Key Constraint','Create the concept_complex table',NULL,'2.0.5'),('200805281224','bmckown','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10046,'MARK_RAN','3:ea32453830c2215bdb209770396002e7','Add Column','Adding the value_complex column to obs.  This may take a long time if you have a large number of observations.',NULL,'2.0.5'),('200805281225','bmckown','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10047,'MARK_RAN','3:5281031bcc075df3b959e94da4adcaa9','Insert Row','Adding a \'complex\' Concept Datatype',NULL,'2.0.5'),('200805281226','bmckown','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10048,'MARK_RAN','3:9a49a3d002485f3a77134d98fb7c8cd8','Drop Table (x2)','Dropping the mimetype and complex_obs tables as they aren\'t needed in the new complex obs setup',NULL,'2.0.5'),('200809191226','smbugua','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10049,'MARK_RAN','3:eed0aa27b44ecf668c81e457d99fa7de','Add Column','Adding the hl7 archive message_state column so that archives can be tracked\n			(preCondition database_version check in place because this change was in the old format in trunk for a while)',NULL,'2.0.5'),('200809191927','smbugua','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10050,'MARK_RAN','3:f0e4fab64749e42770e62e9330c2d288','Rename Column, Modify Column','Adding the hl7 archive message_state column so that archives can be tracked',NULL,'2.0.5'),('200811261102','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10044,'EXECUTED','3:158dd028359ebfd4f1c9bf2e76a5e143','Update Data','Fix field property for new Tribe person attribute',NULL,'2.0.5'),('200901101524','Knoll_Frank','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10051,'EXECUTED','3:feb4a087d13657164e5c3bc787b7f83f','Modify Column','Changing datatype of drug.retire_reason from DATETIME to varchar(255)',NULL,'2.0.5'),('200901130950','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10052,'EXECUTED','3:f1e5e7124bdb4f7378866fdb691e2780','Delete Data (x2)','Remove Manage Tribes and View Tribes privileges from all roles',NULL,'2.0.5'),('200901130951','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10053,'EXECUTED','3:54ac8683819837cc04f1a16b6311d668','Delete Data (x2)','Remove Manage Mime Types, View Mime Types, and Purge Mime Types privilege',NULL,'2.0.5'),('200901161126','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10054,'EXECUTED','3:871b9364dd87b6bfcc0005f40b6eb399','Delete Data','Removed the database_version global property',NULL,'2.0.5'),('20090121-0949','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:38',10055,'EXECUTED','3:8639e35e0238019af2f9e326dd5cbc22','Custom SQL','Switched the default xslt to use PV1-19 instead of PV1-1',NULL,'2.0.5'),('20090122-0853','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10056,'EXECUTED','3:4903c6f81f0309313013851f09a26b85','Custom SQL, Add Lookup Table, Drop Foreign Key Constraint, Delete Data (x2), Drop Table','Remove duplicate concept name tags',NULL,'2.0.5'),('20090123-0305','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10057,'MARK_RAN','3:48cdf2b28fcad687072ac8133e46cba6','Add Unique Constraint','Add unique constraint to the tags table',NULL,'2.0.5'),('20090214-2246','isherman','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10063,'EXECUTED','3:d16c607266238df425db61908e7c8745','Custom SQL','Add weight and cd4 to patientGraphConcepts user property (mysql specific)',NULL,'2.0.5'),('20090214-2247','isherman','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10064,'MARK_RAN','3:e4eeb4a09c2ab695bbde832cd7b6047d','Custom SQL','Add weight and cd4 to patientGraphConcepts user property (using standard sql)',NULL,'2.0.5'),('200902142212','ewolodzko','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10242,'MARK_RAN','3:df93fa2841295b29a0fcd4225c46d1a3','Add Column','Add a sortWeight field to PersonAttributeType',NULL,'2.0.5'),('200902142213','ewolodzko','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10243,'EXECUTED','3:288804e42d575fe62c852ed9daa9d59d','Custom SQL','Add default sortWeights to all current PersonAttributeTypes',NULL,'2.0.5'),('20090224-1002-create-visit_type','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10384,'MARK_RAN','3:ea3c0b323da2d51cf43e982177eace96','Create Table, Add Foreign Key Constraint (x3)','Create visit type table',NULL,'2.0.5'),('20090224-1229','Keelhaul+bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10058,'MARK_RAN','3:f8433194bcb29073c17c7765ce61aab2','Create Table, Add Foreign Key Constraint (x2)','Add location tags table',NULL,'2.0.5'),('20090224-1250','Keelhaul+bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10059,'MARK_RAN','3:8935a56fac2ad91275248d4675c2c090','Create Table, Add Foreign Key Constraint (x2), Add Primary Key','Add location tag map table',NULL,'2.0.5'),('20090224-1256','Keelhaul+bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10060,'MARK_RAN','3:9c0e7238dd1daad9edff381ba22a3ada','Add Column, Add Foreign Key Constraint','Add parent_location column to location table',NULL,'2.0.5'),('20090225-1551','dthomas','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10001,'MARK_RAN','3:a3aed1685bd1051a8c4fae0eab925954',NULL,NULL,NULL,NULL),('20090301-1259','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10062,'EXECUTED','3:21f2ac06dee26613b73003cd1f247ea8','Update Data (x2)','Fixes the description for name layout global property',NULL,'2.0.5'),('20090316-1008','vanand','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10000,'MARK_RAN','3:baa49982f1106c65ba33c845bba149b3',NULL,NULL,NULL,NULL),('20090316-1008-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10432,'EXECUTED','3:aeeb6c14cd22ffa121a2582e04025f5a','Modify Column (x36)','(Fixed)Changing from smallint to BOOLEAN type on BOOLEAN properties',NULL,'2.0.5'),('200903210905','mseaton','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10065,'MARK_RAN','3:720bb7a3f71f0c0a911d3364e55dd72f','Create Table, Add Foreign Key Constraint (x3)','Add a table to enable generic storage of serialized objects',NULL,'2.0.5'),('200903210905-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10066,'EXECUTED','3:a11519f50deeece1f9760d3fc1ac3f05','Modify Column','(Fixed)Add a table to enable generic storage of serialized objects',NULL,'2.0.5'),('20090402-1515-38-cohort','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10071,'MARK_RAN','3:5c65821ef168d9e8296466be5990ae08','Add Column','Adding \"uuid\" column to cohort table',NULL,'2.0.5'),('20090402-1515-38-concept','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10072,'MARK_RAN','3:8004d09d6e2a34623b8d0a13d6c38dc4','Add Column','Adding \"uuid\" column to concept table',NULL,'2.0.5'),('20090402-1515-38-concept_answer','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10073,'MARK_RAN','3:adf3f4ebf7e0eb55eb6927dea7ce2a49','Add Column','Adding \"uuid\" column to concept_answer table',NULL,'2.0.5'),('20090402-1515-38-concept_class','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10074,'MARK_RAN','3:f39e190a2e12c7a6163a0d8a82544228','Add Column','Adding \"uuid\" column to concept_class table',NULL,'2.0.5'),('20090402-1515-38-concept_datatype','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10075,'MARK_RAN','3:d68b3f2323626fee7b433f873a019412','Add Column','Adding \"uuid\" column to concept_datatype table',NULL,'2.0.5'),('20090402-1515-38-concept_description','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10076,'MARK_RAN','3:7d043672ede851c5dcd717171f953c75','Add Column','Adding \"uuid\" column to concept_description table',NULL,'2.0.5'),('20090402-1515-38-concept_map','bwolfe','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10002,'MARK_RAN','3:c1884f56bd70a205b86e7c4038e6c6f9',NULL,NULL,NULL,NULL),('20090402-1515-38-concept_name','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10077,'MARK_RAN','3:822888c5ba1132f6783fbd032c21f238','Add Column','Adding \"uuid\" column to concept_name table',NULL,'2.0.5'),('20090402-1515-38-concept_name_tag','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10078,'MARK_RAN','3:dcb584d414ffd8133c97e42585bd34cd','Add Column','Adding \"uuid\" column to concept_name_tag table',NULL,'2.0.5'),('20090402-1515-38-concept_proposal','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10079,'MARK_RAN','3:fe19ecccb704331741c227aa72597789','Add Column','Adding \"uuid\" column to concept_proposal table',NULL,'2.0.5'),('20090402-1515-38-concept_set','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10080,'MARK_RAN','3:cdc72e16eaec2244c09e9e2fedf5806b','Add Column','Adding \"uuid\" column to concept_set table',NULL,'2.0.5'),('20090402-1515-38-concept_source','bwolfe','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10003,'MARK_RAN','3:ad101415b93eaf653871eddd4fe4fc17',NULL,NULL,NULL,NULL),('20090402-1515-38-concept_state_conversion','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10081,'MARK_RAN','3:5ce8a6cdbfa8742b033b0b1c12e4cd42','Add Column','Adding \"uuid\" column to concept_state_conversion table',NULL,'2.0.5'),('20090402-1515-38-drug','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10082,'MARK_RAN','3:6869bd44f51cb7f63f758fbd8a7fe156','Add Column','Adding \"uuid\" column to drug table',NULL,'2.0.5'),('20090402-1515-38-encounter','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10083,'MARK_RAN','3:0808491f7ec59827a0415f2949b9d90e','Add Column','Adding \"uuid\" column to encounter table',NULL,'2.0.5'),('20090402-1515-38-encounter_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10084,'MARK_RAN','3:9aaac835f4d9579386990d4990ffb9d6','Add Column','Adding \"uuid\" column to encounter_type table',NULL,'2.0.5'),('20090402-1515-38-field','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10085,'MARK_RAN','3:dfee5fe509457ef12b14254bab9e6df5','Add Column','Adding \"uuid\" column to field table',NULL,'2.0.5'),('20090402-1515-38-field_answer','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10086,'MARK_RAN','3:c378494d6e9ae45b278c726256619cd7','Add Column','Adding \"uuid\" column to field_answer table',NULL,'2.0.5'),('20090402-1515-38-field_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10087,'MARK_RAN','3:dfb47f0b85d5bdad77f3a15cc4d180ec','Add Column','Adding \"uuid\" column to field_type table',NULL,'2.0.5'),('20090402-1515-38-form','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10088,'MARK_RAN','3:eb707ff99ed8ca2945a43175b904dea4','Add Column','Adding \"uuid\" column to form table',NULL,'2.0.5'),('20090402-1515-38-form_field','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10089,'MARK_RAN','3:635701ccda0484966f45f0e617119100','Add Column','Adding \"uuid\" column to form_field table',NULL,'2.0.5'),('20090402-1515-38-global_property','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10090,'MARK_RAN','3:1c62ba666b60eaa88ee3a90853f3bf59','Add Column','Adding \"uuid\" column to global_property table',NULL,'2.0.5'),('20090402-1515-38-hl7_in_archive','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10091,'MARK_RAN','3:9c5015280eff821924416112922fd94d','Add Column','Adding \"uuid\" column to hl7_in_archive table',NULL,'2.0.5'),('20090402-1515-38-hl7_in_error','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10092,'MARK_RAN','3:35b94fc079e6de9ada4329a7bbc55645','Add Column','Adding \"uuid\" column to hl7_in_error table',NULL,'2.0.5'),('20090402-1515-38-hl7_in_queue','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10093,'MARK_RAN','3:494d9eaaed055d0c5af4b4d85db2095d','Add Column','Adding \"uuid\" column to hl7_in_queue table',NULL,'2.0.5'),('20090402-1515-38-hl7_source','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10094,'MARK_RAN','3:8bc9839788ef5ab415ccf020eb04a1f7','Add Column','Adding \"uuid\" column to hl7_source table',NULL,'2.0.5'),('20090402-1515-38-location','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10095,'MARK_RAN','3:7e6b762f813310c72026677d540dee57','Add Column','Adding \"uuid\" column to location table',NULL,'2.0.5'),('20090402-1515-38-location_tag','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10096,'MARK_RAN','3:6a94a67e776662268d42f09cf7c66ac0','Add Column','Adding \"uuid\" column to location_tag table',NULL,'2.0.5'),('20090402-1515-38-note','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10097,'MARK_RAN','3:f0fd7b6750d07c973aad667b170cdfa8','Add Column','Adding \"uuid\" column to note table',NULL,'2.0.5'),('20090402-1515-38-notification_alert','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10098,'MARK_RAN','3:f2865558fb76c7584f6e86786b0ffdea','Add Column','Adding \"uuid\" column to notification_alert table',NULL,'2.0.5'),('20090402-1515-38-notification_template','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10099,'MARK_RAN','3:c05536d99eb2479211cb10010d48a2e9','Add Column','Adding \"uuid\" column to notification_template table',NULL,'2.0.5'),('20090402-1515-38-obs','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10100,'MARK_RAN','3:ba99d7eccba2185e9d5ebab98007e577','Add Column','Adding \"uuid\" column to obs table',NULL,'2.0.5'),('20090402-1515-38-orders','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10102,'MARK_RAN','3:732a2d4fd91690d544f0c63bdb65819f','Add Column','Adding \"uuid\" column to orders table',NULL,'2.0.5'),('20090402-1515-38-order_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10101,'MARK_RAN','3:137552884c5eb5af4c3f77c90df514cb','Add Column','Adding \"uuid\" column to order_type table',NULL,'2.0.5'),('20090402-1515-38-patient_identifier','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10103,'MARK_RAN','3:1a9ddcd8997bcf1a9668051d397e41c1','Add Column','Adding \"uuid\" column to patient_identifier table',NULL,'2.0.5'),('20090402-1515-38-patient_identifier_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10104,'MARK_RAN','3:6170d6caa73320fd2433fba0a16e8029','Add Column','Adding \"uuid\" column to patient_identifier_type table',NULL,'2.0.5'),('20090402-1515-38-patient_program','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10105,'MARK_RAN','3:8fb284b435669717f4b5aaa66e61fc10','Add Column','Adding \"uuid\" column to patient_program table',NULL,'2.0.5'),('20090402-1515-38-patient_state','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10106,'MARK_RAN','3:b67eb1bbd3e2912a646f56425c38631f','Add Column','Adding \"uuid\" column to patient_state table',NULL,'2.0.5'),('20090402-1515-38-person','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10107,'MARK_RAN','3:2b89eb77976b9159717e9d7b83c34cf1','Add Column','Adding \"uuid\" column to person table',NULL,'2.0.5'),('20090402-1515-38-person_address','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10108,'MARK_RAN','3:cfdb17b16b6d15477bc72d4d19ac3f29','Add Column','Adding \"uuid\" column to person_address table',NULL,'2.0.5'),('20090402-1515-38-person_attribute','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10109,'MARK_RAN','3:2f6b7fa688987b32d99cda348c6f6c46','Add Column','Adding \"uuid\" column to person_attribute table',NULL,'2.0.5'),('20090402-1515-38-person_attribute_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10110,'MARK_RAN','3:38d4dce320f2fc35db9dfcc2eafc093e','Add Column','Adding \"uuid\" column to person_attribute_type table',NULL,'2.0.5'),('20090402-1515-38-person_name','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10111,'MARK_RAN','3:339f02d6797870f9e7dd704f093b088c','Add Column','Adding \"uuid\" column to person_name table',NULL,'2.0.5'),('20090402-1515-38-privilege','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10112,'MARK_RAN','3:41f52c4340fdc9f0825ea9660edea8ec','Add Column','Adding \"uuid\" column to privilege table',NULL,'2.0.5'),('20090402-1515-38-program','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10113,'MARK_RAN','3:a72f80159cdbd576906cd3b9069d425b','Add Column','Adding \"uuid\" column to program table',NULL,'2.0.5'),('20090402-1515-38-program_workflow','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10114,'MARK_RAN','3:c69183f7e1614d5a338c0d0944f1e754','Add Column','Adding \"uuid\" column to program_workflow table',NULL,'2.0.5'),('20090402-1515-38-program_workflow_state','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10115,'MARK_RAN','3:e25b0fa351bb667af3ff562855f66bb6','Add Column','Adding \"uuid\" column to program_workflow_state table',NULL,'2.0.5'),('20090402-1515-38-relationship','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10116,'MARK_RAN','3:95407167e9f4984de1d710a83371ebd1','Add Column','Adding \"uuid\" column to relationship table',NULL,'2.0.5'),('20090402-1515-38-relationship_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10117,'MARK_RAN','3:f8755b127c004d11a43bfd6558be01b7','Add Column','Adding \"uuid\" column to relationship_type table',NULL,'2.0.5'),('20090402-1515-38-report_object','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10118,'MARK_RAN','3:b7ce0784e817be464370a3154fd4aa9c','Add Column','Adding \"uuid\" column to report_object table',NULL,'2.0.5'),('20090402-1515-38-report_schema_xml','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10119,'MARK_RAN','3:ce7ae79a3e3ce429a56fa658c48889b5','Add Column','Adding \"uuid\" column to report_schema_xml table',NULL,'2.0.5'),('20090402-1515-38-role','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10120,'MARK_RAN','3:f33887a0b51ab366d414e16202cf55db','Add Column','Adding \"uuid\" column to role table',NULL,'2.0.5'),('20090402-1515-38-serialized_object','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10121,'MARK_RAN','3:341cfbdff8ebf188d526bf3348619dcc','Add Column','Adding \"uuid\" column to serialized_object table',NULL,'2.0.5'),('20090402-1516-cohort','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10122,'EXECUTED','3:110084035197514c8d640b915230cf72','Update Data','Generating UUIDs for all rows in cohort table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-concept','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10123,'EXECUTED','3:a44bc743cb837d88f7371282f3a5871e','Update Data','Generating UUIDs for all rows in concept table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-concept_answer','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10124,'EXECUTED','3:f01d7278b153fa10a7d741607501ae1e','Update Data','Generating UUIDs for all rows in concept_answer table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-concept_class','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10125,'EXECUTED','3:786f0ec8beec453ea9487f2e77f9fb4d','Update Data','Generating UUIDs for all rows in concept_class table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-concept_datatype','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10126,'EXECUTED','3:b828e9851365ec70531dabd250374989','Update Data','Generating UUIDs for all rows in concept_datatype table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-concept_description','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10127,'EXECUTED','3:37dbfc43c73553c9c9ecf11206714cc4','Update Data','Generating UUIDs for all rows in concept_description table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-concept_map','bwolfe','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10004,'MARK_RAN','3:e843f99c0371aabee21ca94fcef01f39',NULL,NULL,NULL,NULL),('20090402-1516-concept_name','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10128,'EXECUTED','3:dd414ae9367287c9c03342a79abd1d62','Update Data','Generating UUIDs for all rows in concept_name table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-concept_name_tag','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10129,'EXECUTED','3:cd7b5d0ceeb90b2254708b44c10d03e8','Update Data','Generating UUIDs for all rows in concept_name_tag table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-concept_proposal','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10130,'EXECUTED','3:fb1cfa9c5decbafc3293f3dd1d87ff2b','Update Data','Generating UUIDs for all rows in concept_proposal table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-concept_set','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10131,'EXECUTED','3:3b7f3851624014e740f89bc9a431feaa','Update Data','Generating UUIDs for all rows in concept_set table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-concept_source','bwolfe','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10005,'MARK_RAN','3:53da91ae3e39d7fb7ebca91df3bfd9a6',NULL,NULL,NULL,NULL),('20090402-1516-concept_state_conversion','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10132,'EXECUTED','3:23197d24e498ad86d4e001b183cc0c6b','Update Data','Generating UUIDs for all rows in concept_state_conversion table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-drug','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10133,'EXECUTED','3:40b47df80bd425337b7bdd8b41497967','Update Data','Generating UUIDs for all rows in drug table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-encounter','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10134,'EXECUTED','3:40146708b71d86d4c8c5340767a98f5e','Update Data','Generating UUIDs for all rows in encounter table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-encounter_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10135,'EXECUTED','3:738c6b6244a84fc8e6d582bcd472ffe6','Update Data','Generating UUIDs for all rows in encounter_type table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-field','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10136,'EXECUTED','3:98d2a1550e867e4ef303a4cc47ed904d','Update Data','Generating UUIDs for all rows in field table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-field_answer','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10137,'EXECUTED','3:82bdfe361286d261724eef97dd89e358','Update Data','Generating UUIDs for all rows in field_answer table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-field_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10138,'EXECUTED','3:19a8d007f6147651240ebb9539d3303a','Update Data','Generating UUIDs for all rows in field_type table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-form','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10139,'EXECUTED','3:026ddf1c9050c7367d4eb57dd4105322','Update Data','Generating UUIDs for all rows in form table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-form_field','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10140,'EXECUTED','3:a8b0bcdb35830c2badfdcb9b1cfdd3b5','Update Data','Generating UUIDs for all rows in form_field table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-global_property','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10141,'EXECUTED','3:75a5b4a9473bc9c6bfbabf8e77b0cda7','Update Data','Generating UUIDs for all rows in global_property table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-hl7_in_archive','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10142,'EXECUTED','3:09891436d8ea0ad14f7b52fd05daa237','Update Data','Generating UUIDs for all rows in hl7_in_archive table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-hl7_in_error','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10143,'EXECUTED','3:8d276bbd8bf9d9d1c64756f37ef91ed3','Update Data','Generating UUIDs for all rows in hl7_in_error table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-hl7_in_queue','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10144,'EXECUTED','3:25e8f998171accd46860717f93690ccc','Update Data','Generating UUIDs for all rows in hl7_in_queue table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-hl7_source','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10145,'EXECUTED','3:45c06e034d7158a0d09afae60c4c83d6','Update Data','Generating UUIDs for all rows in hl7_source table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-location','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10146,'EXECUTED','3:fce0f7eaab989f2ff9664fc66d6b8419','Update Data','Generating UUIDs for all rows in location table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-location_tag','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10147,'EXECUTED','3:50f26d1376ea108bbb65fd4d0633e741','Update Data','Generating UUIDs for all rows in location_tag table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-note','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10148,'EXECUTED','3:f5a0eea2a7c59fffafa674de4356e621','Update Data','Generating UUIDs for all rows in note table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-notification_alert','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10149,'EXECUTED','3:481fbab9bd53449903ac193894adbc28','Update Data','Generating UUIDs for all rows in notification_alert table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-notification_template','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10150,'EXECUTED','3:a4a2990465c4c99747f83ea880cac46a','Update Data','Generating UUIDs for all rows in notification_template table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-obs','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10151,'EXECUTED','3:26d80fdd889922821244f84e3f8039e7','Update Data','Generating UUIDs for all rows in obs table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-orders','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10153,'EXECUTED','3:ec3bc80540d78f416e1d4eef62e8e15a','Update Data','Generating UUIDs for all rows in orders table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-order_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10152,'EXECUTED','3:cae66b98b889c7ee1c8d6ab270a8d0d5','Update Data','Generating UUIDs for all rows in order_type table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-patient_identifier','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10154,'EXECUTED','3:647906cc7cf1fde9b7644b8f2541664f','Update Data','Generating UUIDs for all rows in patient_identifier table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-patient_identifier_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10155,'EXECUTED','3:85f8db0310c15a74b17e968c7730ae12','Update Data','Generating UUIDs for all rows in patient_identifier_type table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-patient_program','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10156,'EXECUTED','3:576b7db39f0212f8e92b6f4e1844ea30','Update Data','Generating UUIDs for all rows in patient_program table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-patient_state','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10157,'EXECUTED','3:250eab0f97fc4eeb4f1a930fbccfcf08','Update Data','Generating UUIDs for all rows in patient_state table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-person','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10158,'EXECUTED','3:cedc8bcd77ade51558fb2d12916e31a4','Update Data','Generating UUIDs for all rows in person table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-person_address','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10159,'EXECUTED','3:0f817424ca41e5c5b459591d6e18b3c6','Update Data','Generating UUIDs for all rows in person_address table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-person_attribute','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10160,'EXECUTED','3:7f9e09b1267c4a787a9d3e37acfd5746','Update Data','Generating UUIDs for all rows in person_attribute table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-person_attribute_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10161,'EXECUTED','3:1e5f84054b7b7fdf59673e2260f48d9d','Update Data','Generating UUIDs for all rows in person_attribute_type table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-person_name','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10162,'EXECUTED','3:f827da2c097b01ca9073c258b19e9540','Update Data','Generating UUIDs for all rows in person_name table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-privilege','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10163,'EXECUTED','3:2ab150a53c91ded0c5b53fa99fde4ba2','Update Data','Generating UUIDs for all rows in privilege table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-program','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10164,'EXECUTED','3:132b63f2efcf781187602e043122e7ff','Update Data','Generating UUIDs for all rows in program table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-program_workflow','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10165,'EXECUTED','3:d945359ed4bb6cc6a21f4554a0c50a33','Update Data','Generating UUIDs for all rows in program_workflow table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-program_workflow_state','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10166,'EXECUTED','3:4bc093882ac096562d63562ac76a1ffa','Update Data','Generating UUIDs for all rows in program_workflow_state table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-relationship','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10167,'EXECUTED','3:25e22c04ada4808cc31fd48f23703333','Update Data','Generating UUIDs for all rows in relationship table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-relationship_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10168,'EXECUTED','3:562ad77e9453595c9cd22a2cdde3cc41','Update Data','Generating UUIDs for all rows in relationship_type table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-report_object','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10169,'EXECUTED','3:8531f740c64a0d1605225536c1be0860','Update Data','Generating UUIDs for all rows in report_object table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-report_schema_xml','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10170,'EXECUTED','3:cd9efe4d62f2754b057d2d409d6e826a','Update Data','Generating UUIDs for all rows in report_schema_xml table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-role','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10171,'EXECUTED','3:f75bfc36ad13cb9324b9520804a60141','Update Data','Generating UUIDs for all rows in role table via built in uuid function.',NULL,'2.0.5'),('20090402-1516-serialized_object','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10172,'EXECUTED','3:c809b71d2444a8a8e2c5e5574d344c82','Update Data','Generating UUIDs for all rows in serialized_object table via built in uuid function.',NULL,'2.0.5'),('20090402-1517','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:41',10181,'MARK_RAN','3:4edd135921eb263d4811cf1c22ef4846','Custom Change','Adding UUIDs to all rows in all columns via a java class. (This will take a long time on large databases)',NULL,'2.0.5'),('20090402-1518','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:41',10182,'MARK_RAN','3:a9564fc8de85d37f4748a3fa1e69281c','Add Not-Null Constraint (x52)','Now that UUID generation is done, set the uuid columns to not \"NOT NULL\"',NULL,'2.0.5'),('20090402-1519-cohort','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:42',10183,'EXECUTED','3:260c435f1cf3e3f01d953d630c7a578b','Create Index','Creating unique index on cohort.uuid column',NULL,'2.0.5'),('20090402-1519-concept','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:42',10184,'EXECUTED','3:9e363ee4b39e7fdfb547e3a51ad187c7','Create Index','Creating unique index on concept.uuid column',NULL,'2.0.5'),('20090402-1519-concept_answer','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:42',10185,'EXECUTED','3:34b049a3fd545928760968beb1e98e00','Create Index','Creating unique index on concept_answer.uuid column',NULL,'2.0.5'),('20090402-1519-concept_class','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:42',10186,'EXECUTED','3:0fc95dccef2343850adb1fe49d60f3c3','Create Index','Creating unique index on concept_class.uuid column',NULL,'2.0.5'),('20090402-1519-concept_datatype','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:43',10187,'EXECUTED','3:0cf065b0f780dc2eeca994628af49a34','Create Index','Creating unique index on concept_datatype.uuid column',NULL,'2.0.5'),('20090402-1519-concept_description','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:43',10188,'EXECUTED','3:16ce0ad6c3e37071bbfcaad744693d0f','Create Index','Creating unique index on concept_description.uuid column',NULL,'2.0.5'),('20090402-1519-concept_map','bwolfe','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10006,'MARK_RAN','3:b8a320c1d44ab94e785c9ae6c41378f3',NULL,NULL,NULL,NULL),('20090402-1519-concept_name','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:43',10189,'EXECUTED','3:0d5866c0d3eadc8df09b1a7c160508ca','Create Index','Creating unique index on concept_name.uuid column',NULL,'2.0.5'),('20090402-1519-concept_name_tag','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:43',10190,'EXECUTED','3:7ba597ec0fb5fbfba615ac97df642072','Create Index','Creating unique index on concept_name_tag.uuid column',NULL,'2.0.5'),('20090402-1519-concept_proposal','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:44',10191,'EXECUTED','3:79f9f4af9669c2b03511832a23db55e0','Create Index','Creating unique index on concept_proposal.uuid column',NULL,'2.0.5'),('20090402-1519-concept_set','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:44',10192,'EXECUTED','3:f5ba4e2d5ddd4ec66f43501b9749cf70','Create Index','Creating unique index on concept_set.uuid column',NULL,'2.0.5'),('20090402-1519-concept_source','bwolfe','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10007,'MARK_RAN','3:c7c47d9c2876bfa53542885e304b21e7',NULL,NULL,NULL,NULL),('20090402-1519-concept_state_conversion','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:44',10193,'EXECUTED','3:cc9d9bb0d5eb9f6583cd538919b42b9a','Create Index','Creating unique index on concept_state_conversion.uuid column',NULL,'2.0.5'),('20090402-1519-drug','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:44',10194,'EXECUTED','3:8cac800e9f857e29698e1c80ab7e6a52','Create Index','Creating unique index on drug.uuid column',NULL,'2.0.5'),('20090402-1519-encounter','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:45',10195,'EXECUTED','3:8fd623411a44ffb0d4e3a4139e916585','Create Index','Creating unique index on encounter.uuid column',NULL,'2.0.5'),('20090402-1519-encounter_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:45',10196,'EXECUTED','3:71e0e1df8c290d8b6e81e281154661e0','Create Index','Creating unique index on encounter_type.uuid column',NULL,'2.0.5'),('20090402-1519-field','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:45',10197,'EXECUTED','3:36d9eba3e0a90061c6bf1c8aa483110e','Create Index','Creating unique index on field.uuid column',NULL,'2.0.5'),('20090402-1519-field_answer','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:46',10198,'EXECUTED','3:81572b572f758cac173b5d14516f600e','Create Index','Creating unique index on field_answer.uuid column',NULL,'2.0.5'),('20090402-1519-field_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:46',10199,'EXECUTED','3:a0c3927dfde900959131aeb1490a5f51','Create Index','Creating unique index on field_type.uuid column',NULL,'2.0.5'),('20090402-1519-form','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:46',10200,'EXECUTED','3:61147c780ce563776a1caed795661aca','Create Index','Creating unique index on form.uuid column',NULL,'2.0.5'),('20090402-1519-form_field','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:46',10201,'EXECUTED','3:bd9def4522865d181e42809f9dd5c116','Create Index','Creating unique index on form_field.uuid column',NULL,'2.0.5'),('20090402-1519-global_property','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:47',10202,'EXECUTED','3:0e6b84ad5fffa3fd49242b5475e8eb66','Create Index','Creating unique index on global_property.uuid column',NULL,'2.0.5'),('20090402-1519-hl7_in_archive','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:47',10203,'EXECUTED','3:d2f8921c170e416560c234aa74964346','Create Index','Creating unique index on hl7_in_archive.uuid column',NULL,'2.0.5'),('20090402-1519-hl7_in_error','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:47',10204,'EXECUTED','3:9ccec0729ea1b4eaa5068726f9045c25','Create Index','Creating unique index on hl7_in_error.uuid column',NULL,'2.0.5'),('20090402-1519-hl7_in_queue','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:47',10205,'EXECUTED','3:af537cb4134c3f2ed0357f3280ceb6fe','Create Index','Creating unique index on hl7_in_queue.uuid column',NULL,'2.0.5'),('20090402-1519-hl7_source','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:48',10206,'EXECUTED','3:a6d1847b6a590319206f65be9d1d3c9e','Create Index','Creating unique index on hl7_source.uuid column',NULL,'2.0.5'),('20090402-1519-location','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:48',10207,'EXECUTED','3:c435bd4b405d4f11d919777718aa055c','Create Index','Creating unique index on location.uuid column',NULL,'2.0.5'),('20090402-1519-location_tag','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:48',10208,'EXECUTED','3:33a8a54cde59b23a9cdb7740a9995e1a','Create Index','Creating unique index on location_tag.uuid column',NULL,'2.0.5'),('20090402-1519-note','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:48',10209,'EXECUTED','3:97279b2ce285e56613a10a77c5af32b2','Create Index','Creating unique index on note.uuid column',NULL,'2.0.5'),('20090402-1519-notification_alert','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:49',10210,'EXECUTED','3:a763255eddf8607f7d86afbb3099d4b5','Create Index','Creating unique index on notification_alert.uuid column',NULL,'2.0.5'),('20090402-1519-notification_template','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:49',10211,'EXECUTED','3:9a69bbb343077bc62acdf6a66498029a','Create Index','Creating unique index on notification_template.uuid column',NULL,'2.0.5'),('20090402-1519-obs','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:49',10212,'EXECUTED','3:de9a7a24e527542e6b4a73e2cd31a7f9','Create Index','Creating unique index on obs.uuid column',NULL,'2.0.5'),('20090402-1519-orders','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:50',10214,'EXECUTED','3:848c0a00a32c5eb25041ad058fd38263','Create Index','Creating unique index on orders.uuid column',NULL,'2.0.5'),('20090402-1519-order_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:50',10213,'EXECUTED','3:d938d263e0acf974d43ad81d2fbe05b0','Create Index','Creating unique index on order_type.uuid column',NULL,'2.0.5'),('20090402-1519-patient_identifier','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:50',10215,'EXECUTED','3:43389efa06408c8312d130654309d140','Create Index','Creating unique index on patient_identifier.uuid column',NULL,'2.0.5'),('20090402-1519-patient_identifier_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:50',10216,'EXECUTED','3:3ffe4f31a1c48d2545e8eed4127cc490','Create Index','Creating unique index on patient_identifier_type.uuid column',NULL,'2.0.5'),('20090402-1519-patient_program','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:51',10217,'EXECUTED','3:ce69defda5ba254914f2319f3a7aac02','Create Index','Creating unique index on patient_program.uuid column',NULL,'2.0.5'),('20090402-1519-patient_state','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:51',10218,'EXECUTED','3:a4ca15f62b3c8c43f7f47ef8b9e39cd3','Create Index','Creating unique index on patient_state.uuid column',NULL,'2.0.5'),('20090402-1519-person','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:51',10219,'EXECUTED','3:345a5d4e8dea4d56c1a0784e7b35a801','Create Index','Creating unique index on person.uuid column',NULL,'2.0.5'),('20090402-1519-person_address','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:51',10220,'EXECUTED','3:105ece744a45b624ea8990f152bb8300','Create Index','Creating unique index on person_address.uuid column',NULL,'2.0.5'),('20090402-1519-person_attribute','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:52',10221,'EXECUTED','3:67a8cdda8605c28f76314873d2606457','Create Index','Creating unique index on person_attribute.uuid column',NULL,'2.0.5'),('20090402-1519-person_attribute_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:52',10222,'EXECUTED','3:a234ad0ea13f32fc4529cf556151d611','Create Index','Creating unique index on person_attribute_type.uuid column',NULL,'2.0.5'),('20090402-1519-person_name','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:52',10223,'EXECUTED','3:d18e326ce221b4b1232ce2e355731338','Create Index','Creating unique index on person_name.uuid column',NULL,'2.0.5'),('20090402-1519-privilege','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:53',10224,'EXECUTED','3:47e7f70f34a213d870e2aeed795d5e3d','Create Index','Creating unique index on privilege.uuid column',NULL,'2.0.5'),('20090402-1519-program','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:53',10225,'EXECUTED','3:62f9d9ecd2325d5908237a769e9a8bc7','Create Index','Creating unique index on program.uuid column',NULL,'2.0.5'),('20090402-1519-program_workflow','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:53',10226,'EXECUTED','3:fabb3152f6055dc0071a2e5d6f573d2f','Create Index','Creating unique index on program_workflow.uuid column',NULL,'2.0.5'),('20090402-1519-program_workflow_state','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:53',10227,'EXECUTED','3:4fdf0c20aedcdc87b2c6058a1cc8fce7','Create Index','Creating unique index on program_workflow_state.uuid column',NULL,'2.0.5'),('20090402-1519-relationship','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:53',10228,'EXECUTED','3:c90617ca900b1aef3f29e71f693e8a25','Create Index','Creating unique index on relationship.uuid column',NULL,'2.0.5'),('20090402-1519-relationship_type','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:54',10229,'EXECUTED','3:c9f05aca70b6dad54af121b593587a29','Create Index','Creating unique index on relationship_type.uuid column',NULL,'2.0.5'),('20090402-1519-report_object','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:54',10230,'EXECUTED','3:6069b78580fd0d276f5dae9f3bdf21be','Create Index','Creating unique index on report_object.uuid column',NULL,'2.0.5'),('20090402-1519-report_schema_xml','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:54',10231,'EXECUTED','3:91499d332dda0577fd02b6a6b7b35e99','Create Index','Creating unique index on report_schema_xml.uuid column',NULL,'2.0.5'),('20090402-1519-role','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:55',10232,'EXECUTED','3:c535a800ceb006311bbb7a27e8bab6ea','Create Index','Creating unique index on role.uuid column',NULL,'2.0.5'),('20090402-1519-serialized_object','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:55',10233,'EXECUTED','3:e8f2b1c3a7a67aadc8499ebcb522c91a','Create Index','Creating unique index on serialized_object.uuid column',NULL,'2.0.5'),('20090408-1298','Cory McCarthy','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10068,'EXECUTED','3:defbd13a058ba3563e232c2093cd2b37','Modify Column','Changed the datatype for encounter_type to \'text\' instead of just 50 chars',NULL,'2.0.5'),('200904091023','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10067,'EXECUTED','3:48adc23e9c5d820a87f6c8d61dfb6b55','Delete Data (x4)','Remove Manage Tribes and View Tribes privileges from the privilege table and role_privilege table.\n			The privileges will be recreated by the Tribe module if it is installed.',NULL,'2.0.5'),('20090414-0804','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10173,'EXECUTED','3:479b4df8e3c746b5b96eeea422799774','Drop Foreign Key Constraint','Dropping foreign key on concept_set.concept_id table',NULL,'2.0.5'),('20090414-0805','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10174,'MARK_RAN','3:5017417439ff841eb036ceb94f3c5800','Drop Primary Key','Dropping primary key on concept set table',NULL,'2.0.5'),('20090414-0806','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:40',10175,'MARK_RAN','3:6b9cec59fd607569228bf87d4dffa1a5','Add Column','Adding new integer primary key to concept set table',NULL,'2.0.5'),('20090414-0807','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:41',10176,'MARK_RAN','3:57834f6c953f34035237e06a2dbed9c7','Create Index, Add Foreign Key Constraint','Adding index and foreign key to concept_set.concept_id column',NULL,'2.0.5'),('20090414-0808a','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:41',10177,'EXECUTED','3:6c9d9e6b85c1bf04fdbf9fdec316f2ea','Drop Foreign Key Constraint','Dropping foreign key on patient_identifier.patient_id column',NULL,'2.0.5'),('20090414-0808b','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:41',10178,'MARK_RAN','3:12e01363841135ed0dae46d71e7694cf','Drop Primary Key','Dropping non-integer primary key on patient identifier table before adding a new integer primary key',NULL,'2.0.5'),('20090414-0809','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:41',10179,'MARK_RAN','3:864765efa4cae1c8ffb1138d63f77017','Add Column','Adding new integer primary key to patient identifier table',NULL,'2.0.5'),('20090414-0810','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:41',10180,'MARK_RAN','3:4ca46ee358567e35c897a73c065e3367','Create Index, Add Foreign Key Constraint','Adding index and foreign key on patient_identifier.patient_id column',NULL,'2.0.5'),('20090414-0811a','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:55',10234,'EXECUTED','3:f027a0ad38c0f6302def391da78aaaee','Drop Foreign Key Constraint','Dropping foreign key on concept_word.concept_id column',NULL,'2.0.5'),('20090414-0811b','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:55',10236,'MARK_RAN','3:982d502e56854922542286cead4c09ce','Drop Primary Key','Dropping non-integer primary key on concept word table before adding new integer one',NULL,'2.0.5'),('20090414-0812','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:55',10237,'MARK_RAN','3:948e635fe3f63122856ca9b8a174352b','Add Column','Adding integer primary key to concept word table',NULL,'2.0.5'),('20090414-0812b','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:55',10238,'MARK_RAN','3:bd7731e58f3db9b944905597a08eb6cb','Add Foreign Key Constraint','Re-adding foreign key for concept_word.concept_name_id',NULL,'2.0.5'),('200904271042','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10241,'MARK_RAN','3:db63ce704aff4741c52181d1c825ab62','Drop Column','Remove the now unused synonym column',NULL,'2.0.5'),('20090428-0811aa','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:55',10235,'MARK_RAN','3:58d8f3df1fe704714a7b4957a6c0e7f7','Drop Foreign Key Constraint','Removing concept_word.concept_name_id foreign key so that primary key can be changed to concept_word_id',NULL,'2.0.5'),('20090428-0854','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10239,'EXECUTED','3:11086a37155507c0238c9532f66b172b','Add Foreign Key Constraint','Adding foreign key for concept_word.concept_id column',NULL,'2.0.5'),('200905071626','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10070,'MARK_RAN','3:d29884c3ef8fd867c3c2ffbd557c14c2','Create Index','Add an index to the concept_word.concept_id column (This update may fail if it already exists)',NULL,'2.0.5'),('200905150814','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:39',10069,'EXECUTED','3:44c729b393232d702553e0768cf94994','Delete Data','Deleting invalid concept words',NULL,'2.0.5'),('200905150821','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10240,'EXECUTED','3:c0b7abc7eb00f243325b4a3fb2afc614','Custom SQL','Deleting duplicate concept word keys',NULL,'2.0.5'),('200906301606','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10244,'EXECUTED','3:de40c56c128997509d1d943ed047c5d2','Modify Column','Change person_attribute_type.sort_weight from an integer to a float',NULL,'2.0.5'),('200907161638-1','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10245,'EXECUTED','3:dfd352bdc4c5e6c88cd040d03c782e31','Modify Column','Change obs.value_numeric from a double(22,0) to a double',NULL,'2.0.5'),('200907161638-2','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10246,'EXECUTED','3:a8dc0bd1593e6c99a02db443bc4cb001','Modify Column','Change concept_numeric columns from a double(22,0) type to a double',NULL,'2.0.5'),('200907161638-3','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10247,'EXECUTED','3:47b8adbcd480660765dd117020a1e085','Modify Column','Change concept_set.sort_weight from a double(22,0) to a double',NULL,'2.0.5'),('200907161638-4','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10248,'EXECUTED','3:3ffccaa291298fea317eb7025c058492','Modify Column','Change concept_set_derived.sort_weight from a double(22,0) to a double',NULL,'2.0.5'),('200907161638-5','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10249,'EXECUTED','3:3b31cf625830c7e37fa638dbf9625000','Modify Column','Change drug table columns from a double(22,0) to a double',NULL,'2.0.5'),('200907161638-6','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10250,'EXECUTED','3:dc733faec1539038854c0b559b45da0e','Modify Column','Change drug_order.dose from a double(22,0) to a double',NULL,'2.0.5'),('200908291938-1','dthomas','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10008,'MARK_RAN','3:b99a6d7349d367c30e8b404979e07b89',NULL,NULL,NULL,NULL),('200908291938-2a','dthomas','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10009,'MARK_RAN','3:7e9e8d9bffcb6e602b155827f72a3856',NULL,NULL,NULL,NULL),('20090831-1039-38-scheduler_task_config','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10254,'MARK_RAN','3:54e254379235d5c8b569a00ac7dc9c3f','Add Column','Adding \"uuid\" column to scheduler_task_config table',NULL,'2.0.5'),('20090831-1040-scheduler_task_config','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10255,'EXECUTED','3:a9b26bdab35405050c052a9a3f763db0','Update Data','Generating UUIDs for all rows in scheduler_task_config table via built in uuid function.',NULL,'2.0.5'),('20090831-1041-scheduler_task_config','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10256,'MARK_RAN','3:25127273b2d501664ce325922b0c7db2','Custom Change','Adding UUIDs to all rows in scheduler_task_config table via a java class for non mysql/oracle/mssql databases.',NULL,'2.0.5'),('20090831-1042-scheduler_task_config','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10257,'EXECUTED','3:76d8a8b5d342fc4111034861537315cf','Add Not-Null Constraint','Now that UUID generation is done for scheduler_task_config, set the uuid column to not \"NOT NULL\"',NULL,'2.0.5'),('20090831-1043-scheduler_task_config','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10258,'EXECUTED','3:5408ed04284c4f5d57f5160ca5393733','Create Index','Creating unique index on scheduler_task_config.uuid column',NULL,'2.0.5'),('20090907-1','Knoll_Frank','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10259,'MARK_RAN','3:d6f3ed289cdbce6229b1414ec626a33c','Rename Column','Rename the concept_source.date_voided column to date_retired',NULL,'2.0.5'),('20090907-2a','Knoll_Frank','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10260,'MARK_RAN','3:b71e307e4e782cc5a851f764aa7fc0d0','Drop Foreign Key Constraint','Remove the concept_source.voided_by foreign key constraint',NULL,'2.0.5'),('20090907-2b','Knoll_Frank','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10261,'MARK_RAN','3:14e07ebc0a1138ee973bbb26b568d16e','Rename Column, Add Foreign Key Constraint','Rename the concept_source.voided_by column to retired_by',NULL,'2.0.5'),('20090907-3','Knoll_Frank','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10262,'MARK_RAN','3:adee9ced82158f9a9f3d64245ad591c6','Rename Column','Rename the concept_source.voided column to retired',NULL,'2.0.5'),('20090907-4','Knoll_Frank','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10263,'MARK_RAN','3:ad9b6ed4ef3ae43556d3e8c9e2ec0f5c','Rename Column','Rename the concept_source.void_reason column to retire_reason',NULL,'2.0.5'),('20091001-1023','rcrichton','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10291,'MARK_RAN','3:2bf99392005da4e95178bd1e2c28a87b','Add Column','add retired column to relationship_type table',NULL,'2.0.5'),('20091001-1024','rcrichton','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10292,'MARK_RAN','3:31b7b10f75047606406cea156bcc255f','Add Column','add retired_by column to relationship_type table',NULL,'2.0.5'),('20091001-1025','rcrichton','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10293,'MARK_RAN','3:c6dd75893e5573baa0c7426ecccaa92d','Add Foreign Key Constraint','Create the foreign key from the relationship.retired_by to users.user_id.',NULL,'2.0.5'),('20091001-1026','rcrichton','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10294,'MARK_RAN','3:47cfbab54a8049948784a165ffe830af','Add Column','add date_retired column to relationship_type table',NULL,'2.0.5'),('20091001-1027','rcrichton','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10295,'MARK_RAN','3:2db32da70ac1e319909d692110b8654b','Add Column','add retire_reason column to relationship_type table',NULL,'2.0.5'),('200910271049-1','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10264,'EXECUTED','3:2e54d97b9f1b9f35b77cee691c23b7a9','Update Data (x5)','Setting core field types to have standard UUIDs',NULL,'2.0.5'),('200910271049-10','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10273,'EXECUTED','3:827070940f217296c11ce332dc8858ff','Update Data (x4)','Setting core roles to have standard UUIDs',NULL,'2.0.5'),('200910271049-2','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10265,'EXECUTED','3:3132d4cbfaab0c0b612c3fe1c55bd0f1','Update Data (x7)','Setting core person attribute types to have standard UUIDs',NULL,'2.0.5'),('200910271049-3','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10266,'EXECUTED','3:f4d1a9004f91b6885a86419bc02f9d0b','Update Data (x4)','Setting core encounter types to have standard UUIDs',NULL,'2.0.5'),('200910271049-4','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10267,'EXECUTED','3:0d4f7503bf8f00cb73338bb34305333a','Update Data (x12)','Setting core concept datatypes to have standard UUIDs',NULL,'2.0.5'),('200910271049-5','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10268,'EXECUTED','3:98d8ac75977e1b099a4e45d96c6b1d1a','Update Data (x4)','Setting core relationship types to have standard UUIDs',NULL,'2.0.5'),('200910271049-6','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10269,'EXECUTED','3:19355a03794869edad3889ac0adbdedf','Update Data (x15)','Setting core concept classes to have standard UUIDs',NULL,'2.0.5'),('200910271049-7','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10270,'EXECUTED','3:fe4c89654d02d74de6d8e4b265a33288','Update Data (x2)','Setting core patient identifier types to have standard UUIDs',NULL,'2.0.5'),('200910271049-8','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10271,'EXECUTED','3:dc4462b5b4b13c2bc306506848127556','Update Data','Setting core location to have standard UUIDs',NULL,'2.0.5'),('200910271049-9','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10272,'EXECUTED','3:de2a0ed2adafb53f025039e9e8c6719e','Update Data','Setting core hl7 source to have standard UUIDs',NULL,'2.0.5'),('200912031842','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10277,'EXECUTED','3:b966745213bedaeeabab8a874084bb95','Drop Foreign Key Constraint, Add Foreign Key Constraint','Changing encounter.provider_id to reference person instead of users',NULL,'2.0.5'),('200912031846-1','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10279,'MARK_RAN','3:23e728a7f214127cb91efd40ebbcc2d1','Add Column, Update Data','Adding person_id column to users table (if needed)',NULL,'2.0.5'),('200912031846-2','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10280,'MARK_RAN','3:8d57907defa7e92e018038d57cfa78b4','Update Data, Add Not-Null Constraint','Populating users.person_id',NULL,'2.0.5'),('200912031846-3','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10281,'EXECUTED','3:48a50742f2904682caa1bc469f5b87e3','Add Foreign Key Constraint, Set Column as Auto-Increment','Restoring foreign key constraint on users.person_id',NULL,'2.0.5'),('200912071501-1','arthurs','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10274,'EXECUTED','3:d1158b8a42127d7b8a4d5ad64cc7c225','Update Data','Change name for patient.searchMaxResults global property to person.searchMaxResults',NULL,'2.0.5'),('200912091819','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10282,'MARK_RAN','3:8c0b2b02a94b9c6c9529e1b29207464b','Add Column, Add Foreign Key Constraint','Adding retired metadata columns to users table',NULL,'2.0.5'),('200912091819-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10283,'EXECUTED','3:fd5fd1d2e6884662824bb78c8348fadf','Modify Column','(Fixed)users.retired to BOOLEAN',NULL,'2.0.5'),('200912091820','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10284,'MARK_RAN','3:cba73499d1c4d09b0e4ae3b55ecc7d84','Update Data','Migrating voided metadata to retired metadata for users table',NULL,'2.0.5'),('200912091821','djazayeri','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10012,'MARK_RAN','3:9b38d31ebfe427d1f8d6e8530687f29c',NULL,NULL,NULL,NULL),('200912140038','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10285,'MARK_RAN','3:be3aaa8da16b8a8841509faaeff070b4','Add Column','Adding \"uuid\" column to users table',NULL,'2.0.5'),('200912140039','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10286,'EXECUTED','3:5b2a81ac1efba5495962bfb86e51546d','Update Data','Generating UUIDs for all rows in users table via built in uuid function.',NULL,'2.0.5'),('200912140040','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10287,'MARK_RAN','3:c422b96e5b88eeae4f343d4f988cc4b2','Custom Change','Adding UUIDs to users table via a java class. (This will take a long time on large databases)',NULL,'2.0.5'),('200912141000-drug-add-date-changed','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10479,'MARK_RAN','3:9c9a75e3a78104e72de078ac217b0972','Add Column','Add date_changed column to drug table',NULL,'2.0.5'),('200912141001-drug-add-changed-by','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10480,'MARK_RAN','3:196629c722f52df68b5040e5266ac20f','Add Column, Add Foreign Key Constraint','Add changed_by column to drug table',NULL,'2.0.5'),('200912141552','madanmohan','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10275,'MARK_RAN','3:835b6b98a7a437d959255ac666c12759','Add Column, Add Foreign Key Constraint','Add changed_by column to encounter table',NULL,'2.0.5'),('200912141553','madanmohan','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10276,'MARK_RAN','3:7f768aa879beac091501ac9bb47ece4d','Add Column','Add date_changed column to encounter table',NULL,'2.0.5'),('20091215-0208','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10296,'EXECUTED','3:1c818a60d8ebc36f4b7911051c1f6764','Custom SQL','Prune concepts rows orphaned in concept_numeric tables',NULL,'2.0.5'),('20091215-0209','jmiranda','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10297,'EXECUTED','3:adeadc55e4dd484b1d63cf123e299371','Custom SQL','Prune concepts rows orphaned in concept_complex tables',NULL,'2.0.5'),('20091215-0210','jmiranda','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10011,'MARK_RAN','3:08e8550629e4d5938494500f61d10961',NULL,NULL,NULL,NULL),('200912151032','n.nehete','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10289,'EXECUTED','3:d7d8fededde8a27384ca1eb3f87f7914','Add Not-Null Constraint','Encounter Type should not be null when saving an Encounter',NULL,'2.0.5'),('200912211118','nribeka','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10010,'MARK_RAN','3:1f976b4eedf537d887451246d49db043',NULL,NULL,NULL,NULL),('201001072007','upul','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10290,'MARK_RAN','3:d5d60060fae8e9c30843b16b23bed9db','Add Column','Add last execution time column to scheduler_task_config table',NULL,'2.0.5'),('20100111-0111-associating-daemon-user-with-person','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10462,'MARK_RAN','3:bebb5c508bb53e7d5be6fb3aa259bd2f','Custom SQL','Associating daemon user with a person',NULL,'2.0.5'),('20100128-1','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10251,'MARK_RAN','3:eaa1b8e62aa32654480e7a476dc14a4a','Insert Row','Adding \'System Developer\' role again (see ticket #1499)',NULL,'2.0.5'),('20100128-2','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10252,'MARK_RAN','3:3c486c2ea731dfad7905518cac8d6e70','Update Data','Switching users back from \'Administrator\' to \'System Developer\' (see ticket #1499)',NULL,'2.0.5'),('20100128-3','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:56',10253,'MARK_RAN','3:9acf8cae5d210f88006191e79b76532c','Delete Data','Deleting \'Administrator\' role (see ticket #1499)',NULL,'2.0.5'),('20100306-095513a','thilini.hg','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10298,'MARK_RAN','3:b7a60c3c33a05a71dde5a26f35d85851','Drop Foreign Key Constraint','Dropping unused foreign key from notification alert table',NULL,'2.0.5'),('20100306-095513b','thilini.hg','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10299,'MARK_RAN','3:8a6ebb6aefe04b470d5b3878485f9cc3','Drop Column','Dropping unused user_id column from notification alert table',NULL,'2.0.5'),('20100322-0908','syhaas','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10300,'MARK_RAN','3:94a8aae1d463754d7125cd546b4c590c','Add Column, Update Data','Adding sort_weight column to concept_answers table and initially sets the sort_weight to the concept_answer_id',NULL,'2.0.5'),('20100323-192043','ricardosbarbosa','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10317,'EXECUTED','3:c294c84ac7ff884d1e618f4eb74b0c52','Update Data, Delete Data (x2)','Removing the duplicate privilege \'Add Concept Proposal\' in favor of \'Add Concept Proposals\'',NULL,'2.0.5'),('20100330-190413','ricardosbarbosa','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10318,'EXECUTED','3:d706294defdfb73af9b44db7d37069d0','Update Data, Delete Data (x2)','Removing the duplicate privilege \'Edit Concept Proposal\' in favor of \'Edit Concept Proposals\'',NULL,'2.0.5'),('20100412-2217','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10301,'MARK_RAN','3:0c3a3ea15adefa620ab62145f412d0b6','Add Column','Adding \"uuid\" column to notification_alert_recipient table',NULL,'2.0.5'),('20100412-2218','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10302,'EXECUTED','3:6fae383b5548c214d2ad2c76346e32e3','Update Data','Generating UUIDs for all rows in notification_alert_recipient table via built in uuid function.',NULL,'2.0.5'),('20100412-2219','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10303,'MARK_RAN','3:1401fe5f2d0c6bc23afa70b162e15346','Custom Change','Adding UUIDs to notification_alert_recipient table via a java class (if needed).',NULL,'2.0.5'),('20100412-2220','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10304,'EXECUTED','3:bf4474dd5700b570e158ddc8250c470b','Add Not-Null Constraint','Now that UUID generation is done, set the notification_alert_recipient.uuid column to not \"NOT NULL\"',NULL,'2.0.5'),('20100413-1509','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10305,'MARK_RAN','3:7a3ee61077e4dee1ceb4fe127afc835f','Rename Column','Change location_tag.tag to location_tag.name',NULL,'2.0.5'),('20100415-forgotten-from-before','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10288,'EXECUTED','3:d17699fbec80bd035ecb348ae5382754','Add Not-Null Constraint','Adding not null constraint to users.uuid',NULL,'2.0.5'),('20100419-1209','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10385,'MARK_RAN','3:f87b773f9a8e05892fdbe8740042abb5','Create Table, Add Foreign Key Constraint (x7), Create Index','Create the visit table and add the foreign key for visit_type',NULL,'2.0.5'),('20100419-1209-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10386,'EXECUTED','3:cb5970216f918522df3a059e29506c27','Modify Column','(Fixed)Changed visit.voided to BOOLEAN',NULL,'2.0.5'),('20100423-1402','slorenz','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10307,'MARK_RAN','3:3534020f1c68f70b0e9851d47a4874d6','Create Index','Add an index to the encounter.encounter_datetime column to speed up statistical\n			analysis.',NULL,'2.0.5'),('20100423-1406','slorenz','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10308,'MARK_RAN','3:f058162398862f0bdebc12d7eb54551b','Create Index','Add an index to the obs.obs_datetime column to speed up statistical analysis.',NULL,'2.0.5'),('20100426-1111-add-not-null-personid-contraint','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10463,'EXECUTED','3:a0b90b98be85aabbdebd957744ab805a','Add Not-Null Constraint','Add the not null person id contraint',NULL,'2.0.5'),('20100426-1111-remove-not-null-personid-contraint','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10309,'EXECUTED','3:5bc2abe108ab2765e36294ff465c63a0','Drop Not-Null Constraint','Drop the not null person id contraint',NULL,'2.0.5'),('20100426-1947','syhaas','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10310,'MARK_RAN','3:09adbdc9cb72dee82e67080b01d6578e','Insert Row','Adding daemon user to users table',NULL,'2.0.5'),('20100512-1400','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10312,'MARK_RAN','3:0fbfb53e2e194543d7b3eaa59834e1e6','Insert Row','Create core order_type for drug orders',NULL,'2.0.5'),('20100513-1947','syhaas','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10311,'EXECUTED','3:068c2bd55d9c731941fe9ef66f0011fb','Delete Data (x2)','Removing scheduler.username and scheduler.password global properties',NULL,'2.0.5'),('20100517-1545','wyclif and djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10313,'EXECUTED','3:39a68e6b1954a0954d0f8d0c660a7aff','Custom Change','Switch boolean concepts/observations to be stored as coded',NULL,'2.0.5'),('20100525-818-1','syhaas','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10319,'MARK_RAN','3:ed9dcb5bd0d7312db3123825f9bb4347','Create Table, Add Foreign Key Constraint (x2)','Create active list type table.',NULL,'2.0.5'),('20100525-818-1-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10320,'EXECUTED','3:4a648a54797fef2222764a7ee0b5e05a','Modify Column','(Fixed)Change active_list_type.retired to BOOLEAN',NULL,'2.0.5'),('20100525-818-2','syhaas','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10321,'MARK_RAN','3:bc5a86f0245f6f822a0d343b2fcf8dc6','Create Table, Add Foreign Key Constraint (x7)','Create active list table',NULL,'2.0.5'),('20100525-818-2-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10322,'EXECUTED','3:0a2879b368319f6d1e16d0d4417f4492','Modify Column','(Fixed)Change active_list_type.retired to BOOLEAN',NULL,'2.0.5'),('20100525-818-3','syhaas','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10323,'MARK_RAN','3:d382e7b9e23cdcc33ccde2d3f0473c41','Create Table, Add Foreign Key Constraint','Create allergen table',NULL,'2.0.5'),('20100525-818-4','syhaas','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10324,'MARK_RAN','3:1d6f1abd297c8da5a49d4885d0d34dfb','Create Table','Create problem table',NULL,'2.0.5'),('20100525-818-5','syhaas','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10325,'MARK_RAN','3:2ac51b2e8813d61428367bad9fadaa33','Insert Row (x2)','Inserting default active list types',NULL,'2.0.5'),('20100526-1025','Harsha.cse','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10314,'EXECUTED','3:66ec6553564d30fd63df7c2de41c674f','Drop Not-Null Constraint (x2)','Drop Not-Null constraint from location column in Encounter and Obs table',NULL,'2.0.5'),('20100603-1625-1-person_address','sapna','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10366,'MARK_RAN','3:6048aa2c393c1349de55a5003199fb81','Add Column','Adding \"date_changed\" column to person_address table',NULL,'2.0.5'),('20100603-1625-2-person_address','sapna','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10367,'MARK_RAN','3:5194e3b45b70b003e33d7ab0495f3015','Add Column, Add Foreign Key Constraint','Adding \"changed_by\" column to person_address table',NULL,'2.0.5'),('20100604-0933a','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10315,'EXECUTED','3:9b51b236846a8940de581e199cd76cb2','Add Default Value','Changing the default value to 2 for \'message_state\' column in \'hl7_in_archive\' table',NULL,'2.0.5'),('20100604-0933b','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10316,'EXECUTED','3:67fc4c12418b500aaf3723e8845429e3','Update Data','Converting 0 and 1 to 2 for \'message_state\' column in \'hl7_in_archive\' table',NULL,'2.0.5'),('20100607-1550a','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10326,'MARK_RAN','3:bfb6250277efd8c81326fe8c3dbdfe35','Add Column','Adding \'concept_name_type\' column to concept_name table',NULL,'2.0.5'),('20100607-1550b','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10327,'MARK_RAN','3:3d43124d8265fbf05f1ef4839f14bece','Add Column','Adding \'locale_preferred\' column to concept_name table',NULL,'2.0.5'),('20100607-1550b-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10328,'EXECUTED','3:d0dc8dfe3ac629aecee81ccc11dec9c2','Modify Column','(Fixed)Change concept_name.locale_preferred to BOOLEAN',NULL,'2.0.5'),('20100607-1550c','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10329,'EXECUTED','3:b6573617d37609ae7195fd7a495e2776','Drop Foreign Key Constraint','Dropping foreign key constraint on concept_name_tag_map.concept_name_tag_id',NULL,'2.0.5'),('20100607-1550d','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10330,'EXECUTED','3:f30fd17874ac8294389ee2a44ca7d6ab','Update Data, Delete Data (x2)','Setting the concept name type for short names',NULL,'2.0.5'),('20100607-1550f','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10331,'EXECUTED','3:74026cd4543ebbf561999a81c276224d','Update Data, Delete Data (x2)','Converting concept names with \'preferred\' and \'preferred_XX\' concept name tags to preferred names',NULL,'2.0.5'),('20100607-1550g','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10334,'EXECUTED','3:c3c0a17e0a21d36f38bb2af8f0939da7','Delete Data (x2)','Deleting \'default\' and \'synonym\' concept name tags',NULL,'2.0.5'),('20100607-1550h','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10335,'EXECUTED','3:be7b967ed0e7006373bb616b63726144','Custom Change','Validating and attempting to fix invalid concepts and ConceptNames',NULL,'2.0.5'),('20100607-1550i','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:59',10336,'EXECUTED','3:b6260c13bf055f7917c155596502a24b','Add Foreign Key Constraint','Restoring foreign key constraint on concept_name_tag_map.concept_name_tag_id',NULL,'2.0.5'),('20100621-1443','jkeiper','liquibase-update-to-latest.xml','2016-07-04 10:37:59',10337,'EXECUTED','3:16b4bc3512029cf8d3b3c6bee86ed712','Modify Column','Modify the error_details column of hl7_in_error to hold\n			stacktraces',NULL,'2.0.5'),('201008021047','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:59',10338,'MARK_RAN','3:8612ede2553aab53950fa43d2f8def32','Create Index','Add an index to the person_name.family_name2 to speed up patient and person searches',NULL,'2.0.5'),('201008201345','mseaton','liquibase-update-to-latest.xml','2016-07-04 10:37:59',10339,'EXECUTED','3:5fbbb6215e66847c86483ee7177c3682','Custom Change','Validates Program Workflow States for possible configuration problems and reports warnings',NULL,'2.0.5'),('201008242121','misha680','liquibase-update-to-latest.xml','2016-07-04 10:37:59',10340,'EXECUTED','3:2319aed08c4f6dcd43d4ace5cdf94650','Modify Column','Make person_name.person_id not NULLable',NULL,'2.0.5'),('20100924-1110','mseaton','liquibase-update-to-latest.xml','2016-07-04 10:37:59',10341,'MARK_RAN','3:05ea5f3b806ba47f4a749d3a348c59f7','Add Column, Add Foreign Key Constraint','Add location_id column to patient_program table',NULL,'2.0.5'),('201009281047','misha680','liquibase-update-to-latest.xml','2016-07-04 10:37:59',10342,'MARK_RAN','3:02b5b9a183729968cd4189798ca034bd','Drop Column','Remove the now unused default_charge column',NULL,'2.0.5'),('201010051745','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:59',10343,'EXECUTED','3:04ba6f526a71fc0a2b016fd77eaf9ff5','Update Data','Setting the global property \'patient.identifierRegex\' to an empty string',NULL,'2.0.5'),('201010051746','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:37:59',10344,'EXECUTED','3:cb12dfc563d82529de170ffedf948f90','Update Data','Setting the global property \'patient.identifierSuffix\' to an empty string',NULL,'2.0.5'),('201010151054','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:59',10345,'MARK_RAN','3:26c8ae0c53225f82d4c2a85c09ad9785','Create Index','Adding index to form.published column',NULL,'2.0.5'),('201010151055','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:59',10346,'MARK_RAN','3:1efabdfd082ff2b0a34f570831f74ce5','Create Index','Adding index to form.retired column',NULL,'2.0.5'),('201010151056','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10347,'MARK_RAN','3:00273104184bb4d2bb7155befc77efc3','Create Index','Adding multi column index on form.published and form.retired columns',NULL,'2.0.5'),('201010261143','crecabarren','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10348,'MARK_RAN','3:c02de7e2726893f80ecd1f3ae778cba5','Rename Column','Rename neighborhood_cell column to address3 and increase the size to 255 characters',NULL,'2.0.5'),('201010261145','crecabarren','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10349,'MARK_RAN','3:2d053c2e9b604403df8a408a6bb4f3f8','Rename Column','Rename township_division column to address4 and increase the size to 255 characters',NULL,'2.0.5'),('201010261147','crecabarren','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10350,'MARK_RAN','3:592eee2241fdb1039ba08be07b54a422','Rename Column','Rename subregion column to address5 and increase the size to 255 characters',NULL,'2.0.5'),('201010261149','crecabarren','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10351,'MARK_RAN','3:059e5bf4092d930304f9f0fc305939d9','Rename Column','Rename region column to address6 and increase the size to 255 characters',NULL,'2.0.5'),('201010261151','crecabarren','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10352,'MARK_RAN','3:8756b20f505f8981a43ece7233ce3e2f','Rename Column','Rename neighborhood_cell column to address3 and increase the size to 255 characters',NULL,'2.0.5'),('201010261153','crecabarren','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10353,'MARK_RAN','3:9805b9a214fca5a3509a82864274678e','Rename Column','Rename township_division column to address4 and increase the size to 255 characters',NULL,'2.0.5'),('201010261156','crecabarren','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10354,'MARK_RAN','3:894f4e47fbdc74be94e6ebc9d6fce91e','Rename Column','Rename subregion column to address5 and increase the size to 255 characters',NULL,'2.0.5'),('201010261159','crecabarren','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10355,'MARK_RAN','3:b1827790c63813e6a73d83e2b2d36504','Rename Column','Rename region column to address6 and increase the size to 255 characters',NULL,'2.0.5'),('20101029-1016','gobi/prasann','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10368,'MARK_RAN','3:714ad65f5d84bdcd4d944a4d5583e4d3','Create Table, Add Unique Constraint','Create table to store concept stop words to avoid in search key indexing',NULL,'2.0.5'),('20101029-1026','gobi/prasann','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10369,'MARK_RAN','3:83534d43a9a9cc1ea3a80f1d5f5570af','Insert Row (x10)','Inserting the initial concept stop words',NULL,'2.0.5'),('201011011600','jkeiper','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10357,'MARK_RAN','3:29b35d66dc4168e03e1844296e309327','Create Index','Adding index to message_state column in HL7 archive table',NULL,'2.0.5'),('201011011605','jkeiper','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10358,'EXECUTED','3:c604bc0967765f50145f76e80a4bbc99','Custom Change','Moving \"deleted\" HL7s from HL7 archive table to queue table',NULL,'2.0.5'),('201011051300','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10365,'MARK_RAN','3:fea4ad8ce44911eeaab8ac8c1cc9122d','Create Index','Adding index on notification_alert.date_to_expire column',NULL,'2.0.5'),('201012081716','nribeka','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10363,'MARK_RAN','3:4a97a93f2632fc0c3b088b24535ee481','Delete Data','Removing concept that are concept derived and the datatype',NULL,'2.0.5'),('201012081717','nribeka','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10364,'MARK_RAN','3:ad3d0a18bda7e4869d264c70b8cd8d1d','Drop Table','Removing concept derived tables',NULL,'2.0.5'),('20101209-10000-encounter-add-visit-id-column','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10387,'MARK_RAN','3:7045a94731ef25e04724c77fc97494b4','Add Column, Add Foreign Key Constraint','Adding visit_id column to encounter table',NULL,'2.0.5'),('20101209-1353','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10491,'MARK_RAN','3:9d30d1435a6c10a4b135609dc8e925ca','Add Not-Null Constraint','Adding not-null constraint to orders.as_needed',NULL,'2.0.5'),('20101209-1721','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10359,'MARK_RAN','3:351460e0f822555b77acff1a89bec267','Add Column','Add \'weight\' column to concept_word table',NULL,'2.0.5'),('20101209-1722','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10360,'MARK_RAN','3:d63107017bdcef0e28d7ad5e4df21ae5','Create Index','Adding index to concept_word.weight column',NULL,'2.0.5'),('20101209-1723','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10361,'MARK_RAN','3:25d45d7d5bbff4b24bcc8ff8d34d70d2','Insert Row','Insert a row into the schedule_task_config table for the ConceptIndexUpdateTask',NULL,'2.0.5'),('20101209-1731','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10362,'MARK_RAN','3:6de3e859f77856fe939d3ae6a73b4752','Update Data','Setting the value of \'start_on_startup\' to trigger off conceptIndexUpdateTask on startup',NULL,'2.0.5'),('201012092009','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:00',10356,'EXECUTED','3:15a029c4ffe65710a56d402e608d319a','Modify Column (x10)','Increasing length of address fields in person_address and location to 255',NULL,'2.0.5'),('2011-07-12-1947-add-outcomesConcept-to-program','grwarren','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10398,'MARK_RAN','3:ea2bb0a2ddeade662f956ef113d020ab','Add Column, Add Foreign Key Constraint','Adding the outcomesConcept property to Program',NULL,'2.0.5'),('2011-07-12-2005-add-outcome-to-patientprogram','grwarren','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10399,'MARK_RAN','3:57baf47f9b09b3df649742d69be32015','Add Column, Add Foreign Key Constraint','Adding the outcome property to PatientProgram',NULL,'2.0.5'),('201101121434','gbalaji,gobi','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10378,'MARK_RAN','3:96320c51e6e296e9dc65866a61268e45','Drop Column','Dropping unused date_started column from obs table',NULL,'2.0.5'),('201101221453','suho','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10377,'EXECUTED','3:4088d4906026cc1430fa98e04d294b13','Modify Column','Increasing the serialized_data column of serialized_object to hold mediumtext',NULL,'2.0.5'),('20110124-1030','surangak','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10380,'MARK_RAN','3:e17eee5b8c4bb236a0ea6e6ade5abed7','Add Foreign Key Constraint','Adding correct foreign key for concept_answer.answer_drug',NULL,'2.0.5'),('20110125-1435','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10371,'MARK_RAN','3:dadd9da1dad5f2863f8f6bb24b29d598','Add Column','Adding \'start_date\' column to person_address table',NULL,'2.0.5'),('20110125-1436','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10372,'MARK_RAN','3:68cec89409d2419fe9439f4753a23036','Add Column','Adding \'end_date\' column to person_address table',NULL,'2.0.5'),('201101271456-add-enddate-to-relationship','misha680','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10389,'MARK_RAN','3:b593b864d4a870e3b7ba6b61fda57c8d','Add Column','Adding the end_date column to relationship.',NULL,'2.0.5'),('201101271456-add-startdate-to-relationship','misha680','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10388,'MARK_RAN','3:82020a9f33747f58274196619439781e','Add Column','Adding the start_date column to relationship.',NULL,'2.0.5'),('20110201-1625-1','arahulkmit','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10373,'MARK_RAN','3:4f1b23efba67de1917e312942fe7e744','Add Column','Adding \"date_changed\" column to patient_identifier table',NULL,'2.0.5'),('20110201-1625-2','arahulkmit','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10374,'MARK_RAN','3:01467a1db56ef3db87dc537d40ab22eb','Add Column, Add Foreign Key Constraint','Adding \"changed_by\" column to patient_identifier table',NULL,'2.0.5'),('20110201-1626-1','arahulkmit','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10375,'MARK_RAN','3:63397ce933d1c78309648425fba66a17','Add Column','Adding \"date_changed\" column to relationship table',NULL,'2.0.5'),('20110201-1626-2','arahulkmit','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10376,'MARK_RAN','3:21dae026e42d05b2ebc8fe51408c147f','Add Column, Add Foreign Key Constraint','Adding \"changed_by\" column to relationship table',NULL,'2.0.5'),('201102081800','gbalaji,gobi','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10379,'MARK_RAN','3:779ca58f39b4e3a14a313f8fc416c242','Drop Column','Dropping unused date_stopped column from obs table',NULL,'2.0.5'),('20110218-1206','rubailly','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10013,'MARK_RAN','3:8be61726cd3fed87215557efd284434f',NULL,NULL,NULL,NULL),('20110218-1210','rubailly','liquibase-update-to-latest.xml','2011-09-15 00:00:00',10013,'MARK_RAN','3:4f8818ba08f3a9ce2e2ededfdf5b6fcd',NULL,NULL,NULL,NULL),('201102280948','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:37:57',10278,'EXECUTED','3:98e1075808582c97377651d02faf8f46','Drop Foreign Key Constraint','Removing the foreign key from users.user_id to person.person_id if it still exists',NULL,'2.0.5'),('20110301-1030a','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10413,'MARK_RAN','3:5256e8010fb4c375e2a1ef502176cc2f','Rename Table','Renaming the concept_source table to concept_reference_source',NULL,'2.0.5'),('20110301-1030b','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10414,'MARK_RAN','3:6fc5f514cd9c2ee14481a7f0b10a0c7c','Create Table, Add Foreign Key Constraint (x4)','Adding concept_reference_term table',NULL,'2.0.5'),('20110301-1030b-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10415,'EXECUTED','3:3cf3ba141e6571b900e695b49b6c48a9','Modify Column','(Fixed)Change concept_reference_term.retired to BOOLEAN',NULL,'2.0.5'),('20110301-1030c','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10416,'MARK_RAN','3:d8407baf728a1db5ad5db7c138cb59cb','Create Table, Add Foreign Key Constraint (x3)','Adding concept_map_type table',NULL,'2.0.5'),('20110301-1030c-fix','sunbiz','liquibase-update-to-latest.xml','2011-09-19 00:00:00',10014,'MARK_RAN','3:c02f2825633f1a43fc9303ac21ba2c02',NULL,NULL,NULL,NULL),('20110301-1030d','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10417,'MARK_RAN','3:222ef47c65625a17c268a8f68edaa16e','Rename Table','Renaming the concept_map table to concept_reference_map',NULL,'2.0.5'),('20110301-1030e','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10418,'MARK_RAN','3:50be921cf53ce4a357afc0bac8928495','Add Column','Adding concept_reference_term_id column to concept_reference_map table',NULL,'2.0.5'),('20110301-1030f','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10419,'MARK_RAN','3:5faead5506cbcde69490fef985711d66','Custom Change','Inserting core concept map types',NULL,'2.0.5'),('20110301-1030g','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10420,'MARK_RAN','3:affc4d2a4e3143046cfb75b583c7399a','Add Column, Add Foreign Key Constraint','Adding concept_map_type_id column and a foreign key constraint to concept_reference_map table',NULL,'2.0.5'),('20110301-1030h','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10421,'MARK_RAN','3:4bf584dc7b25a180cc82edb56e1b0e5b','Add Column, Add Foreign Key Constraint','Adding changed_by column and a foreign key constraint to concept_reference_map table',NULL,'2.0.5'),('20110301-1030i','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10422,'MARK_RAN','3:f4d0468db79007d0355f6f461603b2f7','Add Column','Adding date_changed column and a foreign key constraint to concept_reference_map table',NULL,'2.0.5'),('20110301-1030j','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10423,'MARK_RAN','3:a7dc8b89e37fe36263072b43670d7f11','Create Table, Add Foreign Key Constraint (x5)','Adding concept_reference_term_map table',NULL,'2.0.5'),('20110301-1030m','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10424,'MARK_RAN','3:b286407bfcdf3853512cb15009c816f1','Custom Change','Creating concept reference terms from existing rows in the concept_reference_map table',NULL,'2.0.5'),('20110301-1030n','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10425,'MARK_RAN','3:01868c1383e5c9c409282b50e67e878c','Add Foreign Key Constraint','Adding foreign key constraint to concept_reference_map.concept_reference_term_id column',NULL,'2.0.5'),('20110301-1030o','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10426,'MARK_RAN','3:eea9343959864edea569d5a2a2358469','Drop Foreign Key Constraint','Dropping foreign key constraint on concept_reference_map.source column',NULL,'2.0.5'),('20110301-1030p','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10427,'MARK_RAN','3:01bf8c07a05f22df2286a4ee27a7acb4','Drop Column','Dropping concept_reference_map.source column',NULL,'2.0.5'),('20110301-1030q','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10428,'MARK_RAN','3:f45caaf1c7daa7f2cb036f46a20aa4b1','Drop Column','Dropping concept_reference_map.source_code column',NULL,'2.0.5'),('20110301-1030r','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10429,'MARK_RAN','3:23fd6bc96ee0a497cf330ed24ec0075b','Drop Column','Dropping concept_reference_map.comment column',NULL,'2.0.5'),('201103011751','abbas','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10381,'EXECUTED','3:4857dcbefa75784da912bca5caba21b5','Create Table, Add Foreign Key Constraint (x3)','Create the person_merge_log table',NULL,'2.0.5'),('20110326-1','Knoll_Frank','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10456,'EXECUTED','3:3376a34edf88bf2868fd75ba2fb0f6c3','Add Column, Add Foreign Key Constraint','Add obs.previous_version column (TRUNK-420)',NULL,'2.0.5'),('20110326-2','Knoll_Frank','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10459,'EXECUTED','3:7c068bfe918b9d87fefa9f8508e92f58','Custom SQL','Fix all the old void_reason content and add in the new previous_version to the matching obs row (POTENTIALLY VERY SLOW FOR LARGE OBS TABLES)',NULL,'2.0.5'),('20110329-2317','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10382,'EXECUTED','3:371be45e2a3616ce17b6f50862ca196d','Delete Data','Removing \'View Encounters\' privilege from Anonymous user',NULL,'2.0.5'),('20110329-2318','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10383,'EXECUTED','3:eb2ece117d8508e843d11eeed7676b21','Delete Data','Removing \'View Observations\' privilege from Anonymous user',NULL,'2.0.5'),('20110425-1600-create-visit-attribute-type-table','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10390,'MARK_RAN','3:3cf419ea9657f9a072881cafb2543d77','Create Table, Add Foreign Key Constraint (x3)','Creating visit_attribute_type table',NULL,'2.0.5'),('20110425-1600-create-visit-attribute-type-table-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10391,'EXECUTED','3:e4b62b99750c9ee4c213a7bc3101f8a6','Modify Column','(Fixed)Change visit_attribute_type.retired to BOOLEAN',NULL,'2.0.5'),('20110425-1700-create-visit-attribute-table','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10393,'MARK_RAN','3:24e1e30a41f9f5d92f337444fb45402a','Create Table, Add Foreign Key Constraint (x5)','Creating visit_attribute table',NULL,'2.0.5'),('20110425-1700-create-visit-attribute-table-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10394,'EXECUTED','3:8ab9102da66058c326c0a5089de053e8','Modify Column','(Fixed)Change visit_attribute.voided to BOOLEAN',NULL,'2.0.5'),('20110426-11701','zabil','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10435,'MARK_RAN','3:56caae006a3af14242e2ea57627004c7','Create Table, Add Foreign Key Constraint (x4)','Create provider table',NULL,'2.0.5'),('20110426-11701-create-provider-table','dkayiwa','liquibase-schema-only.xml','2016-07-04 10:37:32',87,'EXECUTED','3:56caae006a3af14242e2ea57627004c7','Create Table, Add Foreign Key Constraint (x4)','Create provider table',NULL,'2.0.5'),('20110426-11701-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10436,'EXECUTED','3:f222ec7d41ce0255c667fd79b70bffd2','Modify Column','(Fixed)Change provider.retired to BOOLEAN',NULL,'2.0.5'),('20110510-11702-create-provider-attribute-type-table','zabil','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10437,'EXECUTED','3:7478ac84804d46a4f2b3daa63efe99be','Create Table, Add Foreign Key Constraint (x3)','Creating provider_attribute_type table',NULL,'2.0.5'),('20110510-11702-create-provider-attribute-type-table-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10438,'EXECUTED','3:479636c7572a649889527f670eaff533','Modify Column','(Fixed)Change provider_attribute_type.retired to BOOLEAN',NULL,'2.0.5'),('20110628-1400-create-provider-attribute-table','kishoreyekkanti','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10440,'EXECUTED','3:298aaacafd48547be294f4c9b7c40d35','Create Table, Add Foreign Key Constraint (x5)','Creating provider_attribute table',NULL,'2.0.5'),('20110628-1400-create-provider-attribute-table-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10441,'EXECUTED','3:14d85967e968d0bcd7a49ddeb6f3e540','Modify Column','(Fixed)Change provider_attribute.voided to BOOLEAN',NULL,'2.0.5'),('20110705-2300-create-encounter-role-table','kishoreyekkanti','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10442,'MARK_RAN','3:a381ef81f10e4f7443b4d4c8d6231de8','Create Table, Add Foreign Key Constraint (x3)','Creating encounter_role table',NULL,'2.0.5'),('20110705-2300-create-encounter-role-table-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10443,'EXECUTED','3:bed2af9d6c3d49eacbdaf2174e682671','Modify Column','(Fixed)Change encounter_role.retired to BOOLEAN',NULL,'2.0.5'),('20110705-2311-create-encounter-role-table','dkayiwa','liquibase-schema-only.xml','2016-07-04 10:37:32',88,'EXECUTED','3:a381ef81f10e4f7443b4d4c8d6231de8','Create Table, Add Foreign Key Constraint (x3)','Creating encounter_role table',NULL,'2.0.5'),('20110708-2105','cta','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10397,'MARK_RAN','3:a20e9bb27a1aca73a646ad81ef2b9deb','Add Unique Constraint','Add unique constraint to the concept_source table',NULL,'2.0.5'),('201107192313-change-length-of-regex-column','jtellez','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10395,'EXECUTED','3:db001544cc0f5a1ff42524a9292b028b','Modify Column','Increasing maximum length of patient identifier type regex format',NULL,'2.0.5'),('20110811-1205-create-encounter-provider-table','sree/vishnu','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10444,'EXECUTED','3:e20ca5412e37df98c58a39552aafb5ad','Create Table, Add Foreign Key Constraint (x3)','Creating encounter_provider table',NULL,'2.0.5'),('20110811-1205-create-encounter-provider-table-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10445,'EXECUTED','3:8decefa15168e68297f5f2782991c552','Modify Column','(Fixed)Change encounter_provider.voided to BOOLEAN',NULL,'2.0.5'),('20110817-1544-create-location-attribute-type-table','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10400,'MARK_RAN','3:41fa30c01ec2d1107beccb8126146464','Create Table, Add Foreign Key Constraint (x3)','Creating location_attribute_type table',NULL,'2.0.5'),('20110817-1544-create-location-attribute-type-table-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10401,'EXECUTED','3:53aff6217c6a9a8f1ca414703b1a8720','Modify Column','(Fixed)Change visit_attribute.retired to BOOLEAN',NULL,'2.0.5'),('20110817-1601-create-location-attribute-table','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10403,'MARK_RAN','3:c7cb1b35d68451d10badeb445df599b9','Create Table, Add Foreign Key Constraint (x5)','Creating location_attribute table',NULL,'2.0.5'),('20110817-1601-create-location-attribute-table-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10404,'EXECUTED','3:2450e230f3eda291203485bca6904377','Modify Column','(Fixed)Change visit_attribute.retired to BOOLEAN',NULL,'2.0.5'),('20110819-1455-insert-unknown-encounter-role','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10446,'EXECUTED','3:bfe0b994a3c0a62d0d4c8f7d941991c7','Insert Row','Inserting the unknown encounter role into the encounter_role table',NULL,'2.0.5'),('20110825-1000-creating-providers-for-persons-from-encounter','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10447,'EXECUTED','3:04bc8aa9859f6f8dda065e272ba12e0d','Custom SQL','Creating providers for persons from the encounter table',NULL,'2.0.5'),('20110825-1000-drop-provider-id-column-from-encounter-table','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10449,'EXECUTED','3:2137e4b5198aa5f12059ee0e8837fb04','Drop Foreign Key Constraint, Drop Column','Dropping the provider_id column from the encounter table',NULL,'2.0.5'),('20110825-1000-migrating-providers-to-encounter-provider','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10448,'EXECUTED','3:e7c39080453e862d5a4013c48c9225fc','Custom SQL','Migrating providers from the encounter table to the encounter_provider table',NULL,'2.0.5'),('2011091-0749','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',125,'EXECUTED','3:3534020f1c68f70b0e9851d47a4874d6','Create Index','',NULL,'2.0.5'),('2011091-0750','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',126,'EXECUTED','3:f058162398862f0bdebc12d7eb54551b','Create Index','',NULL,'2.0.5'),('20110913-0300','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10430,'MARK_RAN','3:7ad8f362e4cc6df6e37135cc37546d0d','Drop Foreign Key Constraint, Add Foreign Key Constraint','Remove ON DELETE CASCADE from relationship table for person_a',NULL,'2.0.5'),('20110913-0300b','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10431,'MARK_RAN','3:2486028ce670bdea2a5ced509a335170','Drop Foreign Key Constraint, Add Foreign Key Constraint','Remove ON DELETE CASCADE from relationship table for person_b',NULL,'2.0.5'),('20110914-0104','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:35',317,'EXECUTED','3:b1811e5e43321192b275d6e2fe2fa564','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110914-0114','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',69,'EXECUTED','3:dac2ff60a4f99315d68948e9582af011','Create Table','',NULL,'2.0.5'),('20110914-0117','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:35',318,'EXECUTED','3:5b7f746286a955da60c9fec8d663a0e3','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110914-0245','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:35',319,'EXECUTED','3:48cdf2b28fcad687072ac8133e46cba6','Add Unique Constraint','',NULL,'2.0.5'),('20110914-0306','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',70,'EXECUTED','3:037f98fda886cde764171990d168e97d','Create Table','',NULL,'2.0.5'),('20110914-0308','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:35',320,'EXECUTED','3:6309ad633777b0faf1d9fa394699a789','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110914-0310','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',321,'EXECUTED','3:8c53c44af44d75aadf6cedfc9d13ded1','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110914-0312','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',71,'EXECUTED','3:2a39901427c9e7b84c8578ff7b3099bb','Create Table','',NULL,'2.0.5'),('20110914-0314','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',322,'EXECUTED','3:9cbe2e14482f88864f94d5e630a88b62','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110914-0315','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',323,'EXECUTED','3:18cd917d56887ad924dad367470a8461','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110914-0317','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',98,'EXECUTED','3:cffbf258ca090d095401957df4168175','Add Primary Key','',NULL,'2.0.5'),('20110914-0321','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',324,'EXECUTED','3:67723ac8a4583366b78c9edc413f89eb','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110914-0434','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',326,'EXECUTED','3:081831e316a82683102f298a91116e92','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110914-0435','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',327,'EXECUTED','3:03fa6c6a37a61480c95d5b75e30d4846','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110914-0448','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',72,'EXECUTED','3:ffa1ef2b17d77f87dccbdea0c51249de','Create Table','',NULL,'2.0.5'),('20110914-0453','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',325,'EXECUTED','3:ea43c7690888a7fd47aa7ba39f8006e2','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110914-0509','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',122,'EXECUTED','3:d29884c3ef8fd867c3c2ffbd557c14c2','Create Index','',NULL,'2.0.5'),('20110914-0943','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',123,'EXECUTED','3:c48f2441d83f121db30399d9cd5f7f8b','Create Index','',NULL,'2.0.5'),('20110914-0945','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',328,'EXECUTED','3:ea1fbb819a84a853b4a97f93bd5b8600','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110914-0956','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',124,'EXECUTED','3:719aa7e4120c11889d91214196acfd4c','Create Index','',NULL,'2.0.5'),('20110914-0958','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',329,'EXECUTED','3:ad98b3c7ae60001d0e0a7b927177fb72','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0258','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',330,'EXECUTED','3:bd7731e58f3db9b944905597a08eb6cb','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0259','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',331,'EXECUTED','3:11086a37155507c0238c9532f66b172b','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0357','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',332,'EXECUTED','3:05d531e66cbc42e1eb2d42c8bcf20bc8','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0547','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',333,'EXECUTED','3:f3b0fc223476060082626b3849ee20ad','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0552','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',334,'EXECUTED','3:46e5067fb13cefd224451b25abbd03ae','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0603','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',335,'EXECUTED','3:ca4f567e4d75ede0553e8b32012e4141','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0610','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',336,'EXECUTED','3:d6c6a22571e304640b2ff1be52c76977','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0634','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',337,'EXECUTED','3:c6dd75893e5573baa0c7426ecccaa92d','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0751','sunbiz','liquibase-core-data.xml','2016-07-04 10:37:37',10029,'EXECUTED','3:010949e257976520a6e8c87e419c9435','Insert Row','',NULL,'2.0.5'),('20110915-0803','sunbiz','liquibase-core-data.xml','2016-07-04 10:37:37',10036,'EXECUTED','3:4a09e1959df71d38fa77b249bf032edc','Insert Row','',NULL,'2.0.5'),('20110915-0823','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',338,'EXECUTED','3:beb831615b748a06a8b21dcaeba8c40d','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0824','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',339,'EXECUTED','3:90f1a69f5cae1d2b3b3a2fa8cb1bace2','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0825','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',74,'EXECUTED','3:17eab4b1c4c36b54d8cf8ca26083105c','Create Table','',NULL,'2.0.5'),('20110915-0836','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',340,'EXECUTED','3:53f76b5f2c20d5940518a1b14ebab33e','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0837','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',341,'EXECUTED','3:936ecde7ac26efdd1a4c29260183609c','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0838','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',342,'EXECUTED','3:fc1e68e753194b2f83e014daa0f7cb3e','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0839','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',343,'EXECUTED','3:90bfb3d0edfcfc8091a2ffd943a54e88','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0840','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',344,'EXECUTED','3:9af8eca0bc6b58c3816f871d9f6d5af8','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0841','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',345,'EXECUTED','3:2ca812616a13bac6b0463bf26b9a0fe3','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0842','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',346,'EXECUTED','3:4fd619ffdedac0cf141a7dd1b6e92f9b','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0845','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',75,'EXECUTED','3:4e799d7e5a15e823116caa01ab7ed808','Create Table','',NULL,'2.0.5'),('20110915-0846','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',347,'EXECUTED','3:a41f6272aa79f3259ba24f0a31c51e72','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-0847','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',76,'EXECUTED','3:8c1e49cd3d6402648ee7732ba9948785','Create Table','',NULL,'2.0.5'),('20110915-0848','sunbiz','liquibase-core-data.xml','2016-07-04 10:37:37',10037,'EXECUTED','3:cf7989886ae2624508fdf64b7b656727','Insert Row (x2)','',NULL,'2.0.5'),('20110915-0848','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',77,'EXECUTED','3:071de39e44036bd8adb2b24b011b7369','Create Table','',NULL,'2.0.5'),('20110915-0903','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',348,'EXECUTED','3:b6260c13bf055f7917c155596502a24b','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1045','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',127,'EXECUTED','3:8612ede2553aab53950fa43d2f8def32','Create Index','',NULL,'2.0.5'),('20110915-1049','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',349,'EXECUTED','3:b71f1caa3d14aa6282ef58e2a002f999','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1051','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',128,'EXECUTED','3:26c8ae0c53225f82d4c2a85c09ad9785','Create Index','',NULL,'2.0.5'),('20110915-1052','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',129,'EXECUTED','3:1efabdfd082ff2b0a34f570831f74ce5','Create Index','',NULL,'2.0.5'),('20110915-1053','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',130,'EXECUTED','3:00273104184bb4d2bb7155befc77efc3','Create Index','',NULL,'2.0.5'),('20110915-1103','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',131,'EXECUTED','3:29b35d66dc4168e03e1844296e309327','Create Index','',NULL,'2.0.5'),('20110915-1104','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',132,'EXECUTED','3:d63107017bdcef0e28d7ad5e4df21ae5','Create Index','',NULL,'2.0.5'),('20110915-1107','sunbiz','liquibase-core-data.xml','2016-07-04 10:37:37',10038,'EXECUTED','3:18eb4edef88534b45b384e6bc3ccce75','Insert Row','',NULL,'2.0.5'),('20110915-1133','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',133,'EXECUTED','3:fea4ad8ce44911eeaab8ac8c1cc9122d','Create Index','',NULL,'2.0.5'),('20110915-1135','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',350,'EXECUTED','3:f0bc11508a871044f5a572b7f8103d52','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1148','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',351,'EXECUTED','3:a5ef601dc184a85e988eded2f1f82dcb','Add Unique Constraint','',NULL,'2.0.5'),('20110915-1149','sunbiz','liquibase-core-data.xml','2016-07-04 10:37:37',10039,'EXECUTED','3:83534d43a9a9cc1ea3a80f1d5f5570af','Insert Row (x10)','',NULL,'2.0.5'),('20110915-1202','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',352,'EXECUTED','3:2c58f7f1e2450c60898bffe6933c9b34','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1203','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',353,'EXECUTED','3:5bce62082a32d3624854a198d3fa35b7','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1210','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',354,'EXECUTED','3:e17eee5b8c4bb236a0ea6e6ade5abed7','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1215','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',73,'EXECUTED','3:d772a6a8adedbb1c012dac58ffb221c3','Create Table','',NULL,'2.0.5'),('20110915-1222','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',78,'EXECUTED','3:25ce4e3219f2b8c85e06d47dfc097382','Create Table','',NULL,'2.0.5'),('20110915-1225','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',355,'EXECUTED','3:2d4f77176fd59955ff719c46ae8b0cfc','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1226','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',356,'EXECUTED','3:66155de3997745548dbca510649cd09d','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1227','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',357,'EXECUTED','3:6700b07595d6060269b86903d08bb2a5','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1231','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',79,'EXECUTED','3:e9f6104a25d8b37146b27e568b6e3d3f','Create Table','',NULL,'2.0.5'),('20110915-1240','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',358,'EXECUTED','3:5a30b62738cf57a4804310add8f71b6a','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1241','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',359,'EXECUTED','3:a48aa09c19549e43fc538a70380ae61f','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1242','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',360,'EXECUTED','3:e0e23621fabe23f3f04c4d13105d528c','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1243','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',361,'EXECUTED','3:1d15d848cefc39090e90f3ea78f3cedc','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1244','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',362,'EXECUTED','3:6c5b2018afd741a3c7e39c563212df57','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1245','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',363,'EXECUTED','3:9b5b112797deb6eddc9f0fc01254e378','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1246','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',364,'EXECUTED','3:290a8c07c70dd6a5fe85be2d747ff0d8','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1247','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:33',134,'EXECUTED','3:0644f13c7f4bb764d3b17ad160bd8d41','Create Index','',NULL,'2.0.5'),('20110915-1248','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',365,'EXECUTED','3:5b42d27a7c7edfeb021e1dcfed0f33b3','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1258','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',80,'EXECUTED','3:07687ca4ba9b942a862a41dd9026bc9d','Create Table','',NULL,'2.0.5'),('20110915-1301','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',366,'EXECUTED','3:ef3a47a3fdd809ef4269e9643add2abd','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1302','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',367,'EXECUTED','3:e36c12350ebfbd624bdc6a6599410c85','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1303','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',368,'EXECUTED','3:5917c5e09a3f6077b728a576cd9bacb3','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1307','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',81,'EXECUTED','3:957d888738541ed76dda53e222079fa3','Create Table','',NULL,'2.0.5'),('20110915-1311','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',392,'EXECUTED','3:e88c86892fafb2f897f72a85c66954c0','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1312','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',369,'EXECUTED','3:fe2641c56b27b429c1c4a150e1b9af18','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1313','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',370,'EXECUTED','3:5c7ab96d3967d1ce4e00ebe23f4c4f6e','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1314','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',371,'EXECUTED','3:22902323fcd541f18ca0cb4f38299cb4','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1315','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',372,'EXECUTED','3:dd0d198da3d5d01f93d9acc23e89d51c','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1316','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',373,'EXECUTED','3:f22027f3fc0b1a3a826dc5d810fcd936','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1317','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',374,'EXECUTED','3:68aa00c9f2faa61031d0b4544f4cb31b','Add Unique Constraint','',NULL,'2.0.5'),('20110915-1320','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',375,'EXECUTED','3:5d6a55ee33c33414cccc8b46776a36a4','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1323','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',376,'EXECUTED','3:4c3b84570d45b23d363f6ee76acd966f','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1325','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',82,'EXECUTED','3:0813953451c461376a6ab5a13e4654dd','Create Table','',NULL,'2.0.5'),('20110915-1327','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',377,'EXECUTED','3:3c8aaca28033c8a01e4bceb7421f8e8e','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1328','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',378,'EXECUTED','3:05b6e994f2a09b23826264d31f275b5e','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1329','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',379,'EXECUTED','3:40729ae012b9ed8bd55439b233ec10cc','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1337','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',83,'EXECUTED','3:06fd47a34713fad9678463bba9675496','Create Table','',NULL,'2.0.5'),('20110915-1342','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',380,'EXECUTED','3:bb52caf0ec6e80e24d6fc0c7f2c95631','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1343','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',381,'EXECUTED','3:b36c3436facfe7c9371f7780ebb8701d','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1344','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',382,'EXECUTED','3:010fa7bc125bcb8caa320d38a38a7e3f','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1345','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',383,'EXECUTED','3:e3cdd84f2e6632a4dd8c526cf9ff476e','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1346','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',384,'EXECUTED','3:7f6420b23addd5b33320e04adbc134a3','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1435','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',84,'EXECUTED','3:511f99d7cb13e5fc1112ccb4633e0e45','Create Table','',NULL,'2.0.5'),('20110915-1440','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',385,'EXECUTED','3:2cb254be6daeeebb74fc0e1d64728a62','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1441','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',386,'EXECUTED','3:8bd11d5102eff3b52b1d925e44627a48','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1442','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',387,'EXECUTED','3:4cf7afc33839c19f830e996e8546ea72','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1443','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',388,'EXECUTED','3:cf41f73f64c11150062b2e2254a56908','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1450','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',85,'EXECUTED','3:f9348bf7337d32ebbf98545857b5c8cc','Create Table','',NULL,'2.0.5'),('20110915-1451','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',389,'EXECUTED','3:d98c8bdaacf99764ab3319db03b48542','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1452','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',390,'EXECUTED','3:3a2e67fd1f0215b49711e7e8dccd370d','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1453','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',391,'EXECUTED','3:6b1b7fb75fedc196cf833f04e216b9b2','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1459','sunbiz','liquibase-core-data.xml','2016-07-04 10:37:37',10040,'EXECUTED','3:5faead5506cbcde69490fef985711d66','Custom Change','Inserting core concept map types',NULL,'2.0.5'),('20110915-1524','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',393,'EXECUTED','3:8d609018e78b744ce30e8907ead0bec0','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1528','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:32',86,'EXECUTED','3:e8a5555a214d7bb6f17eb2466f59d12b','Create Table','',NULL,'2.0.5'),('20110915-1530','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',394,'EXECUTED','3:ddc26a0bb350b6c744ed6ff813b5c108','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1531','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',395,'EXECUTED','3:e9fa5722ba00d9b55d813f0fc8e5f9f9','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1532','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',396,'EXECUTED','3:72f0f61a12a3eead113be1fdcabadb6f','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1533','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',397,'EXECUTED','3:0430d8eecce280786a66713abd0b3439','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1534','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:36',398,'EXECUTED','3:21b6cde828dbe885059ea714cda4f470','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1536','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:37',399,'EXECUTED','3:01868c1383e5c9c409282b50e67e878c','Add Foreign Key Constraint','',NULL,'2.0.5'),('20110915-1700','sunbiz','liquibase-schema-only.xml','2016-07-04 10:37:37',402,'EXECUTED','3:ba5b74aeacacec55a49d31074b7e5023','Insert Row (x18)','',NULL,'2.0.5'),('201109152336','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10433,'MARK_RAN','3:a84f855a1db7201e08900f8c7a3d7c5f','Update Data','Updating logging level global property',NULL,'2.0.5'),('20110919-0638','sunbiz','liquibase-update-to-latest.xml','2011-09-19 00:00:00',10015,'MARK_RAN','3:5e540b763c3a16e9d37aa6423b7f798f',NULL,NULL,NULL,NULL),('20110919-0639-void_empty_attributes','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10434,'EXECUTED','3:ccdbab987b09073fc146f3a4a5a9aee4','Custom SQL','Void all attributes that have empty string values.',NULL,'2.0.5'),('20110922-0551','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10306,'MARK_RAN','3:ab9b55e5104645690a4e1c5e35124258','Modify Column','Changing global_property.property from varbinary to varchar',NULL,'2.0.5'),('20110926-1200','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10396,'MARK_RAN','3:bf884233110a210b6ffcef826093cf9d','Custom SQL','Change all empty concept_source.hl7_code to NULL',NULL,'2.0.5'),('201109301703','suho','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10405,'MARK_RAN','3:11456d3e6867f3b521fb35e6f51ebe5a','Update Data','Converting general address format (if applicable)',NULL,'2.0.5'),('201109301704','suho','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10406,'MARK_RAN','3:d64afe121c9355f6bbe46258876ce759','Update Data','Converting Spain address format (if applicable)',NULL,'2.0.5'),('201109301705','suho','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10407,'MARK_RAN','3:d3b0c8265ee27456dc0491ff5fe8ca01','Update Data','Converting Rwanda address format (if applicable)',NULL,'2.0.5'),('201109301706','suho','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10408,'MARK_RAN','3:17d3a0900ca751d8ce775a12444c75bf','Update Data','Converting USA address format (if applicable)',NULL,'2.0.5'),('201109301707','suho','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10409,'MARK_RAN','3:afbd6428d0007325426f3c4446de2e38','Update Data','Converting Kenya address format (if applicable)',NULL,'2.0.5'),('201109301708','suho','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10410,'MARK_RAN','3:570c9234597b477e4feffbaac0469495','Update Data','Converting Lesotho address format (if applicable)',NULL,'2.0.5'),('201109301709','suho','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10411,'MARK_RAN','3:20c95ae336f437b4e0c91be5919b7a2b','Update Data','Converting Malawi address format (if applicable)',NULL,'2.0.5'),('201109301710','suho','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10412,'MARK_RAN','3:b06d71b4c220c7feed9c5a6459bea98a','Update Data','Converting Tanzania address format (if applicable)',NULL,'2.0.5'),('201110051353-fix-visit-attribute-type-columns','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10392,'MARK_RAN','3:d779b41ab27dca879d593aa606016bf6','Add Column (x2)','Refactoring visit_attribute_type table (devs only)',NULL,'2.0.5'),('201110072042-fix-location-attribute-type-columns','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10402,'MARK_RAN','3:2e32ce0f25391341c8855604f4f40654','Add Column (x2)','Refactoring location_attribute_type table (devs only)',NULL,'2.0.5'),('201110072043-fix-provider-attribute-type-columns','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:02',10439,'MARK_RAN','3:31aa196adfe1689c1098c5f36d490902','Add Column (x2)','Refactoring provider_attribute_type table (devs only)',NULL,'2.0.5'),('20111008-0938-1','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10450,'EXECUTED','3:fe6d462ba1a7bd81f4865e472cc223ce','Add Column','Allow Global Properties to be typed',NULL,'2.0.5'),('20111008-0938-2','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10451,'EXECUTED','3:f831d92c11eb6cd6b334d86160db0b95','Add Column','Allow Global Properties to be typed',NULL,'2.0.5'),('20111008-0938-3','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10452,'EXECUTED','3:f7bd79dfed90d56053dc376b6b8ee7e3','Add Column','Allow Global Properties to be typed',NULL,'2.0.5'),('20111008-0938-4','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10453,'EXECUTED','3:65003bd1bf99ff0aa8e2947978c58053','Add Column','Allow Global Properties to be typed',NULL,'2.0.5'),('201110091820-a','jkeiper','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10454,'MARK_RAN','3:364a0c70d2adbff31babab6f60ed72e7','Add Column','Add xslt column back to the form table',NULL,'2.0.5'),('201110091820-b','jkeiper','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10455,'MARK_RAN','3:0b792bf39452f2e81e502a7a98f9f3df','Add Column','Add template column back to the form table',NULL,'2.0.5'),('201110091820-c','jkeiper','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10457,'MARK_RAN','3:f71680d95ecf870619671fb7f416e457','Rename Table','Rename form_resource table to preserve data; 20111010-1515 reference is for bleeding-edge developers and can be generally ignored',NULL,'2.0.5'),('20111010-1515','jkeiper','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10458,'EXECUTED','3:3ccdc9a3ecf811382a0c12825c0aeeb3','Create Table, Add Foreign Key Constraint, Add Unique Constraint','Creating form_resource table',NULL,'2.0.5'),('20111128-1601','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10460,'EXECUTED','3:12fa4687d149a2f17251e546d47369d6','Insert Row','Inserting Auto Close Visits Task into \'schedule_task_config\' table',NULL,'2.0.5'),('20111209-1400-deleting-non-existing-roles-from-role-role-table','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10461,'EXECUTED','3:3d74c1dd987a12d916218d68032d726d','Custom SQL','Deleting non-existing roles from the role_role table',NULL,'2.0.5'),('20111214-1500-setting-super-user-gender','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10464,'EXECUTED','3:2c281abfe7beb51983db13c187c072f3','Custom SQL','Setting super user gender',NULL,'2.0.5'),('20111218-1830','abbas','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10465,'EXECUTED','3:5f096b88988f19d9d3e596c03fba2b90','Add Unique Constraint, Add Column (x6), Add Foreign Key Constraint (x2)','Add unique uuid constraint and attributes inherited from BaseOpenmrsData to the person_merge_log table',NULL,'2.0.5'),('20111218-1830-fix','sunbiz','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10466,'EXECUTED','3:a95b16d8762fef1076564611fb2115ac','Modify Column','(Fixed)Change person_merge_log.voided to BOOLEAN',NULL,'2.0.5'),('20111218-2274','gsluthra','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10467,'MARK_RAN','3:6339df469a35f517ac6e86452aad0155','Update Data','Fix the description for RBC concept',NULL,'2.0.5'),('20111219-1404','bwolfe','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10468,'EXECUTED','3:3f8cfa9c088a103788bcf70de3ffaa8b','Update Data','Fix empty descriptions on relationship types',NULL,'2.0.5'),('20111222-1659','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10469,'EXECUTED','3:990b494647720b680efeefbab2c502de','Create Table, Create Index','Create clob_datatype_storage table',NULL,'2.0.5'),('201118012301','lkellett','liquibase-update-to-latest.xml','2016-07-04 10:38:01',10370,'MARK_RAN','3:0d96c10c52335339b1003e6dd933ccc2','Add Column','Adding the discontinued_reason_non_coded column to orders.',NULL,'2.0.5'),('201202020847','abbas','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10470,'EXECUTED','3:35bf2f2481ee34975e57f08d933583be','Modify data type, Add Not-Null Constraint','Change merged_data column type to CLOB in person_merge_log table',NULL,'2.0.5'),('20120316-1300','mseaton','liquibase.xml','2016-07-04 10:38:27',10643,'EXECUTED','3:0cbaf0a89ef629563c90deccbd82429f','Create Table','Adding calculation_registration table',NULL,'2.0.5'),('20120322-1510','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10471,'EXECUTED','3:7c5913c7091c2b20babb9e825774993c','Add Column','Adding uniqueness_behavior column to patient_identifier_type table',NULL,'2.0.5'),('20120330-0954','jkeiper','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10472,'EXECUTED','3:9c6084b4407395205fa39b34630d3522','Modify data type','Increase size of drug name column to 255 characters',NULL,'2.0.5'),('20120503-djmod','dkayiwa and djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10492,'EXECUTED','3:d31ac18d3a40e45c0ebb399c5d116951','Create Table, Add Foreign Key Constraint (x2)','Create test_order table',NULL,'2.0.5'),('20120504-1000','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10473,'EXECUTED','3:eb6f5e2a2ef5ea111ff238ca1df013f4','Drop Table','Dropping the drug_ingredient table',NULL,'2.0.5'),('20120504-1010','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:03',10474,'EXECUTED','3:4d9ece759a248fa385c3eae6b83995a1','Create Table','Creating the drug_ingredient table',NULL,'2.0.5'),('20120504-1020','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10475,'EXECUTED','3:fcbc8182e908b595ae338ba8402a589c','Add Primary Key','Adding a primary key to the drug_ingredient table',NULL,'2.0.5'),('20120504-1030','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10476,'EXECUTED','3:d802926fcf3eaf3649aca49a26a5f67d','Add Foreign Key Constraint','Adding a new foreign key from drug_ingredient.units to concept.concept_id',NULL,'2.0.5'),('20120504-1040','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10477,'EXECUTED','3:9786bfbb8133493b54ce9026424d5b99','Add Foreign Key Constraint','Adding a new foreign key from drug_ingredient.drug_id to drug.drug_id',NULL,'2.0.5'),('20120504-1050','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10478,'EXECUTED','3:7d43f25c9a3bde54112ddd65627b2c05','Add Foreign Key Constraint','Adding a new foreign key from drug_ingredient.ingredient_id to concept.concept_id',NULL,'2.0.5'),('201205241728-1','mvorobey','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10487,'MARK_RAN','3:70160c0af8222542fa668ac5f5cb99ed','Add Column, Add Foreign Key Constraint','Add optional property view_privilege to encounter_type table',NULL,'2.0.5'),('201205241728-2','mvorobey','liquibase-update-to-latest.xml','2016-07-04 10:38:04',10488,'MARK_RAN','3:dd8de770c99e046ba05bc8348748c33c','Add Column, Add Foreign Key Constraint','Add optional property edit_privilege to encounter_type table',NULL,'2.0.5'),('20120529-2230','mvorobey','liquibase-schema-only.xml','2016-07-04 10:37:37',400,'EXECUTED','3:f3e2c3891054eed4aadc45ad071afd8c','Add Foreign Key Constraint','',NULL,'2.0.5'),('20120529-2231','mvorobey','liquibase-schema-only.xml','2016-07-04 10:37:37',401,'EXECUTED','3:9bc5f03ef0ab12767509be1cb4cc3213','Add Foreign Key Constraint','',NULL,'2.0.5'),('20120613-0930','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10490,'EXECUTED','3:fe6387773a70b574b106b37686a8e8d3','Drop Not-Null Constraint','Dropping not null constraint from provider.identifier column',NULL,'2.0.5'),('20121007-orders_urgency','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10493,'EXECUTED','3:f8eb2228ea34f43ae21bedf4abc8736b','Add Column','Adding urgency column to orders table',NULL,'2.0.5'),('20121007-test_order_laterality','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10494,'EXECUTED','3:1121924c349201e400e03feda110acc3','Modify data type','Changing test_order.laterality to be a varchar',NULL,'2.0.5'),('20121008-order_specimen_source_fk','djazayeri','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10495,'MARK_RAN','3:99464e51d64e056a1e23b30c7aaaf47e','Add Foreign Key Constraint','Adding FK constraint for test_order.specimen_source if necessary',NULL,'2.0.5'),('20121016-1504','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10496,'EXECUTED','3:88db1819c0e9da738ed9332b5de73609','Drop Foreign Key Constraint, Modify Column, Add Foreign Key Constraint','Removing auto increment from test_order.order_id column',NULL,'2.0.5'),('20121020-TRUNK-3610','lluismf','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10497,'EXECUTED','3:a3159e65647f0ff1b667104012b5f4f0','Update Data (x2)','Rename global property autoCloseVisits.visitType to visits.autoCloseVisitType',NULL,'2.0.5'),('20121021-TRUNK-333','lluismf','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10499,'EXECUTED','3:f885cb0eed2a8e2a5786675eeb0ccbc5','Drop Table','Removing concept set derived table',NULL,'2.0.5'),('20121025-TRUNK-213','lluismf','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10498,'EXECUTED','3:65536ae335b0a6cb23619d6ef7ea3274','Modify Column (x2)','Normalize varchar length of locale columns',NULL,'2.0.5'),('20121109-TRUNK-3474','patandre','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10500,'EXECUTED','3:02af0e39e210aeda861f92698ae974f6','Drop Not-Null Constraint','Dropping not null constraint from concept_class.description column',NULL,'2.0.5'),('20121112-TRUNK-3474','patandre','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10501,'EXECUTED','3:30e023e5e3e98190470d951fbbbd9e87','Drop Not-Null Constraint','Dropping not null constraint from concept_datatype.description column',NULL,'2.0.5'),('20121113-TRUNK-3474','patandre','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10502,'EXECUTED','3:76211ab053e8685a4d0b1345f166e965','Drop Not-Null Constraint','Dropping not null constraint from patient_identifier_type.description column',NULL,'2.0.5'),('20121113-TRUNK-3474-person-attribute-type','patandre','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10503,'EXECUTED','3:7ad821b6167ff22a812a6c550d6deb53','Drop Not-Null Constraint','Dropping not null constraint from person_attribute_type.description column',NULL,'2.0.5'),('20121113-TRUNK-3474-privilege','patandre','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10504,'EXECUTED','3:ecf38bb6fb29d96b0e2c75330a637245','Drop Not-Null Constraint','Dropping not null constraint from privilege.description column',NULL,'2.0.5'),('20121114-TRUNK-3474-encounter_type','patandre','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10507,'EXECUTED','3:1cad8ad2c06b02915138dfb36c013770','Drop Not-Null Constraint','Dropping not null constraint from encounter_type.description column',NULL,'2.0.5'),('20121114-TRUNK-3474-relationship_type','patandre','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10506,'EXECUTED','3:232499aa77be5583f87d6528c0c44768','Drop Not-Null Constraint','Dropping not null constraint from relationship_type.description column',NULL,'2.0.5'),('20121114-TRUNK-3474-role','patandre','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10505,'EXECUTED','3:69a164a13e3520d5cdccbf977d07ce89','Drop Not-Null Constraint','Dropping not null constraint from role.description column',NULL,'2.0.5'),('20121212-TRUNK-2768','patandre','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10508,'EXECUTED','3:351f7dba6eea4f007fa5d006219ede9e','Add Column','Adding deathdate_estimated column to person.',NULL,'2.0.5'),('201301031440-TRUNK-4135','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10573,'EXECUTED','3:47e5686e3cb80484b2830afca679ec70','Custom Change','Creating coded order frequencies for drug order frequencies',NULL,'2.0.5'),('201301031448-TRUNK-4135','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10574,'EXECUTED','3:00adfb72966810dd0c048f93b8edd523','Custom Change','Migrating drug order frequencies to coded order frequencies',NULL,'2.0.5'),('201301031455-TRUNK-4135','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10575,'EXECUTED','3:04b95a27ccba87e597395670db081498','Drop Column','Dropping temporary column drug_order.frequency_text',NULL,'2.0.5'),('201306141103-TRUNK-3884','susantan','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10510,'EXECUTED','3:9581f8d869e69d911f04e48591a297d0','Add Foreign Key Constraint (x3)','Adding 3 foreign key relationships (creator,created_by,voided_by) to encounter_provider table',NULL,'2.0.5'),('20130626-TRUNK-439','jthoenes','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10509,'EXECUTED','3:6c0799599f35b4546dafa73968e3a229','Update Data','Adding configurability to Patient Header on Dashboard. Therefore the cd4_count property is dropped and\n            replaced with a header.showConcept property.',NULL,'2.0.5'),('20130809-TRUNK-4044-duplicateEncounterRoleChangeSet','surangak','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10513,'EXECUTED','3:35b07ae88667be5a78002beacd3aa0ed','Custom Change','Custom changesets to identify and resolve duplicate EncounterRole names',NULL,'2.0.5'),('20130809-TRUNK-4044-duplicateEncounterTypeChangeSet','surangak','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10514,'MARK_RAN','3:01a7d7ae88b0280139178f1840d417bd','Custom Change','Custom changesets to identify and resolve duplicate EncounterType names',NULL,'2.0.5'),('20130809-TRUNK-4044-encounter_role_unique_name_constraint','surangak','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10516,'EXECUTED','3:1a5a8ad5971977e0645a6fbc3744f8e2','Add Unique Constraint','Adding the unique constraint to the encounter_role.name column',NULL,'2.0.5'),('20130809-TRUNK-4044-encounter_type_unique_name_constraint','surangak','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10515,'EXECUTED','3:823098007f6e299c0c6555dde6f12255','Add Unique Constraint','Adding the unique constraint to the encounter_type.name column',NULL,'2.0.5'),('20130925-TRUNK-4105','hannes','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10511,'EXECUTED','3:e81f96e97c307c2d265bce32a046d0ca','Create Index','Adding index on concept_reference_term.code column',NULL,'2.0.5'),('20131023-TRUNK-3903','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10512,'MARK_RAN','3:88f8ec2c297875a03fd88ddd2b9f14b9','Add Column','Adding \"display_precision\" column to concept_numeric table',NULL,'2.0.5'),('201310281153-TRUNK-4123','mujir,sushmitharaos','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10543,'EXECUTED','3:b2bffad4e841b61c6397465633cd1064','Add Column, Add Foreign Key Constraint','Adding previous_order_id to orders',NULL,'2.0.5'),('201310281153-TRUNK-4124','mujir,sushmitharaos','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10544,'EXECUTED','3:eb9dec50fead4430dc07b8309e5840ac','Add Column, Update Data, Add Not-Null Constraint','Adding order_action to orders and setting order_actions as NEW for existing orders',NULL,'2.0.5'),('201311041510','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10538,'EXECUTED','3:dfdc279ddc60b9751dc5d655b4c7fc9c','Rename Column','Renaming drug_order.prn column to drug_order.as_needed',NULL,'2.0.5'),('201311041511','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10539,'EXECUTED','3:4a2ee902d6090959a49539d5bc907354','Add Column','Adding as_needed_condition column to drug_order table',NULL,'2.0.5'),('201311041512','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10540,'EXECUTED','3:76a5c7a0b95e971bd540865917efed9c','Add Column','Adding order_number column to orders table',NULL,'2.0.5'),('201311041513','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10541,'MARK_RAN','3:b41217397a18dbe18e07266a9be4a523','Update Data','Setting order numbers for existing orders',NULL,'2.0.5'),('201311041515-TRUNK-4122','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10542,'EXECUTED','3:948975149e69b6862aab0012304d9a80','Add Not-Null Constraint','Making orders.order_number not nullable',NULL,'2.0.5'),('20131210-TRUNK-4130','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10548,'EXECUTED','3:5a2bde236731862f2c6e3e4066705cdf','Add Column','Adding num_refills column to drug_order table',NULL,'2.0.5'),('201312141400-TRUNK-4126','arathy','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10545,'EXECUTED','3:45d8c6ce32076c0fe11f75d1fea1c215','Modify data type, Rename Column','Renaming drug_order.complex to dosing_type',NULL,'2.0.5'),('201312141400-TRUNK-4127','arathy','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10547,'MARK_RAN','3:26f381a2b8f112d98f36c1d0b6cceebd','Update Data (x2)','Converting values in drug_order.dosing_type column',NULL,'2.0.5'),('201312141401-TRUNK-4126','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10546,'EXECUTED','3:cc77a94d57b78ac02e99ed4ca25f6272','Drop Not-Null Constraint','Making drug_order.dosing_type nullable',NULL,'2.0.5'),('20131216-1637','gitahi','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10583,'EXECUTED','3:4b2a0abaf146a7d938b94009d9600eaf','Create Table, Add Foreign Key Constraint (x6)','Add drug_reference_map table',NULL,'2.0.5'),('201312161618-TRUNK-4129','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10554,'EXECUTED','3:a7c821bc60c7410b387aa276540291a9','Add Column, Add Foreign Key Constraint','Adding quantity_units column to drug_order table',NULL,'2.0.5'),('201312161713-TRUNK-4129','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10555,'EXECUTED','3:2e13513e97a1c372818bd9ad1f31c219','Modify data type','Changing quantity column of drug_order to double',NULL,'2.0.5'),('201312162044-TRUNK-4126','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10552,'EXECUTED','3:31378c39bfdf55ec4ec6faff20c9dcf8','Add Column','Adding duration column to drug_order table',NULL,'2.0.5'),('201312162059-TRUNK-4126','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10553,'EXECUTED','3:f6f21104c2e85bacbbe9af09fee348fd','Add Column, Add Foreign Key Constraint','Adding duration_units column to drug_order table',NULL,'2.0.5'),('20131217-TRUNK-4142','banka','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10551,'EXECUTED','3:5c29916ae374ae0cb36ecbf4a9c80e8c','Add Column','Adding comment_to_fulfiller column to orders table',NULL,'2.0.5'),('20131217-TRUNK-4157','banka','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10550,'EXECUTED','3:8553abd1173bf56dad911a11ec0924ce','Add Column','Adding dosing_instructions column to drug_order table',NULL,'2.0.5'),('201312171559-TRUNK-4159','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10549,'EXECUTED','3:0735c719adcee97fbe967460d05bb474','Create Table, Add Foreign Key Constraint (x4)','Create the order_frequency table',NULL,'2.0.5'),('201312181649-TRUNK-4137','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10562,'EXECUTED','3:278cff9c9abc7864dd71bf4cba04c885','Add Column, Add Foreign Key Constraint','Adding frequency column to test_order table',NULL,'2.0.5'),('201312181650-TRUNK-4137','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10563,'EXECUTED','3:c28fa1a77bec305b4d8d23fda254f320','Add Column','Adding number_of_repeats column to test_order table',NULL,'2.0.5'),('201312182214-TRUNK-4136','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10556,'EXECUTED','3:5091764b71670065672afcb69d18efae','Add Column, Add Foreign Key Constraint','Adding route column to drug_order table',NULL,'2.0.5'),('201312182223-TRUNK-4136','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10557,'EXECUTED','3:aea033991dfc56954d1661fdf15c35f7','Drop Column','Dropping equivalent_daily_dose column from drug_order table',NULL,'2.0.5'),('201312191200-TRUNK-4167','banka','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10558,'EXECUTED','3:b6a84072096cf71ca37dc160d0422a2d','Add Column','Adding dose_units column to drug_order table',NULL,'2.0.5'),('201312191300-TRUNK-4167','banka','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10559,'EXECUTED','3:c1bb6f3394f9c391288f2d51384edd3e','Add Foreign Key Constraint','Adding foreignKey constraint on dose_units',NULL,'2.0.5'),('201312201200-TRUNK-4167','banka','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10560,'MARK_RAN','3:c72cd1725e670ea735fc45e6f0f31001','Custom Change','Migrating old text units to coded dose_units in drug_order',NULL,'2.0.5'),('201312201425-TRUNK-4138','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10566,'MARK_RAN','3:e6e37b7b995e2da28448f815211648fd','Update Data','Setting order.discontinued_reason to null for stopped orders',NULL,'2.0.5'),('201312201523-TRUNK-4138','banka','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10565,'EXECUTED','3:8d48725ba6d40d8a19acec61c948a52f','Custom Change','Creating Discontinue Order for discontinued orders',NULL,'2.0.5'),('201312201525-TRUNK-4138','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10567,'MARK_RAN','3:c751cbf452be8b2c05af6d6502ff5dc9','Update Data','Setting orders.discontinued_reason_non_coded to null for stopped orders',NULL,'2.0.5'),('201312201601-TRUNK-4138','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10569,'EXECUTED','3:43505eb22756ea5ec6bee4f8ad750034','Drop Foreign Key Constraint','Dropping fk constraint on orders.discontinued_by column to users.user_id column',NULL,'2.0.5'),('201312201640-TRUNK-4138','banka','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10564,'EXECUTED','3:174ed15d1066200fe48c3ed2b7a262ae','Rename Column','Rename orders.discontinued_date to date_stopped',NULL,'2.0.5'),('201312201651-TRUNK-4138','banka','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10568,'EXECUTED','3:2ccbe6dad392099d9b6dc49e40736879','Drop Column','Removing discontinued from orders',NULL,'2.0.5'),('201312201700-TRUNK-4138','banka','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10570,'EXECUTED','3:1c570e49534bf886108654c28a99bede','Drop Column','Removing discontinued_by from orders',NULL,'2.0.5'),('201312201800-TRUNK-4167','banka','liquibase-update-to-latest.xml','2016-07-04 10:38:07',10561,'EXECUTED','3:8ff2338c8a1df329476e9e60c8ddc7f6','Drop Column','Deleting units column',NULL,'2.0.5'),('201312271822-TRUNK-4156','vinay','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10576,'EXECUTED','3:9e6ef6a4a036e5ff027a6c49557b2939','Create Table, Add Foreign Key Constraint (x3)','Adding care_setting table',NULL,'2.0.5'),('201312271823-TRUNK-4156','vinay','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10577,'EXECUTED','3:036031e437f2baae976f103900455644','Insert Row','Adding OUTPATIENT care setting',NULL,'2.0.5'),('201312271824-TRUNK-4156','vinay','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10578,'EXECUTED','3:b78e4eb1e63a9ca78a51c42f1e2edb00','Insert Row','Adding INPATIENT care setting',NULL,'2.0.5'),('201312271826-TRUNK-4156','vinay','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10579,'EXECUTED','3:142266f644d20a834b2c687abf45f019','Add Column','Add care_setting column to orders table',NULL,'2.0.5'),('201312271827-TRUNK-4156','vinay','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10580,'MARK_RAN','3:ba1d0716249a5f1bde5eec83c190400f','Custom SQL','Set default value for orders.care_setting column for existing rows',NULL,'2.0.5'),('201312271828-TRUNK-4156','vinay','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10581,'EXECUTED','3:7c6fcd23f2fdbcc69ad06bcdd34cb56f','Add Not-Null Constraint','Make care_setting column non-nullable',NULL,'2.0.5'),('201312271829-TRUNK-4156','vinay','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10582,'EXECUTED','3:dc55b130e1d56c8df00b06a5dedd2689','Add Foreign Key Constraint','Add foreign key constraint',NULL,'2.0.5'),('201401031433-TRUNK-4135','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10571,'EXECUTED','3:1e90a9f5f6ffab47ac360dec7497d2f9','Rename Column','Temporarily renaming drug_order.frequency column to frequency_text',NULL,'2.0.5'),('201401031434-TRUNK-4135','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:38:08',10572,'EXECUTED','3:59eef75e78e64cdc999a6d25863c921d','Add Column, Add Foreign Key Constraint','Adding the frequency column to the drug_order table',NULL,'2.0.5'),('201401040436-TRUNK-3919','dkithmal','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10517,'EXECUTED','3:2fde2a2c0d2a917cd7ec0dfc990b96ac','Add Column, Add Foreign Key Constraint','Add changed_by column to location_tag table',NULL,'2.0.5'),('201401040438-TRUNK-3919','dkithmal','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10518,'EXECUTED','3:134b00185f7b61d7d6cccb66717dc4ae','Add Column','Add date_changed column to location_tag table',NULL,'2.0.5'),('201401040440-TRUNK-3919','dkithmal','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10519,'EXECUTED','3:2f3b927e3554c31abebb064835da2efc','Add Column, Add Foreign Key Constraint','Add changed_by column to location table',NULL,'2.0.5'),('201401040442-TRUNK-3919','dkithmal','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10520,'EXECUTED','3:6cf087a28acd99c02c00fd719a26e73b','Add Column','Add date_changed column to location table',NULL,'2.0.5'),('201401101647-TRUNK-4187','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10521,'EXECUTED','3:d41d8cd98f00b204e9800998ecf8427e','Empty','Checks that all existing free text drug order dose units and frequencies have been mapped to\n            concepts, this will fail the upgrade process if any unmapped text is found',NULL,'2.0.5'),('201402041600-TRUNK-4138','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:09',10584,'EXECUTED','3:a09f49aa170ede88187564ce4834956e','Drop Foreign Key Constraint','Temporary dropping foreign key on orders.discontinued_reason column',NULL,'2.0.5'),('201402041601-TRUNK-4138','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:09',10585,'EXECUTED','3:09293693ccab9bd63f0bacbf1229e6b5','Rename Column','Renaming orders.discontinued_reason column to order_reason',NULL,'2.0.5'),('201402041602-TRUNK-4138','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:09',10586,'EXECUTED','3:b93897a56c7e2407d694bd47b9f3ff56','Add Foreign Key Constraint','Adding back foreign key on orders.discontinued_reason column',NULL,'2.0.5'),('201402041604-TRUNK-4138','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:09',10587,'EXECUTED','3:bf80640132c97067d42dabbebd10b7df','Rename Column','Renaming orders.discontinued_reason_non_coded column to order_reason_non_coded',NULL,'2.0.5'),('201402042238-TRUNK-4202','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:09',10589,'MARK_RAN','3:e74c6d63ac01908cc6fb6f3e9b15e2e0','Custom Change','Converting orders.orderer to reference provider.provider_id',NULL,'2.0.5'),('201402051638-TRUNK-4202','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:09',10588,'EXECUTED','3:ddff1dba0827858324336de9baeb93aa','Drop Foreign Key Constraint','Temporarily removing foreign key constraint from orders.orderer column',NULL,'2.0.5'),('201402051639-TRUNK-4202','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10590,'EXECUTED','3:32f643ae7a3cafe84fc11208e116cbf0','Add Foreign Key Constraint','Adding foreign key constraint to orders.orderer column',NULL,'2.0.5'),('201402120720-TRUNK-3902','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10600,'MARK_RAN','3:7ce90e7459b6b840b7d3f246b6ca697b','Rename Column','Rename concept_numeric.precise to concept_numeric.allow_decimal',NULL,'2.0.5'),('201402141515','djazayeri','liquibase.xml','2016-07-04 10:38:30',10671,'EXECUTED','3:dc5e8c155e43c40736dab557a5a3e7f1','Drop Not-Null Constraint','Allow appointment blocks with no provider specified',NULL,'2.0.5'),('201402241055','Akshika','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10536,'EXECUTED','3:10f872c80393f2cb4d1126ec59b54676','Modify Column','Making orders.start_date not nullable',NULL,'2.0.5'),('201402281648-TRUNK-4274','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10537,'EXECUTED','3:1efc6af34de0b0e83ce217febe1c9fa7','Modify Column','Making order.encounter required',NULL,'2.0.5'),('201403011348','alexisduque','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10591,'EXECUTED','3:117c4ea0b0cd79e21a59b5769d020c93','Modify Column','Make orders.orderer not NULLable',NULL,'2.0.5'),('20140304-TRUNK-4170-duplicateLocationAttributeTypeNameChangeSet','harsz89','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10601,'MARK_RAN','3:b74878260cae25b9c209d1b6ea5ddb98','Custom Change','Custom changeset to identify and resolve duplicate Location Attribute Type names',NULL,'2.0.5'),('20140304-TRUNK-4170-location_attribute_type_unique_name','harsz89','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10602,'EXECUTED','3:e11205616b3ee4ad727a2c5031dee884','Add Unique Constraint','Adding the unique constraint to the location_attribute_type.name column',NULL,'2.0.5'),('20140304816-TRUNK-4139','Akshika','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10593,'EXECUTED','3:0d0ffd19df0598f644c863d931abccd2','Add Column','Adding scheduled_date column to orders table',NULL,'2.0.5'),('201403061758-TRUNK-4284','Banka, Vinay','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10592,'EXECUTED','3:008c6f185b3c571b57031314beda2b8f','Insert Row','Inserting Frequency concept class',NULL,'2.0.5'),('201403070132-TRUNK-4286','andras-szell','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10535,'EXECUTED','3:292b7549e0ae96619a6e4fcc30383592','Insert Row','Insert order type for test orders',NULL,'2.0.5'),('20140313-TRUNK-4288','dszafranek','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10594,'EXECUTED','3:3f2f0f3c1bcfe74253de58d32f811e11','Create Table, Add Foreign Key Constraint (x2), Add Primary Key','Add order_type_class_map table',NULL,'2.0.5'),('20140314-TRUNK-4283','dszafranek, wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10522,'EXECUTED','3:d41d8cd98f00b204e9800998ecf8427e','Empty','Checking that all orders have start_date column set',NULL,'2.0.5'),('20140316-TRUNK-4283','dszafranek, wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10524,'EXECUTED','3:d41d8cd98f00b204e9800998ecf8427e','Empty','Checking that all orders have encounter_id column set',NULL,'2.0.5'),('201403171701-appointmentscheduling_appointment_cancel_reason','cioan','liquibase.xml','2016-07-04 10:38:30',10672,'EXECUTED','3:b84649e43ed1b28210d7cbf8d5d1117a','Add Column','Adding cancel_reason column to appointmentscheduling_appointment table',NULL,'2.0.5'),('20140318-TRUNK-4265','jkondrat','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10595,'EXECUTED','3:01f3e7e45562865ed7855cea0b7ccd30','Merge Column, Update Data','Concatenate dose_strength and units to form the value for the new strength field',NULL,'2.0.5'),('201403262140-TRUNK-4265','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10525,'EXECUTED','3:d41d8cd98f00b204e9800998ecf8427e','Empty','Checking if there are any drugs with the dose_strength specified but no units',NULL,'2.0.5'),('201404091110','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10526,'EXECUTED','3:d41d8cd98f00b204e9800998ecf8427e','Empty','Checking if order_type table is already up to date or can be updated automatically',NULL,'2.0.5'),('201404091112','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10530,'EXECUTED','3:2bf52dfa949ba06b05016ce4eb08034b','Add Unique Constraint','Adding unique key constraint to order_type.name column',NULL,'2.0.5'),('201404091128','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10531,'EXECUTED','3:f96fd12ecf53fb18143450f7a0b9c1d9','Add Column','Adding java_class_name column to order_type table',NULL,'2.0.5'),('201404091129','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10532,'EXECUTED','3:d018fdec6bcd0775032bd8df67f57a77','Add Column','Adding parent column to order_type table',NULL,'2.0.5'),('201404091131','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10534,'EXECUTED','3:accf143fbe21963d2ea7fb211424ed4f','Add Not-Null Constraint','Add not-null constraint on order_type.java_class_name column',NULL,'2.0.5'),('201404091516','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10596,'EXECUTED','3:8c5d2f8c49bf08514911c24f60c065e0','Add Column, Add Foreign Key Constraint','Add changed_by column to order_type table',NULL,'2.0.5'),('201404091517','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10597,'EXECUTED','3:24b9f5757f0dec2a910daab2dd138ce1','Add Column','Add date_changed column to order_type table',NULL,'2.0.5'),('201404101130','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10533,'EXECUTED','3:0b986790bfacfc61e7ec851a4e4fbded','Update Data','Setting java_class_name column for drug order type row',NULL,'2.0.5'),('201406201443','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10598,'EXECUTED','3:c1cf2edbf71074e2740d975d89ca202d','Add Column','Add brand_name column to drug_order table',NULL,'2.0.5'),('201406201444','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10599,'EXECUTED','3:da7c9042e009a4cbe61746bd8bf13d12','Add Column','Add dispense_as_written column to drug_order table',NULL,'2.0.5'),('201406211643-TRUNK-4401','harsz89','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10528,'EXECUTED','3:d41d8cd98f00b204e9800998ecf8427e','Empty','Checking that all discontinued orders have the discontinued_date column set',NULL,'2.0.5'),('201406211703-TRUNK-4401','harsz89','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10529,'EXECUTED','3:d41d8cd98f00b204e9800998ecf8427e','Empty','Checking that all discontinued orders have the discontinued_by column set',NULL,'2.0.5'),('201406262016','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10527,'EXECUTED','3:d41d8cd98f00b204e9800998ecf8427e','Empty','Checking that all users that created orders have provider accounts',NULL,'2.0.5'),('20140635-TRUNK-4283','dszafranek, wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:06',10523,'EXECUTED','3:d41d8cd98f00b204e9800998ecf8427e','Empty','Checking that all orders have orderer column set',NULL,'2.0.5'),('20140715-TRUNK-2999-remove_concept_word','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10603,'EXECUTED','3:4ff8bd1176165ac45c42809673d7d12d','Drop Table','Removing the concept_word table (replaced by Lucene)',NULL,'2.0.5'),('20140718-TRUNK-2999-remove_update_concept_index_task','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10604,'EXECUTED','3:a0ca6ff43de07e00f6a494c5b2964de5','Delete Data','Deleting the update concept index task',NULL,'2.0.5'),('20140719-TRUNK-4445-update_dosing_type_to_varchar_255','mihir','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10607,'EXECUTED','3:2948bf1441141d7f36e34cea1cdfb72a','Modify data type','Increase size of dosing type column to 255 characters',NULL,'2.0.5'),('20140724-1528','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10605,'EXECUTED','3:ad5fee02995c649c4b87d5991f5f3723','Drop Default Value','Dropping default value for drug_order.drug_inventory_id',NULL,'2.0.5'),('20140801-TRUNK-4443-rename_order_start_date_to_date_activated','bharti','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10606,'EXECUTED','3:fde9246d6d3b98ac8e64361818ece22c','Rename Column','Renaming the start_date in order table to date_activated',NULL,'2.0.5'),('201408200733-TRUNK-4446','Deepak','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10608,'EXECUTED','3:95a533365792e76a4cd95f8f32c887db','Modify data type','Changing duration column of drug_order to int',NULL,'2.0.5'),('20140917-1-appointmentmentscheduling_appointment_request','mogoodrich','liquibase.xml','2016-07-04 10:38:30',10673,'EXECUTED','3:e325fa7e42b1dd1886e56b5893064c8d','Create Table, Add Foreign Key Constraint (x7)','Create the appointment request table',NULL,'2.0.5'),('201409230113-TRUNK-3484','k-joseph','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10611,'MARK_RAN','3:7a9c7aad5b657556b1103510f8bfc1d9','Update Data','Updating description for visits.encounterTypeToVisitTypeMapping GP to the value set in OpenmrsContants',NULL,'2.0.5'),('201409251456-appointmentscheduling-confidential','djazayeri','liquibase.xml','2016-07-04 10:38:30',10674,'EXECUTED','3:1d17191b84034b89b6402f9e0853a100','Add Column','Adding confidential column to appointmentscheduling_appointment_type table',NULL,'2.0.5'),('20141010-trunk-4492','alec','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10613,'MARK_RAN','3:fbe1f44dafa30068cd7c99a6713f8ee4','Drop Column','Dropping the tribe field from patient table because it has been moved to person_attribute.',NULL,'2.0.5'),('201410291606-TRUNK-3474','jbuczynski','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10614,'EXECUTED','3:03757abe17abedb9ac1cf0b933a35139','Drop Not-Null Constraint','Dropping not null constraint from program.description column',NULL,'2.0.5'),('201410291613-TRUNK-3474','jbuczynski','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10615,'EXECUTED','3:32fa9bee757a0ca295e1545662051b96','Drop Not-Null Constraint','Dropping not null constraint from order_type.description column',NULL,'2.0.5'),('201410291614-TRUNK-3474','jbuczynski','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10616,'EXECUTED','3:8623cbc44ba289fa096e0b5ee4eeaf11','Drop Not-Null Constraint','Dropping not null constraint from concept_name_tag.description column',NULL,'2.0.5'),('201410291616-TRUNK-3474','jbuczynski','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10617,'EXECUTED','3:4393a565e9dad85e69c1cfcd9836d957','Drop Not-Null Constraint','Dropping not null constraint from active_list_type.description column',NULL,'2.0.5'),('20141103-1030','wyclif','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10618,'EXECUTED','3:2b1a52ce2e496ec391e63ce4d9758226','Add Column','Adding form_namespace_and_path column to obs table',NULL,'2.0.5'),('20141121-TRUNK-2193','raff','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10619,'EXECUTED','3:5c3daa4f68e3e650f53b63496a831295','Rename Column','Renaming drug_ingredient.quantity to strength',NULL,'2.0.5'),('20150109-0505','Shruthi, Sravanthi','liquibase.xml','2016-07-04 10:38:29',10662,'EXECUTED','3:dac719171dae4edcff51cef778e34756','Create Table','',NULL,'2.0.5'),('20150109-0521','Shruthi, Sravanthi','liquibase.xml','2016-07-04 10:38:29',10664,'EXECUTED','3:7c55ee9f0149c0c645c0d8f566c42011','Create Index','Creating unique index on condition.uuid column',NULL,'2.0.5'),('20150109-0627','Shruthi, Sravanthi','liquibase.xml','2016-07-04 10:38:29',10663,'EXECUTED','3:643c49c1108fd96b387efa2b8b832252','Add Foreign Key Constraint (x6)','',NULL,'2.0.5'),('20150122-1414','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10332,'EXECUTED','3:b662cac19085b837d902916f20a27da9','Update Data','Reverting concept name type to NULL for concepts having names tagged as default',NULL,'2.0.5'),('20150122-1420','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:37:58',10333,'EXECUTED','3:c75793ade72db0648a912f22f79cf461','Update Data, Delete Data (x2)','Setting concept name type to fully specified for names tagged as default',NULL,'2.0.5'),('20150428-TRUNK-4693-1','mseaton','liquibase-update-to-latest.xml','2016-07-04 10:38:10',10609,'MARK_RAN','3:599624d822c5f7a0d9bc796c4e90a526','Drop Foreign Key Constraint','Removing invalid foreign key constraint from order_type.parent column to order.order_id column',NULL,'2.0.5'),('20150428-TRUNK-4693-2','mseaton','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10610,'EXECUTED','3:706418216753f16ce1b5eb0c45c21e24','Add Foreign Key Constraint','Adding foreign key constraint from order_type.parent column to order_type.order_type_id column',NULL,'2.0.5'),('201506051103-TRUNK-4727','Chethan, Preethi','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10620,'EXECUTED','3:91fd51bed950b30f1b0532b5bdc041cc','Add Column','Adding birthtime column to person',NULL,'2.0.5'),('201508111304','sns.recommind','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10621,'EXECUTED','3:75a849aa0d9c54bb2e052e6a8875d443','Create Index','Add an index to the global_property.property column',NULL,'2.0.5'),('201508111412','sns.recommind','liquibase-update-to-latest.xml','2016-07-04 10:38:12',10622,'EXECUTED','3:226edcaabe81261347e3da63e5321b3f','Create Index','Add an index to the concept_class.name column',NULL,'2.0.5'),('201508111415','sns.recommind','liquibase-update-to-latest.xml','2016-07-04 10:38:12',10623,'EXECUTED','3:ee7859e5c5d801c0125fe02449c1c53b','Create Index','Add an index to the concept_datatype.name column',NULL,'2.0.5'),('20160202-1743','rkorytkowski','liquibase-update-to-latest.xml','2016-07-04 10:38:13',10625,'EXECUTED','3:0b6ba212979a943d1ce2e07e09dd40c0','Add Not-Null Constraint (x53)','Set uuid columns to \"NOT NULL\", if not set already for 1.9.x tables',NULL,'2.0.5'),('20160216-1700','bahmni','liquibase-update-to-latest.xml','2016-07-04 10:38:12',10624,'EXECUTED','3:31fcb8b252da71f39ea960904ca28936','Custom Change','Set uuid for columns in all tables which has uuid as null. This is required for successful run of next changeSet.',NULL,'2.0.5'),('201604030003','vishnuraom','liquibase-update-to-latest.xml','2016-07-04 10:38:13',10626,'MARK_RAN','3:5aa960f190e108ff2a1332dbb10e4180','Update Data (x2)','Converting values in drug_order.dosing_type column(TRUNK-4845)',NULL,'2.0.5'),('3','Eli','liquibase.xml','2016-07-04 10:38:33',10685,'EXECUTED','3:c68cc993b3781695564ebc6f5bdf7831','Add Column','Added synonym_id column',NULL,'2.0.5'),('3-increase-privilege-col-size-person_attribute_type','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:05',10489,'EXECUTED','3:c2465b2417463d93f1101c6683f41250','Drop Foreign Key Constraint, Modify Column, Add Foreign Key Constraint','Increasing the size of the edit_privilege column in the person_attribute_type table',NULL,'2.0.5'),('4','Eli','liquibase.xml','2016-07-04 10:38:33',10686,'EXECUTED','3:531d21b53479c6b78a660d6c540678fb','Add Foreign Key Constraint','Changed cascade options',NULL,'2.0.5'),('appframework-1','djazayeri','liquibase.xml','2016-07-04 10:38:31',10679,'EXECUTED','3:1fff60c3e3407f96174f63eb875e3dd3','Create Table, Add Foreign Key Constraint (x2)','Create table for AppEnabled',NULL,'2.0.5'),('appframework-2','djazayeri','liquibase.xml','2016-07-04 10:38:31',10680,'EXECUTED','3:964d11f2c8a482d315341c315b705101','Drop Table','Drop table for AppEnabled, since we\'ll be using privileges instead',NULL,'2.0.5'),('appframework-3','nutsiepully','liquibase.xml','2016-07-04 10:38:31',10681,'EXECUTED','3:fdc791492c3f51ed1d97890bc099d284','Create Table','Create table to track which AppFramework components are enabled',NULL,'2.0.5'),('appframework-4','Wyclif','liquibase.xml','2016-07-04 10:38:31',10682,'EXECUTED','3:9a5638b21b279ff5fb44a268876bc1d3','Create Table','Create table to store user defined apps',NULL,'2.0.5'),('CSM-102_28052015_1742','k-joseph','liquibase.xml','2016-07-04 10:38:33',10689,'EXECUTED','3:ec04b24604b2d2d31b344eb82f9d173b','Create Table, Add Foreign Key Constraint (x2)','Create chartsearch_history table',NULL,'2.0.5'),('CSM-103_11062015_1634','k-joseph','liquibase.xml','2016-07-04 10:38:33',10692,'EXECUTED','3:1f6e62c3dbf3aa164d84e08bd77f8099','Add Column','Added default_search column',NULL,'2.0.5'),('CSM-103_22062015_1941','k-joseph','liquibase.xml','2016-07-04 10:38:33',10693,'EXECUTED','3:3df7923bf4dffbcf232eca260d676f0f','Rename Column (x3)','Rename uuid to history_uuid, bookmark_uuid, note_uuid\n			respectively',NULL,'2.0.5'),('CSM-111_17072015_1513','k-joseph','liquibase.xml','2016-07-04 10:38:34',10697,'EXECUTED','3:35d5a9cce7158a7bae04c17eca823680','Rename Column (x3)','Re-Renaming history_uuid , bookmark_uuid and note_uuid\n			to uuid',NULL,'2.0.5'),('CSM-113_31072015_1224','k-joseph','liquibase.xml','2016-07-04 10:38:34',10700,'EXECUTED','3:dc615fae56ff5cb8d1d5090308547d62','Update Data (x2)','Enable duplicate and Enable Multiple filtering should be set\n			to false by default',NULL,'2.0.5'),('CSM-58-18062014-2207','k-joseph','liquibase.xml','2016-07-04 10:38:33',10687,'EXECUTED','3:c4c4e6a0d74b7a5e3e2051c971372e24','Create Table','Create chartsearch_categories table',NULL,'2.0.5'),('CSM-58-18062014-2210','k-joseph','liquibase.xml','2016-07-04 10:38:33',10688,'EXECUTED','3:f43e857e15ae3d3c80793b3a0f6761c2','Insert Row (x16)','Adding category filters for all existing concept classes',NULL,'2.0.5'),('CSM-81_02062015_1446','k-joseph','liquibase.xml','2016-07-04 10:38:33',10690,'EXECUTED','3:0b48ce03eef365013c6ea62e8f498e2e','Create Table, Add Foreign Key Constraint (x2)','Create chartsearch_bookmark table',NULL,'2.0.5'),('CSM-90_04062015_2012','k-joseph','liquibase.xml','2016-07-04 10:38:33',10691,'EXECUTED','3:6a193694bb614bf3daa4ce02c67121f6','Create Table, Add Foreign Key Constraint (x2)','Create chartsearch_note table',NULL,'2.0.5'),('CSM-91-07072015-1826','k-joseph','liquibase.xml','2016-07-04 10:38:34',10696,'EXECUTED','3:6b7dcb0965fe3b54f7b1bf46c9621ac3','Insert Row (x2)','Adding category filters Pharmacologic Drug Class and Units of\n			Measure',NULL,'2.0.5'),('CSM-95_01082015_23:38','k-joseph','liquibase.xml','2016-07-04 10:38:34',10701,'EXECUTED','3:56306fbad390c380bc4f25daedad8f09','Add Column','Adding display_name column onto chartsearch_categories table',NULL,'2.0.5'),('CSM-95_02082015_1517','k-joseph','liquibase.xml','2016-07-04 10:38:34',10702,'EXECUTED','3:d95291429b397cc3b3d6e214aea052f2','Insert Row (x2)','Save allergies and appointments category filters',NULL,'2.0.5'),('CSM-95_03082015_0101','k-joseph','liquibase.xml','2016-07-04 10:38:34',10703,'EXECUTED','3:3346a228e7495aa41eca5ecd503e43ed','Drop Column, Create Table, Add Foreign Key Constraint (x2)','Dropping display_name it from chartsearch_categories table\n			and creating a resolving table',NULL,'2.0.5'),('CSM-95_28072015_1241','k-joseph','liquibase.xml','2016-07-04 10:38:34',10698,'EXECUTED','3:3576f19bf9f8f114e883f0bba0d4e4f3','Create Table, Add Foreign Key Constraint','Create chartsearch_preferences table',NULL,'2.0.5'),('CSM-95_29072015_1239','k-joseph','liquibase.xml','2016-07-04 10:38:34',10699,'EXECUTED','3:e247db232a506ce29cb2c60b9f98b473','Insert Row','Adding default preferences for daemon user',NULL,'2.0.5'),('CSM-98-23062015-0037','k-joseph','liquibase.xml','2016-07-04 10:38:34',10694,'MARK_RAN','3:6896336173f1b9aa05f95157f38d4db6','Drop Foreign Key Constraint (x2)','Dropping both fk_synonymgroups_synonyms and\n			fk_synonymgroups_synonyms_cascade foreign keys',NULL,'2.0.5'),('CSM-98-23062015-1440','k-joseph','liquibase.xml','2016-07-04 10:38:34',10695,'EXECUTED','3:532540a3927c5e0aa933058c32c2e954','Drop Table, Create Table, Add Foreign Key Constraint','Re-create chartsearch_synonyms table which is not yet being\n			used',NULL,'2.0.5'),('disable-foreign-key-checks','ben','liquibase-core-data.xml','2016-07-04 10:37:37',10017,'EXECUTED','3:cc124077cda1cfb0c70c1ec823551223','Custom SQL','',NULL,'2.0.5'),('drop-tribe-foreign-key-TRUNK-4492','dkayiwa','liquibase-update-to-latest.xml','2016-07-04 10:38:11',10612,'MARK_RAN','3:6f02e3203c3fe5414a44106b8f16e3cd','Drop Foreign Key Constraint','Dropping foreign key on patient.tribe',NULL,'2.0.5'),('enable-foreign-key-checks','ben','liquibase-core-data.xml','2016-07-04 10:37:37',10041,'EXECUTED','3:fcfe4902a8f3eda10332567a1a51cb49','Custom SQL','',NULL,'2.0.5'),('htmlformentry_html_form_add_date_retired','Mark Goodrich','liquibase.xml','2016-07-04 10:38:24',10631,'EXECUTED','3:0fae116e77e9245e48b2fd0136e4554a','Add Column','Update htmlformentry_html_form table to contain date_retired column',NULL,'2.0.5'),('htmlformentry_html_form_add_description','Mark Goodrich','liquibase.xml','2016-07-04 10:38:24',10629,'EXECUTED','3:b7dfa8d556e716719a689003445420f7','Add Column','Update htmlformentry_html_form table to contain description column',NULL,'2.0.5'),('htmlformentry_html_form_add_foreign_key_to_retired_by','Mark Goodrich','liquibase.xml','2016-07-04 10:38:25',10633,'EXECUTED','3:389e17a21881a440779fccf908ea7b04','Add Foreign Key Constraint','Add foreign key user_who_retired_html_form',NULL,'2.0.5'),('htmlformentry_html_form_add_retired_by','Mark Goodrich','liquibase.xml','2016-07-04 10:38:24',10630,'EXECUTED','3:b914df13901c5c347a2c2b0cc9fde952','Add Column','Update htmlformentry_html_form table to contain retired_by column',NULL,'2.0.5'),('htmlformentry_html_form_add_retire_reason','Mark Goodrich','liquibase.xml','2016-07-04 10:38:24',10632,'EXECUTED','3:b6188c841d77a9d8096df93d9b5c96c0','Add Column','Update htmlformentry_html_form table to contain retire_reason column',NULL,'2.0.5'),('htmlformentry_html_form_add_uuid','Mark Goodrich','liquibase.xml','2016-07-04 10:38:24',10628,'EXECUTED','3:41465834e3be18b330612636d544ff27','Add Column','Update htmlformentry_html_form table to contain uuid column',NULL,'2.0.5'),('htmlformentry_html_form_create_index_for_uuid','Mark Goodrich','liquibase.xml','2016-07-04 10:38:25',10635,'EXECUTED','3:d4625a37da17b4b98970ac8bb18c8d12','Create Index','Create index htmlformentry_html_form_uuid_index',NULL,'2.0.5'),('htmlformentry_html_form_create_table','Darius Jazayeri','liquibase.xml','2016-07-04 10:38:24',10627,'EXECUTED','3:d5454959fd46822d8f742e79204e8713','Create Table, Add Foreign Key Constraint (x3)','Create table htmlformentry_html_form, for storing html form templates',NULL,'2.0.5'),('htmlformentry_html_form_make_name_nullable','Darius Jazayeri','liquibase.xml','2016-07-04 10:38:25',10636,'EXECUTED','3:14ec9c7c2b697ce0a3da0371da162991','Modify Column','Make name column nullable (because we\'re deprecating it)',NULL,'2.0.5'),('htmlformentry_html_form_update_uuid','Mark Goodrich','liquibase.xml','2016-07-04 10:38:25',10634,'EXECUTED','3:069c77d949f8a9c1a9d300057157fc95','Custom SQL, Modify Column','Remove null values from uuid column',NULL,'2.0.5'),('metadatamapping-2011-10-04-a','bwolfe','liquibase.xml','2016-07-04 10:38:26',10638,'EXECUTED','3:35034abcb1ed993cde7f33847ce0ce4c','Update Data','Move MDS property addLocalMappings to metadatamapping',NULL,'2.0.5'),('metadatamapping-2011-10-04-b','bwolfe','liquibase.xml','2016-07-04 10:38:26',10639,'EXECUTED','3:991431e585885ebeeaef03c760b5f6f8','Update Data','Move MDS property localConceptSourceUuid to metadatamapping',NULL,'2.0.5'),('metadatasharing_create_table_exported_package','Dmytro Trifonov','liquibase.xml','2016-07-04 10:38:26',10640,'EXECUTED','3:cd6f473e1eef04494f47fbaaff70a52f','Create Table, Create Index','Create table metadatasharing_exported_package',NULL,'2.0.5'),('metadatasharing_create_table_imported_item','Dmytro Trifonov','liquibase.xml','2016-07-04 10:38:27',10642,'EXECUTED','3:412a3292ac3cc3f3d27fd8334df634c3','Create Table, Create Index','Create table metadatasharing_imported_item',NULL,'2.0.5'),('metadatasharing_create_table_imported_package','Dmytro Trifonov','liquibase.xml','2016-07-04 10:38:27',10641,'EXECUTED','3:582520a49887eb6fd3e36592693e0730','Create Table, Create Index','Create table metadatasharing_imported_package',NULL,'2.0.5'),('RA-354-create-allergy-table-rev1','fbiedrzycki','liquibase.xml','2016-07-04 10:38:31',10675,'EXECUTED','3:2221234d66aaede15b7454864b085edb','Create Table, Add Foreign Key Constraint (x6)','Create allergy table',NULL,'2.0.5'),('RA-355-create-allergy-reaction-table','fbiedrzycki','liquibase.xml','2016-07-04 10:38:31',10676,'EXECUTED','3:3d7654bdf6720157c7e2b091d81b380c','Create Table, Add Foreign Key Constraint (x2)','Create allergy_reaction table',NULL,'2.0.5'),('RA-360-Add-allergy-status-to-patient-2','rpuzdrowski','liquibase.xml','2016-07-04 10:38:31',10677,'EXECUTED','3:4145b719c9b9400f5557e081bdcd75d8','Add Column','Add allergy_status field to the patient table',NULL,'2.0.5'),('reporting_id_set_cleanup','mseaton','liquibase.xml','2016-07-04 10:38:29',10661,'MARK_RAN','3:01cd3b88ed5e29b64b55d614d419cd2b','Drop Table','Removing reporting_idset table that is no longer used',NULL,'2.0.5'),('reporting_migration_1','mseaton','liquibase.xml','2016-07-04 10:38:29',10659,'EXECUTED','3:f6ea0df533cc324ea0d6275d72980c78','Custom SQL (x2)','Remove OpenMRS scheduled tasks produced by the reporting module',NULL,'2.0.5'),('reporting_migration_2','mseaton','liquibase.xml','2016-07-04 10:38:29',10660,'EXECUTED','3:4fb91f1875cf874314393aa47c71ca83','Custom SQL','Rename the default web renderer',NULL,'2.0.5'),('reporting_report_design_1','mseaton','liquibase.xml','2016-07-04 10:38:28',10644,'EXECUTED','3:482e8981e0dce9476eaf481167719579','Create Table, Add Foreign Key Constraint (x4)','Create table to persist report design specifications',NULL,'2.0.5'),('reporting_report_design_2','mseaton','liquibase.xml','2016-07-04 10:38:28',10645,'EXECUTED','3:027dbe5aeecbd08ecb7b5986a25307de','Add Column, Custom SQL','',NULL,'2.0.5'),('reporting_report_design_3','mseaton','liquibase.xml','2016-07-04 10:38:28',10646,'EXECUTED','3:8cac36c2cf24b842b8add2a5cfbb0df1','Custom SQL','',NULL,'2.0.5'),('reporting_report_design_4','mseaton','liquibase.xml','2016-07-04 10:38:28',10647,'EXECUTED','3:bddba97d81d6daa7fd6fbb478897fd84','Drop Foreign Key Constraint','',NULL,'2.0.5'),('reporting_report_design_5','mseaton','liquibase.xml','2016-07-04 10:38:28',10648,'EXECUTED','3:e7dbeb93443f14c60ed2e7cd6aa1e94f','Create Index','',NULL,'2.0.5'),('reporting_report_design_6','mseaton','liquibase.xml','2016-07-04 10:38:28',10649,'EXECUTED','3:e37eef4a9c8063b4abdb8481dd8fe5f8','Drop Column','Step 4 in changing reporting_report_design to reference report definition\n			by uuid rather than id, in order to not tie it directly to the serialized object table\n			Drop report_definition_id column',NULL,'2.0.5'),('reporting_report_design_resource_1','mseaton','liquibase.xml','2016-07-04 10:38:28',10650,'EXECUTED','3:9b1f111c165c213fa2cecc37d87ed843','Create Table, Add Foreign Key Constraint (x4)','Create table to persist report design resources',NULL,'2.0.5'),('reporting_report_processor_1','mseaton','liquibase.xml','2016-07-04 10:38:28',10655,'EXECUTED','3:bc9b147af791381ff5a98a7786f7d638','Create Table, Add Foreign Key Constraint (x3)','Create tables to persist report processors',NULL,'2.0.5'),('reporting_report_processor_2','mseaton','liquibase.xml','2016-07-04 10:38:28',10656,'MARK_RAN','3:9060cd2b9fc7c9a24155302d97123fc8','Drop Table','Drop the reporting_report_request_processor table (creation of this table was done\n			in the old sqldiff and not ported over to liquibase, as it is not needed.  this\n			changeset serves only to clean it up and delete it if is still exists',NULL,'2.0.5'),('reporting_report_processor_3','mseaton','liquibase.xml','2016-07-04 10:38:28',10657,'EXECUTED','3:2a2278e80933adb538d8c308eb812337','Add Column, Add Foreign Key Constraint','Update reporting_report_processor table to have report_design_id column',NULL,'2.0.5'),('reporting_report_processor_4','mseaton','liquibase.xml','2016-07-04 10:38:28',10658,'EXECUTED','3:3073b9e397f194eb5f16906f36e9c474','Add Column, Custom SQL','Update reporting_report_processor table to have processor_mode column\n			and set the value to automatic for all processors that were previously created',NULL,'2.0.5'),('reporting_report_request_1','mseaton','liquibase.xml','2016-07-04 10:38:28',10651,'EXECUTED','3:4d336ce27e1366ee39ea2b0c069e4491','Create Table, Add Foreign Key Constraint','Create tables to persist a report request and save reports',NULL,'2.0.5'),('reporting_report_request_2','mseaton','liquibase.xml','2016-07-04 10:38:28',10652,'EXECUTED','3:90834d1a3dc8b3c1f4109579c9a5b954','Add Column','Add a schedule property to ReportRequest',NULL,'2.0.5'),('reporting_report_request_3','mseaton','liquibase.xml','2016-07-04 10:38:28',10653,'EXECUTED','3:8e88f53150534221e9c32cd0e2c94706','Add Column','Add processAutomatically boolean to ReportRequest',NULL,'2.0.5'),('reporting_report_request_4','mseaton','liquibase.xml','2016-07-04 10:38:28',10654,'EXECUTED','3:e93012ef325cc3aff1c92f0ba78424a8','Add Column','Add minimum_days_to_preserve property to ReportRequest',NULL,'2.0.5'),('TRUNK-4548-MigrateAllergiesChangeSet1','dkayiwa','liquibase.xml','2016-07-04 10:38:31',10678,'MARK_RAN','3:f01b382c42610bb62fff87c7b6fe31db','Custom Change','Custom changeset to migrate allergies from old to new tables',NULL,'2.0.5'),('uiframework-20120913-2055','wyclif','liquibase.xml','2016-07-04 10:38:26',10637,'EXECUTED','3:af5797791243753415f969b558c9a917','Create Table','Adding uiframework_page_view table',NULL,'2.0.5');
/*!40000 ALTER TABLE `liquibasechangelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `liquibasechangeloglock`
--

DROP TABLE IF EXISTS `liquibasechangeloglock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `liquibasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` tinyint(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liquibasechangeloglock`
--

LOCK TABLES `liquibasechangeloglock` WRITE;
/*!40000 ALTER TABLE `liquibasechangeloglock` DISABLE KEYS */;
INSERT INTO `liquibasechangeloglock` VALUES (1,0,NULL,NULL);
/*!40000 ALTER TABLE `liquibasechangeloglock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `location_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) DEFAULT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city_village` varchar(255) DEFAULT NULL,
  `state_province` varchar(255) DEFAULT NULL,
  `postal_code` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `latitude` varchar(50) DEFAULT NULL,
  `longitude` varchar(50) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `county_district` varchar(255) DEFAULT NULL,
  `address3` varchar(255) DEFAULT NULL,
  `address4` varchar(255) DEFAULT NULL,
  `address5` varchar(255) DEFAULT NULL,
  `address6` varchar(255) DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `parent_location` int(11) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  PRIMARY KEY (`location_id`),
  UNIQUE KEY `location_uuid_index` (`uuid`),
  KEY `name_of_location` (`name`),
  KEY `location_retired_status` (`retired`),
  KEY `user_who_created_location` (`creator`),
  KEY `user_who_retired_location` (`retired_by`),
  KEY `parent_location` (`parent_location`),
  KEY `location_changed_by` (`changed_by`),
  CONSTRAINT `location_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `parent_location` FOREIGN KEY (`parent_location`) REFERENCES `location` (`location_id`),
  CONSTRAINT `user_who_created_location` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_location` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (1,'Unknown Location',NULL,'','','','','','',NULL,NULL,1,'2005-09-22 00:00:00',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'8d6c993e-c2cc-11de-8d13-0010c6dffd0f',NULL,NULL);
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_attribute`
--

DROP TABLE IF EXISTS `location_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location_attribute` (
  `location_attribute_id` int(11) NOT NULL AUTO_INCREMENT,
  `location_id` int(11) NOT NULL,
  `attribute_type_id` int(11) NOT NULL,
  `value_reference` text NOT NULL,
  `uuid` char(38) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`location_attribute_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `location_attribute_location_fk` (`location_id`),
  KEY `location_attribute_attribute_type_id_fk` (`attribute_type_id`),
  KEY `location_attribute_creator_fk` (`creator`),
  KEY `location_attribute_changed_by_fk` (`changed_by`),
  KEY `location_attribute_voided_by_fk` (`voided_by`),
  CONSTRAINT `location_attribute_attribute_type_id_fk` FOREIGN KEY (`attribute_type_id`) REFERENCES `location_attribute_type` (`location_attribute_type_id`),
  CONSTRAINT `location_attribute_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `location_attribute_creator_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `location_attribute_location_fk` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `location_attribute_voided_by_fk` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_attribute`
--

LOCK TABLES `location_attribute` WRITE;
/*!40000 ALTER TABLE `location_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `location_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_attribute_type`
--

DROP TABLE IF EXISTS `location_attribute_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location_attribute_type` (
  `location_attribute_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `datatype` varchar(255) DEFAULT NULL,
  `datatype_config` text,
  `preferred_handler` varchar(255) DEFAULT NULL,
  `handler_config` text,
  `min_occurs` int(11) NOT NULL,
  `max_occurs` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`location_attribute_type_id`),
  UNIQUE KEY `uuid` (`uuid`),
  UNIQUE KEY `location_attribute_type_unique_name` (`name`),
  KEY `location_attribute_type_creator_fk` (`creator`),
  KEY `location_attribute_type_changed_by_fk` (`changed_by`),
  KEY `location_attribute_type_retired_by_fk` (`retired_by`),
  CONSTRAINT `location_attribute_type_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `location_attribute_type_creator_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `location_attribute_type_retired_by_fk` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_attribute_type`
--

LOCK TABLES `location_attribute_type` WRITE;
/*!40000 ALTER TABLE `location_attribute_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `location_attribute_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_tag`
--

DROP TABLE IF EXISTS `location_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location_tag` (
  `location_tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  PRIMARY KEY (`location_tag_id`),
  UNIQUE KEY `location_tag_uuid_index` (`uuid`),
  KEY `location_tag_creator` (`creator`),
  KEY `location_tag_retired_by` (`retired_by`),
  KEY `location_tag_changed_by` (`changed_by`),
  CONSTRAINT `location_tag_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `location_tag_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `location_tag_retired_by` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_tag`
--

LOCK TABLES `location_tag` WRITE;
/*!40000 ALTER TABLE `location_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `location_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_tag_map`
--

DROP TABLE IF EXISTS `location_tag_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location_tag_map` (
  `location_id` int(11) NOT NULL,
  `location_tag_id` int(11) NOT NULL,
  PRIMARY KEY (`location_id`,`location_tag_id`),
  KEY `location_tag_map_tag` (`location_tag_id`),
  CONSTRAINT `location_tag_map_location` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `location_tag_map_tag` FOREIGN KEY (`location_tag_id`) REFERENCES `location_tag` (`location_tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_tag_map`
--

LOCK TABLES `location_tag_map` WRITE;
/*!40000 ALTER TABLE `location_tag_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `location_tag_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metadatasharing_exported_package`
--

DROP TABLE IF EXISTS `metadatasharing_exported_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metadatasharing_exported_package` (
  `exported_package_id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `group_uuid` char(38) NOT NULL,
  `version` int(11) NOT NULL,
  `published` tinyint(1) NOT NULL,
  `date_created` datetime NOT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) NOT NULL,
  `content` longblob,
  PRIMARY KEY (`exported_package_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `metadatasharing_package_group_uuid` (`group_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metadatasharing_exported_package`
--

LOCK TABLES `metadatasharing_exported_package` WRITE;
/*!40000 ALTER TABLE `metadatasharing_exported_package` DISABLE KEYS */;
/*!40000 ALTER TABLE `metadatasharing_exported_package` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metadatasharing_imported_item`
--

DROP TABLE IF EXISTS `metadatasharing_imported_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metadatasharing_imported_item` (
  `imported_item_id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `classname` varchar(256) NOT NULL,
  `existing_uuid` char(38) DEFAULT NULL,
  `date_imported` datetime DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `import_type` tinyint(4) DEFAULT '0',
  `assessed` tinyint(1) NOT NULL,
  PRIMARY KEY (`imported_item_id`),
  KEY `metadatasharing_item_uuid` (`uuid`,`existing_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metadatasharing_imported_item`
--

LOCK TABLES `metadatasharing_imported_item` WRITE;
/*!40000 ALTER TABLE `metadatasharing_imported_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `metadatasharing_imported_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metadatasharing_imported_package`
--

DROP TABLE IF EXISTS `metadatasharing_imported_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metadatasharing_imported_package` (
  `imported_package_id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `group_uuid` char(38) NOT NULL,
  `subscription_url` varchar(512) DEFAULT NULL,
  `subscription_status` tinyint(4) DEFAULT '0',
  `date_created` datetime NOT NULL,
  `date_imported` datetime DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `description` varchar(256) NOT NULL,
  `import_config` varchar(1024) DEFAULT NULL,
  `remote_version` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`imported_package_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `metadatasharing_package_uuid` (`group_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metadatasharing_imported_package`
--

LOCK TABLES `metadatasharing_imported_package` WRITE;
/*!40000 ALTER TABLE `metadatasharing_imported_package` DISABLE KEYS */;
/*!40000 ALTER TABLE `metadatasharing_imported_package` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `note` (
  `note_id` int(11) NOT NULL DEFAULT '0',
  `note_type` varchar(50) DEFAULT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `obs_id` int(11) DEFAULT NULL,
  `encounter_id` int(11) DEFAULT NULL,
  `text` text NOT NULL,
  `priority` int(11) DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`note_id`),
  UNIQUE KEY `note_uuid_index` (`uuid`),
  KEY `user_who_changed_note` (`changed_by`),
  KEY `user_who_created_note` (`creator`),
  KEY `encounter_note` (`encounter_id`),
  KEY `obs_note` (`obs_id`),
  KEY `note_hierarchy` (`parent`),
  KEY `patient_note` (`patient_id`),
  CONSTRAINT `encounter_note` FOREIGN KEY (`encounter_id`) REFERENCES `encounter` (`encounter_id`),
  CONSTRAINT `note_hierarchy` FOREIGN KEY (`parent`) REFERENCES `note` (`note_id`),
  CONSTRAINT `obs_note` FOREIGN KEY (`obs_id`) REFERENCES `obs` (`obs_id`),
  CONSTRAINT `patient_note` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON UPDATE CASCADE,
  CONSTRAINT `user_who_changed_note` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_note` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `note`
--

LOCK TABLES `note` WRITE;
/*!40000 ALTER TABLE `note` DISABLE KEYS */;
/*!40000 ALTER TABLE `note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_alert`
--

DROP TABLE IF EXISTS `notification_alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_alert` (
  `alert_id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(512) NOT NULL,
  `satisfied_by_any` tinyint(1) NOT NULL DEFAULT '0',
  `alert_read` tinyint(1) NOT NULL DEFAULT '0',
  `date_to_expire` datetime DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`alert_id`),
  UNIQUE KEY `notification_alert_uuid_index` (`uuid`),
  KEY `alert_date_to_expire_idx` (`date_to_expire`),
  KEY `user_who_changed_alert` (`changed_by`),
  KEY `alert_creator` (`creator`),
  CONSTRAINT `alert_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_changed_alert` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_alert`
--

LOCK TABLES `notification_alert` WRITE;
/*!40000 ALTER TABLE `notification_alert` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_alert` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_alert_recipient`
--

DROP TABLE IF EXISTS `notification_alert_recipient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_alert_recipient` (
  `alert_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `alert_read` tinyint(1) NOT NULL DEFAULT '0',
  `date_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`alert_id`,`user_id`),
  KEY `alert_read_by_user` (`user_id`),
  CONSTRAINT `alert_read_by_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `id_of_alert` FOREIGN KEY (`alert_id`) REFERENCES `notification_alert` (`alert_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_alert_recipient`
--

LOCK TABLES `notification_alert_recipient` WRITE;
/*!40000 ALTER TABLE `notification_alert_recipient` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_alert_recipient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_template`
--

DROP TABLE IF EXISTS `notification_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_template` (
  `template_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `template` text,
  `subject` varchar(100) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `recipients` varchar(512) DEFAULT NULL,
  `ordinal` int(11) DEFAULT '0',
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`template_id`),
  UNIQUE KEY `notification_template_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_template`
--

LOCK TABLES `notification_template` WRITE;
/*!40000 ALTER TABLE `notification_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `obs`
--

DROP TABLE IF EXISTS `obs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `obs` (
  `obs_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) NOT NULL,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `encounter_id` int(11) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `obs_datetime` datetime NOT NULL,
  `location_id` int(11) DEFAULT NULL,
  `obs_group_id` int(11) DEFAULT NULL,
  `accession_number` varchar(255) DEFAULT NULL,
  `value_group_id` int(11) DEFAULT NULL,
  `value_boolean` tinyint(1) DEFAULT NULL,
  `value_coded` int(11) DEFAULT NULL,
  `value_coded_name_id` int(11) DEFAULT NULL,
  `value_drug` int(11) DEFAULT NULL,
  `value_datetime` datetime DEFAULT NULL,
  `value_numeric` double DEFAULT NULL,
  `value_modifier` varchar(2) DEFAULT NULL,
  `value_text` text,
  `value_complex` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `previous_version` int(11) DEFAULT NULL,
  `form_namespace_and_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`obs_id`),
  UNIQUE KEY `obs_uuid_index` (`uuid`),
  KEY `obs_datetime_idx` (`obs_datetime`),
  KEY `obs_concept` (`concept_id`),
  KEY `obs_enterer` (`creator`),
  KEY `encounter_observations` (`encounter_id`),
  KEY `obs_location` (`location_id`),
  KEY `obs_grouping_id` (`obs_group_id`),
  KEY `obs_order` (`order_id`),
  KEY `person_obs` (`person_id`),
  KEY `answer_concept` (`value_coded`),
  KEY `obs_name_of_coded_value` (`value_coded_name_id`),
  KEY `answer_concept_drug` (`value_drug`),
  KEY `user_who_voided_obs` (`voided_by`),
  KEY `previous_version` (`previous_version`),
  CONSTRAINT `answer_concept` FOREIGN KEY (`value_coded`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `answer_concept_drug` FOREIGN KEY (`value_drug`) REFERENCES `drug` (`drug_id`),
  CONSTRAINT `encounter_observations` FOREIGN KEY (`encounter_id`) REFERENCES `encounter` (`encounter_id`),
  CONSTRAINT `obs_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `obs_enterer` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `obs_grouping_id` FOREIGN KEY (`obs_group_id`) REFERENCES `obs` (`obs_id`),
  CONSTRAINT `obs_location` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `obs_name_of_coded_value` FOREIGN KEY (`value_coded_name_id`) REFERENCES `concept_name` (`concept_name_id`),
  CONSTRAINT `obs_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `person_obs` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`) ON UPDATE CASCADE,
  CONSTRAINT `previous_version` FOREIGN KEY (`previous_version`) REFERENCES `obs` (`obs_id`),
  CONSTRAINT `user_who_voided_obs` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `obs`
--

LOCK TABLES `obs` WRITE;
/*!40000 ALTER TABLE `obs` DISABLE KEYS */;
/*!40000 ALTER TABLE `obs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_frequency`
--

DROP TABLE IF EXISTS `order_frequency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_frequency` (
  `order_frequency_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL,
  `frequency_per_day` double DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`order_frequency_id`),
  UNIQUE KEY `concept_id` (`concept_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `order_frequency_creator_fk` (`creator`),
  KEY `order_frequency_retired_by_fk` (`retired_by`),
  KEY `order_frequency_changed_by_fk` (`changed_by`),
  CONSTRAINT `order_frequency_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `order_frequency_concept_id_fk` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `order_frequency_creator_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `order_frequency_retired_by_fk` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_frequency`
--

LOCK TABLES `order_frequency` WRITE;
/*!40000 ALTER TABLE `order_frequency` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_frequency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_type`
--

DROP TABLE IF EXISTS `order_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_type` (
  `order_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` text,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `java_class_name` varchar(255) NOT NULL,
  `parent` int(11) DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  PRIMARY KEY (`order_type_id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `order_type_uuid_index` (`uuid`),
  KEY `order_type_retired_status` (`retired`),
  KEY `type_created_by` (`creator`),
  KEY `user_who_retired_order_type` (`retired_by`),
  KEY `order_type_changed_by` (`changed_by`),
  KEY `order_type_parent_order_type` (`parent`),
  CONSTRAINT `order_type_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `order_type_parent_order_type` FOREIGN KEY (`parent`) REFERENCES `order_type` (`order_type_id`),
  CONSTRAINT `type_created_by` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_order_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_type`
--

LOCK TABLES `order_type` WRITE;
/*!40000 ALTER TABLE `order_type` DISABLE KEYS */;
INSERT INTO `order_type` VALUES (2,'Drug Order','An order for a medication to be given to the patient',1,'2010-05-12 00:00:00',0,NULL,NULL,NULL,'131168f4-15f5-102d-96e4-000c29c2a5d7','org.openmrs.DrugOrder',NULL,NULL,NULL),(3,'Test Order','Order type for test orders',1,'2014-03-09 00:00:00',0,NULL,NULL,NULL,'52a447d3-a64a-11e3-9aeb-50e549534c5e','org.openmrs.TestOrder',NULL,NULL,NULL);
/*!40000 ALTER TABLE `order_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_type_class_map`
--

DROP TABLE IF EXISTS `order_type_class_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_type_class_map` (
  `order_type_id` int(11) NOT NULL,
  `concept_class_id` int(11) NOT NULL,
  PRIMARY KEY (`order_type_id`,`concept_class_id`),
  UNIQUE KEY `concept_class_id` (`concept_class_id`),
  CONSTRAINT `fk_order_type_class_map_concept_class_concept_class_id` FOREIGN KEY (`concept_class_id`) REFERENCES `concept_class` (`concept_class_id`),
  CONSTRAINT `fk_order_type_order_type_id` FOREIGN KEY (`order_type_id`) REFERENCES `order_type` (`order_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_type_class_map`
--

LOCK TABLES `order_type_class_map` WRITE;
/*!40000 ALTER TABLE `order_type_class_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_type_class_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_type_id` int(11) NOT NULL DEFAULT '0',
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `orderer` int(11) NOT NULL,
  `encounter_id` int(11) NOT NULL,
  `instructions` text,
  `date_activated` datetime DEFAULT NULL,
  `auto_expire_date` datetime DEFAULT NULL,
  `date_stopped` datetime DEFAULT NULL,
  `order_reason` int(11) DEFAULT NULL,
  `order_reason_non_coded` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `patient_id` int(11) NOT NULL,
  `accession_number` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `urgency` varchar(50) NOT NULL DEFAULT 'ROUTINE',
  `order_number` varchar(50) NOT NULL,
  `previous_order_id` int(11) DEFAULT NULL,
  `order_action` varchar(50) NOT NULL,
  `comment_to_fulfiller` varchar(1024) DEFAULT NULL,
  `care_setting` int(11) NOT NULL,
  `scheduled_date` datetime DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `orders_uuid_index` (`uuid`),
  KEY `order_creator` (`creator`),
  KEY `orders_in_encounter` (`encounter_id`),
  KEY `type_of_order` (`order_type_id`),
  KEY `order_for_patient` (`patient_id`),
  KEY `user_who_voided_order` (`voided_by`),
  KEY `previous_order_id_order_id` (`previous_order_id`),
  KEY `orders_care_setting` (`care_setting`),
  KEY `discontinued_because` (`order_reason`),
  KEY `fk_orderer_provider` (`orderer`),
  CONSTRAINT `discontinued_because` FOREIGN KEY (`order_reason`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `fk_orderer_provider` FOREIGN KEY (`orderer`) REFERENCES `provider` (`provider_id`),
  CONSTRAINT `order_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `order_for_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON UPDATE CASCADE,
  CONSTRAINT `orders_care_setting` FOREIGN KEY (`care_setting`) REFERENCES `care_setting` (`care_setting_id`),
  CONSTRAINT `orders_in_encounter` FOREIGN KEY (`encounter_id`) REFERENCES `encounter` (`encounter_id`),
  CONSTRAINT `previous_order_id_order_id` FOREIGN KEY (`previous_order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `type_of_order` FOREIGN KEY (`order_type_id`) REFERENCES `order_type` (`order_type_id`),
  CONSTRAINT `user_who_voided_order` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient` (
  `patient_id` int(11) NOT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `allergy_status` varchar(50) NOT NULL DEFAULT 'Unknown',
  PRIMARY KEY (`patient_id`),
  KEY `user_who_changed_pat` (`changed_by`),
  KEY `user_who_created_patient` (`creator`),
  KEY `user_who_voided_patient` (`voided_by`),
  CONSTRAINT `person_id_for_patient` FOREIGN KEY (`patient_id`) REFERENCES `person` (`person_id`) ON UPDATE CASCADE,
  CONSTRAINT `user_who_changed_pat` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_patient` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_patient` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_identifier`
--

DROP TABLE IF EXISTS `patient_identifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_identifier` (
  `patient_identifier_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL DEFAULT '0',
  `identifier` varchar(50) NOT NULL DEFAULT '',
  `identifier_type` int(11) NOT NULL DEFAULT '0',
  `preferred` tinyint(1) NOT NULL DEFAULT '0',
  `location_id` int(11) DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `date_changed` datetime DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`patient_identifier_id`),
  UNIQUE KEY `patient_identifier_uuid_index` (`uuid`),
  KEY `identifier_name` (`identifier`),
  KEY `idx_patient_identifier_patient` (`patient_id`),
  KEY `identifier_creator` (`creator`),
  KEY `defines_identifier_type` (`identifier_type`),
  KEY `patient_identifier_ibfk_2` (`location_id`),
  KEY `identifier_voider` (`voided_by`),
  KEY `patient_identifier_changed_by` (`changed_by`),
  CONSTRAINT `defines_identifier_type` FOREIGN KEY (`identifier_type`) REFERENCES `patient_identifier_type` (`patient_identifier_type_id`),
  CONSTRAINT `identifier_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `identifier_voider` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `patient_identifier_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `patient_identifier_ibfk_2` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_identifier`
--

LOCK TABLES `patient_identifier` WRITE;
/*!40000 ALTER TABLE `patient_identifier` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient_identifier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_identifier_type`
--

DROP TABLE IF EXISTS `patient_identifier_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_identifier_type` (
  `patient_identifier_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `description` text,
  `format` varchar(255) DEFAULT NULL,
  `check_digit` tinyint(1) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `required` tinyint(1) NOT NULL DEFAULT '0',
  `format_description` varchar(255) DEFAULT NULL,
  `validator` varchar(200) DEFAULT NULL,
  `location_behavior` varchar(50) DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `uniqueness_behavior` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`patient_identifier_type_id`),
  UNIQUE KEY `patient_identifier_type_uuid_index` (`uuid`),
  KEY `patient_identifier_type_retired_status` (`retired`),
  KEY `type_creator` (`creator`),
  KEY `user_who_retired_patient_identifier_type` (`retired_by`),
  CONSTRAINT `type_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_patient_identifier_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_identifier_type`
--

LOCK TABLES `patient_identifier_type` WRITE;
/*!40000 ALTER TABLE `patient_identifier_type` DISABLE KEYS */;
INSERT INTO `patient_identifier_type` VALUES (1,'OpenMRS Identification Number','Unique number used in OpenMRS','',1,1,'2005-09-22 00:00:00',0,NULL,'org.openmrs.patient.impl.LuhnIdentifierValidator',NULL,0,NULL,NULL,NULL,'8d793bee-c2cc-11de-8d13-0010c6dffd0f',NULL),(2,'Old Identification Number','Number given out prior to the OpenMRS system (No check digit)','',0,1,'2005-09-22 00:00:00',0,NULL,NULL,NULL,0,NULL,NULL,NULL,'8d79403a-c2cc-11de-8d13-0010c6dffd0f',NULL);
/*!40000 ALTER TABLE `patient_identifier_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_program`
--

DROP TABLE IF EXISTS `patient_program`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_program` (
  `patient_program_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL DEFAULT '0',
  `program_id` int(11) NOT NULL DEFAULT '0',
  `date_enrolled` datetime DEFAULT NULL,
  `date_completed` datetime DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL,
  `outcome_concept_id` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`patient_program_id`),
  UNIQUE KEY `patient_program_uuid_index` (`uuid`),
  KEY `user_who_changed` (`changed_by`),
  KEY `patient_program_creator` (`creator`),
  KEY `patient_in_program` (`patient_id`),
  KEY `program_for_patient` (`program_id`),
  KEY `user_who_voided_patient_program` (`voided_by`),
  KEY `patient_program_location_id` (`location_id`),
  KEY `patient_program_outcome_concept_id_fk` (`outcome_concept_id`),
  CONSTRAINT `patient_in_program` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON UPDATE CASCADE,
  CONSTRAINT `patient_program_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `patient_program_location_id` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `patient_program_outcome_concept_id_fk` FOREIGN KEY (`outcome_concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `program_for_patient` FOREIGN KEY (`program_id`) REFERENCES `program` (`program_id`),
  CONSTRAINT `user_who_changed` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_patient_program` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_program`
--

LOCK TABLES `patient_program` WRITE;
/*!40000 ALTER TABLE `patient_program` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient_program` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_state`
--

DROP TABLE IF EXISTS `patient_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_state` (
  `patient_state_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_program_id` int(11) NOT NULL DEFAULT '0',
  `state` int(11) NOT NULL DEFAULT '0',
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`patient_state_id`),
  UNIQUE KEY `patient_state_uuid_index` (`uuid`),
  KEY `patient_state_changer` (`changed_by`),
  KEY `patient_state_creator` (`creator`),
  KEY `patient_program_for_state` (`patient_program_id`),
  KEY `state_for_patient` (`state`),
  KEY `patient_state_voider` (`voided_by`),
  CONSTRAINT `patient_program_for_state` FOREIGN KEY (`patient_program_id`) REFERENCES `patient_program` (`patient_program_id`),
  CONSTRAINT `patient_state_changer` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `patient_state_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `patient_state_voider` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `state_for_patient` FOREIGN KEY (`state`) REFERENCES `program_workflow_state` (`program_workflow_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_state`
--

LOCK TABLES `patient_state` WRITE;
/*!40000 ALTER TABLE `patient_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `person_id` int(11) NOT NULL AUTO_INCREMENT,
  `gender` varchar(50) DEFAULT '',
  `birthdate` date DEFAULT NULL,
  `birthdate_estimated` tinyint(1) NOT NULL DEFAULT '0',
  `dead` tinyint(1) NOT NULL DEFAULT '0',
  `death_date` datetime DEFAULT NULL,
  `cause_of_death` int(11) DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `deathdate_estimated` tinyint(1) NOT NULL DEFAULT '0',
  `birthtime` time DEFAULT NULL,
  PRIMARY KEY (`person_id`),
  UNIQUE KEY `person_uuid_index` (`uuid`),
  KEY `person_birthdate` (`birthdate`),
  KEY `person_death_date` (`death_date`),
  KEY `person_died_because` (`cause_of_death`),
  KEY `user_who_changed_person` (`changed_by`),
  KEY `user_who_created_person` (`creator`),
  KEY `user_who_voided_person` (`voided_by`),
  CONSTRAINT `person_died_because` FOREIGN KEY (`cause_of_death`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `user_who_changed_person` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_person` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_person` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'M',NULL,0,0,NULL,NULL,NULL,'2005-01-01 00:00:00',NULL,NULL,0,NULL,NULL,NULL,'918c3c5d-41c2-11e6-ac33-00e04c680037',0,NULL);
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_address`
--

DROP TABLE IF EXISTS `person_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_address` (
  `person_address_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) DEFAULT NULL,
  `preferred` tinyint(1) NOT NULL DEFAULT '0',
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city_village` varchar(255) DEFAULT NULL,
  `state_province` varchar(255) DEFAULT NULL,
  `postal_code` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `latitude` varchar(50) DEFAULT NULL,
  `longitude` varchar(50) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `county_district` varchar(255) DEFAULT NULL,
  `address3` varchar(255) DEFAULT NULL,
  `address4` varchar(255) DEFAULT NULL,
  `address5` varchar(255) DEFAULT NULL,
  `address6` varchar(255) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`person_address_id`),
  UNIQUE KEY `person_address_uuid_index` (`uuid`),
  KEY `patient_address_creator` (`creator`),
  KEY `address_for_person` (`person_id`),
  KEY `patient_address_void` (`voided_by`),
  KEY `person_address_changed_by` (`changed_by`),
  CONSTRAINT `address_for_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`) ON UPDATE CASCADE,
  CONSTRAINT `patient_address_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `patient_address_void` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `person_address_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_address`
--

LOCK TABLES `person_address` WRITE;
/*!40000 ALTER TABLE `person_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `person_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_attribute`
--

DROP TABLE IF EXISTS `person_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_attribute` (
  `person_attribute_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) NOT NULL DEFAULT '0',
  `value` varchar(50) NOT NULL DEFAULT '',
  `person_attribute_type_id` int(11) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`person_attribute_id`),
  UNIQUE KEY `person_attribute_uuid_index` (`uuid`),
  KEY `attribute_changer` (`changed_by`),
  KEY `attribute_creator` (`creator`),
  KEY `defines_attribute_type` (`person_attribute_type_id`),
  KEY `identifies_person` (`person_id`),
  KEY `attribute_voider` (`voided_by`),
  CONSTRAINT `attribute_changer` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `attribute_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `attribute_voider` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `defines_attribute_type` FOREIGN KEY (`person_attribute_type_id`) REFERENCES `person_attribute_type` (`person_attribute_type_id`),
  CONSTRAINT `identifies_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_attribute`
--

LOCK TABLES `person_attribute` WRITE;
/*!40000 ALTER TABLE `person_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `person_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_attribute_type`
--

DROP TABLE IF EXISTS `person_attribute_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_attribute_type` (
  `person_attribute_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `description` text,
  `format` varchar(50) DEFAULT NULL,
  `foreign_key` int(11) DEFAULT NULL,
  `searchable` tinyint(1) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `edit_privilege` varchar(255) DEFAULT NULL,
  `sort_weight` double DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`person_attribute_type_id`),
  UNIQUE KEY `person_attribute_type_uuid_index` (`uuid`),
  KEY `attribute_is_searchable` (`searchable`),
  KEY `name_of_attribute` (`name`),
  KEY `person_attribute_type_retired_status` (`retired`),
  KEY `attribute_type_changer` (`changed_by`),
  KEY `attribute_type_creator` (`creator`),
  KEY `user_who_retired_person_attribute_type` (`retired_by`),
  KEY `privilege_which_can_edit` (`edit_privilege`),
  CONSTRAINT `attribute_type_changer` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `attribute_type_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `privilege_which_can_edit` FOREIGN KEY (`edit_privilege`) REFERENCES `privilege` (`privilege`),
  CONSTRAINT `user_who_retired_person_attribute_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_attribute_type`
--

LOCK TABLES `person_attribute_type` WRITE;
/*!40000 ALTER TABLE `person_attribute_type` DISABLE KEYS */;
INSERT INTO `person_attribute_type` VALUES (1,'Race','Group of persons related by common descent or heredity','java.lang.String',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,6,'8d871386-c2cc-11de-8d13-0010c6dffd0f'),(2,'Birthplace','Location of persons birth','java.lang.String',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,0,'8d8718c2-c2cc-11de-8d13-0010c6dffd0f'),(3,'Citizenship','Country of which this person is a member','java.lang.String',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,1,'8d871afc-c2cc-11de-8d13-0010c6dffd0f'),(4,'Mother\'s Name','First or last name of this person\'s mother','java.lang.String',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,5,'8d871d18-c2cc-11de-8d13-0010c6dffd0f'),(5,'Civil Status','Marriage status of this person','org.openmrs.Concept',1054,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,2,'8d871f2a-c2cc-11de-8d13-0010c6dffd0f'),(6,'Health District','District/region in which this patient\' home health center resides','java.lang.String',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,4,'8d872150-c2cc-11de-8d13-0010c6dffd0f'),(7,'Health Center','Specific Location of this person\'s home health center.','org.openmrs.Location',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,3,'8d87236c-c2cc-11de-8d13-0010c6dffd0f');
/*!40000 ALTER TABLE `person_attribute_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_merge_log`
--

DROP TABLE IF EXISTS `person_merge_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_merge_log` (
  `person_merge_log_id` int(11) NOT NULL AUTO_INCREMENT,
  `winner_person_id` int(11) NOT NULL,
  `loser_person_id` int(11) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `merged_data` longtext NOT NULL,
  `uuid` char(38) NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`person_merge_log_id`),
  UNIQUE KEY `person_merge_log_unique_uuid` (`uuid`),
  KEY `person_merge_log_winner` (`winner_person_id`),
  KEY `person_merge_log_loser` (`loser_person_id`),
  KEY `person_merge_log_creator` (`creator`),
  KEY `person_merge_log_changed_by_fk` (`changed_by`),
  KEY `person_merge_log_voided_by_fk` (`voided_by`),
  CONSTRAINT `person_merge_log_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `person_merge_log_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `person_merge_log_loser` FOREIGN KEY (`loser_person_id`) REFERENCES `person` (`person_id`),
  CONSTRAINT `person_merge_log_voided_by_fk` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `person_merge_log_winner` FOREIGN KEY (`winner_person_id`) REFERENCES `person` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_merge_log`
--

LOCK TABLES `person_merge_log` WRITE;
/*!40000 ALTER TABLE `person_merge_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `person_merge_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_name`
--

DROP TABLE IF EXISTS `person_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_name` (
  `person_name_id` int(11) NOT NULL AUTO_INCREMENT,
  `preferred` tinyint(1) NOT NULL DEFAULT '0',
  `person_id` int(11) NOT NULL,
  `prefix` varchar(50) DEFAULT NULL,
  `given_name` varchar(50) DEFAULT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `family_name_prefix` varchar(50) DEFAULT NULL,
  `family_name` varchar(50) DEFAULT NULL,
  `family_name2` varchar(50) DEFAULT NULL,
  `family_name_suffix` varchar(50) DEFAULT NULL,
  `degree` varchar(50) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`person_name_id`),
  UNIQUE KEY `person_name_uuid_index` (`uuid`),
  KEY `first_name` (`given_name`),
  KEY `last_name` (`family_name`),
  KEY `middle_name` (`middle_name`),
  KEY `family_name2` (`family_name2`),
  KEY `user_who_made_name` (`creator`),
  KEY `name_for_person` (`person_id`),
  KEY `user_who_voided_name` (`voided_by`),
  CONSTRAINT `name_for_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`) ON UPDATE CASCADE,
  CONSTRAINT `user_who_made_name` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_name` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_name`
--

LOCK TABLES `person_name` WRITE;
/*!40000 ALTER TABLE `person_name` DISABLE KEYS */;
INSERT INTO `person_name` VALUES (1,1,1,NULL,'Super','',NULL,'User',NULL,NULL,NULL,1,'2005-01-01 00:00:00',0,NULL,NULL,NULL,NULL,NULL,'918ce59b-41c2-11e6-ac33-00e04c680037');
/*!40000 ALTER TABLE `person_name` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `privilege`
--

DROP TABLE IF EXISTS `privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `privilege` (
  `privilege` varchar(255) NOT NULL,
  `description` text,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`privilege`),
  UNIQUE KEY `privilege_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `privilege`
--

LOCK TABLES `privilege` WRITE;
/*!40000 ALTER TABLE `privilege` DISABLE KEYS */;
INSERT INTO `privilege` VALUES ('Add Allergies','Add allergies','89f88b3d-f2f2-4f1c-bd72-977281159bc6'),('Add Cohorts','Able to add a cohort to the system','918d1267-41c2-11e6-ac33-00e04c680037'),('Add Concept Proposals','Able to add concept proposals to the system','918d1361-41c2-11e6-ac33-00e04c680037'),('Add Encounters','Able to add patient encounters','918d13b2-41c2-11e6-ac33-00e04c680037'),('Add HL7 Inbound Archive','Able to add an HL7 archive item','f89229b6-cc68-4f7d-8332-e11f248a65e4'),('Add HL7 Inbound Exception','Able to add an HL7 error item','b114c604-58fe-403b-a0da-355fe941ff5b'),('Add HL7 Inbound Queue','Able to add an HL7 Queue item','a63432e7-f885-4985-8977-a4d1152003ab'),('Add HL7 Source','Able to add an HL7 Source','953b30e6-9685-4bc0-9a98-e82fd0726ddb'),('Add Observations','Able to add patient observations','918d13f9-41c2-11e6-ac33-00e04c680037'),('Add Orders','Able to add orders','918d1439-41c2-11e6-ac33-00e04c680037'),('Add Patient Identifiers','Able to add patient identifiers','918d147a-41c2-11e6-ac33-00e04c680037'),('Add Patient Programs','Able to add patients to programs','918d14bf-41c2-11e6-ac33-00e04c680037'),('Add Patients','Able to add patients','918d14ff-41c2-11e6-ac33-00e04c680037'),('Add People','Able to add person objects','918d153d-41c2-11e6-ac33-00e04c680037'),('Add Problems','Add problems','2cc8d4a0-0e6a-492c-bd83-dfd98f2d427c'),('Add Relationships','Able to add relationships','918d156c-41c2-11e6-ac33-00e04c680037'),('Add Report Objects','Able to add report objects','918d1598-41c2-11e6-ac33-00e04c680037'),('Add Reports','Able to add reports','918d15c6-41c2-11e6-ac33-00e04c680037'),('Add Users','Able to add users to OpenMRS','918d15f0-41c2-11e6-ac33-00e04c680037'),('Add Visits','Able to add visits','2103b792-c9fe-437c-ac77-fa919bb5e351'),('App: adminui.configuremetadata','Able to access configure metadata app','ab115877-8134-4283-8675-cad671594c01'),('App: appointmentschedulingui.appointmentTypes','Access to the Manage Service Types app','4d1e8545-04fe-4421-b708-53216234658e'),('App: appointmentschedulingui.home','Ability to view the appointment app on the home page','6fbeda88-6013-463d-85e4-f4c1077edac2'),('App: appointmentschedulingui.providerSchedules','Access to the Manage Provider Schedules app','7d3517a5-2dc4-434d-8e06-1aabcae353d8'),('App: appointmentschedulingui.viewAppointments','Access to Manage Appointments and Daily Scheduled Appointments (but not the ability to book appointments from these pages)','d06c956e-c1ee-40c8-b519-ce84145465fa'),('App: coreapps.activeVisits','Able to access the active visits app','11af4d48-757d-488c-bbe1-c76d66bd02fa'),('App: coreapps.dataManagement','Able to access data management apps','8f1f30c2-1ef8-4f6f-920f-d05ed3f04f66'),('App: coreapps.findPatient','Able to access the find patient app','ad06c331-ea34-46f9-b82a-d85c47b7dd14'),('App: coreapps.mergePatient','Able to access the merge patient app','5f3f6fc2-83a7-4e47-93f1-9b460fcd9ed2'),('App: coreapps.patientDashboard','Able to access the patient dashboard','d372be72-a89b-40ef-80f3-8cd07fcd3727'),('App: coreapps.patientVisits','Able to access the patient visits screen','b96e58e9-c62d-475f-8f08-8b0171aabeb1'),('App: referenceapplication.legacyAdmin','Able to access the advanced administration app','2560e81f-5af4-4691-850b-551cda7049e0'),('App: referenceapplication.manageApps','Able to manage app definitions','92d3ca15-7945-4476-8309-67c45ac7b136'),('App: referenceapplication.styleGuide','Able to access the style guide app','e2c8b068-e845-4240-8846-1aac05ec3e63'),('App: referenceapplication.vitals','Able to access the vitals app','a740c488-c9f6-4ef7-b8f8-48b7f5f841d8'),('App: registrationapp.registerPatient','Able to access the register patient app','781905c9-5c7e-48f5-8e31-ef8e351659d2'),('Assign System Developer Role','Able to assign System Developer role','982b188e-944a-4943-996d-ea2be62ea804'),('Configure Visits','Able to choose encounter visit handler and enable/disable encounter visits','1eb87d4f-ac6f-4d1e-9729-63a6ef962b47'),('Delete Cohorts','Able to add a cohort to the system','918d161c-41c2-11e6-ac33-00e04c680037'),('Delete Concept Proposals','Able to delete concept proposals from the system','918d1649-41c2-11e6-ac33-00e04c680037'),('Delete Encounters','Able to delete patient encounters','918d1678-41c2-11e6-ac33-00e04c680037'),('Delete HL7 Inbound Archive','Able to delete/retire an HL7 archive item','69558ee3-2c86-4b5d-8d30-7f3b41a4dd6b'),('Delete HL7 Inbound Exception','Able to delete an HL7 archive item','4dfe1026-2e1a-41b6-8209-fa321e10f304'),('Delete HL7 Inbound Queue','Able to delete an HL7 Queue item','cc62ca53-45ae-4a1a-a412-bbaa8e894186'),('Delete Notes','Able to delete patient notes','576c3fdf-5bae-42dc-9e1c-cde552a5c72f'),('Delete Observations','Able to delete patient observations','918d16a3-41c2-11e6-ac33-00e04c680037'),('Delete Orders','Able to delete orders','918d16cd-41c2-11e6-ac33-00e04c680037'),('Delete Patient Identifiers','Able to delete patient identifiers','918d16f7-41c2-11e6-ac33-00e04c680037'),('Delete Patient Programs','Able to delete patients from programs','918d1727-41c2-11e6-ac33-00e04c680037'),('Delete Patients','Able to delete patients','918d1753-41c2-11e6-ac33-00e04c680037'),('Delete People','Able to delete objects','918d177e-41c2-11e6-ac33-00e04c680037'),('Delete Relationships','Able to delete relationships','918d17a9-41c2-11e6-ac33-00e04c680037'),('Delete Report Objects','Able to delete report objects','918d17d6-41c2-11e6-ac33-00e04c680037'),('Delete Reports','Able to delete reports','918d1800-41c2-11e6-ac33-00e04c680037'),('Delete Users','Able to delete users in OpenMRS','918d1842-41c2-11e6-ac33-00e04c680037'),('Delete Visits','Able to delete visits','7331c183-04a2-4444-a06b-8be53d8c86cb'),('Edit Allergies','Able to edit allergies','2d50e4ae-d9b9-44f6-b43d-8f849aabdeb8'),('Edit Cohorts','Able to add a cohort to the system','918d186d-41c2-11e6-ac33-00e04c680037'),('Edit Concept Proposals','Able to edit concept proposals in the system','918d1895-41c2-11e6-ac33-00e04c680037'),('Edit Encounters','Able to edit patient encounters','918d18c1-41c2-11e6-ac33-00e04c680037'),('Edit Notes','Able to edit patient notes','db2918fe-a89a-4c42-a4cc-7d88bc1482a9'),('Edit Observations','Able to edit patient observations','918d18e9-41c2-11e6-ac33-00e04c680037'),('Edit Orders','Able to edit orders','918d1912-41c2-11e6-ac33-00e04c680037'),('Edit Patient Identifiers','Able to edit patient identifiers','918d193c-41c2-11e6-ac33-00e04c680037'),('Edit Patient Programs','Able to edit patients in programs','918d1966-41c2-11e6-ac33-00e04c680037'),('Edit Patients','Able to edit patients','918d1991-41c2-11e6-ac33-00e04c680037'),('Edit People','Able to edit person objects','918d19bb-41c2-11e6-ac33-00e04c680037'),('Edit Problems','Able to edit problems','cef92e7f-b826-4b88-a489-bf06b86915d1'),('Edit Relationships','Able to edit relationships','918d19e4-41c2-11e6-ac33-00e04c680037'),('Edit Report Objects','Able to edit report objects','918d1a0d-41c2-11e6-ac33-00e04c680037'),('Edit Reports','Able to edit reports','918d1a37-41c2-11e6-ac33-00e04c680037'),('Edit User Passwords','Able to change the passwords of users in OpenMRS','918d1a62-41c2-11e6-ac33-00e04c680037'),('Edit Users','Able to edit users in OpenMRS','918d1a8d-41c2-11e6-ac33-00e04c680037'),('Edit Visits','Able to edit visits','7250f50a-049f-432b-9748-bb0e287908e1'),('Form Entry','Allows user to access Form Entry pages/functions','918d1abe-41c2-11e6-ac33-00e04c680037'),('Generate Batch of Identifiers','Allows user to generate a batch of identifiers to a file for offline use','d2315cd4-9631-46d7-a070-7fa2450394cc'),('Get Allergies','Able to get allergies','d05118c6-2490-4d78-a41a-390e3596a220'),('Get Care Settings','Able to get Care Settings','f7389b08-5aa3-4670-82c3-395dd24acf00'),('Get Concept Classes','Able to get concept classes','d05118c6-2490-4d78-a41a-390e3596a238'),('Get Concept Datatypes','Able to get concept datatypes','d05118c6-2490-4d78-a41a-390e3596a237'),('Get Concept Map Types','Able to get concept map types','d05118c6-2490-4d78-a41a-390e3596a230'),('Get Concept Proposals','Able to get concept proposals to the system','d05118c6-2490-4d78-a41a-390e3596a250'),('Get Concept Reference Terms','Able to get concept reference terms','d05118c6-2490-4d78-a41a-390e3596a229'),('Get Concept Sources','Able to get concept sources','d05118c6-2490-4d78-a41a-390e3596a231'),('Get Concepts','Able to get concept entries','d05118c6-2490-4d78-a41a-390e3596a251'),('Get Database Changes','Able to get database changes from the admin screen','d05118c6-2490-4d78-a41a-390e3596a222'),('Get Encounter Roles','Able to get encounter roles','d05118c6-2490-4d78-a41a-390e3596a210'),('Get Encounter Types','Able to get encounter types','d05118c6-2490-4d78-a41a-390e3596a247'),('Get Encounters','Able to get patient encounters','d05118c6-2490-4d78-a41a-390e3596a248'),('Get Field Types','Able to get field types','d05118c6-2490-4d78-a41a-390e3596a234'),('Get Forms','Able to get forms','d05118c6-2490-4d78-a41a-390e3596a240'),('Get Global Properties','Able to get global properties on the administration screen','d05118c6-2490-4d78-a41a-390e3596a226'),('Get HL7 Inbound Archive','Able to get an HL7 archive item','d05118c6-2490-4d78-a41a-390e3596a217'),('Get HL7 Inbound Exception','Able to get an HL7 error item','d05118c6-2490-4d78-a41a-390e3596a216'),('Get HL7 Inbound Queue','Able to get an HL7 Queue item','d05118c6-2490-4d78-a41a-390e3596a218'),('Get HL7 Source','Able to get an HL7 Source','d05118c6-2490-4d78-a41a-390e3596a219'),('Get Identifier Types','Able to get patient identifier types','d05118c6-2490-4d78-a41a-390e3596a239'),('Get Location Attribute Types','Able to get location attribute types','d05118c6-2490-4d78-a41a-390e3596a212'),('Get Locations','Able to get locations','d05118c6-2490-4d78-a41a-390e3596a246'),('Get Notes','Able to get patient notes','1acf0788-16ea-4a35-8eca-ecfbe4ef3b92'),('Get Observations','Able to get patient observations','d05118c6-2490-4d78-a41a-390e3596a245'),('Get Order Frequencies','Able to get Order Frequencies','ef2c1dff-a87a-43fb-b117-2014c081632b'),('Get Order Types','Able to get order types','d05118c6-2490-4d78-a41a-390e3596a233'),('Get Orders','Able to get orders','d05118c6-2490-4d78-a41a-390e3596a241'),('Get Patient Cohorts','Able to get patient cohorts','d05118c6-2490-4d78-a41a-390e3596a242'),('Get Patient Identifiers','Able to get patient identifiers','d05118c6-2490-4d78-a41a-390e3596a243'),('Get Patient Programs','Able to get which programs that patients are in','d05118c6-2490-4d78-a41a-390e3596a227'),('Get Patients','Able to get patients','d05118c6-2490-4d78-a41a-390e3596a244'),('Get People','Able to get person objects','d05118c6-2490-4d78-a41a-390e3596a224'),('Get Person Attribute Types','Able to get person attribute types','d05118c6-2490-4d78-a41a-390e3596a225'),('Get Privileges','Able to get user privileges','d05118c6-2490-4d78-a41a-390e3596a236'),('Get Problems','Able to get problems','d05118c6-2490-4d78-a41a-390e3596a221'),('Get Programs','Able to get patient programs','d05118c6-2490-4d78-a41a-390e3596a228'),('Get Providers','Able to get Providers','d05118c6-2490-4d78-a41a-390e3596a211'),('Get Relationship Types','Able to get relationship types','d05118c6-2490-4d78-a41a-390e3596a232'),('Get Relationships','Able to get relationships','d05118c6-2490-4d78-a41a-390e3596a223'),('Get Roles','Able to get user roles','d05118c6-2490-4d78-a41a-390e3596a235'),('Get Users','Able to get users in OpenMRS','d05118c6-2490-4d78-a41a-390e3596a249'),('Get Visit Attribute Types','Able to get visit attribute types','d05118c6-2490-4d78-a41a-390e3596a213'),('Get Visit Types','Able to get visit types','d05118c6-2490-4d78-a41a-390e3596a215'),('Get Visits','Able to get visits','d05118c6-2490-4d78-a41a-390e3596a214'),('Manage Address Templates','Able to add/edit/delete address templates','871325bc-aeda-46bf-871f-4b9d8fcad15c'),('Manage Alerts','Able to add/edit/delete user alerts','918d1ae6-41c2-11e6-ac33-00e04c680037'),('Manage Appointment Types','Ability to add/edit/purge appointment types','7f274ba2-e7e1-458d-bfcc-de921cc564ed'),('Manage Appointments Settings','Ability to manage the appointment scheduling module settings','8188fcb0-c7f5-4cfa-b644-b244a9b84cf4'),('Manage Atlas Data','Allows user to set/update/delete atlas data','52de6157-b6a9-43d3-bacd-38082efb328b'),('Manage Auto Generation Options','Allows user add, edit, and remove auto-generation options','03f781fe-b648-433e-aa9c-e89cbb265306'),('Manage Cohort Definitions','Add/Edit/Remove Cohort Definitions','c5ca5f59-273f-416e-8baf-289b8a1bcbaf'),('Manage Concept Classes','Able to add/edit/retire concept classes','918d1b18-41c2-11e6-ac33-00e04c680037'),('Manage Concept Datatypes','Able to add/edit/retire concept datatypes','918d1b43-41c2-11e6-ac33-00e04c680037'),('Manage Concept Map Types','Able to add/edit/retire concept map types','a8020153-17c2-4a9b-a516-7bc8972fe68f'),('Manage Concept Name tags','Able to add/edit/delete concept name tags','c4b704c5-6dad-4dbe-b3e5-e767312e918c'),('Manage Concept Reference Terms','Able to add/edit/retire reference terms','e21db429-ff89-427d-ae81-e7822a39a85a'),('Manage Concept Sources','Able to add/edit/delete concept sources','918d1b6f-41c2-11e6-ac33-00e04c680037'),('Manage Concept Stop Words','Able to view/add/remove the concept stop words','c64973c6-7561-4fc8-ab2b-1ea26a982813'),('Manage Concepts','Able to add/edit/delete concept entries','918d1b9b-41c2-11e6-ac33-00e04c680037'),('Manage Data Set Definitions','Add/Edit/Remove Data Set Definitions','3c5388f7-f5ae-40b5-9378-966f99fcf9ef'),('Manage Dimension Definitions','Add/Edit/Remove Dimension Definitions','d0868868-e622-4ca0-b1ab-741fcb1dc1e6'),('Manage Encounter Roles','Able to add/edit/retire encounter roles','ac403193-701d-4160-b5cf-c98f399df684'),('Manage Encounter Types','Able to add/edit/delete encounter types','918d1bc7-41c2-11e6-ac33-00e04c680037'),('Manage Field Types','Able to add/edit/retire field types','918d1bf1-41c2-11e6-ac33-00e04c680037'),('Manage FormEntry XSN','Allows user to upload and edit the xsns stored on the server','918d1c1b-41c2-11e6-ac33-00e04c680037'),('Manage Forms','Able to add/edit/delete forms','918d1c46-41c2-11e6-ac33-00e04c680037'),('Manage Global Properties','Able to add/edit global properties','918d1c72-41c2-11e6-ac33-00e04c680037'),('Manage HL7 Messages','Able to add/edit/delete HL7 messages','7404e594-30e6-4139-9db1-a8cd8f5ca3b5'),('Manage Identifier Sources','Allows user add, edit, and remove identifier sources','8de373dd-f520-4299-90b5-39e1a1873aa8'),('Manage Identifier Types','Able to add/edit/delete patient identifier types','918d1c9d-41c2-11e6-ac33-00e04c680037'),('Manage Implementation Id','Able to view/add/edit the implementation id for the system','d893e44b-fa5c-4c28-9465-ea02cd2c583b'),('Manage Indicator Definitions','Add/Edit/Remove Indicator Definitions','f64401c7-cd44-4a19-8a3d-c8febcf96f88'),('Manage Location Attribute Types','Able to add/edit/retire location attribute types','e7ec7e6c-f358-4520-825f-3dc8b7951f47'),('Manage Location Tags','Able to add/edit/delete location tags','21f4220d-0f39-46d5-a9fc-c1d72aa46dd3'),('Manage Locations','Able to add/edit/delete locations','918d1cc7-41c2-11e6-ac33-00e04c680037'),('Manage Modules','Able to add/remove modules to the system','918d1cf1-41c2-11e6-ac33-00e04c680037'),('Manage Order Frequencies','Able to add/edit/retire Order Frequencies','94a83b73-4454-425c-977c-40f0a83f7e16'),('Manage Order Types','Able to add/edit/retire order types','918d1dca-41c2-11e6-ac33-00e04c680037'),('Manage Person Attribute Types','Able to add/edit/delete person attribute types','918d1dfb-41c2-11e6-ac33-00e04c680037'),('Manage Privileges','Able to add/edit/delete privileges','918d1eac-41c2-11e6-ac33-00e04c680037'),('Manage Programs','Able to add/view/delete patient programs','918d1edc-41c2-11e6-ac33-00e04c680037'),('Manage Provider Schedules','Ability to add/edit/purge appointment blocks','57773485-9db5-41bc-9f46-a9bf869e6374'),('Manage Providers','Able to edit Provider','6c96e5c2-fd58-46e9-a25d-4bf71ecdcc65'),('Manage Relationship Types','Able to add/edit/retire relationship types','918d1f08-41c2-11e6-ac33-00e04c680037'),('Manage Relationships','Able to add/edit/delete relationships','918d1f38-41c2-11e6-ac33-00e04c680037'),('Manage Report Definitions','Add/Edit/Remove Report Definitions','5746f6a3-4c91-4975-bb5f-a810965d7c53'),('Manage Report Designs','Add/Edit/Remove Report Designs','6cff30a1-a8b7-41b1-b7cd-17673260198b'),('Manage Reports','Base privilege for add/edit/delete reporting definitions. This gives access to the administrative menus, but you need to grant additional privileges to manage each specific type of reporting definition','8b3e4b56-4a5f-4057-ad00-0d5ed01d9714'),('Manage RESTWS','Allows to configure RESTWS module','6536fcc4-aaba-42f0-8cf6-596e68ceb81e'),('Manage Roles','Able to add/edit/delete user roles','918d1f63-41c2-11e6-ac33-00e04c680037'),('Manage Scheduled Report Tasks','Manage Task Scheduling in Reporting Module','a49aeb0d-488d-40ab-b3e7-b3ad9f3cffea'),('Manage Scheduler','Able to add/edit/remove scheduled tasks','918d1f8e-41c2-11e6-ac33-00e04c680037'),('Manage Search Index','Able to manage the search index','d6d52473-2ea4-4391-abeb-2b12ca43df8f'),('Manage synonym group','Ability to manage synonym group','635293cb-079f-4a4c-90b2-9b2b1ff8eaa9'),('Manage Synonym Groups','Ability to manage synonym groups','b05c5fde-04bc-41f2-a324-5c56ea7dbe6d'),('Manage Token Registrations','Allows to create/update/delete token registrations','b9ae795a-7c08-4a38-b730-fc2c33149281'),('Manage Visit Attribute Types','Able to add/edit/retire visit attribute types','757710db-ce94-47ef-93e2-63bbb125ce99'),('Manage Visit Types','Able to add/edit/delete visit types','463a91f7-0358-41c4-80da-9ef6647d2736'),('Metadata Mapping','Allows the user to prepare concepts for publishing/subscribing','9009d95b-d7a4-482f-a9f1-a4941d86e8ea'),('Patient Dashboard - View Chart Search Section','Ability view Chart Search Section in Patient Dashboard','630ef8cb-37fb-4fb7-922c-de9af88ec624'),('Patient Dashboard - View Demographics Section','Able to view the \'Demographics\' tab on the patient dashboard','918d1fbb-41c2-11e6-ac33-00e04c680037'),('Patient Dashboard - View Encounters Section','Able to view the \'Encounters\' tab on the patient dashboard','918d1feb-41c2-11e6-ac33-00e04c680037'),('Patient Dashboard - View Forms Section','Allows user to view the Forms tab on the patient dashboard','918d201f-41c2-11e6-ac33-00e04c680037'),('Patient Dashboard - View Graphs Section','Able to view the \'Graphs\' tab on the patient dashboard','918d2052-41c2-11e6-ac33-00e04c680037'),('Patient Dashboard - View Overview Section','Able to view the \'Overview\' tab on the patient dashboard','918d2085-41c2-11e6-ac33-00e04c680037'),('Patient Dashboard - View Patient Summary','Able to view the \'Summary\' tab on the patient dashboard','918d20b7-41c2-11e6-ac33-00e04c680037'),('Patient Dashboard - View Regimen Section','Able to view the \'Regimen\' tab on the patient dashboard','918d20e6-41c2-11e6-ac33-00e04c680037'),('Patient Dashboard - View Visits Section','Able to view the \'Visits\' tab on the patient dashboard','e629c843-4aba-431a-bcdb-eb598d5a2126'),('Patient Overview - View Allergies','Able to view the Allergies portlet on the patient overview tab','d05118c6-2490-4d78-a41a-390e3596a261'),('Patient Overview - View Patient Actions','Able to view the Patient Actions portlet on the patient overview tab','d05118c6-2490-4d78-a41a-390e3596a264'),('Patient Overview - View Patient Flags','Able to view the \'Patient Flags\' portlet on the patient dashboard\'s overview tab','fb1d4dae-f75c-4001-9903-9623aeb62672'),('Patient Overview - View Problem List','Able to view the Problem List portlet on the patient overview tab','d05118c6-2490-4d78-a41a-390e3596a260'),('Patient Overview - View Programs','Able to view the Programs portlet on the patient overview tab','d05118c6-2490-4d78-a41a-390e3596a263'),('Patient Overview - View Relationships','Able to view the Relationships portlet on the patient overview tab','d05118c6-2490-4d78-a41a-390e3596a262'),('Provider Management - Admin','Allows access to admin pages of the provider management module','d924bb9e-7be1-41ae-ac1d-207edf206fba'),('Provider Management API','Allows access to all provider management service and provider suggestion service API method','b6aeb11b-4ac6-492d-a1fe-71534e8ec8ff'),('Provider Management API - Read-only','Allows access to all provider management service and provider suggestion service API methods that are read-only','919b6ff1-7c57-427a-b41b-cd67bd97953b'),('Provider Management Dashboard - Edit Patients','Allows access to editing patient information on the provider management dashboard','38e121ae-a542-42bb-a26a-f77a9794b005'),('Provider Management Dashboard - Edit Providers','Allows access to editing provider information on the provider management dashboard','cef0c163-b320-4576-9928-f313072f9c69'),('Provider Management Dashboard - View Historical','Allows access to viewing historical patient (if user has view patients right) and supervisee information on the provider management dashboard','2b52f8ad-5fe6-471a-aa7a-217cbeb14ec7'),('Provider Management Dashboard - View Patients','Allows access to viewing patient information on the provider management dashboard','78ae126d-f86a-47a1-9c12-fe6f28de11e8'),('Provider Management Dashboard - View Providers','Allows access to viewing provider information on the provider management dashboard','49866b57-cfd5-43d3-a70d-ee06a2abe9a2'),('Purge Field Types','Able to purge field types','918d211b-41c2-11e6-ac33-00e04c680037'),('Remove Allergies','Remove allergies','5b2c4a91-83ab-4e9d-b4b3-f6d42ee55c72'),('Remove Problems','Remove problems','56376c60-8917-4411-8809-49b05deb56bb'),('Request Appointments','Ability to request new appointments','aa090ed6-3742-4297-bef2-6b3325f4cbc2'),('Run Chart Search commands','Ability to run Solr commands','f42ab6c9-5332-4308-816c-36a1c9b1f83c'),('Run Reports','Schedule the running of a report','f685ae68-1da2-4854-9008-7e4199fc66fe'),('Schedule Appointments','Ability to schedule new appointments','a90c1b7c-199d-44ac-8223-59b68168394f'),('Share Metadata','Allows user to export and import metadata','6a610af2-2138-43d3-ba6b-84531239de9c'),('Squeezing Appointments','Ability to override the constraints and schedule appointments into full slots','f18ecde1-3c9d-417b-bc14-f728edae7358'),('Task: appointmentschedulingui.bookAppointments','Ability book appointments, cancel appointments, and flag appointments as needs reschedule; Access to the Manage Rescheduled app','c4a6cdd8-82a5-414f-9a73-ff51c1df18fa'),('Task: appointmentschedulingui.overbookAppointments','Ability to overbook time slots','09207d96-fd34-4a64-821d-6b521a6a4425'),('Task: appointmentschedulingui.requestAppointments','Ability to request an appointment for a patient','0735d5e9-6a33-4fc5-88c2-899c6b788954'),('Task: appointmentschedulingui.viewConfidential','Ability to see details of confidential appointments (you also need View Appointments privileges)','64231c8f-67cd-4fac-8ed6-01ed6e01a21b'),('Task: coreapps.createRetrospectiveVisit','Able to create a retrospective visit','4b8fe2be-7c18-4181-9163-829b62bbeef9'),('Task: coreapps.createVisit','Able to create a visit','e424d5fe-e1ee-436c-a5ca-57cc84cedd37'),('Task: coreapps.endVisit','Able to end a visit','456e6776-7692-4e59-a35c-a5e30fb65267'),('Task: coreapps.mergeVisits','Able to merge visits','c866b9a8-87fd-4e57-b2cf-bd9a9b5daf75'),('Task: Modify Allergies','Able to add, edit, delete allergies','c0308303-f56e-47f1-bf6d-5356b9ca5819'),('Update Appointment Status','Ability to modify appointment states','8bd7d6bb-178e-4737-b457-c18907907444'),('Update HL7 Inbound Archive','Able to update an HL7 archive item','9b667036-5534-4768-894d-154f8159e5c2'),('Update HL7 Inbound Exception','Able to update an HL7 archive item','4bf90e10-87fe-4789-9d2a-566bd26dd974'),('Update HL7 Inbound Queue','Able to update an HL7 Queue item','8c066463-559e-47ff-b167-cf9f164ff263'),('Update HL7 Source','Able to update an HL7 Source','c8afcad6-e4d8-42d3-afcb-2baf44b79195'),('Upload Batch of Identifiers','Allows user to upload a batch of identifiers','9739d0ad-7801-4c1f-86e0-07c415243853'),('Upload XSN','Allows user to upload/overwrite the XSNs defined for forms','918d214a-41c2-11e6-ac33-00e04c680037'),('View Administration Functions','Able to view the \'Administration\' link in the navigation bar','918d2174-41c2-11e6-ac33-00e04c680037'),('View Allergies','Able to view allergies in OpenMRS','918d21a2-41c2-11e6-ac33-00e04c680037'),('View Appointment Types','Ability to view appointment types','aebf4018-6c10-47ca-86fa-3973edba3c2d'),('View Appointments','Ability to view appointments','7840fa79-4439-4e04-bf98-3e0732b14652'),('View Appointments Blocks','Ability to view the appointment blocks','9f1c27b3-4899-41a3-b7bd-c56bd8a79072'),('View Appointments Statistics','Ability to view the appointments statistics page','d554ad3f-86c8-479e-a8b1-618af4b37149'),('View Calculations','Allows to view Calculations','63664e35-3fab-42dc-afca-2ded4c16f25a'),('View Concept Classes','Able to view concept classes','918d228d-41c2-11e6-ac33-00e04c680037'),('View Concept Datatypes','Able to view concept datatypes','918d22c1-41c2-11e6-ac33-00e04c680037'),('View Concept Proposals','Able to view concept proposals to the system','918d22f0-41c2-11e6-ac33-00e04c680037'),('View Concept Sources','Able to view concept sources','918d231d-41c2-11e6-ac33-00e04c680037'),('View Concepts','Able to view concept entries','918d2349-41c2-11e6-ac33-00e04c680037'),('View Data Entry Statistics','Able to view data entry statistics from the admin screen','918d2374-41c2-11e6-ac33-00e04c680037'),('View Encounter Types','Able to view encounter types','918d23a2-41c2-11e6-ac33-00e04c680037'),('View Encounters','Able to view patient encounters','918d23cf-41c2-11e6-ac33-00e04c680037'),('View Field Types','Able to view field types','918d23fc-41c2-11e6-ac33-00e04c680037'),('View Forms','Able to view forms','918d2427-41c2-11e6-ac33-00e04c680037'),('View Global Properties','Able to view global properties on the administration screen','918d24f4-41c2-11e6-ac33-00e04c680037'),('View Identifier Types','Able to view patient identifier types','918d2521-41c2-11e6-ac33-00e04c680037'),('View Locations','Able to view locations','918d2549-41c2-11e6-ac33-00e04c680037'),('View Navigation Menu','Ability to see the navigation menu','918d2574-41c2-11e6-ac33-00e04c680037'),('View Observations','Able to view patient observations','918d259e-41c2-11e6-ac33-00e04c680037'),('View Order Types','Able to view order types','918d25c8-41c2-11e6-ac33-00e04c680037'),('View Orders','Able to view orders','918d25f2-41c2-11e6-ac33-00e04c680037'),('View Patient Appointment History','Ability to view the appointment history tab on the patient\'s dashboard','924716e3-653a-4a57-92db-9452e97bfd76'),('View Patient Cohorts','Able to view patient cohorts','918d261c-41c2-11e6-ac33-00e04c680037'),('View Patient Identifiers','Able to view patient identifiers','918d26d2-41c2-11e6-ac33-00e04c680037'),('View Patient Programs','Able to see which programs that patients are in','918d26ff-41c2-11e6-ac33-00e04c680037'),('View Patients','Able to view patients','918d272a-41c2-11e6-ac33-00e04c680037'),('View People','Able to view person objects','918d2754-41c2-11e6-ac33-00e04c680037'),('View Person Attribute Types','Able to view person attribute types','918d277e-41c2-11e6-ac33-00e04c680037'),('View Privileges','Able to view user privileges','918d27aa-41c2-11e6-ac33-00e04c680037'),('View Problems','Able to view problems in OpenMRS','918d27d3-41c2-11e6-ac33-00e04c680037'),('View Programs','Able to view patient programs','918d27fd-41c2-11e6-ac33-00e04c680037'),('View Provider Schedules','Ability to View Provider Schedules','af9efeae-bae9-48a6-b08e-1654f4428d62'),('View Relationship Types','Able to view relationship types','918d2827-41c2-11e6-ac33-00e04c680037'),('View Relationships','Able to view relationships','918d2853-41c2-11e6-ac33-00e04c680037'),('View Report Objects','Able to view report objects','918d287e-41c2-11e6-ac33-00e04c680037'),('View Reports','Able to view reports','918d28a9-41c2-11e6-ac33-00e04c680037'),('View RESTWS','Gives access to RESTWS in administration','865dd557-b686-4e6b-baf1-6c577afb5483'),('View Roles','Able to view user roles','918d28d5-41c2-11e6-ac33-00e04c680037'),('View Token Registrations','Allows to view token registrations','d5e2e1fa-dcc9-4fbc-8f09-280a63abb8a7'),('View Unpublished Forms','Able to view and fill out unpublished forms','918d2900-41c2-11e6-ac33-00e04c680037'),('View Users','Able to view users in OpenMRS','918d292e-41c2-11e6-ac33-00e04c680037');
/*!40000 ALTER TABLE `privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `program`
--

DROP TABLE IF EXISTS `program`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program` (
  `program_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `outcomes_concept_id` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL,
  `description` text,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`program_id`),
  UNIQUE KEY `program_uuid_index` (`uuid`),
  KEY `user_who_changed_program` (`changed_by`),
  KEY `program_concept` (`concept_id`),
  KEY `program_creator` (`creator`),
  KEY `program_outcomes_concept_id_fk` (`outcomes_concept_id`),
  CONSTRAINT `program_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `program_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `program_outcomes_concept_id_fk` FOREIGN KEY (`outcomes_concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `user_who_changed_program` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `program`
--

LOCK TABLES `program` WRITE;
/*!40000 ALTER TABLE `program` DISABLE KEYS */;
/*!40000 ALTER TABLE `program` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `program_workflow`
--

DROP TABLE IF EXISTS `program_workflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_workflow` (
  `program_workflow_id` int(11) NOT NULL AUTO_INCREMENT,
  `program_id` int(11) NOT NULL DEFAULT '0',
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`program_workflow_id`),
  UNIQUE KEY `program_workflow_uuid_index` (`uuid`),
  KEY `workflow_changed_by` (`changed_by`),
  KEY `workflow_concept` (`concept_id`),
  KEY `workflow_creator` (`creator`),
  KEY `program_for_workflow` (`program_id`),
  CONSTRAINT `program_for_workflow` FOREIGN KEY (`program_id`) REFERENCES `program` (`program_id`),
  CONSTRAINT `workflow_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `workflow_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `workflow_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `program_workflow`
--

LOCK TABLES `program_workflow` WRITE;
/*!40000 ALTER TABLE `program_workflow` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_workflow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `program_workflow_state`
--

DROP TABLE IF EXISTS `program_workflow_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_workflow_state` (
  `program_workflow_state_id` int(11) NOT NULL AUTO_INCREMENT,
  `program_workflow_id` int(11) NOT NULL DEFAULT '0',
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `initial` tinyint(1) NOT NULL DEFAULT '0',
  `terminal` tinyint(1) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`program_workflow_state_id`),
  UNIQUE KEY `program_workflow_state_uuid_index` (`uuid`),
  KEY `state_changed_by` (`changed_by`),
  KEY `state_concept` (`concept_id`),
  KEY `state_creator` (`creator`),
  KEY `workflow_for_state` (`program_workflow_id`),
  CONSTRAINT `state_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `state_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `state_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `workflow_for_state` FOREIGN KEY (`program_workflow_id`) REFERENCES `program_workflow` (`program_workflow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `program_workflow_state`
--

LOCK TABLES `program_workflow_state` WRITE;
/*!40000 ALTER TABLE `program_workflow_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_workflow_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provider`
--

DROP TABLE IF EXISTS `provider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `provider` (
  `provider_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `provider_role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`provider_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `provider_changed_by_fk` (`changed_by`),
  KEY `provider_person_id_fk` (`person_id`),
  KEY `provider_retired_by_fk` (`retired_by`),
  KEY `provider_creator_fk` (`creator`),
  KEY `provider_role_id` (`provider_role_id`),
  CONSTRAINT `provider_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `provider_creator_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `provider_ibfk_1` FOREIGN KEY (`provider_role_id`) REFERENCES `providermanagement_provider_role` (`provider_role_id`),
  CONSTRAINT `provider_person_id_fk` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`),
  CONSTRAINT `provider_retired_by_fk` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provider`
--

LOCK TABLES `provider` WRITE;
/*!40000 ALTER TABLE `provider` DISABLE KEYS */;
/*!40000 ALTER TABLE `provider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provider_attribute`
--

DROP TABLE IF EXISTS `provider_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `provider_attribute` (
  `provider_attribute_id` int(11) NOT NULL AUTO_INCREMENT,
  `provider_id` int(11) NOT NULL,
  `attribute_type_id` int(11) NOT NULL,
  `value_reference` text NOT NULL,
  `uuid` char(38) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`provider_attribute_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `provider_attribute_provider_fk` (`provider_id`),
  KEY `provider_attribute_attribute_type_id_fk` (`attribute_type_id`),
  KEY `provider_attribute_creator_fk` (`creator`),
  KEY `provider_attribute_changed_by_fk` (`changed_by`),
  KEY `provider_attribute_voided_by_fk` (`voided_by`),
  CONSTRAINT `provider_attribute_attribute_type_id_fk` FOREIGN KEY (`attribute_type_id`) REFERENCES `provider_attribute_type` (`provider_attribute_type_id`),
  CONSTRAINT `provider_attribute_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `provider_attribute_creator_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `provider_attribute_provider_fk` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`provider_id`),
  CONSTRAINT `provider_attribute_voided_by_fk` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provider_attribute`
--

LOCK TABLES `provider_attribute` WRITE;
/*!40000 ALTER TABLE `provider_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `provider_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provider_attribute_type`
--

DROP TABLE IF EXISTS `provider_attribute_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `provider_attribute_type` (
  `provider_attribute_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `datatype` varchar(255) DEFAULT NULL,
  `datatype_config` text,
  `preferred_handler` varchar(255) DEFAULT NULL,
  `handler_config` text,
  `min_occurs` int(11) NOT NULL,
  `max_occurs` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`provider_attribute_type_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `provider_attribute_type_creator_fk` (`creator`),
  KEY `provider_attribute_type_changed_by_fk` (`changed_by`),
  KEY `provider_attribute_type_retired_by_fk` (`retired_by`),
  CONSTRAINT `provider_attribute_type_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `provider_attribute_type_creator_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `provider_attribute_type_retired_by_fk` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provider_attribute_type`
--

LOCK TABLES `provider_attribute_type` WRITE;
/*!40000 ALTER TABLE `provider_attribute_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `provider_attribute_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `providermanagement_provider_role`
--

DROP TABLE IF EXISTS `providermanagement_provider_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `providermanagement_provider_role` (
  `provider_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`provider_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `providermanagement_provider_role`
--

LOCK TABLES `providermanagement_provider_role` WRITE;
/*!40000 ALTER TABLE `providermanagement_provider_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `providermanagement_provider_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `providermanagement_provider_role_provider_attribute_type`
--

DROP TABLE IF EXISTS `providermanagement_provider_role_provider_attribute_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `providermanagement_provider_role_provider_attribute_type` (
  `provider_role_id` int(11) NOT NULL,
  `provider_attribute_type_id` int(11) NOT NULL,
  KEY `provider_role_id` (`provider_role_id`),
  KEY `provider_attribute_type_id` (`provider_attribute_type_id`),
  CONSTRAINT `providermanagement_prpat_provider_attribute_type_fk` FOREIGN KEY (`provider_attribute_type_id`) REFERENCES `provider_attribute_type` (`provider_attribute_type_id`),
  CONSTRAINT `providermanagement_prpat_provider_role_fk` FOREIGN KEY (`provider_role_id`) REFERENCES `providermanagement_provider_role` (`provider_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `providermanagement_provider_role_provider_attribute_type`
--

LOCK TABLES `providermanagement_provider_role_provider_attribute_type` WRITE;
/*!40000 ALTER TABLE `providermanagement_provider_role_provider_attribute_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `providermanagement_provider_role_provider_attribute_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `providermanagement_provider_role_relationship_type`
--

DROP TABLE IF EXISTS `providermanagement_provider_role_relationship_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `providermanagement_provider_role_relationship_type` (
  `provider_role_id` int(11) NOT NULL,
  `relationship_type_id` int(11) NOT NULL,
  KEY `provider_role_id` (`provider_role_id`),
  KEY `relationship_type_id` (`relationship_type_id`),
  CONSTRAINT `providermanagement_provider_role_relationship_type_ibfk_1` FOREIGN KEY (`provider_role_id`) REFERENCES `providermanagement_provider_role` (`provider_role_id`),
  CONSTRAINT `providermanagement_provider_role_relationship_type_ibfk_2` FOREIGN KEY (`relationship_type_id`) REFERENCES `relationship_type` (`relationship_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `providermanagement_provider_role_relationship_type`
--

LOCK TABLES `providermanagement_provider_role_relationship_type` WRITE;
/*!40000 ALTER TABLE `providermanagement_provider_role_relationship_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `providermanagement_provider_role_relationship_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `providermanagement_provider_role_supervisee_provider_role`
--

DROP TABLE IF EXISTS `providermanagement_provider_role_supervisee_provider_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `providermanagement_provider_role_supervisee_provider_role` (
  `provider_role_id` int(11) NOT NULL,
  `supervisee_provider_role_id` int(11) NOT NULL,
  KEY `provider_role_id` (`provider_role_id`),
  KEY `supervisee_provider_role_id` (`supervisee_provider_role_id`),
  CONSTRAINT `providermanagement_prspr_provider_role_fk` FOREIGN KEY (`provider_role_id`) REFERENCES `providermanagement_provider_role` (`provider_role_id`),
  CONSTRAINT `providermanagement_prspr_supervisee_role_fk` FOREIGN KEY (`supervisee_provider_role_id`) REFERENCES `providermanagement_provider_role` (`provider_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `providermanagement_provider_role_supervisee_provider_role`
--

LOCK TABLES `providermanagement_provider_role_supervisee_provider_role` WRITE;
/*!40000 ALTER TABLE `providermanagement_provider_role_supervisee_provider_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `providermanagement_provider_role_supervisee_provider_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `providermanagement_provider_suggestion`
--

DROP TABLE IF EXISTS `providermanagement_provider_suggestion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `providermanagement_provider_suggestion` (
  `provider_suggestion_id` int(11) NOT NULL AUTO_INCREMENT,
  `criteria` varchar(5000) NOT NULL,
  `evaluator` varchar(255) NOT NULL,
  `relationship_type_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`provider_suggestion_id`),
  KEY `relationship_type_id` (`relationship_type_id`),
  CONSTRAINT `providermanagement_provider_suggestion_ibfk_1` FOREIGN KEY (`relationship_type_id`) REFERENCES `relationship_type` (`relationship_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `providermanagement_provider_suggestion`
--

LOCK TABLES `providermanagement_provider_suggestion` WRITE;
/*!40000 ALTER TABLE `providermanagement_provider_suggestion` DISABLE KEYS */;
/*!40000 ALTER TABLE `providermanagement_provider_suggestion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `providermanagement_supervision_suggestion`
--

DROP TABLE IF EXISTS `providermanagement_supervision_suggestion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `providermanagement_supervision_suggestion` (
  `supervision_suggestion_id` int(11) NOT NULL AUTO_INCREMENT,
  `criteria` varchar(5000) NOT NULL,
  `evaluator` varchar(255) NOT NULL,
  `provider_role_id` int(11) NOT NULL,
  `suggestion_type` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`supervision_suggestion_id`),
  KEY `provider_role_id` (`provider_role_id`),
  CONSTRAINT `providermanagement_supervision_suggestion_ibfk_1` FOREIGN KEY (`provider_role_id`) REFERENCES `providermanagement_provider_role` (`provider_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `providermanagement_supervision_suggestion`
--

LOCK TABLES `providermanagement_supervision_suggestion` WRITE;
/*!40000 ALTER TABLE `providermanagement_supervision_suggestion` DISABLE KEYS */;
/*!40000 ALTER TABLE `providermanagement_supervision_suggestion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relationship`
--

DROP TABLE IF EXISTS `relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relationship` (
  `relationship_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_a` int(11) NOT NULL,
  `relationship` int(11) NOT NULL DEFAULT '0',
  `person_b` int(11) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `date_changed` datetime DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`relationship_id`),
  UNIQUE KEY `relationship_uuid_index` (`uuid`),
  KEY `relation_creator` (`creator`),
  KEY `person_a_is_person` (`person_a`),
  KEY `person_b_is_person` (`person_b`),
  KEY `relationship_type_id` (`relationship`),
  KEY `relation_voider` (`voided_by`),
  KEY `relationship_changed_by` (`changed_by`),
  CONSTRAINT `person_a_is_person` FOREIGN KEY (`person_a`) REFERENCES `person` (`person_id`),
  CONSTRAINT `person_b_is_person` FOREIGN KEY (`person_b`) REFERENCES `person` (`person_id`),
  CONSTRAINT `relation_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `relation_voider` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `relationship_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `relationship_type_id` FOREIGN KEY (`relationship`) REFERENCES `relationship_type` (`relationship_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relationship`
--

LOCK TABLES `relationship` WRITE;
/*!40000 ALTER TABLE `relationship` DISABLE KEYS */;
/*!40000 ALTER TABLE `relationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relationship_type`
--

DROP TABLE IF EXISTS `relationship_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relationship_type` (
  `relationship_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `a_is_to_b` varchar(50) NOT NULL,
  `b_is_to_a` varchar(50) NOT NULL,
  `preferred` tinyint(1) NOT NULL DEFAULT '0',
  `weight` int(11) NOT NULL DEFAULT '0',
  `description` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`relationship_type_id`),
  UNIQUE KEY `relationship_type_uuid_index` (`uuid`),
  KEY `user_who_created_rel` (`creator`),
  KEY `user_who_retired_relationship_type` (`retired_by`),
  CONSTRAINT `user_who_created_rel` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_relationship_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relationship_type`
--

LOCK TABLES `relationship_type` WRITE;
/*!40000 ALTER TABLE `relationship_type` DISABLE KEYS */;
INSERT INTO `relationship_type` VALUES (1,'Doctor','Patient',0,0,'Relationship from a primary care provider to the patient',1,'2007-05-04 00:00:00',0,NULL,NULL,NULL,'8d919b58-c2cc-11de-8d13-0010c6dffd0f'),(2,'Sibling','Sibling',0,0,'Relationship between brother/sister, brother/brother, and sister/sister',1,'2007-05-04 00:00:00',0,NULL,NULL,NULL,'8d91a01c-c2cc-11de-8d13-0010c6dffd0f'),(3,'Parent','Child',0,0,'Relationship from a mother/father to the child',1,'2007-05-04 00:00:00',0,NULL,NULL,NULL,'8d91a210-c2cc-11de-8d13-0010c6dffd0f'),(4,'Aunt/Uncle','Niece/Nephew',0,0,'Relationship from a parent\'s sibling to a child of that parent',1,'2007-05-04 00:00:00',0,NULL,NULL,NULL,'8d91a3dc-c2cc-11de-8d13-0010c6dffd0f'),(5,'Supervisor','Supervisee',0,0,'Provider supervisor to provider supervisee relationship',1,'2016-07-04 10:38:26',0,NULL,NULL,NULL,'2a5f4ff4-a179-4b8a-aa4c-40f71956ebbc');
/*!40000 ALTER TABLE `relationship_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_object`
--

DROP TABLE IF EXISTS `report_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_object` (
  `report_object_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `report_object_type` varchar(255) NOT NULL,
  `report_object_sub_type` varchar(255) NOT NULL,
  `xml_data` text,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`report_object_id`),
  UNIQUE KEY `report_object_uuid_index` (`uuid`),
  KEY `user_who_changed_report_object` (`changed_by`),
  KEY `report_object_creator` (`creator`),
  KEY `user_who_voided_report_object` (`voided_by`),
  CONSTRAINT `report_object_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_changed_report_object` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_report_object` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_object`
--

LOCK TABLES `report_object` WRITE;
/*!40000 ALTER TABLE `report_object` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_object` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_schema_xml`
--

DROP TABLE IF EXISTS `report_schema_xml`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_schema_xml` (
  `report_schema_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `xml_data` text NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`report_schema_id`),
  UNIQUE KEY `report_schema_xml_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_schema_xml`
--

LOCK TABLES `report_schema_xml` WRITE;
/*!40000 ALTER TABLE `report_schema_xml` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_schema_xml` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reporting_report_design`
--

DROP TABLE IF EXISTS `reporting_report_design`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reporting_report_design` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `renderer_type` varchar(255) NOT NULL,
  `properties` text,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `report_definition_uuid` char(38) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `creator for reporting_report_design` (`creator`),
  KEY `changed_by for reporting_report_design` (`changed_by`),
  KEY `retired_by for reporting_report_design` (`retired_by`),
  KEY `report_definition_uuid for reporting_report_design` (`report_definition_uuid`),
  CONSTRAINT `changed_by for reporting_report_design` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `creator for reporting_report_design` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `retired_by for reporting_report_design` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reporting_report_design`
--

LOCK TABLES `reporting_report_design` WRITE;
/*!40000 ALTER TABLE `reporting_report_design` DISABLE KEYS */;
/*!40000 ALTER TABLE `reporting_report_design` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reporting_report_design_resource`
--

DROP TABLE IF EXISTS `reporting_report_design_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reporting_report_design_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `report_design_id` int(11) NOT NULL DEFAULT '0',
  `content_type` varchar(50) DEFAULT NULL,
  `extension` varchar(20) DEFAULT NULL,
  `contents` longblob,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `report_design_id for reporting_report_design_resource` (`report_design_id`),
  KEY `creator for reporting_report_design_resource` (`creator`),
  KEY `changed_by for reporting_report_design_resource` (`changed_by`),
  KEY `retired_by for reporting_report_design_resource` (`retired_by`),
  CONSTRAINT `changed_by for reporting_report_design_resource` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `creator for reporting_report_design_resource` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `report_design_id for reporting_report_design_resource` FOREIGN KEY (`report_design_id`) REFERENCES `reporting_report_design` (`id`),
  CONSTRAINT `retired_by for reporting_report_design_resource` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reporting_report_design_resource`
--

LOCK TABLES `reporting_report_design_resource` WRITE;
/*!40000 ALTER TABLE `reporting_report_design_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `reporting_report_design_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reporting_report_processor`
--

DROP TABLE IF EXISTS `reporting_report_processor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reporting_report_processor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `processor_type` varchar(255) NOT NULL,
  `configuration` mediumtext,
  `run_on_success` tinyint(1) NOT NULL DEFAULT '1',
  `run_on_error` tinyint(1) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `report_design_id` int(11) DEFAULT NULL,
  `processor_mode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `creator for reporting_report_processor` (`creator`),
  KEY `changed_by for reporting_report_processor` (`changed_by`),
  KEY `retired_by for reporting_report_processor` (`retired_by`),
  KEY `reporting_report_processor_report_design` (`report_design_id`),
  CONSTRAINT `changed_by for reporting_report_processor` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `creator for reporting_report_processor` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `reporting_report_processor_report_design` FOREIGN KEY (`report_design_id`) REFERENCES `reporting_report_design` (`id`),
  CONSTRAINT `retired_by for reporting_report_processor` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reporting_report_processor`
--

LOCK TABLES `reporting_report_processor` WRITE;
/*!40000 ALTER TABLE `reporting_report_processor` DISABLE KEYS */;
/*!40000 ALTER TABLE `reporting_report_processor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reporting_report_request`
--

DROP TABLE IF EXISTS `reporting_report_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reporting_report_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `base_cohort_uuid` char(38) DEFAULT NULL,
  `base_cohort_parameters` text,
  `report_definition_uuid` char(38) NOT NULL,
  `report_definition_parameters` text,
  `renderer_type` varchar(255) NOT NULL,
  `renderer_argument` varchar(255) DEFAULT NULL,
  `requested_by` int(11) NOT NULL DEFAULT '0',
  `request_datetime` datetime NOT NULL,
  `priority` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `evaluation_start_datetime` datetime DEFAULT NULL,
  `evaluation_complete_datetime` datetime DEFAULT NULL,
  `render_complete_datetime` datetime DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `schedule` varchar(100) DEFAULT NULL,
  `process_automatically` tinyint(1) NOT NULL DEFAULT '0',
  `minimum_days_to_preserve` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `requested_by for reporting_report_request` (`requested_by`),
  CONSTRAINT `requested_by for reporting_report_request` FOREIGN KEY (`requested_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reporting_report_request`
--

LOCK TABLES `reporting_report_request` WRITE;
/*!40000 ALTER TABLE `reporting_report_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `reporting_report_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role` varchar(50) NOT NULL DEFAULT '',
  `description` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`role`),
  UNIQUE KEY `role_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('Anonymous','Privileges for non-authenticated users.','774b2af3-6437-4e5a-a310-547554c7c65c'),('Authenticated','Privileges gained once authentication has been established.','f7fd42ef-880e-40c5-972d-e4ae7c990de2'),('Provider','All users with the \'Provider\' role will appear as options in the default Infopath ','8d94f280-c2cc-11de-8d13-0010c6dffd0f'),('System Developer','Developers of the OpenMRS .. have additional access to change fundamental structure of the database model.','8d94f852-c2cc-11de-8d13-0010c6dffd0f');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_privilege`
--

DROP TABLE IF EXISTS `role_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_privilege` (
  `role` varchar(50) NOT NULL DEFAULT '',
  `privilege` varchar(255) NOT NULL,
  PRIMARY KEY (`privilege`,`role`),
  KEY `role_privilege_to_role` (`role`),
  CONSTRAINT `privilege_definitions` FOREIGN KEY (`privilege`) REFERENCES `privilege` (`privilege`),
  CONSTRAINT `role_privilege_to_role` FOREIGN KEY (`role`) REFERENCES `role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_privilege`
--

LOCK TABLES `role_privilege` WRITE;
/*!40000 ALTER TABLE `role_privilege` DISABLE KEYS */;
INSERT INTO `role_privilege` VALUES ('Authenticated','Get Concept Classes'),('Authenticated','Get Concept Datatypes'),('Authenticated','Get Encounter Types'),('Authenticated','Get Field Types'),('Authenticated','Get Global Properties'),('Authenticated','Get Identifier Types'),('Authenticated','Get Locations'),('Authenticated','Get Order Types'),('Authenticated','Get Person Attribute Types'),('Authenticated','Get Privileges'),('Authenticated','Get Relationship Types'),('Authenticated','Get Relationships'),('Authenticated','Get Roles'),('Authenticated','Patient Overview - View Relationships'),('Authenticated','View Concept Classes'),('Authenticated','View Concept Datatypes'),('Authenticated','View Encounter Types'),('Authenticated','View Field Types'),('Authenticated','View Global Properties'),('Authenticated','View Identifier Types'),('Authenticated','View Locations'),('Authenticated','View Order Types'),('Authenticated','View Person Attribute Types'),('Authenticated','View Privileges'),('Authenticated','View Relationship Types'),('Authenticated','View Relationships'),('Authenticated','View Roles');
/*!40000 ALTER TABLE `role_privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_role`
--

DROP TABLE IF EXISTS `role_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_role` (
  `parent_role` varchar(50) NOT NULL DEFAULT '',
  `child_role` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`parent_role`,`child_role`),
  KEY `inherited_role` (`child_role`),
  CONSTRAINT `inherited_role` FOREIGN KEY (`child_role`) REFERENCES `role` (`role`),
  CONSTRAINT `parent_role` FOREIGN KEY (`parent_role`) REFERENCES `role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_role`
--

LOCK TABLES `role_role` WRITE;
/*!40000 ALTER TABLE `role_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scheduler_task_config`
--

DROP TABLE IF EXISTS `scheduler_task_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_task_config` (
  `task_config_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `schedulable_class` text,
  `start_time` datetime DEFAULT NULL,
  `start_time_pattern` varchar(50) DEFAULT NULL,
  `repeat_interval` int(11) NOT NULL DEFAULT '0',
  `start_on_startup` tinyint(1) NOT NULL DEFAULT '0',
  `started` tinyint(1) NOT NULL DEFAULT '0',
  `created_by` int(11) DEFAULT '0',
  `date_created` datetime DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `last_execution_time` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`task_config_id`),
  UNIQUE KEY `scheduler_task_config_uuid_index` (`uuid`),
  KEY `scheduler_changer` (`changed_by`),
  KEY `scheduler_creator` (`created_by`),
  CONSTRAINT `scheduler_changer` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `scheduler_creator` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scheduler_task_config`
--

LOCK TABLES `scheduler_task_config` WRITE;
/*!40000 ALTER TABLE `scheduler_task_config` DISABLE KEYS */;
INSERT INTO `scheduler_task_config` VALUES (2,'Auto Close Visits Task','Stops all active visits that match the visit type(s) specified by the value of the global property \'visits.autoCloseVisitType\'','org.openmrs.scheduler.tasks.AutoCloseVisitsTask','2011-11-28 23:59:59','MM/dd/yyyy HH:mm:ss',86400,0,0,1,'2016-07-04 10:38:03',NULL,NULL,NULL,'8c17b376-1a2b-11e1-a51a-00248140a5eb');
/*!40000 ALTER TABLE `scheduler_task_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scheduler_task_config_property`
--

DROP TABLE IF EXISTS `scheduler_task_config_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_task_config_property` (
  `task_config_property_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` text,
  `task_config_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`task_config_property_id`),
  KEY `task_config_for_property` (`task_config_id`),
  CONSTRAINT `task_config_for_property` FOREIGN KEY (`task_config_id`) REFERENCES `scheduler_task_config` (`task_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scheduler_task_config_property`
--

LOCK TABLES `scheduler_task_config_property` WRITE;
/*!40000 ALTER TABLE `scheduler_task_config_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `scheduler_task_config_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serialized_object`
--

DROP TABLE IF EXISTS `serialized_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serialized_object` (
  `serialized_object_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(5000) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `subtype` varchar(255) NOT NULL,
  `serialization_class` varchar(255) NOT NULL,
  `serialized_data` mediumtext NOT NULL,
  `date_created` datetime NOT NULL,
  `creator` int(11) NOT NULL,
  `date_changed` datetime DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `date_retired` datetime DEFAULT NULL,
  `retired_by` int(11) DEFAULT NULL,
  `retire_reason` varchar(1000) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`serialized_object_id`),
  UNIQUE KEY `serialized_object_uuid_index` (`uuid`),
  KEY `serialized_object_creator` (`creator`),
  KEY `serialized_object_changed_by` (`changed_by`),
  KEY `serialized_object_retired_by` (`retired_by`),
  CONSTRAINT `serialized_object_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `serialized_object_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `serialized_object_retired_by` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serialized_object`
--

LOCK TABLES `serialized_object` WRITE;
/*!40000 ALTER TABLE `serialized_object` DISABLE KEYS */;
/*!40000 ALTER TABLE `serialized_object` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_order`
--

DROP TABLE IF EXISTS `test_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_order` (
  `order_id` int(11) NOT NULL DEFAULT '0',
  `specimen_source` int(11) DEFAULT NULL,
  `laterality` varchar(20) DEFAULT NULL,
  `clinical_history` text,
  `frequency` int(11) DEFAULT NULL,
  `number_of_repeats` int(11) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `test_order_specimen_source_fk` (`specimen_source`),
  KEY `test_order_frequency_fk` (`frequency`),
  CONSTRAINT `test_order_frequency_fk` FOREIGN KEY (`frequency`) REFERENCES `order_frequency` (`order_frequency_id`),
  CONSTRAINT `test_order_order_id_fk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `test_order_specimen_source_fk` FOREIGN KEY (`specimen_source`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_order`
--

LOCK TABLES `test_order` WRITE;
/*!40000 ALTER TABLE `test_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uiframework_user_defined_page_view`
--

DROP TABLE IF EXISTS `uiframework_user_defined_page_view`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uiframework_user_defined_page_view` (
  `page_view_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `template_type` varchar(50) NOT NULL,
  `template_text` mediumtext NOT NULL,
  `uuid` varchar(38) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  PRIMARY KEY (`page_view_id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uiframework_user_defined_page_view`
--

LOCK TABLES `uiframework_user_defined_page_view` WRITE;
/*!40000 ALTER TABLE `uiframework_user_defined_page_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `uiframework_user_defined_page_view` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_property`
--

DROP TABLE IF EXISTS `user_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_property` (
  `user_id` int(11) NOT NULL DEFAULT '0',
  `property` varchar(100) NOT NULL DEFAULT '',
  `property_value` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`user_id`,`property`),
  CONSTRAINT `user_property_to_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_property`
--

LOCK TABLES `user_property` WRITE;
/*!40000 ALTER TABLE `user_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL DEFAULT '0',
  `role` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`role`,`user_id`),
  KEY `user_role_to_users` (`user_id`),
  CONSTRAINT `role_definitions` FOREIGN KEY (`role`) REFERENCES `role` (`role`),
  CONSTRAINT `user_role_to_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,'Provider'),(1,'System Developer');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `system_id` varchar(50) NOT NULL DEFAULT '',
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `salt` varchar(128) DEFAULT NULL,
  `secret_question` varchar(255) DEFAULT NULL,
  `secret_answer` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `person_id` int(11) NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user_who_changed_user` (`changed_by`),
  KEY `user_creator` (`creator`),
  KEY `user_who_retired_this_user` (`retired_by`),
  KEY `person_id_for_user` (`person_id`),
  CONSTRAINT `person_id_for_user` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`),
  CONSTRAINT `user_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_changed_user` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_this_user` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','','4a1750c8607dfa237de36c6305715c223415189','c788c6ad82a157b712392ca695dfcf2eed193d7f',NULL,NULL,1,'2005-01-01 00:00:00',NULL,NULL,1,0,NULL,NULL,NULL,'9bc405bf-41c2-11e6-ac33-00e04c680037'),(2,'daemon','daemon',NULL,NULL,NULL,NULL,1,'2010-04-26 13:25:00',NULL,NULL,1,0,NULL,NULL,NULL,'A4F30A1B-5EB9-11DF-A648-37A07F9C90FB');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visit`
--

DROP TABLE IF EXISTS `visit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit` (
  `visit_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL,
  `visit_type_id` int(11) NOT NULL,
  `date_started` datetime NOT NULL,
  `date_stopped` datetime DEFAULT NULL,
  `indication_concept_id` int(11) DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`visit_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `visit_patient_index` (`patient_id`),
  KEY `visit_type_fk` (`visit_type_id`),
  KEY `visit_location_fk` (`location_id`),
  KEY `visit_creator_fk` (`creator`),
  KEY `visit_voided_by_fk` (`voided_by`),
  KEY `visit_changed_by_fk` (`changed_by`),
  KEY `visit_indication_concept_fk` (`indication_concept_id`),
  CONSTRAINT `visit_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `visit_creator_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `visit_indication_concept_fk` FOREIGN KEY (`indication_concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `visit_location_fk` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `visit_patient_fk` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `visit_type_fk` FOREIGN KEY (`visit_type_id`) REFERENCES `visit_type` (`visit_type_id`),
  CONSTRAINT `visit_voided_by_fk` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visit`
--

LOCK TABLES `visit` WRITE;
/*!40000 ALTER TABLE `visit` DISABLE KEYS */;
/*!40000 ALTER TABLE `visit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visit_attribute`
--

DROP TABLE IF EXISTS `visit_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_attribute` (
  `visit_attribute_id` int(11) NOT NULL AUTO_INCREMENT,
  `visit_id` int(11) NOT NULL,
  `attribute_type_id` int(11) NOT NULL,
  `value_reference` text NOT NULL,
  `uuid` char(38) NOT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` tinyint(1) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`visit_attribute_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `visit_attribute_visit_fk` (`visit_id`),
  KEY `visit_attribute_attribute_type_id_fk` (`attribute_type_id`),
  KEY `visit_attribute_creator_fk` (`creator`),
  KEY `visit_attribute_changed_by_fk` (`changed_by`),
  KEY `visit_attribute_voided_by_fk` (`voided_by`),
  CONSTRAINT `visit_attribute_attribute_type_id_fk` FOREIGN KEY (`attribute_type_id`) REFERENCES `visit_attribute_type` (`visit_attribute_type_id`),
  CONSTRAINT `visit_attribute_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `visit_attribute_creator_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `visit_attribute_visit_fk` FOREIGN KEY (`visit_id`) REFERENCES `visit` (`visit_id`),
  CONSTRAINT `visit_attribute_voided_by_fk` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visit_attribute`
--

LOCK TABLES `visit_attribute` WRITE;
/*!40000 ALTER TABLE `visit_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `visit_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visit_attribute_type`
--

DROP TABLE IF EXISTS `visit_attribute_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_attribute_type` (
  `visit_attribute_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `datatype` varchar(255) DEFAULT NULL,
  `datatype_config` text,
  `preferred_handler` varchar(255) DEFAULT NULL,
  `handler_config` text,
  `min_occurs` int(11) NOT NULL,
  `max_occurs` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`visit_attribute_type_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `visit_attribute_type_creator_fk` (`creator`),
  KEY `visit_attribute_type_changed_by_fk` (`changed_by`),
  KEY `visit_attribute_type_retired_by_fk` (`retired_by`),
  CONSTRAINT `visit_attribute_type_changed_by_fk` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `visit_attribute_type_creator_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `visit_attribute_type_retired_by_fk` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visit_attribute_type`
--

LOCK TABLES `visit_attribute_type` WRITE;
/*!40000 ALTER TABLE `visit_attribute_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `visit_attribute_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visit_type`
--

DROP TABLE IF EXISTS `visit_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visit_type` (
  `visit_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`visit_type_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `visit_type_creator` (`creator`),
  KEY `visit_type_changed_by` (`changed_by`),
  KEY `visit_type_retired_by` (`retired_by`),
  CONSTRAINT `visit_type_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `visit_type_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `visit_type_retired_by` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visit_type`
--

LOCK TABLES `visit_type` WRITE;
/*!40000 ALTER TABLE `visit_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `visit_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-04 10:40:50
