
INSERT INTO Users(id, name, lastname, telegram_id, email, password, role, created_when, modified_when)
VALUES ('f0e1a34a-bdd3-11ed-afa1-0242ac120002', 'Екатерина', 'Смирнова', '501727035', 'katy@gmail.com', 'katy',  'STUDENT', NOW() - 0.04, NOW() - 0.04);
INSERT INTO Users(id, name, lastname, telegram_id, email, password, role, created_when, modified_when)
VALUES ('f0e1a62e-bdd3-11ed-afa1-0242ac120002', 'Дарья', 'Потомова', '291891029', 'dpotomova@gmail.com', 'potomova0',  'STUDENT', NOW() - 1, NOW() - 1);
INSERT INTO Users(id, name, lastname, telegram_id, role, created_when, modified_when)
VALUES ('f0e1a796-bdd3-11ed-afa1-0242ac120002', 'Анна', 'Ахматова', '33122991',  'STUDENT', NOW(), NOW());
INSERT INTO Users(id, name, lastname, telegram_id, password, role, created_when, modified_when)
VALUES ('f0e1ad2c-bdd3-11ed-afa1-0242ac120002', 'Валерия', 'Ахматова', '1267829941',  'valah123',  'STUDENT', NOW() - 3.05, NOW() - 0.007);
INSERT INTO Users(id, name, lastname, telegram_id, email, role, created_when, modified_when)
VALUES ('f0e1ad3c-bdd3-11ed-afa1-0242ac120002', 'Иван', 'Ахматов', '56830149', 'ivan@yandex.ru', 'PARENT', NOW() - 2, NOW() - 0.005);
INSERT INTO Users(id, name, lastname, telegram_id, email,  role, created_when, modified_when)
VALUES ('f0e1af98-bdd3-11ed-afa1-0242ac120002', 'Пётр', 'Потомов', '139064307', 'potompetr@mail.ru', 'PARENT', NOW() - 5, NOW());
INSERT INTO Users(id, name, lastname, telegram_id, role, created_when, modified_when)
VALUES ('f0e1b0b0-bdd3-11ed-afa1-0242ac120002', 'Данил', 'Комиссаров', '363133882', 'TEACHER', NOW() - 0.2, NOW() - 0.2);
INSERT INTO Users(id, name, lastname, telegram_id, email, password, role, created_when, modified_when)
VALUES ('f0e1b1be-bdd3-11ed-afa1-0242ac120002', 'Ксения', 'Львова', '111030045', 'kslvova@yandex.ru', 'lvovaK',  'TEACHER', NOW() - 1.02, NOW());
INSERT INTO Users(id, name, lastname, telegram_id, email, password, role, created_when, modified_when)
VALUES ('f0e1b2d6-bdd3-11ed-afa1-0242ac120002', 'Наталья', 'Юлина', '742410348', 'yulinan@mail.ru', 'Nyulina',  'ADMIN', NOW() - 10.01, NOW() - 10.01);

INSERT INTO Family(id, student_id, parent_id, created_when, modified_when)
VALUES ('83d7fef0-bdda-11ed-afa1-0242ac120002', 'f0e1a62e-bdd3-11ed-afa1-0242ac120002', 'f0e1af98-bdd3-11ed-afa1-0242ac120002',  NOW() - 1,  NOW() - 1);
INSERT INTO Family(id, student_id, parent_id, created_when, modified_when)
VALUES ('83d801e8-bdda-11ed-afa1-0242ac120002', 'f0e1a796-bdd3-11ed-afa1-0242ac120002', 'f0e1ad3c-bdd3-11ed-afa1-0242ac120002', NOW(), NOW());
INSERT INTO Family(id, student_id, parent_id, created_when, modified_when)
VALUES ('83d80328-bdda-11ed-afa1-0242ac120002', 'f0e1ad2c-bdd3-11ed-afa1-0242ac120002', 'f0e1ad3c-bdd3-11ed-afa1-0242ac120002', NOW() - 2, NOW());

