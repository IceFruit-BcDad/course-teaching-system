CREATE TABLE IF NOT EXISTS course
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    classification_id BIGINT                NOT NULL,
    name              VARCHAR(255)          NULL,
    create_user_id    BIGINT                NOT NULL,
    create_time       datetime              NULL,
    last_modify_time  datetime              NULL,
    cover_url         VARCHAR(255)          NULL,
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
    type          INT               NOT NULL,
    create_time   datetime              NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS chapter
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    course_id        BIGINT                NOT NULL,
    parent_id        BIGINT                NULL,
    title            VARCHAR(255)          NOT NULL,
    content_url      VARCHAR(255)          NOT NULL,
    create_time      datetime              NOT NULL,
    last_modify_time datetime              NOT NULL,
    CONSTRAINT pk_chapter PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS file
(
    id               VARCHAR(255) NOT NULL,
    filename              VARCHAR(255) NOT NULL,
    used             VARCHAR(255) NULL,
    create_time      datetime     NOT NULL,
    last_modify_time datetime     NOT NULL,
    CONSTRAINT pk_file PRIMARY KEY (id)
);