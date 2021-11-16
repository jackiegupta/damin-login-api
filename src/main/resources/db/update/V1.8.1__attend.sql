SET search_path TO gate_schema;

CREATE TABLE attend (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100),
  code CHARACTER VARYING(20) UNIQUE,
  value CHARACTER VARYING(15) NOT NULL,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

ALTER SEQUENCE attend_id_seq RESTART WITH 1000;

ALTER TABLE gate_schema.user
ADD COLUMN user_id360 BIGINT DEFAULT 0;

INSERT INTO attend (id,name,value,status,create_id,create_date) VALUES 
(1,'Điểm danh ngày 1','50','ACTIVE',1,NOW()),
(2,'Điểm danh ngày 2','100','ACTIVE',1,NOW()),
(3,'Điểm danh ngày 3','150','ACTIVE',1,NOW()),
(4,'Điểm danh ngày 4','200','ACTIVE',1,NOW()),
(5,'Điểm danh ngày 5','250','ACTIVE',1,NOW()),
(6,'Điểm danh ngày 6','300','ACTIVE',1,NOW()),
(7,'Điểm danh ngày 7','350','ACTIVE',1,NOW());


CREATE TABLE levels (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100),
  code CHARACTER VARYING(20) UNIQUE,
  value CHARACTER VARYING(15) NOT NULL,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

ALTER SEQUENCE levels_id_seq RESTART WITH 1000;

INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (1,'LV1','LEVEL1',0,'ACTIVE',1,NOW());	
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (2,'LV2','LEVEL2',25,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (3,'LV3','LEVEL3',51,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (4,'LV4','LEVEL4',77,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (5,'LV5','LEVEL5',105 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (6,'LV6','LEVEL6',134 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (7,'LV7','LEVEL7',163 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (8,'LV8','LEVEL8',195 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (9,'LV9','LEVEL9',227 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (10 ,'LV10','LEVEL10',262 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (11 ,'LV11','LEVEL11',298 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (12 ,'LV12','LEVEL12',336 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (13 ,'LV13','LEVEL13',377 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (14 ,'LV14','LEVEL14',420 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (15 ,'LV15','LEVEL15',466 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (16 ,'LV16','LEVEL16',515 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (17 ,'LV17','LEVEL17',567 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (18 ,'LV18','LEVEL18',624 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (19 ,'LV19','LEVEL19',685 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (20 ,'LV20','LEVEL20',751 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (21 ,'LV21','LEVEL21',823 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (22 ,'LV22','LEVEL22',900 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (23 ,'LV23','LEVEL23',985 ,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (24 ,'LV24','LEVEL24',1078,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (25 ,'LV25','LEVEL25',1179,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (26 ,'LV26','LEVEL26',1291,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (27 ,'LV27','LEVEL27',1414,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (28 ,'LV28','LEVEL28',1549,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (29 ,'LV29','LEVEL29',1700,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (30 ,'LV30','LEVEL30',1867,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (31 ,'LV31','LEVEL31',2053,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (32 ,'LV32','LEVEL32',2261,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (33 ,'LV33','LEVEL33',2494,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (34 ,'LV34','LEVEL34',2756,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (35 ,'LV35','LEVEL35',3051,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (36 ,'LV36','LEVEL36',3383,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (37 ,'LV37','LEVEL37',3760,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (38 ,'LV38','LEVEL38',4187,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (39 ,'LV39','LEVEL39',4674,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (40 ,'LV40','LEVEL40',5229,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (41 ,'LV41','LEVEL41',5864,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (42 ,'LV42','LEVEL42',6592,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (43 ,'LV43','LEVEL43',7430,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (44 ,'LV44','LEVEL44',8395,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (45 ,'LV45','LEVEL45',9511,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (46 ,'LV46','LEVEL46',10805,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (47 ,'LV47','LEVEL47',12308,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (48 ,'LV48','LEVEL48',14060,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (49 ,'LV49','LEVEL49',16105,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (50 ,'LV50','LEVEL50',18501,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (51 ,'LV51','LEVEL51',21313,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (52 ,'LV52','LEVEL52',24623,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (53 ,'LV53','LEVEL53',28529,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (54 ,'LV54','LEVEL54',33149,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (55 ,'LV55','LEVEL55',38629,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (56 ,'LV56','LEVEL56',45145,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (57 ,'LV57','LEVEL57',52911,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (58 ,'LV58','LEVEL58',62193,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (59 ,'LV59','LEVEL59',73311,'ACTIVE',1,NOW());
INSERT INTO gate_schema.levels (id,name,code,value,status,create_id,create_date) VALUES (60 ,'LV60','LEVEL60',86665,'ACTIVE',1,NOW());


CREATE TABLE vippoint (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100),
  code CHARACTER VARYING(20) UNIQUE,
  value CHARACTER VARYING(15) NOT NULL,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

ALTER SEQUENCE vippoint_id_seq RESTART WITH 1000;

INSERT INTO vippoint (id,name,code,value,status,create_id,create_date) VALUES 
(1,'VIP1','VIP1','100','ACTIVE',1,NOW()),
(2,'VIP2','VIP2','500','ACTIVE',1,NOW()),
(3,'VIP3','VIP3','2000','ACTIVE',1,NOW()),
(4,'VIP4','VIP4','5000','ACTIVE',1,NOW()),
(5,'VIP5','VIP5','10000','ACTIVE',1,NOW()),
(6,'VIP6','VIP6','20000','ACTIVE',1,NOW()),
(7,'VIP7','VIP7','50000','ACTIVE',1,NOW()),
(8,'VIP8','VIP8','100000','ACTIVE',1,NOW()),
(9,'VIP9','VIP9','200000','ACTIVE',1,NOW()),
(10,'VIP10','VIP10','500000 ','ACTIVE',1,NOW()),
(11,'VIP11','VIP11','1000000','ACTIVE',1,NOW()),
(12,'VIP12','VIP12','2000000','ACTIVE',1,NOW());


CREATE TABLE partner (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(200) NOT NULL,
  title CHARACTER VARYING(500) NOT NULL,
  position INT DEFAULT '0', 
  photo CHARACTER VARYING(150),
  content TEXT,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

INSERT INTO partner (id,name,title,position,photo,content,status,create_id,create_date)VALUES
(1,'Vinaphone','https://vinaphone.com.vn/',1,'https://gaka.vn:7755/v1/resource/image/partner/Vinaphone.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(2,'PayTech','https://paytech.vn/',1,'https://gaka.vn:7755/v1/resource/image/partner/Vtc.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(3,'Momo','https://momo.vn/',1,'https://gaka.vn:7755/v1/resource/image/partner/Paytech.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(4,'VNPT','http://vnpttelecom.info/',1,'https://gaka.vn:7755/v1/resource/image/news/1000_4798ff9c-8b9e-411f-88c8-c4492f1c70af.png','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(5,'Sports Perfecta','https://www.sportsperfecta.com/',1,'https://gaka.vn:7755/v1/resource/image/news/1001_6b930a67-1172-4695-a4de-5efe97822455.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW());
ALTER SEQUENCE partner_id_seq RESTART WITH 10;

CREATE TABLE support (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(200) NOT NULL,
  title CHARACTER VARYING(500) NOT NULL,
  position INT DEFAULT '0', 
  photo CHARACTER VARYING(150),
  content TEXT,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);
ALTER SEQUENCE support_id_seq RESTART WITH 1;

ALTER TABLE gate_schema.game
ADD COLUMN h5sdk BOOLEAN DEFAULT FALSE,
ADD COLUMN h5desc CHARACTER VARYING(2000);