SET search_path TO gate_schema;

CREATE TABLE task (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(200),
  code CHARACTER VARYING(30) UNIQUE,
  rice INT  DEFAULT 0,
  seed INT  DEFAULT 0,
  exp_point INT  DEFAULT 0,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NOT NULL,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

ALTER SEQUENCE task_id_seq RESTART WITH 1000;

INSERT INTO task (id,name,code,rice,seed,exp_point,status,create_id,start_date, end_date, create_date) VALUES 
(1,'Đăng nhập','LOGIN',0,10,0,'ACTIVE',1,NOW() - interval '1 days', NOW() + interval '30 days', NOW()),
(2,'Chơi 5 game bất kỳ','PLAYGAME',0,10,0,'ACTIVE',1,NOW() - interval '1 days', NOW() + interval '30 days', NOW()),
(3,'Nạp thẻ bất kỳ','RECHARGE',0,10,0,'ACTIVE',1,NOW() - interval '1 days', NOW() + interval '30 days', NOW()),
(4,'Chuyển gạo vào game','TRANSFER_RICE2GAME',0,10,0,'ACTIVE',1,NOW() - interval '1 days', NOW() + interval '30 days', NOW()),
(5,'Gạo đổi sang thóc','TRANSFER_RICE2SEED',0,10,0,'ACTIVE',1,NOW() - interval '1 days', NOW() + interval '30 days', NOW());


CREATE TABLE pool_task (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  pool_id INT NOT NULL,
  task_id INT NOT NULL,
  status CHARACTER VARYING(50) DEFAULT 'ACTIVE',
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(),
  UNIQUE (pool_id,task_id),
  FOREIGN KEY (pool_id) REFERENCES pool (id),
  FOREIGN KEY (task_id) REFERENCES task (id)
);

INSERT INTO pool_task(pool_id,task_id) VALUES
	(	1	,	1	),
(	2	,	1	),
(	3	,	1	),
(	4	,	1	),
(	5	,	1	);

CREATE TABLE user_task (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  task_id INT NOT NULL,
  status CHARACTER VARYING(50) DEFAULT 'ACTIVE',
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(),
  UNIQUE (user_id,task_id),
  FOREIGN KEY (user_id) REFERENCES "user" (id),
  FOREIGN KEY (task_id) REFERENCES task (id)
);

INSERT INTO user_task(user_id,task_id) VALUES
	(1,1),(2,1),(3,1),(4,1),(5,1);
