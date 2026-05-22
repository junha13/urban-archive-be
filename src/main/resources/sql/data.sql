INSERT INTO tb_user (login_id, login_password, student_id, name, email, phone, role, status)
VALUES
    ('admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3Y1tlRy.9uBvH996', '00000000', '관리자', 'admin@naver.com','010-2103-3406', 'ROLE_ADMIN', 'ACTIVE'),
    ('user01', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3Y1tlRy.9uBvH996', '20211234', '김철수', 'chulsoo@naver.com','010-2103-3406', 'ROLE_USER', 'ACTIVE'),
    ('user02', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3Y1tlRy.9uBvH996', '20215678', '이영희', 'younghee@gmail.com', '010-2103-3406','ROLE_USER', 'ACTIVE');

INSERT INTO tb_subject (subject_name)
VALUES
    ('데이터베이스 실무'),
    ('도시정보시스템(GIS)'),
    ('캡스톤 디자인'),
    ('디지털트윈 기반 도시설계');

INSERT INTO tb_record (user_number, subject_number, type, title, description, file_url, grade, semester)
VALUES
    (2, 1, 'urban','안양 아카이브 데이터베이스 설계도', 'ERD 설계 및 정규화 과정을 기록한 문서입니다.', '', 3, '1학기'),
    (3, 2, 'urban', '안양시 유동인구 분석 결과', 'GIS를 활용한 도시 밀집도 분석 자료입니다.', '', 4, '1학기');

INSERT INTO tb_record_tag (record_number, user_number)
VALUES
    (1, 3), -- 1번 기록에 이영희 태그
    (1, 1), -- 1번 기록에 관리자 태그
    (2, 2); -- 2번 기록에 김철수 태그

INSERT INTO tb_search_keyword (keyword, category)
VALUES ('도시계획', '도시계획'),('도시설계', '도시계획') ,('디지털트윈', 'GIS');