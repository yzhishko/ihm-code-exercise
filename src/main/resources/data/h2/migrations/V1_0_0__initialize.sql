CREATE TABLE advertiser(id LONG PRIMARY KEY auto_increment, name VARCHAR(255) UNIQUE, contactName VARCHAR(255), creditLimit DECIMAL(20,2));

insert into advertiser(name, contactName, creditLimit) values('IHeartMedia', 'Yury Zhyshko', 100050.99)