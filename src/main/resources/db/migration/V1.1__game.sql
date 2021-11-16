SET search_path TO gate_schema;

CREATE TABLE game (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100) NOT NULL, 
  link_game CHARACTER VARYING(300),
  content CHARACTER VARYING(500) NOT NULL, 
  game_type INT NOT NULL,
  amount BIGINT DEFAULT 0,
  currency CHARACTER VARYING(20),
  platform CHARACTER VARYING(50),
  android_link CHARACTER VARYING(300),
  ios_link CHARACTER VARYING(300),
  windown_link CHARACTER VARYING(300),
  home_page CHARACTER VARYING(300),
  fan_page CHARACTER VARYING(300),
  publish_id INT NOT NULL DEFAULT 0,
  news BOOLEAN DEFAULT FALSE,
  hot BOOLEAN DEFAULT FALSE,
  top BOOLEAN DEFAULT FALSE,
  position  INT DEFAULT 0,
  photo CHARACTER VARYING(200),
  status CHARACTER VARYING(50),
  create_id BIGINT NULL,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP
);


INSERT INTO game (id,name,link_game,content,photo,news,status,create_id,create_date,game_type,publish_id,amount,currency,platform,android_link,ios_link,windown_link,home_page,fan_page) VALUES
(1,'Fly Car Tên dài quá nên phải cắt bỏ Tên dài quá nên phải cắt bỏ 1','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Tên dài quá nên phải cắt bỏ 1','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(2,'Fly Car Tên dài quá nên phải cắt bỏ 2','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 2','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(3,'Fly Car Tên dài quá nên phải cắt bỏ 3','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 3','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(4,'Fly Car Tên dài quá nên phải cắt bỏ 4','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 4','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(5,'Fly Car Tên dài quá nên phải cắt bỏ 5','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 5','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(6,'Fly Car Tên dài quá nên phải cắt bỏ 6','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 6','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(7,'Fly Car Tên dài quá nên phải cắt bỏ 7','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 7','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(8,'Fly Car Tên dài quá nên phải cắt bỏ 8','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 8','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(9,'Fly Car Tên dài quá nên phải cắt bỏ 9','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 9','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(10,'Fly Car Tên dài quá nên phải cắt bỏ Hello','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 10','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(11,'Fly Car Tên dài quá nên phải cắt bỏ ABC','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 11','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(12,'Fly Car Tên dài quá nên phải cắt bỏ 123','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 456','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(13,'Fly Car Tên dài quá nên phải cắt bỏ 123','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 456','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com');

ALTER SEQUENCE game_id_seq RESTART WITH 20;

INSERT INTO game (id,name,link_game,content,photo,news,status,create_id,create_date,game_type,publish_id,amount,currency,platform,android_link,ios_link,windown_link,home_page,fan_page) VALUES
(14,'Fly Car Tên dài quá nên phải cắt bỏ new 111','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 111','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(15,'Fly Car Tên dài quá nên phải cắt bỏ new 112','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 112','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(16,'Fly Car Tên dài quá nên phải cắt bỏ new 113','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 113','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(17,'Fly Car Tên dài quá nên phải cắt bỏ new 114','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 114','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(18,'Fly Car Tên dài quá nên phải cắt bỏ new 115','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 115','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(19,'Fly Car Tên dài quá nên phải cắt bỏ new 116','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 116','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(20,'Fly Car Tên dài quá nên phải cắt bỏ new 117','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 117','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(21,'Fly Car Tên dài quá nên phải cắt bỏ new 118','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 118','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(22,'Fly Car Tên dài quá nên phải cắt bỏ new 119','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 119','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(23,'Fly Car Tên dài quá nên phải cắt bỏ new 120','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 120','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(24,'Fly Car Tên dài quá nên phải cắt bỏ new 121','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 121','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(25,'Fly Car Tên dài quá nên phải cắt bỏ new 122','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 122','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(26,'Fly Car Tên dài quá nên phải cắt bỏ new 123','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 123','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(27,'Fly Car Tên dài quá nên phải cắt bỏ new 124','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ 124','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com');


INSERT INTO game (id,name,link_game,content,photo,hot,status,create_id,create_date,game_type,publish_id,amount,currency,platform,android_link,ios_link,windown_link,home_page,fan_page) VALUES
(28,'Fly Car Tên dài quá nên phải cắt bỏ Hot 124','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 124','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(29,'Fly Car Tên dài quá nên phải cắt bỏ Hot 125','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 125','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(30,'Fly Car Tên dài quá nên phải cắt bỏ Hot 126','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 126','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(31,'Fly Car Tên dài quá nên phải cắt bỏ Hot 127','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 127','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(32,'Fly Car Tên dài quá nên phải cắt bỏ Hot 128','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 128','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(33,'Fly Car Tên dài quá nên phải cắt bỏ Hot 129','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 129','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(34,'Fly Car Tên dài quá nên phải cắt bỏ Hot 130','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 130','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(35,'Fly Car Tên dài quá nên phải cắt bỏ Hot 131','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 131','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(36,'Fly Car Tên dài quá nên phải cắt bỏ Hot 132','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 132','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(37,'Fly Car Tên dài quá nên phải cắt bỏ Hot 133','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 133','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(38,'Fly Car Tên dài quá nên phải cắt bỏ Hot 134','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 134','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(39,'Fly Car Tên dài quá nên phải cắt bỏ Hot 135','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 135','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(40,'Fly Car Tên dài quá nên phải cắt bỏ Hot 136','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ Hot 136','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com');


INSERT INTO game (id,name,link_game,content,photo,top,status,create_id,create_date,game_type,publish_id,amount,currency,platform,android_link,ios_link,windown_link,home_page,fan_page) VALUES
(41,'Fly Car Tên dài quá nên phải cắt bỏ top 147','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 147','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(42,'Fly Car Tên dài quá nên phải cắt bỏ top 148','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 148','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(43,'Fly Car Tên dài quá nên phải cắt bỏ top 149','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 149','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(44,'Fly Car Tên dài quá nên phải cắt bỏ top 150','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 150','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(45,'Fly Car Tên dài quá nên phải cắt bỏ top 151','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 151','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(46,'Fly Car Tên dài quá nên phải cắt bỏ top 152','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 152','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(47,'Fly Car Tên dài quá nên phải cắt bỏ top 153','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 153','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(48,'Fly Car Tên dài quá nên phải cắt bỏ top 154','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 154','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(49,'Fly Car Tên dài quá nên phải cắt bỏ top 155','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 155','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(50,'Fly Car Tên dài quá nên phải cắt bỏ top 156','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 156','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(51,'Fly Car Tên dài quá nên phải cắt bỏ top 157','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 157','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(52,'Fly Car Tên dài quá nên phải cắt bỏ top 158','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 158','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(53,'Fly Car Tên dài quá nên phải cắt bỏ top 159','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 159','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(54,'Fly Car Tên dài quá nên phải cắt bỏ top 160','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ top 160','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com');


INSERT INTO game (id,name,link_game,content,photo,hot,status,create_id,create_date,game_type,publish_id,amount,currency,platform,android_link,ios_link,windown_link,home_page,fan_page) VALUES
(55 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 168','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 168','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(56 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 169','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 169','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(57 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 170','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 170','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(58 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 171','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 171','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(59 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 172','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 172','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(60 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 173','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 173','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(61 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 174','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 174','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(62 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 175','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 175','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),2,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(63 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 176','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 176','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(64 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 177','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 177','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(65 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 178','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 178','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(66 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 179','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 179','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(67 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 180','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 180','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(68 ,'Fly Car Tên dài quá nên phải cắt bỏ hot 181','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ hot 181','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),1,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com');

INSERT INTO game (id,name,link_game,content,photo,hot,status,create_id,create_date,game_type,publish_id,amount,currency,platform,android_link,ios_link,windown_link,home_page,fan_page) VALUES
(69,'Fly Car Tên dài quá nên phải cắt bỏ city 1','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(70,'Fly Car Tên dài quá nên phải cắt bỏ city 2','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(71,'Fly Car Tên dài quá nên phải cắt bỏ city 3','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(72,'Fly Car Tên dài quá nên phải cắt bỏ city 4','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(73,'Fly Car Tên dài quá nên phải cắt bỏ city 5','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(74,'Fly Car Tên dài quá nên phải cắt bỏ city 6','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(75,'Fly Car Tên dài quá nên phải cắt bỏ city 7','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(76,'Fly Car Tên dài quá nên phải cắt bỏ city 8','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(77,'Fly Car Tên dài quá nên phải cắt bỏ city 9','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(78,'Fly Car Tên dài quá nên phải cắt bỏ city 10','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(79,'Fly Car Tên dài quá nên phải cắt bỏ city 11','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(80,'Fly Car Tên dài quá nên phải cắt bỏ city 12','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(81,'Fly Car Tên dài quá nên phải cắt bỏ city 13','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square1.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com'),
(82,'Fly Car Tên dài quá nên phải cắt bỏ city 14','http://mini.gmoviet.com/vme/sat-thu-bong-dem/','Fly Car Tên dài quá nên phải cắt bỏ city','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg',true,'ACTIVE',1,NOW(),8,1,1000,'RICE','APP','https://play.google.com','https://www.apple.com/vn/app-store/','https://360game.vn/','https://360game.vn/','https://facebook.com');

ALTER SEQUENCE game_id_seq RESTART WITH 1000;


CREATE TABLE user_game (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  game_id INT NOT NULL,
  id360 INT,
  amount INT DEFAULT 0,
  status CHARACTER VARYING(50) DEFAULT 'ACTIVE',
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(),
  UNIQUE (user_id,game_id),
  FOREIGN KEY (user_id) REFERENCES "user" (id),
  FOREIGN KEY (game_id) REFERENCES game (id)
);

INSERT INTO user_game(user_id,game_id,amount) VALUES
	(1,2,3000),(1,3,3000),(1,4,3000),(1,5,3000),(1,6,3000),(1,8,3000),
	(2,1,3000),(2,3,3000),(5,4,3000),(5,5,3000),
	(3,1,3000),(3,2,3000),(6,4,3000),(6,5,3000);
INSERT INTO user_game(user_id,game_id,id360,amount) VALUES 
(1,1,28,1000);




CREATE TABLE game_type (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100) NOT NULL,
  position INT DEFAULT '0', 
  content CHARACTER VARYING(500) NOT NULL, 
  photo CHARACTER VARYING(200),
  status CHARACTER VARYING(50),
  create_id BIGINT NULL,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP
);

INSERT INTO game_type (id,name,content,photo,status,create_id,create_date) VALUES
(1,'ARPG','Game nhập vai hành động','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(2,'MMORPG','Game nhập vai trực tuyến nhiều người chơi','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(3,'RPG','Game nhập vai huyền thoại','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(4,'SLG','Game chiến thuật phổ biến','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(5,'SPORT','Game thể thao','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(6,'MOBA','Game phổ biến nhất thế giới','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(7,'Puzzle','Trò chơi','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(8,'City','Game City','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW());
ALTER SEQUENCE game_type_id_seq RESTART WITH 20;



CREATE TABLE publish (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100) NOT NULL,
  position INT DEFAULT '0', 
  content CHARACTER VARYING(500) NOT NULL, 
  photo CHARACTER VARYING(200),
  status CHARACTER VARYING(50),
  create_id BIGINT NULL,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP
);

INSERT INTO  publish(id,name,content,photo,status,create_id,create_date) VALUES
(1,'VNG','Game VNG','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(2,'VTV','Game VTV','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(3,'Publish3','Game Pub3','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(4,'Publish4','Game Pub4','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(5,'Publish5','Game Pub5','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(6,'Publish6','Game Pub6','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW()),
(7,'Publish7','Game Pub7','https://gaka.vn:7755/v1/resource/image/game/game_icon_square3.jpg','ACTIVE',1,NOW());
ALTER SEQUENCE publish_id_seq RESTART WITH 20;
