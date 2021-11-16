SET search_path TO gate_schema;

CREATE TABLE event (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100) NOT NULL,
  title CHARACTER VARYING(200) NOT NULL,
  gift_count INT DEFAULT 0,
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NOT NULL,
  position INT DEFAULT '0', 
  photo CHARACTER VARYING(150),
  content TEXT,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);
ALTER SEQUENCE event_id_seq RESTART WITH 10;

INSERT INTO event (id,name,title,gift_count,start_date,end_date,position,photo,content,status,create_id,create_date)VALUES
(1,'Event - Tương tác fan cứng','Hãy tham gia comment và tag người bạn trên fanpage',1000,NOW() - interval '1 days',NOW() + interval '30 days',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(2,'Event - Tương tác fan cứng','Hãy tham gia comment và tag người bạn trên fanpage',1000,NOW() - interval '1 days',NOW() + interval '30 days',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(3,'Event - Tương tác fan cứng','Hãy tham gia comment và tag người bạn trên fanpage',1000,NOW() - interval '1 days',NOW() + interval '30 days',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(4,'Event - Tương tác fan cứng','Hãy tham gia comment và tag người bạn trên fanpage',1000,NOW() - interval '1 days',NOW() + interval '30 days',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(5,'Event - Tương tác fan cứng','Hãy tham gia comment và tag người bạn trên fanpage',1000,NOW() - interval '1 days',NOW() + interval '30 days',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(6,'Event - Tương tác fan cứng','Hãy tham gia comment và tag người bạn trên fanpage',1000,NOW() - interval '1 days',NOW() + interval '30 days',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(7,'Event - Tương tác fan cứng','Hãy tham gia comment và tag người bạn trên fanpage',1000,NOW() - interval '1 days',NOW() + interval '30 days',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(8,'Event - Tương tác fan cứng','Hãy tham gia comment và tag người bạn trên fanpage',1000,NOW() - interval '1 days',NOW() + interval '30 days',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(9,'Event - Tương tác fan cứng','Hãy tham gia comment và tag người bạn trên fanpage',1000,NOW() - interval '1 days',NOW() + interval '30 days',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW());

ALTER SEQUENCE event_id_seq RESTART WITH 1000;
