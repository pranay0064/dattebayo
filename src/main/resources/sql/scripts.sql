create database dattebayo;

use dattebayo;

CREATE TABLE users
(
    id                        varchar(36)  NOT NULL,
    username                 varchar(36)  unique,
    password                 varchar(100) NOT NULL,
    leetcode_profile_url         varchar(56)  DEFAULT NULL,
    visibility                   tinyint(1)        DEFAULT '0',
    PRIMARY KEY (id)
);

alter table users add column email varchar(96) unique;