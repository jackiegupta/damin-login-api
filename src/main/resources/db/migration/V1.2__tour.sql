SET search_path TO gate_schema;

CREATE TABLE tour (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100) NOT NULL,
  title CHARACTER VARYING(200) NOT NULL,
  game_id INT NOT NULL,
  price BIGINT DEFAULT 0, 
  user_count  INT NOT NULL,
  hot BOOLEAN DEFAULT false, 
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NOT NULL,
  win_price BIGINT DEFAULT 0,
  win_item_id BIGINT,
  win_user_id BIGINT,
  position INT DEFAULT '0', 
  photo CHARACTER VARYING(150),
  content CHARACTER VARYING(2000) NOT NULL,
  rule CHARACTER VARYING(2000) NOT NULL,
  type CHARACTER VARYING(100),
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);
ALTER SEQUENCE tour_id_seq RESTART WITH 10;

INSERT INTO tour (id,name,title,game_id,price,user_count,hot,start_date,end_date,win_price,win_item_id,win_user_id,position,photo,content,rule,status,type,create_id,create_date)VALUES
(1,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',1,10000,10,TRUE,NOW() - interval '2 days',NOW() + interval '10 days',100000,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','ACTIVE','POINT',1,NOW()),
(2,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',2,10001,11,TRUE,NOW() - interval '2 days',NOW() + interval '10 days',100001,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','ACTIVE','GROUP',1,NOW()),
(3,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',3,10002,12,TRUE,NOW() - interval '2 days',NOW() + interval '10 days',100002,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','ACTIVE','POINT',1,NOW()),
(4,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',4,10003,13,TRUE,NOW() - interval '2 days',NOW() + interval '10 days',100003,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','ACTIVE','POINT',1,NOW()),
(5,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',5,10004,14,TRUE,NOW() - interval '2 days',NOW() + interval '10 days',100004,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','ACTIVE','POINT',1,NOW()),
(6,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',6,10005,15,TRUE,NOW() - interval '2 days',NOW() + interval '10 days',100005,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','ACTIVE','GROUP',1,NOW()),
(7,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',7,10006,16,TRUE,NOW() - interval '2 days',NOW() + interval '10 days',100006,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','ACTIVE','POINT',1,NOW()),
(8,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',8,10007,17,TRUE,NOW() - interval '2 days',NOW() + interval '10 days',100007,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','ACTIVE','GROUP',1,NOW()),
(9,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',9,10008,18,TRUE,NOW() - interval '2 days',NOW() + interval '10 days',100008,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','ACTIVE','POINT',1,NOW()),
(10,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',10,10009,19,TRUE,NOW() - interval '2 days',NOW() + interval '10 days',100009,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','ACTIVE','POINT',1,NOW()),
(11,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',11,10010,20,TRUE,NOW() - interval '2 days',NOW() + interval '10 days',100010,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','ACTIVE','POINT',1,NOW()),
(12,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',12,10011,21,TRUE,NOW() - interval '5 days',NOW() - interval '2 days',100011,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn', 'Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','FINISH','GROUP',1,NOW()),
(13,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',13,10012,22,TRUE,NOW() - interval '5 days',NOW() - interval '2 days',100012,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn', 'Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','FINISH','POINT',1,NOW()),
(14,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',14,10013,23,TRUE,NOW() - interval '5 days',NOW() - interval '2 days',100013,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn', 'Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','FINISH','GROUP',1,NOW()),
(15,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',15,10014,24,TRUE,NOW() - interval '5 days',NOW() - interval '2 days',100014,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn', 'Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','FINISH','POINT',1,NOW()),
(16,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',16,10015,25,TRUE,NOW() - interval '5 days',NOW() - interval '2 days',100015,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn', 'Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','FINISH','POINT',1,NOW()),
(17,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',17,10016,26,TRUE,NOW() - interval '5 days',NOW() - interval '1 days',100016,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn', 'Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','FINISH','GROUP',1,NOW()),
(18,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',18,10017,27,TRUE,NOW() - interval '5 days',NOW() - interval '1 days',100017,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn', 'Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','FINISH','POINT',1,NOW()),
(19,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',19,10018,28,TRUE,NOW() + interval '1 days',NOW() + interval '10 days',100018,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn', 'Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','COMMING','POINT',1,NOW()),
(20,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',20,10019,29,TRUE,NOW() + interval '1 days',NOW() + interval '10 days',100019,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn', 'Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','COMMING','GROUP',1,NOW()),
(21,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',21,10020,30,TRUE,NOW() + interval '2 days',NOW() + interval '10 days',100020,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn', 'Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','COMMING','GROUP',1,NOW()),
(22,'Vinh danh anh hùng 2021','Hãy tham gia game Fly Car cùng tour đấu hấp dẫn nhất trong năm',22,10021,31,TRUE,NOW() + interval '2 days',NOW() + interval '10 days',100021,1,1,1,'https://gaka.vn:7755/v1/resource/image/banner/aces.png','Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn', 'Giải đấu quy tụ những game thủ nổi bật nhất trong bảo xếp hạng của năm. Giải thưởng cực hấp dẫn','COMMING','POINT',1,NOW());
ALTER SEQUENCE tour_id_seq RESTART WITH 1000;



