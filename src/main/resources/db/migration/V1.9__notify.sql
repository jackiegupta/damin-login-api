SET search_path TO gate_schema;

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


CREATE TABLE email (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  receiver CHARACTER VARYING(50) NOT NULL,
  sender CHARACTER VARYING(50),
  subject CHARACTER VARYING(100),
  content CHARACTER VARYING(1000) NOT NULL, 
  status CHARACTER VARYING(50),
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);
ALTER SEQUENCE email_id_seq RESTART WITH 100;

CREATE TABLE sms (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  phone CHARACTER VARYING(15) NOT NULL,
  sender CHARACTER VARYING(50),
  subject CHARACTER VARYING(100),
  message CHARACTER VARYING(200) NOT NULL, 
  status CHARACTER VARYING(50),
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);
ALTER SEQUENCE sms_id_seq RESTART WITH 100;

CREATE TABLE notify (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  user_id BIGINT NULL,
  title CHARACTER VARYING(100) NOT NULL,
  short_content CHARACTER VARYING(500) NOT NULL, 
  full_content CHARACTER VARYING(500) NOT NULL, 
  photo CHARACTER VARYING(200),
  status CHARACTER VARYING(50),
  type CHARACTER VARYING(50),
  target CHARACTER VARYING(9192),
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);
ALTER SEQUENCE notify_id_seq RESTART WITH 100;

INSERT INTO notify (id,title,short_content,full_content,status, type, target, user_id) VALUES
(1,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',1),
(100,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','TEXT','',1),
(200,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','LINK','https://Gaka.vn/#/room/1',1),
(300,'Nạp gạo thành công','Nạp gạo thành công','Đấu giá sim <b>8888 giá</b> rẻ nhất','SUCCESS','HTML','1',1),
(400,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',1),
(500,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',1),
(600,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',1),
(700,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',1),
(800,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',1),
(900,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',1),
(1000,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',1),
(1100,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',1),
(1200,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',0),
(1300,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','LINK','https://Gaka.vn/#/rule',0),
(1400,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',0),
(1500,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',0),
(1600,'Nạp gạo thành công','Nạp gạo thành công','Nạp gạo thành công','SUCCESS','ROOM','1',0)
;

