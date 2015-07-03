-- phpMyAdmin SQL Dump
-- version 4.0.10.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 03, 2015 at 06:38 PM
-- Server version: 5.6.24-72.2-log
-- PHP Version: 5.4.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `medicaq1_medicohome`
--
CREATE DATABASE IF NOT EXISTS `medicaq1_medicohome` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `medicaq1_medicohome`;

-- --------------------------------------------------------

--
-- Table structure for table `forgetpass`
--

DROP TABLE IF EXISTS `forgetpass`;
CREATE TABLE IF NOT EXISTS `forgetpass` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(15) NOT NULL,
  `code` varchar(16) NOT NULL,
  `time` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=32 ;

-- --------------------------------------------------------

--
-- Table structure for table `medicine_type`
--

DROP TABLE IF EXISTS `medicine_type`;
CREATE TABLE IF NOT EXISTS `medicine_type` (
  `type_id` int(2) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(300) NOT NULL,
  PRIMARY KEY (`type_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table `medicines`
--

DROP TABLE IF EXISTS `medicines`;
CREATE TABLE IF NOT EXISTS `medicines` (
  `med_id` int(6) NOT NULL AUTO_INCREMENT,
  `generic` varchar(300) NOT NULL,
  `type` int(2) NOT NULL,
  `package_unit` varchar(200) NOT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`med_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=32 ;

-- --------------------------------------------------------

--
-- Table structure for table `order_log`
--

DROP TABLE IF EXISTS `order_log`;
CREATE TABLE IF NOT EXISTS `order_log` (
  `order_id` int(8) NOT NULL AUTO_INCREMENT,
  `user_id` int(6) NOT NULL,
  `user_device` text NOT NULL,
  `order_text` text NOT NULL,
  `coord1` float NOT NULL,
  `coord2` float NOT NULL,
  `address` text NOT NULL,
  `accepted` int(1) NOT NULL DEFAULT '0',
  `order_time` varchar(20) NOT NULL,
  `accepted_time` varchar(20) NOT NULL,
  `chemist_id` int(6) NOT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=121 ;

-- --------------------------------------------------------

--
-- Table structure for table `stores`
--

DROP TABLE IF EXISTS `stores`;
CREATE TABLE IF NOT EXISTS `stores` (
  `store_id` int(6) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `owner` text NOT NULL,
  `address` text NOT NULL,
  `code` varchar(15) NOT NULL,
  `device_id` text NOT NULL,
  `location` text NOT NULL,
  `email` varchar(300) NOT NULL,
  `contact` varchar(12) NOT NULL,
  `minorder` int(6) NOT NULL,
  `express` int(1) NOT NULL DEFAULT '0',
  `rating` float NOT NULL,
  `coord1` float NOT NULL,
  `coord2` float NOT NULL,
  `note` text NOT NULL,
  PRIMARY KEY (`store_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=69 ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(6) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(150) NOT NULL,
  `last_name` varchar(150) NOT NULL,
  `email` varchar(500) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `password` varchar(128) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=39 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
