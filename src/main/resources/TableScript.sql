drop schema if exists reward_points_db;
create schema reward_points_db;
use reward_points_db;

CREATE TABLE customer (
    customer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    contact VARCHAR(50),
);

CREATE TABLE transaction (
    transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_number VARCHAR(100) UNIQUE,
    transaction_mode VARCHAR(100),
    amount DECIMAL(10,2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    points_earned INT NOT NULL DEFAULT 0,
    customer_id BIGINT NOT NULL REFERENCES customer(customer_id)
);

INSERT INTO customer VALUES(200,"Kevin","Kevin@gmail.com","9623456785");
INSERT INTO customer VALUES(500,"Ken","Ken@gmail.com","3454354678",200);

SELECT * FROM customer;
SELECT * FROM transaction;