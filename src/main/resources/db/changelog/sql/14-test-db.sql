
INSERT INTO Users(id, name, lastname, telegram_id, email, password, role, created_when, modified_when)
VALUES ('f0e1a34a-bdd3-11ed-afa1-0242ac120002', 'Екатерина', 'Смирнова', '1', 'katy@gmail.com', 'katy',  'STUDENT', NOW() - interval '1 hour', NOW() - interval '1 hour');
INSERT INTO Users(id, name, lastname, telegram_id, email, password, role, created_when, modified_when)
VALUES ('f0e1a62e-bdd3-11ed-afa1-0242ac120002', 'Дарья', 'Потомова', '2', 'dpotomova@gmail.com', 'potomova0',  'STUDENT', NOW() - interval '1 day', NOW() - interval '1 day');
INSERT INTO Users(id, name, lastname, telegram_id, role, created_when, modified_when)
VALUES ('f0e1a796-bdd3-11ed-afa1-0242ac120002', 'Анна', 'Ахматова', '3',  'STUDENT', NOW(), NOW());
INSERT INTO Users(id, name, lastname, telegram_id, password, role, created_when, modified_when)
VALUES ('f0e1ad2c-bdd3-11ed-afa1-0242ac120002', 'Валерия', 'Ахматова', '4',  'valah123',  'STUDENT', NOW() - interval '3 days 30 minutes', NOW() - interval '10 minutes');
INSERT INTO Users(id, name, lastname, telegram_id, email, role, created_when, modified_when)
VALUES ('f0e1ad2c-bdd3-11ed-afa1-0242ac120002', 'Иван', 'Ахматов', '5', 'ivan@yandex.ru', 'PARENT', NOW() - interval '2 days', NOW() - interval '5 minutes');
INSERT INTO Users(id, name, lastname, telegram_id, email,  role, created_when, modified_when)
VALUES ('f0e1af98-bdd3-11ed-afa1-0242ac120002', 'Пётр', 'Потомов', '6', 'potompetr@mail.ru', 'PARENT', NOW() - interval '5 days', NOW());
INSERT INTO Users(id, name, lastname, telegram_id, role, created_when, modified_when)
VALUES ('f0e1b0b0-bdd3-11ed-afa1-0242ac120002', 'Данил', 'Комиссаров', '7', 'TEACHER', NOW() - interval '7 hours', NOW() - interval '7 hours');
INSERT INTO Users(id, name, lastname, telegram_id, email, password, role, created_when, modified_when)
VALUES ('f0e1b1be-bdd3-11ed-afa1-0242ac120002', 'Ксения', 'Львова', '8', 'kslvova@yandex.ru', 'lvovaK',  'TEACHER', NOW() - interval '1 day 35 minutes', NOW());
INSERT INTO Users(id, name, lastname, telegram_id, email, password, role, created_when, modified_when)
VALUES ('f0e1b2d6-bdd3-11ed-afa1-0242ac120002', 'Наталья', 'Юлина', '9', 'yulinan@mail.ru', 'Nyulina',  'ADMIN', NOW() - interval '10 days 15 minutes', NOW() - interval '10 days 15 minutes');

INSERT INTO Family(id, student_id, parent_id, created_when, modified_when)
VALUES ('83d7fef0-bdda-11ed-afa1-0242ac120002', 'f0e1a62e-bdd3-11ed-afa1-0242ac120002', 'f0e1af98-bdd3-11ed-afa1-0242ac120002',  NOW() - interval '1 day',  NOW() - interval '1 day');
INSERT INTO Family(id, student_id, parent_id, created_when, modified_when)
VALUES ('83d801e8-bdda-11ed-afa1-0242ac120002', 'f0e1a796-bdd3-11ed-afa1-0242ac120002', 'f0e1ad2c-bdd3-11ed-afa1-0242ac120002', NOW(), NOW());
INSERT INTO Family(id, student_id, parent_id, created_when, modified_when)
VALUES ('83d80328-bdda-11ed-afa1-0242ac120002', 'f0e1ad2c-bdd3-11ed-afa1-0242ac120002', 'f0e1ad2c-bdd3-11ed-afa1-0242ac120002', NOW() - interval '2 days', NOW());

