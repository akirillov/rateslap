--- !Ups

CREATE TABLE `ratings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `game` varchar(45) NOT NULL,
  `platform` varchar(10) NOT NULL,
  `type` varchar(45) NOT NULL,
  `date` varchar(10) NOT NULL,
  `country` varchar(45) NOT NULL,
  `rank` int(11) NOT NULL,
  `hash` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `HASH` (`hash`,`game`,`platform`,`type`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--- !Downs

DROP TABLE  ratings;