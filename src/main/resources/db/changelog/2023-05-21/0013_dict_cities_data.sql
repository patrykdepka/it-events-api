--liquibase formatted sql
--changeset Patryk Depka:0013
INSERT INTO `dict_city` (`uuid`, `urn_name`, `display_name`)
VALUES
    (UUID(), 'rzeszow', 'Rzeszów'),
    (UUID(), 'warszawa', 'Warszawa');