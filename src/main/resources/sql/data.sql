INSERT INTO tb_user (login_id,login_password,student_id,name,email,role,status)
VALUES ('admin','$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3Y1tlRy.9uBvH996', '00000000', '관리자','admin@naver.com','ROLE_ADMIN','ACTIVE');
INSERT INTO tb_search_keyword (keyword, category)
VALUES ('도시계획', '도시계획'),('도시설계', '도시계획') ,('디지털트윈', 'GIS');