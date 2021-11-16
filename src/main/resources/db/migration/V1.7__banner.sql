SET search_path TO gate_schema;

CREATE TABLE banner (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100),
  title CHARACTER VARYING(300),
  content CHARACTER VARYING(500) NOT NULL,
  link CHARACTER VARYING(100),
  game_id BIGINT,
  position INT DEFAULT -1,
  photo CHARACTER VARYING(200),
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

ALTER SEQUENCE banner_id_seq RESTART WITH 1000;

INSERT INTO banner (id,name,title,content,photo,game_id,position,status,create_id,create_date) VALUES 
(1,'banner vé vào chơi game','Vào chơi game miễn phí','Vào chơi game miễn phí','https://gaka.vn:7755/v1/resource/image/banner/aces.png',1,1,'ACTIVE',1,NOW()),
(112,'banner vé vào chơi game','Vào chơi game miễn phí','Vào chơi game miễn phí','https://gaka.vn:7755/v1/resource/image/banner/bejeweled.png',2,1,'ACTIVE',1,NOW()),
(2,'banner vé vào chơi  game','Vào chơi game miễn phí','Vào chơi game miễn phí','https://gaka.vn:7755/v1/resource/image/banner/cookie.png',3,2,'ACTIVE',1,NOW()),
(122,'banner vé vào chơi game','Vào chơi game miễn phí','Vào chơi game miễn phí','https://gaka.vn:7755/v1/resource/image/banner/cookie_connect.jpg',4,2,'ACTIVE',1,NOW()),
(3,'banner vé vào chơi  game','Vào chơi game miễn phí','Vào chơi game miễn phí','https://gaka.vn:7755/v1/resource/image/banner/cookie_connect_small.jpg',5,3,'ACTIVE',1,NOW()),
(132,'banner vé vào chơi game','Vào chơi game miễn phí','Vào chơi game miễn phí','https://gaka.vn:7755/v1/resource/image/banner/crazycake.png',6,3,'ACTIVE',1,NOW()),
(4,'banner vé vào chơi  game','Vào chơi game miễn phí','Vào chơi game miễn phí','https://gaka.vn:7755/v1/resource/image/banner/payday.png',7,4,'ACTIVE',1,NOW()),
(142,'banner vé vào chơi game','Vào chơi game miễn phí','Vào chơi game miễn phí','https://gaka.vn:7755/v1/resource/image/banner/aces.png',8,4,'ACTIVE',1,NOW()),
(5,'banner vé vào chơi  game','Vào chơi game miễn phí','Vào chơi game miễn phí','https://gaka.vn:7755/v1/resource/image/banner/cookie_connect.png',9,5,'ACTIVE',1,NOW());

