CREATE TABLE worker (
	id	varchar(20) primary key,
	password	varchar(12),
	name	varchar(20),
	rank	varchar(10),
	isAdmin boolean
);

CREATE TABLE member (
	id	serial		primary key,
	name	varchar(20),
	rank	varchar(20),
	phone	varchar(11),
  department varchar(20)
);

CREATE TABLE equipment (
	sn	varchar(20) primary key,
	type	varchar(10),
	model	varchar(20), --모델명
	check (type in ('computer','monitor','printer'))
);

CREATE TABLE disorder (
  id serial primary key,
  requester_id int,
  sn	varchar(20),
	datetime	timestamptz,
	problem	text,
	foreign key (sn) references equipment (sn),
  foreign key (requester_id) references member (id)
);

CREATE TABLE fix (
  worker_id varchar(20),
  disorder_id int,
	start timestamptz,
	finish timestamptz,
	primary key (worker_id, disorder_id),
	foreign key (worker_id) references worker (id),
	foreign key (disorder_id) references disorder (id) on delete cascade
);


CREATE TABLE item (
	id	serial		primary key,
	category	varchar(15),
	description	varchar(100),
	condition	varchar(20),
	buy_it_now	int,
	posted	timestamptz default current_timestamp,
	status	varchar(10) not null default 'Open',
	seller	varchar(20),
	foreign key (seller) references worker(id),
	check (condition in ('New','Like-new','Good','Acceptable')),
	check (category in ('Electronics','Books','Home','Sporting Goods','Souvenir','Others')),
 check (status in ('Open','Ended','Cancelled'))
);

CREATE TABLE bid (
	id	serial	primary key,
	posted	timestamptz not null default current_timestamp,
  close timestamptz not null default current_timestamp + interval '2 day',
	price	int,
	bidder	varchar(20),
	item_id	int,
	foreign key (bidder) references worker (id),
	foreign key (item_id) references item (id),
 --er diagram에는 close 정보 넣어야됨. derived로
	unique (price, bidder, item_id)
);

CREATE TABLE billing (
	-- 하나의 아이템에 여러 결제 가능. 취소했다가 다시...
	id serial primary key,
	purchase_date	timestamptz not null default current_timestamp,
	buyer_pay	int,
	--er다이어그램에 seller_paid 정보 넣어야 함.
	seller	varchar(20),
	buyer	varchar(20),
	status varchar(10) not null default 'Done',
	item_id int,
	foreign key (seller) references worker (id),
	foreign key (buyer) references worker (id),
	foreign key (item_id) references item (id),
 check (status in ('Done', 'Error', 'Cancelled'))
);
