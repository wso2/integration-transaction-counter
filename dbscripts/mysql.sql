CREATE DATABASE IF NOT EXISTS TransactionRecordDB;

CREATE TABLE TransactionRecords (
    id INT AUTO_INCREMENT PRIMARY KEY,
    host VARCHAR(255),
    serverID VARCHAR(255),
    serverType VARCHAR(20),
    count INT,
    recordedTime TIMESTAMP
);
