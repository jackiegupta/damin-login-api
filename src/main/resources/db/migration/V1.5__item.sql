SET search_path TO gate_schema;

CREATE TABLE item (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100),
  title CHARACTER VARYING(100),
  price BIGINT DEFAULT 0,
  currency CHARACTER VARYING(20),
  game_id INT DEFAULT 0,
  position INT DEFAULT -1,
  photo CHARACTER VARYING(200),
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

ALTER SEQUENCE item_id_seq RESTART WITH 1000;

INSERT INTO item (id,name,title,price,currency,photo,game_id,position,status,create_id,create_date) VALUES 
(1,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,1,'ACTIVE',1,NOW()),
(112,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,1,'ACTIVE',1,NOW()),
(2,'Item vé vào chơi  game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,2,'ACTIVE',1,NOW()),
(122,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,2,'ACTIVE',1,NOW()),
(3,'Item vé vào chơi  game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,3,'ACTIVE',1,NOW()),
(132,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,3,'ACTIVE',1,NOW()),
(4,'Item vé vào chơi  game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,4,'ACTIVE',1,NOW()),
(142,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,4,'ACTIVE',1,NOW()),
(5,'Item vé vào chơi  game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,5,'ACTIVE',1,NOW()),
(152,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,5,'ACTIVE',1,NOW()),
(6,'Item vé vào chơi  game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,6,'ACTIVE',1,NOW()),
(162,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,6,'ACTIVE',1,NOW()),
(7,'Item vé vào chơi  game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,7,'ACTIVE',1,NOW()),
(172,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,7,'ACTIVE',1,NOW()),
(8,'Item vé vào chơi  game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,8,'ACTIVE',1,NOW()),
(182,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,8,'ACTIVE',1,NOW()),
(9,'Item vé vào chơi  game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,9,'ACTIVE',1,NOW()),
(192,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,9,'ACTIVE',1,NOW()),
(10,'Item vé vào chơi  game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,10,'ACTIVE',1,NOW()),
(102,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,10,'ACTIVE',1,NOW()),
(11,'Item vé vào chơi  game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,11,'ACTIVE',1,NOW()),
(212,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,11,'ACTIVE',1,NOW()),
(12,'Item vé vào chơi  game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,12,'ACTIVE',1,NOW()),
(222,'Item vé vào chơi game','Vào chơi game miễn phí',1000,'RICE','https://gaka.vn:7755/v1/resource/image/game/game_icon_square2.jpg',0,12,'ACTIVE',1,NOW());

CREATE TABLE user_item (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  item_id INT NOT NULL,
  count INT DEFAULT 0,
  status CHARACTER VARYING(50) DEFAULT 'ACTIVE',
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(),
  UNIQUE (user_id,item_id),
  FOREIGN KEY (user_id) REFERENCES "user" (id),
  FOREIGN KEY (item_id) REFERENCES item (id)
);

INSERT INTO user_item(user_id,item_id,count) VALUES
(1,1,1),(1,2,1),(1,3,1),(1,4,1),(1,5,1),
(2,1,2),(2,2,2),(2,3,2),(2,4,2),(2,5,2),
(3,1,3),(3,2,3),(3,3,3),(3,4,3),(3,5,3),
(4,1,4),(4,2,4),(4,3,4),(4,4,4),(4,5,4),
(5,1,5),(5,2,5),(5,3,5),(5,4,5),(5,5,5),
(6,1,6),(6,2,6),(6,3,6),(6,4,6),(6,5,6),
(7,1,7),(7,2,7),(7,3,7),(7,4,7),(7,5,7),
(8,1,8),(8,2,8),(8,3,8),(8,4,8),(8,5,8),
(9,1,9),(9,2,9),(9,3,9),(9,4,9),(9,5,9);