CREATE TABLE "result" (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  tour_id INT NOT NULL,
  user_id BIGINT NOT NULL,
  position INT DEFAULT -1,
  win BOOLEAN default false,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(), 
  FOREIGN KEY (tour_id) REFERENCES "tour" (id),  
  FOREIGN KEY (user_id) REFERENCES "user" (id)  
);

ALTER SEQUENCE result_id_seq RESTART WITH 1000;

INSERT INTO "result" (id,tour_id,user_id,position,win,status,create_id,create_date) VALUES 
(1,1,1,1,true,'ACTIVE',1,NOW()),
(2,2,2,1,true,'ACTIVE',1,NOW()),
(3,3,3,1,true,'ACTIVE',1,NOW()),
(4,4,4,1,true,'ACTIVE',1,NOW()),
(5,5,5,1,true,'ACTIVE',1,NOW()),
(6,6,6,1,true,'ACTIVE',1,NOW()),
(7,7,7,1,true,'ACTIVE',1,NOW()),
(8,8,8,1,true,'ACTIVE',1,NOW()),
(9,9,9,1,true,'ACTIVE',1,NOW()),
(10,10,10,1,true,'ACTIVE',1,NOW()),
(11,11,11,1,true,'ACTIVE',1,NOW()),
(12,12,12,1,true,'ACTIVE',1,NOW()),
(13,13,66,1,true,'ACTIVE',1,NOW()),
(14,14,88,1,true,'ACTIVE',1,NOW()),
(15,15,95,1,true,'ACTIVE',1,NOW()),
(16,16,99,1,true,'ACTIVE',1,NOW()),
(17,17,1,1,true,'ACTIVE',1,NOW()),
(18,18,2,1,true,'ACTIVE',1,NOW()),
(19,19,3,1,true,'ACTIVE',1,NOW()),
(20,20,4,1,true,'ACTIVE',1,NOW()),
(21,21,5,1,true,'ACTIVE',1,NOW()),
(22,22,6,1,true,'ACTIVE',1,NOW()),
(23,1,7,1,true,'ACTIVE',1,NOW()),
(24,2,8,1,true,'ACTIVE',1,NOW()),
(25,3,9,1,true,'ACTIVE',1,NOW()),
(26,4,10,1,true,'ACTIVE',1,NOW()),
(27,5,11,1,true,'ACTIVE',1,NOW()),
(28,6,12,1,true,'ACTIVE',1,NOW()),
(29,7,66,1,true,'ACTIVE',1,NOW()),
(30,8,88,1,true,'ACTIVE',1,NOW()),
(31,9,95,1,true,'ACTIVE',1,NOW()),
(32,10,99,1,true,'ACTIVE',1,NOW());
