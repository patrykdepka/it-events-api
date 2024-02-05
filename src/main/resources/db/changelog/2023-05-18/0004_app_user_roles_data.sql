--liquibase formatted sql
--changeset Patryk Depka:0004-1
INSERT INTO `app_user_roles` (`app_user_id`, `role`)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER'),
       (4, 'ROLE_USER'),
       (5, 'ROLE_USER'),
       (6, 'ROLE_USER'),
       (7, 'ROLE_USER'),
       (8, 'ROLE_USER'),
       (9, 'ROLE_USER'),
       (10, 'ROLE_USER'),
       (11, 'ROLE_USER'),
       (12, 'ROLE_USER'),
       (13, 'ROLE_USER'),
       (14, 'ROLE_USER'),
       (15, 'ROLE_USER');