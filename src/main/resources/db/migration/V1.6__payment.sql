SET search_path TO gate_schema;

CREATE TABLE product (
  id SERIAL NOT NULL PRIMARY KEY,
  price INT NOT NULL,
  amount INT NOT NULL,
  currency CHARACTER VARYING(20),
  discount INT DEFAULT '0',
  exp INT DEFAULT 0,
  status CHARACTER VARYING(50),
  create_id BIGINT,
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW()
);

ALTER SEQUENCE product_id_seq RESTART WITH 1000;

INSERT INTO product (id,price,amount,currency,discount,exp,status,create_id,create_date) VALUES 
(10,  10000, 16000,'RICE',10,100,'ACTIVE',1,NOW()),
(20,  20000, 32000,'RICE',10,200,'ACTIVE',1,NOW()),
(50,  50000, 84000,'RICE',10,300,'ACTIVE',1,NOW()),
(100,100000,168000,'RICE',10,400,'ACTIVE',1,NOW()),
(200,200000,340000,'RICE',10,600,'ACTIVE',1,NOW()),
(500,500000,856000,'RICE',10,1000,'ACTIVE',1,NOW()),
(910,  10000, 16000,'SEED',10,0,'ACTIVE',1,NOW()),
(920,  20000, 32000,'SEED',10,0,'ACTIVE',1,NOW()),
(950,  50000, 84000,'SEED',10,0,'ACTIVE',1,NOW()),
(9100,100000,168000,'SEED',10,0,'ACTIVE',1,NOW()),
(9200,200000,340000,'SEED',10,0,'ACTIVE',1,NOW()),
(9500,500000,856000,'SEED',10,0,'ACTIVE',1,NOW());


CREATE TABLE payment (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  product_id INT NOT NULL,
  bill_id CHARACTER VARYING(30) NOT NULL,
  ref_id CHARACTER VARYING(30) NOT NULL,
  price INT NOT NULL,
  method CHARACTER VARYING(50),
  status CHARACTER VARYING(50),
  create_date TIMESTAMP DEFAULT NOW(),
  update_date TIMESTAMP DEFAULT NOW(),
  UNIQUE (user_id,product_id, bill_id),
  FOREIGN KEY (user_id) REFERENCES "user" (id),
  FOREIGN KEY (product_id) REFERENCES product (id)
);
ALTER SEQUENCE payment_id_seq RESTART WITH 1000;

