-- You can use this file to load seed data into the database using SQL statements
insert into ACTIVITY (id, name) values (101, 'vélo');
insert into ACTIVITY (id, name) values (102, 'course');
insert into ACTIVITY (id, name) values (103, 'moto');
insert into ACTIVITY (id, name) values (104, 'marche');

insert into MEMBER (id, name, email, phone_number, status) values (1001, 'John Smith', 'john.smith@mailinator.com', '2125551212', 'ACTIVE');
insert into MEMBER (id, name, email, phone_number, status) values (1002, 'Jimi Hendrix', 'jimi@free.fr', '3216549870', 'ACTIVE');

insert into MEMBER_ACTIVITY (member_id, activities_id) values (1001, 101);
insert into MEMBER_ACTIVITY (member_id, activities_id) values (1001, 103);
insert into MEMBER_ACTIVITY (member_id, activities_id) values (1001, 104);
insert into MEMBER_ACTIVITY (member_id, activities_id) values (1002, 101);
insert into MEMBER_ACTIVITY (member_id, activities_id) values (1002, 102);
