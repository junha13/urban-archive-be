DROP TABLE IF EXISTS tbl_board_like;
DROP TABLE IF EXISTS tbl_board;
DROP TABLE IF EXISTS tbl_agora_like;
DROP TABLE IF EXISTS tbl_agora;
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
    phone           VARCHAR(20)     NOT NULL,
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

CREATE TABLE tbl_agora (
    agora_number         SERIAL          PRIMARY KEY,
    parent_agora_number  INTEGER         ,
    root_agora_number    INTEGER         ,
    user_number          INTEGER         NOT NULL,
    title                VARCHAR(255)    ,
    content              TEXT            NOT NULL,
    node_type            VARCHAR(20)     NOT NULL,
    depth                INTEGER         NOT NULL DEFAULT 0,
    like_count           INTEGER         NOT NULL DEFAULT 0,
    status               VARCHAR(10)     NOT NULL DEFAULT 'ACTIVE',
    created_at           TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_agora_parent FOREIGN KEY (parent_agora_number)
    REFERENCES tbl_agora(agora_number) ON DELETE CASCADE,
    CONSTRAINT fk_agora_root FOREIGN KEY (root_agora_number)
    REFERENCES tbl_agora(agora_number) ON DELETE CASCADE,
    CONSTRAINT fk_agora_user FOREIGN KEY (user_number)
    REFERENCES tb_user(user_number) ON DELETE CASCADE,
    CONSTRAINT ck_agora_node_type CHECK (node_type IN ('POST', 'COMMENT')),
    CONSTRAINT ck_agora_depth CHECK (depth IN (0, 1)),
    CONSTRAINT ck_agora_post_shape CHECK (
        (node_type = 'POST' AND title IS NOT NULL AND parent_agora_number IS NULL AND root_agora_number IS NULL AND depth = 0)
        OR
        (node_type = 'COMMENT' AND title IS NULL AND root_agora_number IS NOT NULL)
    ),
    CONSTRAINT ck_agora_comment_shape CHECK (
        node_type = 'POST'
        OR
        (depth = 0 AND parent_agora_number IS NULL)
        OR
        (depth = 1 AND parent_agora_number IS NOT NULL)
    )
);

CREATE TABLE tbl_agora_like (
    agora_like_number    SERIAL          PRIMARY KEY,
    agora_number         INTEGER         NOT NULL,
    user_number          INTEGER         NOT NULL,
    created_at           TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_agora_like_agora FOREIGN KEY (agora_number)
    REFERENCES tbl_agora(agora_number) ON DELETE CASCADE,
    CONSTRAINT fk_agora_like_user FOREIGN KEY (user_number)
    REFERENCES tb_user(user_number) ON DELETE CASCADE,
    CONSTRAINT uq_agora_like UNIQUE (agora_number, user_number)
);

CREATE TABLE tbl_board (
    board_number          SERIAL          PRIMARY KEY,
    parent_board_number   INTEGER         ,
    root_board_number     INTEGER         ,
    board_type            VARCHAR(50)     NOT NULL,
    user_number           INTEGER         NOT NULL,
    title                 VARCHAR(255)    ,
    content               TEXT            NOT NULL,
    node_type             VARCHAR(20)     NOT NULL,
    depth                 INTEGER         NOT NULL DEFAULT 0,
    like_count            INTEGER         NOT NULL DEFAULT 0,
    status                VARCHAR(10)     NOT NULL DEFAULT 'ACTIVE',
    created_at            TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_board_parent FOREIGN KEY (parent_board_number)
    REFERENCES tbl_board(board_number) ON DELETE CASCADE,
    CONSTRAINT fk_board_root FOREIGN KEY (root_board_number)
    REFERENCES tbl_board(board_number) ON DELETE CASCADE,
    CONSTRAINT fk_board_user FOREIGN KEY (user_number)
    REFERENCES tb_user(user_number) ON DELETE CASCADE,
    CONSTRAINT ck_board_node_type CHECK (node_type IN ('POST', 'COMMENT')),
    CONSTRAINT ck_board_depth CHECK (depth IN (0, 1)),
    CONSTRAINT ck_board_post_shape CHECK (
        (node_type = 'POST' AND title IS NOT NULL AND parent_board_number IS NULL AND root_board_number IS NULL AND depth = 0)
        OR
        (node_type = 'COMMENT' AND title IS NULL AND root_board_number IS NOT NULL)
    ),
    CONSTRAINT ck_board_comment_shape CHECK (
        node_type = 'POST'
        OR
        (depth = 0 AND parent_board_number IS NULL)
        OR
        (depth = 1 AND parent_board_number IS NOT NULL)
    )
);

CREATE TABLE tbl_board_like (
    board_like_number     SERIAL          PRIMARY KEY,
    board_number          INTEGER         NOT NULL,
    user_number           INTEGER         NOT NULL,
    created_at            TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_board_like_board FOREIGN KEY (board_number)
    REFERENCES tbl_board(board_number) ON DELETE CASCADE,
    CONSTRAINT fk_board_like_user FOREIGN KEY (user_number)
    REFERENCES tb_user(user_number) ON DELETE CASCADE,
    CONSTRAINT uq_board_like UNIQUE (board_number, user_number)
);
