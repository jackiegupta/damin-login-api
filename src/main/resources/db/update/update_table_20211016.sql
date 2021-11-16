ALTER TABLE gate_schema.game
ADD COLUMN h5sdk BOOLEAN DEFAULT FALSE,
ADD COLUMN h5desc CHARACTER VARYING(2000);



INSERT INTO gate_schema.setting (id,name,code,value,status,create_id,create_date) VALUES 
(1,'Tổng Thành viên (Tỷ lệ %)','COUNT_MEMBER','500','ACTIVE',1,NOW()),
(2,'Tổng Onlice (Tỷ lệ %)','ONLINE','300','ACTIVE',1,NOW());



ALTER TABLE gate_schema.user
ADD COLUMN user_id360 BIGINT DEFAULT 0;
