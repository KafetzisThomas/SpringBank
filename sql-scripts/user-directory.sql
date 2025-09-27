CREATE DATABASE IF NOT EXISTS springbank;
USE springbank;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
    email varchar(50) NOT NULL,
    password char(60) NOT NULL,
    enabled tinyint NOT NULL,
    PRIMARY KEY (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE authorities (
   email varchar(50) NOT NULL,
   authority varchar(50) NOT NULL,
   UNIQUE KEY ix_auth_email (email, authority),
   CONSTRAINT fk_auth_users FOREIGN KEY (email) REFERENCES users (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Default user
-- email: john@doe.com
-- password: test123

INSERT INTO users VALUES ('john@doe.com', '$2a$12$geC49PgWMdvp3vdZ7f/aUOdDiWxxSYkLaO24yQHwYATVngPhBPXPq', 1);
INSERT INTO authorities VALUES ('john@doe.com','ROLE_USER')
