--liquibase formatted sql
--changeset Patryk Depka:0003-1
INSERT INTO `app_user` (`uuid`, `first_name`, `last_name`, `email`, `password`, `enabled`, `account_non_locked`)
VALUES
    -- admin@example.com / qwerty
    (UUID(), 'Admin', 'Admin', 'admin@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- organizer@example.com / qwerty
    (UUID(), 'Organizer', 'Organizer', 'organizer@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- user@example.com / qwerty
    (UUID(), 'User', 'User', 'user@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- jankowalski@example.com / qwerty
    (UUID(), 'Jan', 'Kowalski', 'jankowalski@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- patrykkowalski@example.com / qwerty
    (UUID(), 'Patryk', 'Kowalski', 'patrykkowalski@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- mateuszkowalski@example.com / qwerty
    (UUID(), 'Mateusz', 'Kowalski', 'mateuszkowalski@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- zuzannakowalska@example.com / qwerty
    (UUID(), 'Zofia', 'Kowalska', 'zofiakowalska@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- mariakowalska@example.com / qwerty
    (UUID(), 'Maria', 'Kowalska', 'mariakowalska@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- stanislawakowalska@example.com / qwerty
    (UUID(), 'Stanisława', 'Kowalska', 'stanislawakowalska@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- jannowak@example.com / qwerty
    (UUID(), 'Jan', 'Nowak', 'jannowak@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- patryknowak@example.com / qwerty
    (UUID(), 'Patryk', 'Nowak', 'patryknowak@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- mateusznowak@example.com / qwerty
    (UUID(), 'Mateusz', 'Nowak', 'mateusznowak@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- zuzannakowalska@example.com / qwerty
    (UUID(), 'Zofia', 'Nowak', 'zofianowak@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- mariakowalska@example.com / qwerty
    (UUID(), 'Maria', 'Nowak', 'marianowak@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1),
    -- stanislawanowak@example.com / qwerty
    (UUID(), 'Stanisława', 'Nowak', 'stanislawanowak@example.com', '{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe', 1, 1);