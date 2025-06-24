drop schema if exists reward_points_db;
create schema reward_points_db;
use reward_points_db;

CREATE TABLE customer (
    customer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    contact VARCHAR(50)
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

INSERT INTO customer VALUES(100,"Kevin","Kevin@gmail.com","9623456785");
INSERT INTO customer VALUES(101,"Ken","Ken@gmail.com","3454354678");

-- Sample transactions for customer_id = 100 (Kevin)
INSERT INTO transaction (transaction_number, transaction_mode, amount, transaction_date, points_earned, customer_id)
VALUES 
('TXN1001', 'CARD', 120.00, '2025-04-10 10:00:00', 90, 100),  
('TXN1002', 'UPI', 75.00, '2025-05-15 14:30:00', 25, 100),    
('TXN1003', 'CASH', 45.00, '2025-06-20 09:15:00', 0, 100);     

-- Sample transactions for customer_id = 101 (Ken)
INSERT INTO transaction (transaction_number, transaction_mode, amount, transaction_date, points_earned, customer_id)
VALUES 
('TXN5001', 'CREDIT_CARD', 110.00, '2025-04-05 12:00:00', 70, 101), 
('TXN5002', 'DEBIT_CARD', 95.00, '2025-05-18 18:45:00', 45, 101),    
('TXN5003', 'NET_BANKING', 60.00, '2025-06-25 11:00:00', 10, 101); 


SELECT * FROM customer;
SELECT * FROM transaction;