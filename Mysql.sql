-- Create database
CREATE DATABASE IF NOT EXISTS banking_system;

-- Use database
USE banking_system;

-- Accounts Table
CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    account_holder VARCHAR(50) NOT NULL,
    balance DOUBLE DEFAULT 0,
    pin INT NOT NULL
);

-- Transactions Table

CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20),
    type VARCHAR(20),
    amount DOUBLE,
    details VARCHAR(100),
    date_time VARCHAR(50)
);