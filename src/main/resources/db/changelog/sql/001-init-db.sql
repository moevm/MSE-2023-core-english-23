drop table if exists lesson cascade;
drop table if exists lesson_history cascade;
drop table if exists lesson_info cascade;
drop table if exists subscription cascade;
drop table if exists users cascade;
drop table if exists family cascade;


create table family
(
    id            uuid         not null,
    created_when  timestamp(6) not null,
    modified_when timestamp(6) not null,
    parent_id     uuid         not null,
    student_id    uuid         not null,
    primary key (id)
);
create table lesson
(
    id              uuid         not null,
    created_when    timestamp(6) not null,
    modified_when   timestamp(6) not null,
    date            timestamp(6) not null,
    link            varchar(255),
    status          varchar(255) not null,
    topic           varchar(255),
    subscription_id uuid,
    primary key (id)
);
create table lesson_history
(
    id            uuid         not null,
    created_when  timestamp(6) not null,
    modified_when timestamp(6) not null,
    timestamp     timestamp(6),
    type          varchar(255) not null,
    lesson_id     uuid         not null,
    primary key (id)
);
create table lesson_info
(
    id              uuid         not null,
    created_when    timestamp(6) not null,
    modified_when   timestamp(6) not null,
    attendance      varchar(255) not null,
    family_comment  varchar(255),
    score           integer      not null,
    teacher_comment varchar(255),
    lesson_id       uuid         not null,
    primary key (id)
);
create table subscription
(
    id            uuid         not null,
    created_when  timestamp(6) not null,
    modified_when timestamp(6) not null,
    end_date      timestamp(6),
    lessons_rest  integer,
    start_date    timestamp(6),
    status        varchar(255) not null,
    type          varchar(255) not null,
    student_id    uuid         not null,
    teacher_id    uuid         not null,
    primary key (id)
);
create table users
(
    id            uuid         not null,
    created_when  timestamp(6) not null,
    modified_when timestamp(6) not null,
    email         varchar(255),
    lastname      varchar(255),
    name          varchar(255) not null,
    password      varchar(255),
    role          varchar(255) not null,
    telegram_id   varchar(255) not null,
    primary key (id)
);
alter table if exists users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table if exists users
    add constraint UK_dus03vmwyluiy7k2p2gcrqbms unique (telegram_id);
alter table if exists family
    add constraint FKgihrox891wh3crxfrgdbw3wyb foreign key (parent_id) references users;
alter table if exists family
    add constraint FKxxwi01jbkisgd8mw6bgiv05l foreign key (student_id) references users;
alter table if exists lesson
    add constraint FK4npl37bhrs3ipcrncnlb97af0 foreign key (subscription_id) references subscription;
alter table if exists lesson_history
    add constraint FKdgqe6beumvugmtk7tbey13u41 foreign key (lesson_id) references lesson;
alter table if exists lesson_info
    add constraint FKdc0ymou55n3692hygj41fh6ii foreign key (lesson_id) references lesson;
alter table if exists subscription
    add constraint FK7wbpf6unoa55dp84lmovmn3r7 foreign key (student_id) references users;
alter table if exists subscription
    add constraint FKnto2aelbxlg3k2v5ofwfd9avr foreign key (teacher_id) references users;