INSERT INTO payment(id,user_id,product_id,price,method,status,bill_id,ref_id,create_date) VALUES
(1,1,10,10000,'VITAPAY','PAID', 'PE15000250218', 'VME15000250218',NOW() - interval '1 days'),
(2,1,10,10001,'VITAPAY','PAID', 'PE15000251219', 'VME15000251219',NOW() - interval '2 days'),
(3,1,10,10002,'VITAPAY','PAID', 'PE15000252220', 'VME15000252220',NOW() - interval '3 days'),
(4,1,10,10003,'VITAPAY','PAID', 'PE15000253221', 'VME15000253221',NOW() - interval '4 days'),
(5,1,10,10004,'VITAPAY','PAID', 'PE15000254222', 'VME15000254222',NOW() - interval '5 days'),
(6,1,10,10005,'VITAPAY','PAID', 'PE15000255223', 'VME15000255223',NOW() - interval '6 days'),
(7,1,10,10006,'VITAPAY','PAID', 'PE15000256224', 'VME15000256224',NOW() - interval '7 days'),
(8,1,10,10007,'VITAPAY','PAID', 'PE15000257225', 'VME15000257225',NOW() - interval '8 days'),
(9,1,10,10008,'VITAPAY','PAID', 'PE15000258226', 'VME15000258226',NOW() - interval '9 days'),
(10,1,10,10009,'VITAPAY','PAID','PE15000259227','VME15000259227',NOW() - interval '10 days'),
(11,1,10,10010,'VITAPAY','PAID','PE15000260228','VME15000260228',NOW() - interval '11 days'),
(12,1,10,10011,'VITAPAY','PAID','PE15000261229','VME15000261229',NOW() - interval '12 days'),
(13,1,10,10012,'VITAPAY','PAID','PE15000262230','VME15000262230',NOW() - interval '13 days'),
(14,1,10,10013,'VITAPAY','PAID','PE15000263231','VME15000263231',NOW() - interval '14 days'),
(15,1,10,10014,'VITAPAY','PAID','PE15000264232','VME15000264232',NOW() - interval '15 days'),
(16,1,10,10015,'VITAPAY','PAID','PE15000265233','VME15000265233',NOW() - interval '16 days'),
(17,1,10,10016,'VITAPAY','PAID','PE15000266234','VME15000266234',NOW() - interval '17 days'),
(18,1,10,10017,'VITAPAY','PAID','PE15000267235','VME15000267235',NOW() - interval '18 days'),
(19,1,10,10018,'VITAPAY','PAID','PE15000268236','VME15000268236',NOW() - interval '19 days'),
(20,1,10,10019,'VITAPAY','PAID','PE15000269237','VME15000269237',NOW() - interval '20 days'),
(21,1,10,10020,'VITAPAY','PAID','PE15000270238','VME15000270238',NOW() - interval '21 days'),
(22,1,10,10021,'VITAPAY','PAID','PE15000271239','VME15000271239',NOW() - interval '22 days'),
(23,1,10,10022,'VITAPAY','PAID','PE15000272240','VME15000272240',NOW() - interval '23 days'),
(24,1,10,10023,'VITAPAY','PAID','PE15000273241','VME15000273241',NOW() - interval '24 days'),
(25,1,10,10024,'VITAPAY','PAID','PE15000274242','VME15000274242',NOW() - interval '25 days'),
(26,1,10,10025,'VITAPAY','PAID','PE15000275243','VME15000275243',NOW() - interval '26 days'),
(27,1,10,10026,'VITAPAY','PAID','PE15000276244','VME15000276244',NOW() - interval '27 days'),
(28,1,10,10027,'VITAPAY','PAID','PE15000277245','VME15000277245',NOW() - interval '28 days'),
(29,1,10,10028,'VITAPAY','PAID','PE15000278246','VME15000278246',NOW() - interval '29 days'),
(30,1,10,10029,'VITAPAY','PAID','PE15000279247','VME15000279247',NOW() - interval '30 days'),
(31,1,10,10030,'VITAPAY','PAID','PE15000280248','VME15000280248',NOW() - interval '31 days'),
(32,2,10,10030,'VITAPAY','PAID','PE15000280248','VME15000280248',NOW() - interval '1 days'),
(33,2,10,10031,'VITAPAY','PAID','PE15000281249','VME15000281248',NOW() - interval '2 days'),
(34,2,10,10032,'VITAPAY','PAID','PE15000282250','VME15000282248',NOW() - interval '3 days'),
(35,2,10,10033,'VITAPAY','PAID','PE15000283251','VME15000283248',NOW() - interval '4 days'),
(36,2,10,10034,'VITAPAY','PAID','PE15000284252','VME15000284248',NOW() - interval '5 days'),
(37,2,10,10035,'VITAPAY','PAID','PE15000285253','VME15000285248',NOW() - interval '6 days'),
(38,2,10,10036,'VITAPAY','PAID','PE15000286254','VME15000286248',NOW() - interval '7 days'),
(39,2,10,10037,'VITAPAY','PAID','PE15000287255','VME15000287248',NOW() - interval '8 days'),
(40,2,10,10038,'VITAPAY','PAID','PE15000288256','VME15000288248',NOW() - interval '9 days'),
(41,2,10,10039,'VITAPAY','PAID','PE15000289257','VME15000289248',NOW() - interval '10 days'),
(42,2,10,10040,'VITAPAY','PAID','PE15000290258','VME15000290248',NOW() - interval '11 days'),
(43,2,10,10041,'VITAPAY','PAID','PE15000291259','VME15000291248',NOW() - interval '12 days'),
(44,2,10,10042,'VITAPAY','PAID','PE15000292260','VME15000292248',NOW() - interval '13 days'),
(45,2,10,10043,'VITAPAY','PAID','PE15000293261','VME15000293248',NOW() - interval '14 days'),
(46,2,10,10044,'VITAPAY','PAID','PE15000294262','VME15000294248',NOW() - interval '15 days'),
(47,2,10,10045,'VITAPAY','PAID','PE15000295263','VME15000295248',NOW() - interval '16 days'),
(48,2,10,10046,'VITAPAY','PAID','PE15000296264','VME15000296248',NOW() - interval '17 days'),
(49,2,10,10047,'VITAPAY','PAID','PE15000297265','VME15000297248',NOW() - interval '18 days'),
(50,2,10,10048,'VITAPAY','PAID','PE15000298266','VME15000298248',NOW() - interval '19 days'),
(51,2,10,10049,'VITAPAY','PAID','PE15000299267','VME15000299248',NOW() - interval '20 days'),
(52,2,10,10050,'VITAPAY','PAID','PE15000300268','VME15000300248',NOW() - interval '21 days'),
(53,2,10,10051,'VITAPAY','PAID','PE15000301269','VME15000301248',NOW() - interval '22 days'),
(54,2,10,10052,'VITAPAY','PAID','PE15000302270','VME15000302248',NOW() - interval '23 days'),
(55,2,10,10053,'VITAPAY','PAID','PE15000303271','VME15000303248',NOW() - interval '24 days'),
(56,2,10,10054,'VITAPAY','PAID','PE15000304272','VME15000304248',NOW() - interval '25 days'),
(57,2,10,10055,'VITAPAY','PAID','PE15000305273','VME15000305248',NOW() - interval '26 days'),
(58,2,10,10056,'VITAPAY','PAID','PE15000306274','VME15000306248',NOW() - interval '27 days'),
(59,2,10,10057,'VITAPAY','PAID','PE15000307275','VME15000307248',NOW() - interval '28 days'),
(60,2,10,10058,'VITAPAY','PAID','PE15000308276','VME15000308248',NOW() - interval '29 days'),
(61,2,10,10059,'VITAPAY','PAID','PE15000309277','VME15000309248',NOW() - interval '30 days'),
(62,2,10,10060,'VITAPAY','PAID','PE15000310278','VME15000310248',NOW() - interval '31 days'),
(63,2,10,10061,'VITAPAY','PAID','PE15000311279','VME15000311248',NOW() - interval '32 days'),
(64,2,10,10062,'VITAPAY','PAID','PE15000312280','VME15000312248',NOW() - interval '33 days'),
(65,2,10,10063,'VITAPAY','PAID','PE15000313281','VME15000313248',NOW() - interval '34 days'),
(66,2,10,10064,'VITAPAY','PAID','PE15000314282','VME15000314248',NOW() - interval '35 days'),
(67,2,10,10065,'VITAPAY','PAID','PE15000315283','VME15000315248',NOW() - interval '36 days'),
(68,3,10,10030,'VITAPAY','PAID','PE15000280248','VME15000280248',NOW() - interval '1 days'),
(69,3,10,10031,'VITAPAY','PAID','PE15000281249','VME15000281248',NOW() - interval '2 days'),
(70,3,10,10032,'VITAPAY','PAID','PE15000282250','VME15000282248',NOW() - interval '3 days'),
(71,3,10,10033,'VITAPAY','PAID','PE15000283251','VME15000283248',NOW() - interval '4 days'),
(72,3,10,10034,'VITAPAY','PAID','PE15000284252','VME15000284248',NOW() - interval '5 days'),
(73,3,10,10035,'VITAPAY','PAID','PE15000285253','VME15000285248',NOW() - interval '6 days'),
(74,3,10,10036,'VITAPAY','PAID','PE15000286254','VME15000286248',NOW() - interval '7 days'),
(75,3,10,10037,'VITAPAY','PAID','PE15000287255','VME15000287248',NOW() - interval '8 days'),
(76,3,10,10038,'VITAPAY','PAID','PE15000288256','VME15000288248',NOW() - interval '9 days'),
(77,3,10,10039,'VITAPAY','PAID','PE15000289257','VME15000289248',NOW() - interval '10 days'),
(78,3,10,10040,'VITAPAY','PAID','PE15000290258','VME15000290248',NOW() - interval '11 days'),
(79,3,10,10041,'VITAPAY','PAID','PE15000291259','VME15000291248',NOW() - interval '12 days'),
(80,3,10,10042,'VITAPAY','PAID','PE15000292260','VME15000292248',NOW() - interval '13 days'),
(81,3,10,10043,'VITAPAY','PAID','PE15000293261','VME15000293248',NOW() - interval '14 days'),
(82,3,10,10044,'VITAPAY','PAID','PE15000294262','VME15000294248',NOW() - interval '15 days'),
(83,3,10,10045,'VITAPAY','PAID','PE15000295263','VME15000295248',NOW() - interval '16 days'),
(84,3,10,10046,'VITAPAY','PAID','PE15000296264','VME15000296248',NOW() - interval '17 days'),
(85,3,10,10047,'VITAPAY','PAID','PE15000297265','VME15000297248',NOW() - interval '18 days'),
(86,3,10,10048,'VITAPAY','PAID','PE15000298266','VME15000298248',NOW() - interval '19 days'),
(87,3,10,10049,'VITAPAY','PAID','PE15000299267','VME15000299248',NOW() - interval '20 days'),
(88,3,10,10050,'VITAPAY','PAID','PE15000300268','VME15000300248',NOW() - interval '21 days'),
(89,3,10,10051,'VITAPAY','PAID','PE15000301269','VME15000301248',NOW() - interval '22 days'),
(90,3,10,10052,'VITAPAY','PAID','PE15000302270','VME15000302248',NOW() - interval '23 days'),
(91,3,10,10053,'VITAPAY','PAID','PE15000303271','VME15000303248',NOW() - interval '24 days'),
(92,3,10,10054,'VITAPAY','PAID','PE15000304272','VME15000304248',NOW() - interval '25 days'),
(93,3,10,10055,'VITAPAY','PAID','PE15000305273','VME15000305248',NOW() - interval '26 days'),
(94,3,10,10056,'VITAPAY','PAID','PE15000306274','VME15000306248',NOW() - interval '27 days'),
(95,3,10,10057,'VITAPAY','PAID','PE15000307275','VME15000307248',NOW() - interval '28 days'),
(96,3,10,10058,'VITAPAY','PAID','PE15000308276','VME15000308248',NOW() - interval '29 days'),
(97,3,10,10059,'VITAPAY','PAID','PE15000309277','VME15000309248',NOW() - interval '30 days'),
(98,3,10,10060,'VITAPAY','PAID','PE15000310278','VME15000310248',NOW() - interval '31 days'),
(99,3,10,10061,'VITAPAY','PAID','PE15000311279','VME15000311248',NOW() - interval '32 days'),
(100,3,10,10062,'VITAPAY','PAID','PE15000312280','VME15000312248',NOW() - interval '33 days'),
(101,3,10,10063,'VITAPAY','PAID','PE15000313281','VME15000313248',NOW() - interval '34 days'),
(102,3,10,10064,'VITAPAY','PAID','PE15000314282','VME15000314248',NOW() - interval '35 days'),
(103,3,10,10065,'VITAPAY','PAID','PE15000315283','VME15000315248',NOW() - interval '36 days'),
(104,4,10,50030,'VITAPAY','PAID','PE15000480248','VME15000480248',NOW() - interval '1 days'),
(105,4,10,50031,'VITAPAY','PAID','PE15000481249','VME15000481248',NOW() - interval '2 days'),
(106,4,10,50032,'VITAPAY','PAID','PE15000482250','VME15000482248',NOW() - interval '3 days'),
(107,4,10,50033,'VITAPAY','PAID','PE15000483251','VME15000483248',NOW() - interval '4 days'),
(108,4,10,50034,'VITAPAY','PAID','PE15000484252','VME15000484248',NOW() - interval '5 days'),
(109,4,10,50035,'VITAPAY','PAID','PE15000485253','VME15000485248',NOW() - interval '6 days'),
(110,4,10,50036,'VITAPAY','PAID','PE15000486254','VME15000486248',NOW() - interval '7 days'),
(111,4,10,50037,'VITAPAY','PAID','PE15000487255','VME15000487248',NOW() - interval '8 days'),
(112,4,10,50038,'VITAPAY','PAID','PE15000488256','VME15000488248',NOW() - interval '9 days'),
(113,4,10,50039,'VITAPAY','PAID','PE15000489257','VME15000489248',NOW() - interval '10 days'),
(114,4,10,50040,'VITAPAY','PAID','PE15000490258','VME15000490248',NOW() - interval '11 days'),
(115,4,10,50041,'VITAPAY','PAID','PE15000491259','VME15000491248',NOW() - interval '12 days'),
(116,4,10,50042,'VITAPAY','PAID','PE15000492260','VME15000492248',NOW() - interval '13 days'),
(117,4,10,50043,'VITAPAY','PAID','PE15000493261','VME15000493248',NOW() - interval '14 days'),
(118,4,10,50044,'VITAPAY','PAID','PE15000494262','VME15000494248',NOW() - interval '15 days'),
(119,4,10,50045,'VITAPAY','PAID','PE15000495263','VME15000495248',NOW() - interval '16 days'),
(120,4,10,50046,'VITAPAY','PAID','PE15000496264','VME15000496248',NOW() - interval '17 days'),
(121,4,10,50047,'VITAPAY','PAID','PE15000497265','VME15000497248',NOW() - interval '18 days'),
(122,4,10,50048,'VITAPAY','PAID','PE15000498266','VME15000498248',NOW() - interval '19 days'),
(123,4,10,17049,'VITAPAY','PAID','PE15000499267','VME15000499248',NOW() - interval '20 days'),
(124,4,10,17050,'VITAPAY','PAID','PE15000400268','VME15000400248',NOW() - interval '21 days'),
(125,4,10,17051,'VITAPAY','PAID','PE15000401269','VME15000401248',NOW() - interval '22 days'),
(126,4,10,17052,'VITAPAY','PAID','PE15000402270','VME15000402248',NOW() - interval '23 days'),
(127,4,10,17053,'VITAPAY','PAID','PE15000403271','VME15000403248',NOW() - interval '24 days'),
(128,4,10,17054,'VITAPAY','PAID','PE15000404272','VME15000404248',NOW() - interval '25 days'),
(129,4,10,17055,'VITAPAY','PAID','PE15000405273','VME15000405248',NOW() - interval '26 days'),
(130,4,10,17056,'VITAPAY','PAID','PE15000406274','VME15000406248',NOW() - interval '27 days'),
(131,4,10,17057,'VITAPAY','PAID','PE15000407275','VME15000407248',NOW() - interval '28 days'),
(132,4,10,17058,'VITAPAY','PAID','PE15000408276','VME15000408248',NOW() - interval '29 days'),
(133,4,10,17059,'VITAPAY','PAID','PE15000409277','VME15000409248',NOW() - interval '30 days'),
(134,4,10,17060,'VITAPAY','PAID','PE15000410278','VME15000410248',NOW() - interval '31 days'),
(135,4,10,17061,'VITAPAY','PAID','PE15000411279','VME15000411248',NOW() - interval '32 days'),
(136,4,10,17062,'VITAPAY','PAID','PE15000412280','VME15000412248',NOW() - interval '33 days'),
(137,4,10,17063,'VITAPAY','PAID','PE15000413281','VME15000413248',NOW() - interval '34 days'),
(138,4,10,17064,'VITAPAY','PAID','PE15000414282','VME15000414248',NOW() - interval '35 days'),
(139,4,10,17065,'VITAPAY','PAID','PE15000415283','VME15000415248',NOW() - interval '36 days');