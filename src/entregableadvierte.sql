CREATE DATABASE  IF NOT EXISTS `adviertedb` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `adviertedb`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: adviertedb
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `idcategoria` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idcategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'alumno'),(2,'administrativo'),(3,'jefe de practica'),(4,'profesor'),(5,'egresado'),(6,'seguridad');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorito`
--

DROP TABLE IF EXISTS `favorito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorito` (
  `usuario_codigo` varchar(8) NOT NULL,
  `incidencia_idincidencia` int NOT NULL,
  `esfavorito` tinyint DEFAULT '0',
  PRIMARY KEY (`usuario_codigo`,`incidencia_idincidencia`),
  KEY `fk_usuario_has_incidencia_incidencia1_idx` (`incidencia_idincidencia`),
  KEY `fk_usuario_has_incidencia_usuario1_idx` (`usuario_codigo`),
  CONSTRAINT `fk_usuario_has_incidencia_incidencia1` FOREIGN KEY (`incidencia_idincidencia`) REFERENCES `incidencia` (`idincidencia`),
  CONSTRAINT `fk_usuario_has_incidencia_usuario1` FOREIGN KEY (`usuario_codigo`) REFERENCES `usuario` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorito`
--

LOCK TABLES `favorito` WRITE;
/*!40000 ALTER TABLE `favorito` DISABLE KEYS */;
/*!40000 ALTER TABLE `favorito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fotoincidencia`
--

DROP TABLE IF EXISTS `fotoincidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fotoincidencia` (
  `foto` mediumblob NOT NULL,
  `idincidencia` int NOT NULL,
  KEY `fk_fotoincidencia_incidencia1_idx` (`idincidencia`),
  CONSTRAINT `fk_fotoincidencia_incidencia1` FOREIGN KEY (`idincidencia`) REFERENCES `incidencia` (`idincidencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fotoincidencia`
--

LOCK TABLES `fotoincidencia` WRITE;
/*!40000 ALTER TABLE `fotoincidencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `fotoincidencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incidencia`
--

DROP TABLE IF EXISTS `incidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `incidencia` (
  `idincidencia` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(45) NOT NULL,
  `descripcion` varchar(300) DEFAULT NULL,
  `fecha` datetime NOT NULL,
  `estado` varchar(10) NOT NULL DEFAULT 'registrado',
  `urgencia` varchar(8) NOT NULL,
  `ubicacion` varchar(100) NOT NULL,
  `tipoincidencia` int NOT NULL,
  `zonapucp` varchar(100) NOT NULL,
  PRIMARY KEY (`idincidencia`),
  KEY `fk_incidencia_tipoincidencia1_idx` (`tipoincidencia`),
  CONSTRAINT `fk_incidencia_tipoincidencia1` FOREIGN KEY (`tipoincidencia`) REFERENCES `tipoincidencia` (`idtipoincidencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incidencia`
--

LOCK TABLES `incidencia` WRITE;
/*!40000 ALTER TABLE `incidencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `incidencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipoincidencia`
--

DROP TABLE IF EXISTS `tipoincidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipoincidencia` (
  `idtipoincidencia` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(60) NOT NULL,
  `color` varchar(45) NOT NULL,
  `logo` blob NOT NULL,
  PRIMARY KEY (`idtipoincidencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipoincidencia`
--

LOCK TABLES `tipoincidencia` WRITE;
/*!40000 ALTER TABLE `tipoincidencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipoincidencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `codigo` varchar(8) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `dni` varchar(8) DEFAULT NULL,
  `celular` varchar(9) DEFAULT NULL,
  `correo` varchar(80) NOT NULL,
  `categoria` int NOT NULL,
  `foto` longblob,
  `suspendido` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`codigo`),
  KEY `fk_usuario_categoria_idx` (`categoria`),
  CONSTRAINT `fk_usuario_categoria` FOREIGN KEY (`categoria`) REFERENCES `categoria` (`idcategoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('19906044','Angel','Bautista','00555304','991022450','eg.angel.bautista@pucp.pe',1,NULL,0),('20196044','Angel Alvaro','Bautista Cutipa','71555304','941022450','a20196044@pucp.edu.pe',1,NULL,0),('20196045','Alvaro','Cutipa','00555300','99192833','aasdasd@pucp.pe',1,NULL,1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-09-13 19:02:11
