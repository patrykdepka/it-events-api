--liquibase formatted sql
--changeset Patryk Depka:0007-1
UPDATE `app_user`
SET date_of_birth='1990-01-01', city = 'Warszawa', bio = 'Cześć! Jestem administratorem tego serwisu.'
WHERE email = 'admin@example.com';
--changeset Patryk Depka:0007-2
UPDATE `app_user`
SET date_of_birth='1990-01-01', city = 'Warszawa', bio = 'Cześć! Jestem organizatorem wydarzeń.'
WHERE email = 'organizer@example.com';
--changeset Patryk Depka:0007-3
UPDATE `app_user`
SET date_of_birth='1990-01-01', city = 'Warszawa', bio = 'Cześć! Jestem użytkownikiem tego serwisu.'
WHERE email = 'user@example.com';
--changeset Patryk Depka:0007-4
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Rzeszów', bio = 'Cześć! Nazywam się Jan Kowalski i mieszkam w Rzeszowie. Jestem absolwentem informatyki na Politechnice Rzeszowskiej.'
WHERE email = 'jankowalski@example.com';
--changeset Patryk Depka:0007-5
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Rzeszów', bio = 'Cześć! Nazywam się Patryk Kowalski i mieszkam w Rzeszowie. Jestem absolwentem informatyki na WSIiZ.'
WHERE email = 'patrykkowalski@example.com';
--changeset Patryk Depka:0007-6
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Rzeszów', bio = 'Cześć! Nazywam się Mateusz Kowalski i mieszkam w Rzeszowie. Jestem absolwentem informatyki na Politechnice Rzeszowskiej.'
WHERE email = 'mateuszkowalski@example.com';
--changeset Patryk Depka:0007-7
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Rzeszów', bio = 'Cześć! Nazywam się Zofia Kowalska i mieszkam w Rzeszowie. Jestem absolwentką informatyki na WSIiZ.'
WHERE email = 'zofiakowalska@example.com';
--changeset Patryk Depka:0007-8
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Rzeszów', bio = 'Cześć! Nazywam się Maria Kowalska i mieszkam w Rzeszowie. Jestem absolwentką informatyki na Politechnice Rzeszowskiej.'
WHERE email = 'mariakowalska@example.com';
--changeset Patryk Depka:0007-9
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Rzeszów', bio = 'Cześć! Nazywam się Stanisława Kowalska i mieszkam w Rzeszowie. Jestem absolwentką informatyki na WSIiZ.'
WHERE email = 'stanislawakowalska@example.com';
--changeset Patryk Depka:0007-10
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Warszawa', bio = 'Cześć! Nazywam się Jan Nowak i mieszkam w Warszawie. Jestem absolwentem informatyki na Politechnice Warszawskiej.'
WHERE email = 'jannowak@example.com';
--changeset Patryk Depka:0007-11
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Warszawa', bio = 'Cześć! Nazywam się Patryk Nowak i mieszkam w Warszawie. Jestem absolwentem informatyki na Politechnice Warszawskiej.'
WHERE email = 'patryknowak@example.com';
--changeset Patryk Depka:0007-12
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Warszawa', bio = 'Cześć! Nazywam się Mateusz Nowak i mieszkam w Warszawie. Jestem absolwentem informatyki na Politechnice Warszawskiej.'
WHERE email = 'mateusznowak@example.com';
--changeset Patryk Depka:0007-13
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Warszawa', bio = 'Cześć! Nazywam się Zofia Nowak i mieszkam w Warszawie. Jestem absolwentem informatyki na Politechnice Warszawskiej.'
WHERE email = 'zofianowak@example.com';
--changeset Patryk Depka:0007-14
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Warszawa', bio = 'Cześć! Nazywam się Maria Nowak i mieszkam w Warszawie. Jestem absolwentem informatyki na Politechnice Warszawskiej.'
WHERE email = 'marianowak@example.com';
--changeset Patryk Depka:0007-15
UPDATE `app_user`
SET date_of_birth='1995-10-06', city = 'Warszawa', bio = 'Cześć! Nazywam się Stanisława Nowak i mieszkam w Warszawie. Jestem absolwentem informatyki na Politechnice Warszawskiej.'
WHERE email = 'stanislawanowak@example.com';