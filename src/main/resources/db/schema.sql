CREATE TABLE IF NOT EXISTS course
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    classification_id BIGINT                NOT NULL,
    name              VARCHAR(255)          NULL,
    create_user_id    BIGINT                NOT NULL,
    create_time       datetime              NULL,
    last_modify_time  datetime              NULL,
    CONSTRAINT pk_course PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS classification
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(255)          NULL,
    level     INT                   NOT NULL,
    parent_id BIGINT                NULL,
    CONSTRAINT pk_classification PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)          NULL,
    phone_number  VARCHAR(255)          NULL,
    password_hash VARCHAR(255)          NULL,
    type_id       BIGINT                NOT NULL,
    create_time   datetime              NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_type
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NULL,
    CONSTRAINT pk_usertype PRIMARY KEY (id)
);