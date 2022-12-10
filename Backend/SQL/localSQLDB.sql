-- Authors: Chris Costa, Matt Graham

create database if not exists flickpick;

use flickpick;

DROP TABLE IF EXISTS `genres`;
DROP TABLE IF EXISTS `links`;
DROP TABLE IF EXISTS `reviews`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `movies`;
DROP TABLE IF EXISTS `friends`;
DROP TABLE IF EXISTS `messages`;

CREATE TABLE `movies` (
  `id` int UNIQUE NOT NULL auto_increment,
  `title` varchar(255) NOT NULL,
  `minutes` int NOT NULL,
  `year` int NOT NULL,
  `rating` float NOT NULL DEFAULT 0,
  `description` varchar(8000) NOT NULL,
  `picture` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `genres` (
  `id` int UNIQUE NOT NULL auto_increment,
  `genre1` varchar(255),
  `genre2` varchar(255),
  `genre3` varchar(255),
  PRIMARY KEY (`id`),
  FOREIGN KEY(`id`) REFERENCES `movies`(id)
);

CREATE TABLE `links` (
  `id` int UNIQUE NOT NULL auto_increment,
  `netflix` varchar(255),
  `hulu` varchar(255),
  `hbo_max` varchar(255),
  `disney_plus` varchar(255),
  `amazon_prime` varchar(255),
  PRIMARY KEY (`id`),
  FOREIGN KEY(`id`) REFERENCES `movies`(id)
);

CREATE TABLE `users` (
  `id` int auto_increment UNIQUE NOT NULL,
  `username` varchar(255) UNIQUE NOT NULL,
  `email` varchar(255) UNIQUE NOT NULL,
  `user_type` varchar(255) NOT NULL DEFAULT "Guest",
  `password` varchar(255) NOT NULL,
  `movieid` int NOT NULL DEFAULT 1,
  `pfp` VARCHAR(255) NOT NULL,
   PRIMARY KEY (`id`)
   -- FOREIGN KEY(`movieid`) references `movies`(id)
);

CREATE TABLE `friends` (
  `id` int auto_increment UNIQUE NOT NULL,
  `user1` int NOT NULL,
  `user2` int NOT NULL,
  primary key(`id`)
);

CREATE TABLE `reviews` (
	`id` int auto_increment UNIQUE NOT NULL,
    `movieid` int NOT NULL DEFAULT 1,
    `userid` int NOT NULL DEFAULT 1,
    `rating` float NOT NULL DEFAULT 0,
    `reason` varchar(255) NOT NULL,
    FOREIGN KEY(`movieid`) references `movies`(id),
    FOREIGN KEY(`userid`) references `users`(id)
);

CREATE TABLE `messages` (
	`id` int auto_increment UNIQUE NOT NULL,
    `senderid` int NOT NULL,
    `receiverid` int NOT NULL,
    `friendshipid` int NOT NULL,
    `message` varchar(255),
    FOREIGN KEY(`id`) references `users`(id)
);
