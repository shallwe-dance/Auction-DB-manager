delete from billing;
delete from bid;
delete from item;

delete from fix;

delete from disorder;

delete from member;
delete from worker;
delete from equipment;

insert into worker values ('admin','1234','admin','Capt',TRUE);
insert into worker values ('test','1234','test','Amn',FALSE);
insert into worker values ('seller1','1234','seller1','Amn',FALSE);
insert into worker values ('seller2','1234','seller2','Amn',FALSE);
insert into worker values ('buyer1','1234','buyer1','Amn',FALSE);
insert into worker values ('buyer2','1234','buyer2','Amn',FALSE);
insert into worker values ('bidder1','1234','bidder1','Amn',FALSE);
insert into worker values ('bidder2','1234','bidder2','Amn',FALSE);
insert into worker values ('worker1','1234','worker1','SrA',FALSE);
insert into worker values ('worker2','1234','worker2','A1C',FALSE);

insert into item (category, description, condition, buy_it_now, status, seller) values ('Books','Database System Concepts', 'Acceptable', 40000, 'Open', 'seller1');
insert into item (category, description, condition, buy_it_now, status, seller) values ('Books','Operating System Concepts', 'Like-new', 40000, 'Open', 'seller2');
insert into item (category, description, condition, buy_it_now, posted, seller) values ('Electronics','iPad Air 5', 'Good', 900000, current_timestamp-interval '6 month', 'seller2');
insert into item (category, description, condition, buy_it_now, status, seller) values ('Home', 'Dining Table', 'Like-new', 150000, 'Open', 'seller2');
insert into item (category, description, condition, buy_it_now, status, seller) values ('Books', 'Artificial Intelligence', 'Good', 25000, 'Open', 'seller1');
insert into item (category, description, condition, buy_it_now, status, seller) values ('Sporting Goods', 'Tennis Racket', 'New', 15000, 'Open', 'seller2');

insert into bid (price, bidder, item_id) values (25000,'bidder1',1);
insert into bid (price, bidder, item_id) values (30000,'bidder1',1);
insert into bid (price, bidder, item_id) values (25000,'bidder1',2);
insert into bid (price, bidder, item_id) values (30000,'bidder2',2);


insert into equipment values ('c1', 'computer', 'ny2020');
insert into equipment values ('c2', 'computer', 'ny2020');
insert into equipment values ('c3', 'computer', 'h2023');
insert into equipment values ('c4', 'computer', 'h2023');
insert into equipment values ('m1', 'monitor', 'lg2021');
insert into equipment values ('m2', 'monitor', 'lg2022');
insert into equipment values ('p1','printer','l655');
insert into equipment values ('p2','printer','l655');
insert into equipment values ('p3','printer','c5290');

insert into member(name, rank, phone, department) values ('Jeon', '2nd Lt', '5253', 'Logistics');
insert into member(name, rank, phone, department) values ('Amy', '1st Sgt', '3636','Information');
insert into member(name, rank, phone, department) values ('John', 'Capt', '5252', 'Logistics');
insert into member(name, rank, phone, department) values ('Sarah', 'Lt', '0110', 'HR');
insert into member(name, rank, phone, department) values ('Tom', 'Sgt', '2234', 'Finance');
insert into member(name, rank, phone, department) values ('Alice', '1st Lt', '3725', 'Information');
insert into member(name, rank, phone, department) values ('Mark', '2nd Lt', '3122', 'Information');


-- disorder 테이블에 데이터 삽입 (Asia/Seoul 시간대 적용)
insert into disorder (datetime, problem, sn, requester_id) values ('2024-11-10 10:00:00+09', 'Computer not starting', 'c1',1);
insert into disorder (datetime, problem, sn, requester_id) values ('2024-11-09 10:30:00+09', 'Frequent bluescreen', 'c2',2);
insert into disorder (datetime, problem, sn, requester_id) values ('2024-11-08 09:30:00+09', 'Format', 'c3',3);
insert into disorder (datetime, problem, sn, requester_id) values ('2024-11-08 09:20:00+09', 'Windows update', 'c4',5);
insert into disorder (datetime, problem, sn, requester_id) values ('2024-11-12 13:00:00+09', 'Overheating issue', 'c4', 4);
insert into disorder (datetime, problem, sn, requester_id) values ('2024-11-12 16:00:00+09', 'No power', 'c4', 4);
insert into disorder (datetime, problem, sn, requester_id) values ('2024-11-11 15:30:00+09', 'Screen flickering', 'm1',1);
insert into disorder (datetime, problem, sn, requester_id) values ('2024-11-11 14:00:00+09', 'Screen not displaying', 'm1', 1);
insert into disorder (datetime, problem, sn, requester_id) values ('2024-11-12 09:00:00+09', 'Paper jam', 'p2',6);
insert into disorder (datetime, problem, sn, requester_id) values ('2024-11-13 10:30:00+09', 'Printer not connecting', 'p2', 6);

-- fix 테이블에 데이터 삽입 (Asia/Seoul 시간대 적용)
insert into fix values ('worker1', 1, '2024-11-10 10:30:00+09', '2024-11-10 12:00:00+09');
insert into fix values ('worker2', 5, '2024-11-11 16:00:00+09', '2024-11-11 17:30:00+09');
insert into fix (worker_id, disorder_id, start) values ('worker1',4, '2024-11-13 14:21:00+09');




