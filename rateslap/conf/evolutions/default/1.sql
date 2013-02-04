--- !Ups

CREATE TABLE `ratings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `game` varchar(45) NOT NULL,
  `platform` varchar(10) NOT NULL,
  `type` varchar(45) NOT NULL,
  `date` varchar(10) NOT NULL,
  `country` varchar(45) NOT NULL,
  `rank` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `HASH` (`game`, `date`, `platform`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--- !Downs

DROP TABLE  ratings;