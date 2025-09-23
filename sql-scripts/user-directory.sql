CREATE DATABASE IF NOT EXISTS `bankingsystem`;
USE `bankingsystem`;

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
    `username` varchar(50) NOT NULL,
    `password` char(60) NOT NULL,
    `enabled` tinyint NOT NULL,
    PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Default username: johndoe
-- Default password: test123

INSERT INTO `users`
VALUES
    ('johndoe', '$2a$12$geC49PgWMdvp3vdZ7f/aUOdDiWxxSYkLaO24yQHwYATVngPhBPXPq', 1);

CREATE TABLE `authorities` (
   `username` varchar(50) NOT NULL,
   `authority` varchar(50) NOT NULL,
   UNIQUE KEY `ix_auth_username` (`username`,`authority`),
   CONSTRAINT `fk_auth_users` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `authorities`
VALUES
    ('johndoe','ROLE_USER')
