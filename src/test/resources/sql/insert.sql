insert into locations (id, slug, name)
values (1, 'spb', 'Saint Petersburg');
insert into locations (id, slug, name)
values (2, 'msk', 'Moscow');

SELECT setval ('locations_id_seq', (SELECT MAX(id) FROM locations));

insert into events (id, name, dates, price, locations_id)
values (1, 'Festival ONE', '2024-10-26', '500', '2');
insert into events (id, name, dates, price, locations_id)
values (2, 'Festival TWO', '2024-11-20', '1500', '1');
insert into events (id, name, dates, price, locations_id)
values (3, 'Festival THREE', '2025-12-26', '700', '2');

SELECT setval ('events_id_seq', (SELECT MAX(id) FROM events));

DELETE FROM users_roles;

DELETE FROM users;

DELETE FROM roles;

insert into roles (id, name)
values (1, 'ROLE_USER');
insert into roles (id, name)
values (2, 'ROLE_ADMIN');

SELECT setval ('roles_id_seq', (SELECT MAX(id) FROM roles));

insert into users (id, name, email, password)
values (1, 'Admin1', 'admin1@mail.ru', '$2a$10$KPWzn316j.F2heWJsuLqRebL21P3GXmCcKljaBKGr3IilP5vtSAHW');
insert into users (id, name, email, password)
values (2, 'User1', 'user1@mail.ru', '$2a$10$ldztH/FtpD7i8OAnBHuW2.tzI.YOETg0CvJFz9vkm.bMmTJlKyc26');
insert into users (id, name, email, password)
values (3, 'User2', 'user2@mail.ru', '$2a$10$ldztH/FtpD7i8OAnBHuW2.tzI.YOETg0CvJFz9vkm.bMmTJlKyc26');
insert into users (id, name, email, password)
values (4, 'Admin2', 'admin2@mail.ru', '$2a$10$KPWzn316j.F2heWJsuLqRebL21P3GXmCcKljaBKGr3IilP5vtSAHW');

SELECT setval ('users_id_seq', (SELECT MAX(id) FROM users));

insert into users_roles (id, users_id, roles_id)
values (1, 1, 2);
insert into users_roles (id, users_id, roles_id)
values (2, 2, 1);
insert into users_roles (id, users_id, roles_id)
values (3, 3, 1);
insert into users_roles (id, users_id, roles_id)
values (4, 4, 2);

SELECT setval ('users_roles_id_seq', (SELECT MAX(id) FROM users_roles));