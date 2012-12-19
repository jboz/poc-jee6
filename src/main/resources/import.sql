-- a mother and his child
insert into Partner(id, name, birthdate) values(22, 'Maman Pierre', '1970-06-18');
insert into Partner(id, name, birthdate) values(33, 'Pierre', '2000-01-01');

-- older contract, accepted, create at birth of his child
insert into Ensured(id, partner_id, acceptation) values(444, 22, 'ACCEPTED');
insert into ContractCase(id, effectDate, ensureda_id, state) values(1000, '2000-01-01', 444, 'POLICY');

-- new contract, to cover her with his child
insert into Ensured(id, partner_id, acceptation) values(555, 22, 'IN_PROGRESS');
insert into Ensured(id, partner_id, acceptation) values(666, 33, 'IN_PROGRESS');
insert into ContractCase(id, effectDate, ensureda_id, ensuredb_id, state) values(1001, '2013-01-01', 555, 666, 'PROPAL');