INSERT INTO Subscription(id, student_id, teacher_id, type, status, lessons_rest, start_date, end_date, created_when, modified_when)
VALUES ('a63960e2-bdda-11ed-afa1-0242ac120002', 'f0e1a34a-bdd3-11ed-afa1-0242ac120002', 'f0e1b1be-bdd3-11ed-afa1-0242ac120002', 'TIME_BASED', 'NOT_YET_STARTED', 3, NOW() + 1,NOW() + 31, NOW(), NOW());
INSERT INTO Subscription(id, student_id, teacher_id, type, status, lessons_rest, start_date, end_date, created_when, modified_when)
VALUES ('a639681c-bdda-11ed-afa1-0242ac120002', 'f0e1a62e-bdd3-11ed-afa1-0242ac120002', 'f0e1b0b0-bdd3-11ed-afa1-0242ac120002', 'TIME_BASED', 'ACTIVE', 2, NOW() - 1, NOW() + 364, NOW() - 1, NOW());
INSERT INTO Subscription(id, student_id, teacher_id, type, status, lessons_rest, start_date, end_date, created_when, modified_when)
VALUES ('a6396a88-bdda-11ed-afa1-0242ac120002', 'f0e1a796-bdd3-11ed-afa1-0242ac120002', 'f0e1b0b0-bdd3-11ed-afa1-0242ac120002', 'QUANTITY_BASED', 'ACTIVE', 1, NOW() - 3,NOW() + 62, NOW() - 0.15, NOW() - 0.15);
INSERT INTO Subscription(id, student_id, teacher_id, type, status, lessons_rest, start_date, end_date, created_when, modified_when)
VALUES ('a6396c54-bdda-11ed-afa1-0242ac120002', 'f0e1ad2c-bdd3-11ed-afa1-0242ac120002', 'f0e1b1be-bdd3-11ed-afa1-0242ac120002', 'QUANTITY_BASED', 'ACTIVE', 2, NOW() - 20,NOW() + 39, NOW() - 0.2, NOW());

INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e23aa-bddf-11ed-afa1-0242ac120002', NOW() + 1,'Прошедшее время', 'https://us05web.zoom.us/j', 'NOT_STARTED_YET', 'a63960e2-bdda-11ed-afa1-0242ac120002',  NOW(), NOW());
INSERT INTO Lesson(id, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e2224-bddf-11ed-afa1-0242ac120002','Будушее время', 'https://us05web.zoom.us/a', 'NOT_STARTED_YET', 'a63960e2-bdda-11ed-afa1-0242ac120002',  NOW(), NOW());
INSERT INTO Lesson(id, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e2264-bddf-11ed-afa1-0242ac120002','Настоящее время', 'https://us05web.zoom.us/m', 'NOT_STARTED_YET', 'a63960e2-bdda-11ed-afa1-0242ac120002',  NOW(), NOW());
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e231a-bddf-11ed-afa1-0242ac120002', NOW() - 0.9,'Артикли', 'https://us05web.zoom.us/tt', 'ENDED', 'a639681c-bdda-11ed-afa1-0242ac120002',  NOW() - 1, NOW() - 0.9);
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e24b8-bddf-11ed-afa1-0242ac120002', NOW() + 7.05,'Произношение', 'https://us05web.zoom.us/ra', 'CANCELLED', 'a639681c-bdda-11ed-afa1-0242ac120002',  NOW() - 1, NOW());
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e25b2-bddf-11ed-afa1-0242ac120002', NOW() - 3,'Суффиксы', 'https://us05web.zoom.us/mk', 'ENDED', 'a6396a88-bdda-11ed-afa1-0242ac120002',  NOW() - 0.15, NOW() - 0.15);
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e26c0-bddf-11ed-afa1-0242ac120002', NOW() - 0.1,'Приставки', 'https://us05web.zoom.us/ls', 'CANCELLED_BY_TEACHER', 'a6396a88-bdda-11ed-afa1-0242ac120002',  NOW() - 0.15, NOW() - 0.1);
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e28be-bddf-11ed-afa1-0242ac120002', NOW(),'Проработка акцента', 'https://us05web.zoom.us/mk', 'IN_PROGRESS', 'a6396a88-bdda-11ed-afa1-0242ac120002',  NOW() - 0.2, NOW());
INSERT INTO Lesson(id, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e29b8-bddf-11ed-afa1-0242ac120002','Чтение', 'https://us05web.zoom.us/ls', 'CANCELLED_BY_STUDENT', 'a6396a88-bdda-11ed-afa1-0242ac120002',  NOW() - 0.2, NOW() - 0.08);
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4ef9b8-bddf-11ed-afa1-0242ac120002', NOW() - 14,'Подготовка к экзамену', 'https://us05web.zoom.us/ls', 'ENDED', 'a6396c54-bdda-11ed-afa1-0242ac120002',  NOW() - 0.2, NOW() - 0.08);
INSERT INTO Lesson(id, date, topic, link, status, subscription_id, created_when, modified_when)
VALUES ('6b4e29b8-bddf-14ed-afa1-0242ac120002', NOW() + 14,'Экзамен', 'https://us05web.zoom.us/ls', 'NOT_STARTED_YET', 'a6396c54-bdda-11ed-afa1-0242ac120002',  NOW() - 0.2, NOW() - 0.08);


INSERT INTO lesson_info(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a30544-bde2-11ed-afa1-0242ac120002', '6b4e23aa-bddf-11ed-afa1-0242ac120002', 'NOT_YET_ATTENDED',  NOW(), NOW());
INSERT INTO lesson_info(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a308be-bde2-11ed-afa1-0242ac120002', '6b4e2224-bddf-11ed-afa1-0242ac120002', 'NOT_YET_ATTENDED',  NOW(), NOW());
INSERT INTO lesson_info(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a308be-1de2-11ed-afa1-0242ac120002', '6b4e2264-bddf-11ed-afa1-0242ac120002', 'NOT_YET_ATTENDED',  NOW(), NOW());
INSERT INTO lesson_info(id, lesson_id, attendance, score, family_comment, teacher_comment, homework_completed, teacher_comment_for_parent, created_when, modified_when)
VALUES ('a1a30abc-bde2-11ed-afa1-0242ac120002', '6b4e231a-bddf-11ed-afa1-0242ac120002', 'ATTENDED', 10, 'Отличное занятие. Ребёнок доволен', 'Домашнее задание: стр.24', FALSE, 'Занятие прошло отлично. Пронаблюдайте, пожалуйста, что ребенок выполнит дз.', NOW() - 1, NOW() - 0.9);
INSERT INTO lesson_info(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a30fd0-bde2-11ed-afa1-0242ac120002', '6b4e24b8-bddf-11ed-afa1-0242ac120002', 'CANCELLED', NOW() - 1, NOW());
INSERT INTO lesson_info(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a3112e-bde2-11ed-afa1-0242ac120002', '6b4e25b2-bddf-11ed-afa1-0242ac120002', 'NOT_YET_ATTENDED',  NOW() - 0.15, NOW() - 0.15);
INSERT INTO lesson_info(id, lesson_id, attendance, teacher_comment, created_when, modified_when)
VALUES ('a1a31278-bde2-11ed-afa1-0242ac120002', '6b4e26c0-bddf-11ed-afa1-0242ac120002', 'CANCELLED',  'Отмена занятия по причине государственного праздника.', NOW() - 0.15, NOW() - 0.1);
INSERT INTO lesson_info(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a3139a-bde2-11ed-afa1-0242ac120002', '6b4e28be-bddf-11ed-afa1-0242ac120002', 'NOT_YET_ATTENDED', NOW() - 0.2, NOW());
INSERT INTO lesson_info(id, lesson_id, attendance, family_comment, created_when, modified_when)
VALUES ('a1a314bc-bde2-11ed-afa1-0242ac120002', '6b4e29b8-bddf-11ed-afa1-0242ac120002', 'CANCELLED', 'Отмена занятия по причине болезни.', NOW() - 0.2, NOW() - 0.08);
INSERT INTO lesson_info(id, lesson_id, attendance, score, family_comment, teacher_comment, homework_completed, teacher_comment_for_parent, created_when, modified_when)
VALUES ('a1a30abc-bde2-11ed-afa2-0242ac120002', '6b4ef9b8-bddf-11ed-afa1-0242ac120002', 'ATTENDED', 10, 'Отличное занятие. Ребёнок очень доволен', 'Домашнее задание: отдохнуть)', True, 'Ребенок готов к экзамену!', NOW() - 14, NOW() - 14);
INSERT INTO lesson_info(id, lesson_id, attendance, created_when, modified_when)
VALUES ('a1a308be-1de2-11ed-ada1-0242ac120002', '6b4e29b8-bddf-14ed-afa1-0242ac120002', 'NOT_YET_ATTENDED',  NOW() - 14, NOW() - 14);


INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2826b1c-bdea-11ed-afa1-0242ac120002', 'CREATED', NOW() - 1, '6b4e23aa-bddf-11ed-afa1-0242ac120002', NOW() - 1, NOW() - 1);
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2826cb6-bdea-11ed-afa1-0242ac120002', 'UPDATED', NOW() - 0.9, '6b4e23aa-bddf-11ed-afa1-0242ac120002', NOW() - 1, NOW() - 0.9);
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2826946-bdea-11ed-afa1-0242ac120002', 'CREATED', NOW(), '6b4e2224-bddf-11ed-afa1-0242ac120002', NOW(), NOW());
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2826941-bdea-11ed-afa1-0242ac120002', 'CREATED', NOW(), '6b4e2264-bddf-11ed-afa1-0242ac120002', NOW(), NOW());
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2826941-bdea-12ed-afa1-0242ac120002', 'CREATED', NOW(), '6b4e231a-bddf-11ed-afa1-0242ac120002', NOW(), NOW());
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2826f2c-bdea-11ed-afa1-0242ac120002', 'CREATED', NOW() - 1, '6b4e24b8-bddf-11ed-afa1-0242ac120002', NOW() - 1, NOW() - 1);
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c28270b2-bdea-11ed-afa1-0242ac120002', 'CANCELLED', NOW(), '6b4e24b8-bddf-11ed-afa1-0242ac120002', NOW() - 1, NOW());
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c28274ea-bdea-11ed-afa1-0242ac120002', 'CREATED', NOW() - 0.15, '6b4e25b2-bddf-11ed-afa1-0242ac120002', NOW() - 0.15, NOW() - 0.15);
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2827698-bdea-11ed-afa1-0242ac120002', 'CREATED', NOW() - 0.15, '6b4e26c0-bddf-11ed-afa1-0242ac120002', NOW() -0.15, NOW() - 0.15);
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2827832-bdea-11ed-afa1-0242ac120002', 'CANCELLED', NOW() - 0.1, '6b4e26c0-bddf-11ed-afa1-0242ac120002', NOW() - 0.15, NOW() - 0.1);
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c28279c2-bdea-11ed-afa1-0242ac120002', 'CREATED', NOW() - 0.2, '6b4e28be-bddf-11ed-afa1-0242ac120002', NOW() - 0.2, NOW() - 0.2);
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2827b48-bdea-11ed-afa1-0242ac120002', 'UPDATED', NOW(), '6b4e28be-bddf-11ed-afa1-0242ac120002', NOW() - 0.2, NOW());
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2827cec-bdea-11ed-afa1-0242ac120002', 'CREATED', NOW() - 0.2, '6b4e29b8-bddf-11ed-afa1-0242ac120002', NOW() - 0.2, NOW() - 0.2);
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2827e7c-bdea-11ed-afa1-0242ac120002', 'CANCELLED', NOW() - 0.08, '6b4e29b8-bddf-11ed-afa1-0242ac120002', NOW() - 0.2, NOW() - 0.08);
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2827cec-bdea-11ed-afa1-0142ac120002', 'CREATED', NOW() - 0.2, '6b4ef9b8-bddf-11ed-afa1-0242ac120002', NOW() - 0.2, NOW() - 0.2);
INSERT INTO lesson_history(id, type, timestamp, lesson_id, created_when, modified_when)
VALUES ('c2827cec-bdea-11ed-afa1-0232ac120002', 'CREATED', NOW() - 0.2, '6b4e29b8-bddf-14ed-afa1-0242ac120002', NOW() - 0.2, NOW() - 0.2);


