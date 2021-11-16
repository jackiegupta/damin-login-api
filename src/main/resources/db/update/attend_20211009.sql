SET search_path TO gate_schema;

CREATE TABLE attend (
  id SERIAL NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(100),
  code CHARACTER VARYING(20) UNIQUE,
  value CHARACTER VARYING(15) NOT NULL,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

ALTER SEQUENCE attend_id_seq RESTART WITH 1000;

INSERT INTO attend (id,name,value,status,create_id,create_date) VALUES 
(1,'Điểm danh ngày 1','50','ACTIVE',1,NOW()),
(2,'Điểm danh ngày 2','100','ACTIVE',1,NOW()),
(3,'Điểm danh ngày 3','150','ACTIVE',1,NOW()),
(4,'Điểm danh ngày 4','200','ACTIVE',1,NOW()),
(5,'Điểm danh ngày 5','250','ACTIVE',1,NOW()),
(6,'Điểm danh ngày 6','300','ACTIVE',1,NOW()),
(7,'Điểm danh ngày 7','350','ACTIVE',1,NOW());

ALTER TABLE gate_schema."event" ALTER COLUMN gift_count SET DEFAULT 0;