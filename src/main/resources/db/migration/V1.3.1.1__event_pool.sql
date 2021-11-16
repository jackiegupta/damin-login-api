SET search_path TO gate_schema;

CREATE TABLE event_pool (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  event_id INT NOT NULL,
  pool_id INT NOT NULL,
  status CHARACTER VARYING(50) DEFAULT 'ACTIVE',
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(),
  UNIQUE (event_id,pool_id),
  FOREIGN KEY (event_id) REFERENCES event (id),
  FOREIGN KEY (pool_id) REFERENCES pool (id)
);

INSERT INTO event_pool(event_id,pool_id) VALUES
(	1	,	1	),
(	2	,	1	),
(	3	,	1	);


CREATE TABLE event_game (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  event_id INT NOT NULL,
  game_id INT NOT NULL,
  status CHARACTER VARYING(50) DEFAULT 'ACTIVE',
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(),
  UNIQUE (event_id,game_id),
  FOREIGN KEY (event_id) REFERENCES event (id),
  FOREIGN KEY (game_id) REFERENCES game (id)
);
INSERT INTO event_game(event_id,game_id) VALUES
(	1	,	3	),
(	2	,	2	),
(	3	,	1	);


CREATE TABLE user_giftcode (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  giftcode_id INT NOT NULL,
  game_id INT,
  server_id INT,
  actor_name  CHARACTER VARYING(50),
  status CHARACTER VARYING(50) DEFAULT 'ACTIVE',
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(),
  UNIQUE (user_id,giftcode_id),
  FOREIGN KEY (user_id) REFERENCES "user" (id),
  FOREIGN KEY (giftcode_id) REFERENCES giftcode (id)
);


