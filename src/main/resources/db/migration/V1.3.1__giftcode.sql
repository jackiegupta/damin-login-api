SET search_path TO gate_schema;

CREATE TABLE pool (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100),
  title CHARACTER VARYING(500),
  content CHARACTER VARYING(500),
  count INT DEFAULT 1,
  size INT  DEFAULT 6,
  type CHARACTER VARYING(50),
  photo CHARACTER VARYING(150),
  game_id INT,
  event_id INT,
  rice INT  DEFAULT 0,
  seed INT  DEFAULT 0,
  exp_point INT  DEFAULT 0,
  items CHARACTER VARYING(2000),
  scope CHARACTER VARYING(50),
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(),
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NOT NULL,
  FOREIGN KEY (event_id) REFERENCES event (id)  
);

ALTER SEQUENCE pool_id_seq RESTART WITH 1000;

INSERT INTO pool (id,name,title,content, photo, count,size,type,game_id,event_id,scope,status,create_date,start_date,end_date, items) VALUES 
(1,'Giftcode 1','Comment 1','Comment 2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',1,2,'SINGLE',NULL,1,'EVENT','ACTIVE',NOW(),NOW() - interval '1 days',NOW() + interval '30 days','1-3,2-1'),
(2,'Giftcode 2','Comment 1','Comment 2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',1,2,'SINGLE',1,NULL,'GAME','ACTIVE',NOW(),NOW() - interval '1 days',NOW() + interval '30 days','1-3,2-1'),
(3,'Giftcode 3','Comment 1','Comment 2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',1,7,'MULTI',NULL,1,'EVENT','ACTIVE',NOW(),NOW() - interval '1 days',NOW() + interval '30 days','1-3,2-1'),
(4,'Giftcode 4','Comment 1','Comment 2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',1,2,'ALL',1,NULL,'GAME','ACTIVE',NOW(),NOW() - interval '1 days',NOW() + interval '30 days','1,2'),
(5,'Giftcode 5','Comment 1','Comment 2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',1,2,'SINGLE',NULL,1,'EVENT','ACTIVE',NOW(),NOW() - interval '1 days',NOW() + interval '30 days','1-3,2-1');

CREATE TABLE giftcode (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  code CHARACTER VARYING(20) unique,
  type CHARACTER VARYING(50),
  pool_id INT,
  event_id INT,
  game_id INT,
  user_id BIGINT,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(), 
  FOREIGN KEY (pool_id) REFERENCES pool (id)  
);

INSERT INTO giftcode (id,code,type,pool_id,event_id,user_id,status,create_date) VALUES 
(1,'SCODE1','SINGLE',1,NULL,1,'ACTIVE',NOW()),
(2,'SCODE2','SINGLE',2,NULL,2,'ACTIVE',NOW()),
(3,'MCODE1','MULTI',3,3,NULL,'INACTIVE',NOW());

ALTER SEQUENCE giftcode_id_seq RESTART WITH 1000;
