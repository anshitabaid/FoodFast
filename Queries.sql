Table creation

create table users 
(phone_num varchar (10) primary key,
name varchar (20),
address varchar (30),
password varchar (10)
);

create table restaurant (
r_id number (10) primary key, 
r_name varchar  (20),
address varchar (30));

create table cuisine (c_name varchar (10) primary key);

create table restaurant_cuisine 
(
    rc_id number primary key,
    r_id number references restuarant,
    cuisine varchar (20)references cuisine,
)

create table orders (
o_id number primary key, 
price int, 
phone_num varchar (20) references users, 
r_id number references restaurant, 
order_time timestamp);

create table dish (d_id number primary key, 
r_id number references restaurant, 
d_name varchar (20), 
price number, 
isVeg number);

create table order_has_dishes (
ohd_id number primary key, 
o_id number references orders, 
d_id number references dish, 
quantity int);

Insertions
insert into restaurant values (1, 'Dollops', 'Tiger Circle');
insert into restaurant values (2, 'Hadiqa', 'Tiger Circle');


insert into cuisine values ('Indian');
insert into cuisine values ('Italian');

insert into restaurant_cuisine values(1, 1, 'Indian');
insert into restaurant_cuisine values(2, 2, 'Italian');

insert into dish values(1, 1, 'Chicken Tikka', 140, 0);
insert into dish values(2, 1, 'Paneer Tikka', 120, 1);

Queries

For restaurants
---------------

List all restaurants
select r_name, address from restaurant

Filter restaurants by name
Select r_name, address from restaurant where upper(r_name) like upper ('dollops');

Filter restaurants by cuisine
with rids (id) as (select r_id from restaurant_cuisine where cuisine = 'Indian') select r_name from restaurant, rids where rids.id = restaurant.r_id;

Filter restaurants by name and cuisine
with rids (id) as (select r_id from restaurant_cuisine where cuisine = 'Indian') select r_name from restaurant, rids where rids.id = restaurant.r_id and r_name = 'Dollops'

List orderid, restaurant and total for past orders by a user
select o_id, r_name, price from orders join restaurant on orders.r_id = restaurant.r_id where phone_num = '9748700604' order by order_time desc;

For dishes in a restaurant
--------------------------

List all dishes of a restaurant
select d_name, price from dish where r_id = 1;

Sort dish by price 
select d_name, price from dish where r_id =1 order by price;

Sort dish by popularity
with dishpop (did, c) as 
(select d_id, count(d_id) as count from order_has_dishes group by d_id) 
select d_name, price from dish, dishpop where r_id = 1
and dishpop.did = dish.d_id order by c desc;

Display only vegetarian dishes of a restaurant
select d_name, price from dish where r_id =1 and isVeg = 1;

Display vegetarian dishes sorted by price
select d_name, price from dish where r_id =1 and isVeg = 1 order by price;

Display vegetarian dishes sorted by popularity
with dishpop (did, c) as 
(select d_id, count(d_id) as count from order_has_dishes group by d_id) 
select d_name, price from dish, dishpop where r_id = 1
and dishpop.did = dish.d_id and isVeg = 1 order by c desc;


For order cart
--------------
Procedure to insert order details to order table

create or replace procedure genOrderID (
    price number,
    phone_num varchar,
    r_id number,
    order_time varchar, 
    newid out number
)
as
maxoid int:=1;
begin
    select max(o_id) into maxoid from orders;
    newid := maxoid + 1;
    insert into orders values (newid, price, phone_num, r_id, to_timestamp (order_time, 'yyyy/MM/dd HH24.mi.ss'));
end;
/


