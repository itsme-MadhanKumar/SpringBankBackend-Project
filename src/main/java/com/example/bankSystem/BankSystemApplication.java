package com.example.bankSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BankSystemApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(BankSystemApplication.class, args);
	}
}

/*
create database BANK;
USE BANK;
CREATE TABLE users (
    fullname VARCHAR(255),
    mobile VARCHAR(20),
    aadhar VARCHAR(12) PRIMARY KEY,
    pan_id VARCHAR(10),
    address VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    status VARCHAR(50),   -- Can be "active", "inactive", etc.
    balance VARCHAR(20),  -- You may want to store balance as DECIMAL, not VARCHAR
    role VARCHAR(50),      -- Could be "user", "admin", etc.
	profile_pic LONGBLOB,      -- Added column to store profile pictures as binary
    payment_password VARCHAR(225)
);
CREATE TABLE action_taken (
    aadhar VARCHAR(12),
    action VARCHAR(100),
    timestamp VARCHAR(100),
    date VARCHAR(100),
    bolo_image LONGBLOB
);
CREATE TABLE transaction_history (
    UTR_id VARCHAR(50) PRIMARY KEY,        -- Unique Transaction Reference ID
    Sender_id VARCHAR(12),                 -- Aadhar of sender
    Receiver_id VARCHAR(12),               -- Aadhar of receiver
    Credited VARCHAR(20),                  -- Amount credited (if receiver)
    Debited VARCHAR(20),                   -- Amount debited (if sender)
    time_stamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    payment_status VARCHAR(50),            -- e.g. "success", "failed", "pending"
    bank_balance VARCHAR(20)               -- Remaining balance after transaction (for sender)
);
CREATE TABLE notifications (
    userId VARCHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'UNREAD', -- UNREAD, READ, ARCHIVED
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);
select * from notifications;
CREATE TABLE ug_loan_applications (
    application_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50),
    name VARCHAR(100),
    age VARCHAR(10),
    dob DATE,
    tenth_percentage VARCHAR(10),
    twelfth_percentage VARCHAR(10),
    university_name VARCHAR(100),
    course_name VARCHAR(100),
    course_duration VARCHAR(50),
    counseling_code VARCHAR(50),
    aadhaar_card VARCHAR(20),
    pan_card VARCHAR(20),
    father_income VARCHAR(20),
    collateral_documents_path TEXT,
    loan_amount VARCHAR(20),
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(30)
);
select * from ug_loan_applications;
drop table ug_loan_applications;
CREATE TABLE pg_loan_applications (
    application_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50),
    name VARCHAR(100),
    age VARCHAR(10),
    dob DATE,
    tenth_percentage VARCHAR(10),
    twelfth_percentage VARCHAR(10),
    final_cgpa VARCHAR(10),
    university_name VARCHAR(100),
    course_name VARCHAR(100),
    course_duration VARCHAR(50),
    counseling_code VARCHAR(50),
    aadhaar_card VARCHAR(20),
    pan_card VARCHAR(20),
    father_income VARCHAR(20),
    collateral_documents_path TEXT,
    loan_amount VARCHAR(20),
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      status VARCHAR(30)
);
select * from pg_loan_applications;
drop table pg_loan_applications;
CREATE TABLE abroad_loan_applications (
    application_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50),
    name VARCHAR(100),
    father_name VARCHAR(100),
    mother_name VARCHAR(100),
    community VARCHAR(50),
    age VARCHAR(10),
    dob DATE,
    tenth_percentage VARCHAR(10),
    twelfth_percentage VARCHAR(10),
    applying_for VARCHAR(10),
    university_name VARCHAR(100),
    course_name VARCHAR(100),
    course_duration VARCHAR(50),
    aadhaar_card VARCHAR(20),
    pan_card VARCHAR(20),
    father_income VARCHAR(20),
    collateral_documents_path TEXT,
    travel_expenses VARCHAR(50),
    loan_amount VARCHAR(20),
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      status VARCHAR(30)
);
select * from abroad_loan_applications;
drop table abroad_loan_applications;-*/