INSERT INTO Subscription(id, student_id, teacher_id, type, status, start_date, end_date, created_when, modified_when)
VALUES ('a63960e2-bdda-11ed-afa1-0242ac120002', 'f0e1a34a-bdd3-11ed-afa1-0242ac120002', 'f0e1b1be-bdd3-11ed-afa1-0242ac120002', TIME_BASED, NOT_YET_STARTED, NOW() + interval '1 day',NOW() + interval '31 days', NOW(), NOW());
INSERT INTO Subscription(id, student_id, teacher_id, type, status, start_date, end_date, created_when, modified_when)
VALUES ('a639681c-bdda-11ed-afa1-0242ac120002', 'f0e1a62e-bdd3-11ed-afa1-0242ac120002', 'f0e1b0b0-bdd3-11ed-afa1-0242ac120002', TIME_BASED, CANCELLED, NOW() - interval '1 day', NOW() + interval '364 days', NOW() - interval '1 day', NOW());
INSERT INTO Subscription(id, student_id, teacher_id, type, status, lessons_rest, created_when, modified_when)
VALUES ('a6396a88-bdda-11ed-afa1-0242ac120002', 'f0e1a796-bdd3-11ed-afa1-0242ac120002', 'f0e1b0b0-bdd3-11ed-afa1-0242ac120002', QUANTITY_BASED, ACTIVE, 16, NOW() - interval '5 hours', NOW() - interval '5 hours');
INSERT INTO Subscription(id, student_id, teacher_id, type, status, lessons_rest, created_when, modified_when)
VALUES ('a6396c54-bdda-11ed-afa1-0242ac120002', 'f0e1ad2c-bdd3-11ed-afa1-0242ac120002', 'f0e1b1be-bdd3-11ed-afa1-0242ac120002', QUANTITY_BASED, ACTIVE, 8, NOW() - interval '7 hours', NOW());

INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e1ec8-bddf-11ed-afa1-0242ac120002', NOW() + interval '2 days','Прошедшее время', 'https://us05web.zoom.us/j', NOT_STARTED_YET, 'a63960e2-bdda-11ed-afa1-0242ac120002',  NOW(), NOW());
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e2224-bddf-11ed-afa1-0242ac120002', NOW() + interval '8 days','Будушее время', 'https://us05web.zoom.us/a', NOT_STARTED_YET, 'a63960e2-bdda-11ed-afa1-0242ac120002',  NOW(), NOW());
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e23aa-bddf-11ed-afa1-0242ac120002', NOW() - interval '23 hours','Артикли', 'https://us05web.zoom.us/tt', ENDED, 'a639681c-bdda-11ed-afa1-0242ac120002',  NOW() - interval '1 day', NOW() - interval '23 hours');
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e24b8-bddf-11ed-afa1-0242ac120002', NOW() + interval '7 days 1 hour','Произношение', 'https://us05web.zoom.us/ra', CANCELLED, 'a639681c-bdda-11ed-afa1-0242ac120002',  NOW() - interval '1 day', NOW());
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e25b2-bddf-11ed-afa1-0242ac120002', NOW() + interval '1 hour','Суффиксы', 'https://us05web.zoom.us/mk', NOT_STARTED_YET, 'a6396a88-bdda-11ed-afa1-0242ac120002',  NOW() - interval '5 hours', NOW() - interval '5 hours');
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e26c0-bddf-11ed-afa1-0242ac120002', NOW() + interval '8 days 1 hour','Приставки', 'https://us05web.zoom.us/ls', CANCELLED_BY_TEACHER, 'a6396a88-bdda-11ed-afa1-0242ac120002',  NOW() - interval '5 hours', NOW() - interval '3 hours');
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e28be-bddf-11ed-afa1-0242ac120002', NOW(),'Проработка акцента', 'https://us05web.zoom.us/mk', IN_PROGRESS, 'a6396a88-bdda-11ed-afa1-0242ac120002',  NOW() - interval '7 hours', NOW());
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e29b8-bddf-11ed-afa1-0242ac120002', NOW() + interval '14 days','Чтение', 'https://us05web.zoom.us/ls', CANCELLED_BY_STUDENT, 'a6396a88-bdda-11ed-afa1-0242ac120002',  NOW()- interval '7 hours', NOW()- interval '2 hours');

