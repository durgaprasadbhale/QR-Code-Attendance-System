CREATE DATABASE qr_attendance;

USE qr_attendance;

CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    year INT,
    email VARCHAR(100),
    qr_code_path VARCHAR(255)
);

CREATE TABLE attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(20),
    attendance_date DATE,
    attendance_time TIME,
    status VARCHAR(20),
    FOREIGN KEY (roll_number) REFERENCES students(roll_number)
);
ALTER TABLE students
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
