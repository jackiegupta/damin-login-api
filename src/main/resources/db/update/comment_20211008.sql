drop table gate_schema."comment";

CREATE TABLE gate_schema."comment" (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  user_id BIGINT DEFAULT 0,
  title CHARACTER VARYING(200),
  content CHARACTER VARYING(500),
  target CHARACTER VARYING(200),
  seen BOOLEAN default false,
  status CHARACTER VARYING(50),
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW() 
);

ALTER SEQUENCE comment_id_seq RESTART WITH 1000;

INSERT INTO gate_schema."comment" (id,user_id,title,content,seen,target,status,create_date) VALUES 
(1,1,'Chúc mừng','Chúc mừng bạn lên level 2',FALSE,'','UNACTIVE',NOW()),
(2,1,'Quà tặng','Quà tặng tham gia event',FALSE,'','UNACTIVE',NOW()),
(3,0,'Quà tặng','Quà tặng ra mắt game mới',FALSE,'https://gamemoi.com','UNACTIVE',NOW());
