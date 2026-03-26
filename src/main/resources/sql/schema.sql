DROP TABLE IF EXISTS tb_record_tag;
DROP TABLE IF EXISTS tb_record;
DROP TABLE IF EXISTS tb_subject;
DROP TABLE IF EXISTS tb_user;
DROP TABLE IF EXISTS tb_news;
DROP TABLE IF EXISTS tb_search_keyword;

CREATE TABLE tb_user (
    user_number     SERIAL          PRIMARY KEY,
    login_id        VARCHAR(50)     NOT NULL UNIQUE,
    login_password  VARCHAR(255)    NOT NULL,
    student_id      VARCHAR(20)     NOT NULL,
    name            VARCHAR(50)     NOT NULL,
    nick_name       VARCHAR(50)     ,
    profile_image   VARCHAR(255)    ,
    lab             VARCHAR(100)    ,
    description     TEXT            ,
    email           VARCHAR(100)    NOT NULL UNIQUE,
    role            VARCHAR(20)     NOT NULL,
    status          VARCHAR(10)     NOT NULL,
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_news (
    news_number     SERIAL          PRIMARY KEY,
    title           VARCHAR(255)    NOT NULL,
    link            VARCHAR(255)    UNIQUE NOT NULL,
    description     TEXT            NOT NULL,
    keyword_list    TEXT            NOT NULL,
    keyword_category    TEXT            NOT NULL,
    pubDate         TIMESTAMP       NOT NULL,
    upload_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_search_keyword (
    keyword_number  SERIAL          PRIMARY KEY,
    keyword         VARCHAR(100)    NOT NULL,
    category        VARCHAR(100)    NOT NULL
);


CREATE TABLE tb_subject (
    subject_number  SERIAL          PRIMARY KEY,
    subject_name    VARCHAR(500)    NOT NULL UNIQUE -- 중복 과목명 방지
);

CREATE TABLE tb_record (
    record_number   SERIAL          PRIMARY KEY,
    user_number     INTEGER         NOT NULL,
    subject_number  INTEGER         NOT NULL,
    title           VARCHAR(255)    NOT NULL,
    description     TEXT,
    file_url        VARCHAR(500),
    grade           INTEGER         NOT NULL,
    semester        VARCHAR(20)     NOT NULL,
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_artwork_user FOREIGN KEY (user_number)
    REFERENCES tb_user(user_number) ON DELETE CASCADE,
    CONSTRAINT fk_record_subject FOREIGN KEY (subject_number)
    REFERENCES tb_subject(subject_number)
);

CREATE TABLE tb_record_tag (
    tag_number      SERIAL          PRIMARY KEY,
    record_number   INTEGER         NOT NULL,
    user_number     INTEGER         NOT NULL,
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tag_record FOREIGN KEY (record_number)
    REFERENCES tb_record(record_number) ON DELETE CASCADE,
    CONSTRAINT fk_tag_user FOREIGN KEY (user_number)
    REFERENCES tb_user(user_number) ON DELETE CASCADE,
    CONSTRAINT uq_record_user_tag UNIQUE (record_number, user_number)
);