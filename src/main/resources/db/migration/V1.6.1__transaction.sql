SET search_path TO gate_schema;

CREATE TABLE transaction (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  from_user_id BIGINT NOT NULL,
  to_user_id BIGINT NOT NULL,
  amount BIGINT NOT NULL,
  currency CHARACTER VARYING(50),
  type CHARACTER VARYING(50),
  type_change CHARACTER VARYING(50),
  title CHARACTER VARYING(2000),
  content CHARACTER VARYING(10000),
  status CHARACTER VARYING(50),
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

ALTER SEQUENCE transaction_id_seq RESTART WITH 1000;
INSERT INTO gate_schema.transaction (id,from_user_id,to_user_id,amount,currency,TYPE,title, status,type_change,create_date) VALUES 
(1,1,95,1000,'RICE','BUYGAME','Đăng ký game 1','CREATED','MINUS',NOW()),
(2,1,95,2000,'RICE','BUYITEM','Mua vật phẩm Vòng Kim Cô','CREATED','MINUS',NOW()),
(3,1,95,100,'RICE','GIFTCODE','Kích hoạt giftcode 1A12DAHDKH','CREATED','PLUS',NOW()),
(4,1,95,10,'SEED','ATTEND','Điểm danh ngày 19/08/2021 11:11:20','CREATED','PLUS',NOW()),
(5,1,95,10,'SEED','ATTEND','Điểm danh ngày 1/09/2021 11:11:20','CREATED','PLUS',NOW()),
(6,1,95,10,'SEED','ATTEND','Điểm danh ngày 2/08/2021 11:11:20','CREATED','PLUS',NOW()),
(7,1,95,10,'SEED','ATTEND','Điểm danh ngày 3/08/2021 11:11:20','CREATED','PLUS',NOW()),
(8,1,95,10,'SEED','ATTEND','Điểm danh ngày 4/08/2021 11:11:20','CREATED','PLUS',NOW());