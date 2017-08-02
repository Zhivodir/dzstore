DROP TABLE IF EXISTS `share_folder_for_user`;
DROP TABLE IF EXISTS `share_file_for_user`;
DROP TABLE IF EXISTS `files`;
DROP TABLE IF EXISTS `folders`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
    `id` int(4) NOT NULL AUTO_INCREMENT,
    `login` varchar(60) NOT NULL,
    `password` varchar(10) NOT NULL,
    `role` varchar(15) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `folders` (
    `id` int(4) NOT NULL AUTO_INCREMENT,
    `name` varchar(60) NOT NULL,
    `user_id` int(4) NOT NULL,
    `parent_id` int(3) NULL,
    `starred` boolean,
    `inbin` boolean,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `files` (
    `id` int(4) NOT NULL AUTO_INCREMENT,
    `name` varchar(60) NOT NULL,
    `size` int(15) NOT NULL,
    `type` varchar(50) NOT NULL,
    `user_id` int(4) NOT NULL,
    `parent_id` int(3) NULL,
    `starred` boolean, 
    `inbin` boolean,
    `data` MEDIUMBLOB NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `share_folder_for_user` (
    `id_folder` int(4) NOT NULL,
    `id_user` int(4) NOT NULL,
    PRIMARY KEY (`id_folder`, `id_user`),
    FOREIGN KEY (`id_folder`) REFERENCES `folders` (id),
FOREIGN KEY (`id_user`) REFERENCES `users` (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `share_file_for_user` (
    `id_file` int(4) NOT NULL,
    `id_user` int(4) NOT NULL,
    PRIMARY KEY (`id_file`, `id_user`),
    FOREIGN KEY (`id_file`) REFERENCES `files` (id),
    FOREIGN KEY (`id_user`) REFERENCES `users` (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


