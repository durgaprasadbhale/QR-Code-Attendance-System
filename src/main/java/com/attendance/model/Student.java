package com.attendance.model;

public class Student {

    private int studentId;
    private String rollNumber;
    private String name;
    private String department;
    private int year;
    private String email;
    private String qrCodePath;

    public Student() {
    }

    public Student(int studentId, String rollNumber, String name,
                   String department, int year,
                   String email, String qrCodePath) {

        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.name = name;
        this.department = department;
        this.year = year;
        this.email = email;
        this.qrCodePath = qrCodePath;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQrCodePath() {
        return qrCodePath;
    }

    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }
}