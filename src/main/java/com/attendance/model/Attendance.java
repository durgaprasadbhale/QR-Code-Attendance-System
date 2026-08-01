package com.attendance.model;

import java.sql.Date;
import java.sql.Time;

public class Attendance {

    private int attendanceId;
    private String rollNumber;
    private String studentName;
    private String department;
    private int year;
    private Date attendanceDate;
    private Time attendanceTime;
    private String status;

    public Attendance() {
    }

    public Attendance(int attendanceId,
                      String rollNumber,
                      String studentName,
                      String department,
                      int year,
                      Date attendanceDate,
                      Time attendanceTime,
                      String status) {

        this.attendanceId = attendanceId;
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.department = department;
        this.year = year;
        this.attendanceDate = attendanceDate;
        this.attendanceTime = attendanceTime;
        this.status = status;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Time getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(Time attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}