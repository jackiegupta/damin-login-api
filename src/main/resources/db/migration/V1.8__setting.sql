SET search_path TO gate_schema;

CREATE TABLE setting (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100),
  code CHARACTER VARYING(20) UNIQUE,
  value CHARACTER VARYING(15) NOT NULL,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

ALTER SEQUENCE setting_id_seq RESTART WITH 1000;

INSERT INTO setting (id,name,code,value,status,create_id,create_date) VALUES 
(11,'Kinh nghiệm hạng Đồng','BRONZE','5000','ACTIVE',1,NOW()),
(12,'Kinh nghiệm hạng Bạc','SILVER','10000','ACTIVE',1,NOW()),
(13,'Kinh nghiệm hạng Vàng','GOLD','15000','ACTIVE',1,NOW()),
(14,'Kinh nghiệm giữ hạng Bạc kim','PLATIN','5000','ACTIVE',1,NOW()),
(15,'Ngày hết hạn sau kinh nghiệm','EXP_EXPIE','30','ACTIVE',1,NOW());


INSERT INTO setting (id,name,code,value,status,create_id,create_date) VALUES 
(21,'Điểm cho hạng VIP1','VIP1','1000','ACTIVE',1,NOW()),
(22,'Điểm cho hạng VIP2','VIP2','5000','ACTIVE',1,NOW()),
(23,'Điểm giữ hạng VIP3','VIP3','1000','ACTIVE',1,NOW());

INSERT INTO setting (id,name,code,value,status,create_id,create_date) VALUES 
(31,'Banner (đv: giây)','BANNER','2','ACTIVE',1,NOW()),
(32,'Giới thiệu bạn bè nhận Thóc','INVITE','100','ACTIVE',1,NOW());

INSERT INTO gate_schema.setting (id,name,code,value,status,create_id,create_date) VALUES 
(1,'Tổng Thành viên (Tỷ lệ %)','COUNT_MEMBER','500','ACTIVE',1,NOW()),
(2,'Tổng Onlice (Tỷ lệ %)','ONLINE','300','ACTIVE',1,NOW());