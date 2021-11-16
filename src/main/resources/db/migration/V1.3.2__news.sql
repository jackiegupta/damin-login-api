SET search_path TO gate_schema;


CREATE TABLE category_news (
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
ALTER SEQUENCE category_news_id_seq RESTART WITH 10;

INSERT INTO category_news (id,name,title,position,photo,content,status,create_id,create_date)VALUES
(1,'Cửa hành vip - Điểm sẽ hết hạn','',1,'','','ACTIVE',1,NOW()),
(2,'Cấp độ người dùng - Hướng dẫn nâng cấp hàng ngày','Cấp độ người dùng - Hướng dẫn nâng cấp hàng ngày',1,'','Cấp độ người dùng - Hướng dẫn nâng cấp hàng ngàyQuà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(3,'Hoàn thiện account','Hãy tham gia comment và tag người bạn trên fanpage',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(4,'Đối tác','Hãy tham gia comment và tag người bạn trên fanpage',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(5,'category_news - 7','Hãy tham gia comment và tag người bạn trên fanpage',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(6,'category_news - 8','Hãy tham gia comment và tag người bạn trên fanpage',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(7,'category_news - 9','Hãy tham gia comment và tag người bạn trên fanpage',1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW());

ALTER SEQUENCE category_news_id_seq RESTART WITH 1000;

CREATE TABLE news (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(200) NOT NULL,
  title CHARACTER VARYING(500) NOT NULL,
  category_id BIGINT,
  position INT DEFAULT '0', 
  photo CHARACTER VARYING(150),
  content TEXT,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);
ALTER SEQUENCE news_id_seq RESTART WITH 10;

INSERT INTO news (id,name,title,category_id,position,photo,content,status,create_id,create_date)VALUES
(1,'Điểm sẽ hết hạn','Chỉ cần thực hiện 1 giao dịch có tích lỹ điểm thưởng trước ngày hết hạn để gia hạn hiệu lực sử dụng điểm thưởng',1,1,'','Tìm hiểu thêm Tìm hiểu thêm Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(2,'Cách tích thêm điểm','Cách tích thêm điểm',1,1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(3,'Trở thành thành viên Đồng','Hãy tham gia comment và tag người bạn trên fanpage',1,1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(4,'Trở thành thành viên Bạc','Hãy tham gia comment và tag người bạn trên fanpage',1,1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(5,'Trở thành thành viên Vàng','Hãy tham gia comment và tag người bạn trên fanpage',1,1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(6,'Trở thành thành viên Kim cương','Hãy tham gia comment và tag người bạn trên fanpage',1,1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(7,'Hướng dẫn nâng cấp hàng ngày','Hãy tham gia comment và tag người bạn trên fanpage',2,1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(8,'Hoàn thiện account nhận thưởng','Hãy tham gia comment và tag người bạn trên fanpage',3,1,'https://gaka.vn:7755/v1/resource/image/game/game_icon_square4.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(9,'Vinaphone','https://vinaphone.com.vn/',4,1,'https://gaka.vn:7755/v1/resource/image/partner/Vinaphone.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(10,'PayTech','https://paytech.vn/',4,1,'https://gaka.vn:7755/v1/resource/image/partner/Vtc.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(11,'Momo','https://momo.vn/',4,1,'https://gaka.vn:7755/v1/resource/image/partner/Paytech.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(12,'VNPT','http://vnpttelecom.info/',4,1,'https://gaka.vn:7755/v1/resource/image/news/1000_4798ff9c-8b9e-411f-88c8-c4492f1c70af.png','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW()),
(13,'Sports Perfecta','https://www.sportsperfecta.com/',4,1,'https://gaka.vn:7755/v1/resource/image/news/1001_6b930a67-1172-4695-a4de-5efe97822455.jpg','Quà tặng KNB X500,Đá Vũ Tiên, Linh Diễm Bậc 1x10, Cống Hiến X3333','ACTIVE',1,NOW());

ALTER SEQUENCE news_id_seq RESTART WITH 1000;