INSERT INTO LessonInfo(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a30544-bde2-11ed-afa1-0242ac120002', '6b4e23aa-bddf-11ed-afa1-0242ac120002', NOT_YET_ATTENDED,  NOW(), NOW());
INSERT INTO LessonInfo(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a308be-bde2-11ed-afa1-0242ac120002', '6b4e2224-bddf-11ed-afa1-0242ac120002', NOT_YET_ATTENDED,  NOW(), NOW());
INSERT INTO LessonInfo(id, lesson_id, attendance, score, family_comment, teacher_comment, created_when, modified_when)
VALUES ('a1a30abc-bde2-11ed-afa1-0242ac120002', '6b4e23aa-bddf-11ed-afa1-0242ac120002', ATTENDED, 10, 'Отличное занятие. Ребёнок доволен', 'Домашнее задание: стр.24', NOW() - interval '1 day', NOW() - interval '23 hours');
INSERT INTO LessonInfo(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a30fd0-bde2-11ed-afa1-0242ac120002', '6b4e24b8-bddf-11ed-afa1-0242ac120002', CANCELLED,  NOW() - interval '1 day', NOW());
INSERT INTO LessonInfo(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a3112e-bde2-11ed-afa1-0242ac120002', '6b4e25b2-bddf-11ed-afa1-0242ac120002', NOT_YET_ATTENDED,  NOW() - interval '5 hours', NOW() - interval '5 hours');
INSERT INTO LessonInfo(id, lesson_id, attendance, teacher_comment, created_when, modified_when)
VALUES ('a1a31278-bde2-11ed-afa1-0242ac120002', '6b4e26c0-bddf-11ed-afa1-0242ac120002', CANCELLED,  'Отмена занятия по причине государственного праздника.', NOW() - interval '5 hours', NOW() - interval '3 hours');
INSERT INTO LessonInfo(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a3139a-bde2-11ed-afa1-0242ac120002', '6b4e28be-bddf-11ed-afa1-0242ac120002', NOT_YET_ATTENDED,  NOW() - interval '7 hours', NOW());
INSERT INTO LessonInfo(id, lesson_id, attendance, family_comment, created_when, modified_when)
VALUES ('a1a314bc-bde2-11ed-afa1-0242ac120002', '6b4e29b8-bddf-11ed-afa1-0242ac120002', CANCELLED,  'Отмена занятия по причине болезни.', NOW()- interval '7 hours', NOW()- interval '2 hours');

INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2826626-bdea-11ed-afa1-0242ac120002', CREATED, NOW(), '6b4e1ec8-bddf-11ed-afa1-0242ac120002', NOW(), NOW());
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2826946-bdea-11ed-afa1-0242ac120002', CREATED, NOW(), '6b4e2224-bddf-11ed-afa1-0242ac120002', NOW(), NOW());
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2826b1c-bdea-11ed-afa1-0242ac120002', CREATED, NOW() - interval '1 day', '6b4e23aa-bddf-11ed-afa1-0242ac120002', NOW() - interval '1 day', NOW() - interval '1 day');
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2826cb6-bdea-11ed-afa1-0242ac120002', UPDATED, NOW() - interval '23 hours', '6b4e23aa-bddf-11ed-afa1-0242ac120002', NOW() - interval '1 day', NOW() - interval '23 hours');
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2826f2c-bdea-11ed-afa1-0242ac120002', CREATED, NOW() - interval '1 day', '6b4e24b8-bddf-11ed-afa1-0242ac120002', NOW() - interval '1 day', NOW() - interval '1 day');
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when, cancelled_when)
VALUES ('c28270b2-bdea-11ed-afa1-0242ac120002', CANCELLED, NOW(), '6b4e24b8-bddf-11ed-afa1-0242ac120002', NOW() - interval '1 day', NOW(),  NOW());
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c28274ea-bdea-11ed-afa1-0242ac120002', CREATED, NOW() - interval '5 hours', '6b4e25b2-bddf-11ed-afa1-0242ac120002', NOW() - interval '5 hours', NOW() - interval '5 hours');
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2827698-bdea-11ed-afa1-0242ac120002', CREATED, NOW() - interval '5 hours', '6b4e26c0-bddf-11ed-afa1-0242ac120002', NOW() - interval '5 hours', NOW() - interval '5 hours');
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when, cancelled_when)
VALUES ('c2827832-bdea-11ed-afa1-0242ac120002', CANCELLED, NOW() - interval '3 hours', '6b4e26c0-bddf-11ed-afa1-0242ac120002', NOW() - interval '5 hours', NOW() - interval '3 hours', NOW() - interval '3 hours');
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c28279c2-bdea-11ed-afa1-0242ac120002', CREATED, NOW() - interval '7 hours', '6b4e28be-bddf-11ed-afa1-0242ac120002', NOW() - interval '7 hours', NOW() - interval '7 hours');
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2827b48-bdea-11ed-afa1-0242ac120002', UPDATED, NOW(), '6b4e28be-bddf-11ed-afa1-0242ac120002', NOW() - interval '7 hours', NOW());
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2827cec-bdea-11ed-afa1-0242ac120002', CREATED, NOW()- interval '7 hours', '6b4e29b8-bddf-11ed-afa1-0242ac120002', NOW() - interval '7 hours', NOW() - interval '7 hours');
INSERT INTO LessonHistory(id, type, timestamp, lesson_id, created_when, modified_when, cancelled_when)
VALUES ('c2827e7c-bdea-11ed-afa1-0242ac120002', CANCELLED, NOW() - interval '2 hours', '6b4e29b8-bddf-11ed-afa1-0242ac120002', NOW()- interval '7 hours', NOW()- interval '2 hours', NOW()- interval '2 hours');



