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