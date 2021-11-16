SET search_path TO gate_schema;

CREATE TABLE bonus (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(200),
  title CHARACTER VARYING(500),
  level_id INT,
  rice INT  DEFAULT 0,
  seed INT  DEFAULT 0,
  exp_point INT  DEFAULT 0,
  items CHARACTER VARYING(100),
  photo CHARACTER VARYING(200),
  scope CHARACTER VARYING(50),
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

ALTER SEQUENCE bonus_id_seq RESTART WITH 1000;

INSERT INTO bonus (id,name,title, level_id,rice,seed,exp_point,items, photo, scope,status,create_date) VALUES 
(1,'Phần thưởng level 1','Mô tả Phần thưởng level 1',1,0,100,50,'1-2,3-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELUP','ACTIVE',NOW()),
(2,'Phần thưởng level 2','Mô tả Phần thưởng level 2',2,0,100,50,'1-2,3-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg','LEVELUP','ACTIVE',NOW()),
(3,'Phần thưởng level 3','Mô tả Phần thưởng level 3',3,0,100,50,'1-2,3-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','LEVELUP','ACTIVE',NOW()),
(4,'Phần thưởng level 4','Mô tả Phần thưởng level 4',4,0,100,50,'1-2,3-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELUP','ACTIVE',NOW()),
(5,'Phần thưởng level 5','Mô tả Phần thưởng level 5',5,0,100,50,'1-2,3-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg','LEVELUP','ACTIVE',NOW()),
(6,'Phần thưởng level 6','Mô tả Phần thưởng level 6',6,0,100,50,'1-2,3-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','LEVELUP','ACTIVE',NOW()),
(7,'Phần thưởng level 7','Mô tả Phần thưởng level 7',7,0,100,50,'1-2,3-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELUP','ACTIVE',NOW()),
(8,'Phần thưởng level 8','Mô tả Phần thưởng level 8',8,0,100,50,'1-2,3-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg','LEVELGIFT1','ACTIVE',NOW()),
(9,'Phần thưởng level 1 - Huy Hiệu','Mô tả Phần thưởng level 1',1,0,0,0,'1-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg','LEVELGIFT1','ACTIVE',NOW()),
(10,'Phần thưởng level 2 - Huy Hiệu','Mô tả Phần thưởng level 2',2,0,0,0,'2-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','LEVELGIFT1','ACTIVE',NOW()),
(11,'Phần thưởng level 3 - Huy Hiệu','Mô tả Phần thưởng level 3',3,0,0,0,'3-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELGIFT1','ACTIVE',NOW()),
(12,'Phần thưởng level 4 - Huy Hiệu','Mô tả Phần thưởng level 4',4,0,0,0,'4-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square5.jpg','LEVELGIFT1','ACTIVE',NOW()),
(13,'Phần thưởng level 5 - Huy Hiệu','Mô tả Phần thưởng level 5',5,0,0,0,'5-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg','LEVELGIFT1','ACTIVE',NOW()),
(14,'Phần thưởng level 6 - Huy Hiệu','Mô tả Phần thưởng level 6',6,0,0,0,'6-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg','LEVELGIFT1','ACTIVE',NOW()),
(15,'Phần thưởng level 7 - Huy Hiệu','Mô tả Phần thưởng level 7',7,0,0,0,'7-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELGIFT1','ACTIVE',NOW()),
(16,'Phần thưởng level 8 - Huy Hiệu','Mô tả Phần thưởng level 8',8,0,0,0,'8-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELGIFT1','ACTIVE',NOW()),
(17,'Phần thưởng level 1 - Khung Thần Thánh','Mô tả Phần thưởng level 1',1,0,0,0,'1-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg','LEVELGIFT2','ACTIVE',NOW()),
(18,'Phần thưởng level 2 - Khung Thần Thánh','Mô tả Phần thưởng level 2',2,0,0,0,'2-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','LEVELGIFT2','ACTIVE',NOW()),
(19,'Phần thưởng level 3 - Khung Thần Thánh','Mô tả Phần thưởng level 3',3,0,0,0,'3-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELGIFT2','ACTIVE',NOW()),
(20,'Phần thưởng level 4 - Khung Thần Thánh','Mô tả Phần thưởng level 4',4,0,0,0,'4-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square5.jpg','LEVELGIFT2','ACTIVE',NOW()),
(21,'Phần thưởng level 5 - Khung Thần Thánh','Mô tả Phần thưởng level 5',5,0,0,0,'5-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg','LEVELGIFT2','ACTIVE',NOW()),
(22,'Phần thưởng level 6 - Khung Thần Thánh','Mô tả Phần thưởng level 6',6,0,0,0,'6-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg','LEVELGIFT2','ACTIVE',NOW()),
(23,'Phần thưởng level 7 - Khung Thần Thánh','Mô tả Phần thưởng level 7',7,0,0,0,'7-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELGIFT2','ACTIVE',NOW()),
(24,'Phần thưởng level 8 - Khung Thần Thánh','Mô tả Phần thưởng level 8',8,0,0,0,'8-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELGIFT2','ACTIVE',NOW()),
(25,'Phần thưởng level 1 - Ảnh nền Galaxy','Mô tả Phần thưởng level 1',1,0,0,0,'1-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg','LEVELGIFT3','ACTIVE',NOW()),
(26,'Phần thưởng level 2 - Ảnh nền Galaxy','Mô tả Phần thưởng level 2',2,0,0,0,'2-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','LEVELGIFT3','ACTIVE',NOW()),
(27,'Phần thưởng level 3 - Ảnh nền Galaxy','Mô tả Phần thưởng level 3',3,0,0,0,'3-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELGIFT3','ACTIVE',NOW()),
(28,'Phần thưởng level 4 - Ảnh nền Galaxy','Mô tả Phần thưởng level 4',4,0,0,0,'4-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square5.jpg','LEVELGIFT3','ACTIVE',NOW()),
(29,'Phần thưởng level 5 - Ảnh nền Galaxy','Mô tả Phần thưởng level 5',5,0,0,0,'5-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg','LEVELGIFT3','ACTIVE',NOW()),
(30,'Phần thưởng level 6 - Ảnh nền Galaxy','Mô tả Phần thưởng level 6',6,0,0,0,'6-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg','LEVELGIFT3','ACTIVE',NOW()),
(31,'Phần thưởng level 7 - Ảnh nền Galaxy','Mô tả Phần thưởng level 7',7,0,0,0,'7-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELGIFT3','ACTIVE',NOW()),
(32,'Phần thưởng level 8 - Ảnh nền Galaxy','Mô tả Phần thưởng level 8',8,0,0,0,'8-2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','LEVELGIFT3','ACTIVE',NOW())
;


CREATE TABLE user_bonus (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  bonus_id INT NOT NULL,
  status CHARACTER VARYING(50) DEFAULT 'ACTIVE',
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(),
  UNIQUE (user_id,bonus_id),
  FOREIGN KEY (user_id) REFERENCES "user" (id),
  FOREIGN KEY (bonus_id) REFERENCES bonus (id)
);

INSERT INTO user_bonus(user_id,bonus_id) VALUES
	(1,1),(2,1);