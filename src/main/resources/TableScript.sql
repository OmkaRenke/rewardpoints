drop schema if exists reward_points_db;
create schema reward_points_db;
use reward_points_db;


CREATE TABLE retailer (
    retailer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    contact VARCHAR(50)
);

CREATE TABLE customer (
    customer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    contact VARCHAR(50),
    retailer_id BIGINT NOT NULL REFERENCES retailer(retailer_id)
    
);

CREATE TABLE transaction (
    transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_number VARCHAR(100) UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    	points_earned INT NOT NULL DEFAULT 0,
    retailer_id BIGINT NOT NULL REFERENCES retailer(retailer_id),
    customer_id BIGINT NOT NULL REFERENCES customer(customer_id)
);


INSERT INTO retailer VALUES(100,"Jack","jack@gmail.com","9876564326");
INSERT INTO customer VALUES(200,"Kevin","Kevin@gmail.com","9623456785",100);

SELECT * FROM retailer;
SELECT * FROM customer;
SELECT * FROM transaction;
