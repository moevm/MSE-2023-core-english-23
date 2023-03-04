
CREATE TABLE users
(
    id                     BIGINT       NOT NULL,
    telegram               VARCHAR(255) NOT NULL,
    email                  VARCHAR(255) NOT NULL,
    name                   VARCHAR(255) NOT NULL,
    lastname               VARCHAR(255) NOT NULL,
    password               VARCHAR(255) NOT NULL,
    registration_timestamp date,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE subscription
(
    subscription_id BIGINT NOT NULL,
    student_id      BIGINT,
    lessons_rest    INT    NOT NULL,
    start_date      date   NOT NULL,
    end_date        date   NOT NULL,
    subscription    BIGINT,
    CONSTRAINT pk_subscription PRIMARY KEY (subscription_id)
);

ALTER TABLE subscription
    ADD CONSTRAINT FK_SUBSCRIPTION_ON_STUDENT FOREIGN KEY (student_id) REFERENCES student (id);

ALTER TABLE subscription
    ADD CONSTRAINT FK_SUBSCRIPTION_ON_SUBSCRIPTION FOREIGN KEY (subscription) REFERENCES student (id);

CREATE TABLE teacher
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_teacher PRIMARY KEY (id)
);

ALTER TABLE teacher
    ADD CONSTRAINT FK_TEACHER_ON_ID FOREIGN KEY (id) REFERENCES users (id);

CREATE TABLE student
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_student PRIMARY KEY (id)
);

ALTER TABLE student
    ADD CONSTRAINT FK_STUDENT_ON_ID FOREIGN KEY (id) REFERENCES users (id);

CREATE TABLE family
(
    parent_id  BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    CONSTRAINT pk_family PRIMARY KEY (parent_id, student_id)
);

CREATE TABLE parent
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_parent PRIMARY KEY (id)
);

ALTER TABLE parent
    ADD CONSTRAINT FK_PARENT_ON_ID FOREIGN KEY (id) REFERENCES users (id);

ALTER TABLE family
    ADD CONSTRAINT fk_family_on_parent FOREIGN KEY (parent_id) REFERENCES parent (id);

ALTER TABLE family
    ADD CONSTRAINT fk_family_on_student FOREIGN KEY (student_id) REFERENCES student (id);

CREATE TABLE lesson
(
    lesson_id       BIGINT NOT NULL,
    teacher_id      BIGINT,
    subscription_id BIGINT,
    topic           VARCHAR(255),
    link            VARCHAR(255),
    date            date,
    CONSTRAINT pk_lesson PRIMARY KEY (lesson_id)
);

ALTER TABLE lesson
    ADD CONSTRAINT FK_LESSON_ON_SUBSCRIPTION FOREIGN KEY (subscription_id) REFERENCES subscription (subscription_id);

ALTER TABLE lesson
    ADD CONSTRAINT FK_LESSON_ON_TEACHER FOREIGN KEY (teacher_id) REFERENCES teacher (id);

CREATE TABLE lesson_info
(
    lesson_info_id  BIGINT NOT NULL,
    state           INT    NOT NULL,
    teacher_comment VARCHAR(255),
    student_comment VARCHAR(255),
    CONSTRAINT pk_lesson_info PRIMARY KEY (lesson_info_id)
);