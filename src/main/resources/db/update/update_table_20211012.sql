ALTER TABLE gate_schema.tour ALTER COLUMN title TYPE varchar(2000) USING title::varchar;
ALTER TABLE gate_schema.tour ALTER COLUMN name TYPE varchar(500) USING name::varchar;

SET search_path TO gate_schema;
CREATE TABLE partner (
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
ALTER SEQUENCE partner_id_seq RESTART WITH 1;



CREATE TABLE support (
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
ALTER SEQUENCE support_id_seq RESTART WITH 1;