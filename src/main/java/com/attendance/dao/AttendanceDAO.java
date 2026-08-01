package com.attendance.dao;

import com.attendance.database.DBConnection;
import com.attendance.model.Attendance;
import com.attendance.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    //------------------------------------------
    // Get All Attendance
    //------------------------------------------

    public List<Attendance> getAllAttendance() {

        List<Attendance> attendanceList = new ArrayList<>();

        String sql = """
                SELECT
                    a.attendance_id,
                    s.roll_number,
                    s.name,
                    s.department,
                    s.year,
                    a.attendance_date,
                    a.attendance_time,
                    a.status
                FROM attendance a
                JOIN students s
                ON a.roll_number = s.roll_number
                ORDER BY a.attendance_date DESC,
                         a.attendance_time DESC
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Attendance attendance = new Attendance();

                attendance.setAttendanceId(
                        rs.getInt("attendance_id"));

                attendance.setRollNumber(
                        rs.getString("roll_number"));

                attendance.setStudentName(
                        rs.getString("name"));

                attendance.setDepartment(
                        rs.getString("department"));

                attendance.setYear(
                        rs.getInt("year"));

                attendance.setAttendanceDate(
                        rs.getDate("attendance_date"));

                attendance.setAttendanceTime(
                        rs.getTime("attendance_time"));

                attendance.setStatus(
                        rs.getString("status"));

                attendanceList.add(attendance);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return attendanceList;

    }

    //------------------------------------------
    // Search Attendance
    //------------------------------------------

    public List<Attendance> searchAttendance(String keyword) {

        List<Attendance> attendanceList = new ArrayList<>();

        String sql = """
                SELECT
                    a.attendance_id,
                    s.roll_number,
                    s.name,
                    s.department,
                    s.year,
                    a.attendance_date,
                    a.attendance_time,
                    a.status
                FROM attendance a
                JOIN students s
                ON a.roll_number = s.roll_number
                WHERE
                    s.roll_number LIKE ?
                    OR s.name LIKE ?
                ORDER BY a.attendance_date DESC,
                         a.attendance_time DESC
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Attendance attendance = new Attendance();

                attendance.setAttendanceId(rs.getInt("attendance_id"));
                attendance.setRollNumber(rs.getString("roll_number"));
                attendance.setStudentName(rs.getString("name"));
                attendance.setDepartment(rs.getString("department"));
                attendance.setYear(rs.getInt("year"));
                attendance.setAttendanceDate(rs.getDate("attendance_date"));
                attendance.setAttendanceTime(rs.getTime("attendance_time"));
                attendance.setStatus(rs.getString("status"));

                attendanceList.add(attendance);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return attendanceList;

    }

    //------------------------------------------
    // Today's Attendance
    //------------------------------------------

    public List<Attendance> getTodayAttendance() {

        List<Attendance> attendanceList = new ArrayList<>();

        String sql = """
                SELECT
                    a.attendance_id,
                    s.roll_number,
                    s.name,
                    s.department,
                    s.year,
                    a.attendance_date,
                    a.attendance_time,
                    a.status
                FROM attendance a
                JOIN students s
                ON a.roll_number = s.roll_number
                WHERE a.attendance_date = CURDATE()
                ORDER BY a.attendance_time DESC
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Attendance attendance = new Attendance();

                attendance.setAttendanceId(rs.getInt("attendance_id"));
                attendance.setRollNumber(rs.getString("roll_number"));
                attendance.setStudentName(rs.getString("name"));
                attendance.setDepartment(rs.getString("department"));
                attendance.setYear(rs.getInt("year"));
                attendance.setAttendanceDate(rs.getDate("attendance_date"));
                attendance.setAttendanceTime(rs.getTime("attendance_time"));
                attendance.setStatus(rs.getString("status"));

                attendanceList.add(attendance);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return attendanceList;

    }
    private boolean studentExists(String rollNumber) {

        String sql = "SELECT roll_number FROM students WHERE roll_number = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, rollNumber);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }
    private boolean alreadyMarkedToday(String rollNumber) {

        String sql = """
            SELECT attendance_id
            FROM attendance
            WHERE roll_number = ?
            AND attendance_date = CURDATE()
            """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, rollNumber);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }
    private boolean markAttendance(String rollNumber) {

        String sql = """
            INSERT INTO attendance
            (roll_number, attendance_date, attendance_time, status)
            VALUES (?, CURDATE(), CURTIME(), 'Present')
            """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, rollNumber);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }
    public String markAttendanceWithStatus(String rollNumber) {

        if (!studentExists(rollNumber)) {
            return "INVALID_QR";
        }

        if (alreadyMarkedToday(rollNumber)) {
            return "ALREADY_MARKED";
        }

        if (markAttendance(rollNumber)) {
            return "SUCCESS";
        }

        return "FAILED";

    }
    //------------------------------------------
// Get Attendance By Date
//------------------------------------------

    public List<Attendance> getAttendanceByDate(Date selectedDate) {

        List<Attendance> attendanceList = new ArrayList<>();

        String sql = """
            SELECT
                a.attendance_id,
                s.roll_number,
                s.name,
                s.department,
                s.year,
                a.attendance_date,
                a.attendance_time,
                a.status
            FROM attendance a
            JOIN students s
            ON a.roll_number = s.roll_number
            WHERE a.attendance_date = ?
            ORDER BY a.attendance_time ASC
            """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setDate(1, selectedDate);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Attendance attendance = new Attendance();

                attendance.setAttendanceId(rs.getInt("attendance_id"));
                attendance.setRollNumber(rs.getString("roll_number"));
                attendance.setStudentName(rs.getString("name"));
                attendance.setDepartment(rs.getString("department"));
                attendance.setYear(rs.getInt("year"));
                attendance.setAttendanceDate(rs.getDate("attendance_date"));
                attendance.setAttendanceTime(rs.getTime("attendance_time"));
                attendance.setStatus(rs.getString("status"));

                attendanceList.add(attendance);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return attendanceList;

    }

    //------------------------------------------
// Get Attendance By Month
//------------------------------------------

    public List<Attendance> getAttendanceByMonth(int month, int year) {

        List<Attendance> attendanceList = new ArrayList<>();

        String sql = """
            SELECT
                a.attendance_id,
                s.roll_number,
                s.name,
                s.department,
                s.year,
                a.attendance_date,
                a.attendance_time,
                a.status
            FROM attendance a
            JOIN students s
            ON a.roll_number = s.roll_number
            WHERE MONTH(a.attendance_date)=?
            AND YEAR(a.attendance_date)=?
            ORDER BY a.attendance_date ASC,
                     a.attendance_time ASC
            """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, month);
            ps.setInt(2, year);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Attendance attendance = new Attendance();

                attendance.setAttendanceId(rs.getInt("attendance_id"));
                attendance.setRollNumber(rs.getString("roll_number"));
                attendance.setStudentName(rs.getString("name"));
                attendance.setDepartment(rs.getString("department"));
                attendance.setYear(rs.getInt("year"));
                attendance.setAttendanceDate(rs.getDate("attendance_date"));
                attendance.setAttendanceTime(rs.getTime("attendance_time"));
                attendance.setStatus(rs.getString("status"));

                attendanceList.add(attendance);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }


        return attendanceList;

    }
    public List<Student> getAbsentStudentsToday() {

        List<Student> students = new ArrayList<>();

        String sql = """
            SELECT *
            FROM students
            WHERE roll_number NOT IN (
                SELECT roll_number
                FROM attendance
                WHERE attendance_date = CURDATE()
            )
            ORDER BY roll_number
            """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Student student = new Student();

                student.setStudentId(rs.getInt("student_id"));
                student.setRollNumber(rs.getString("roll_number"));
                student.setName(rs.getString("name"));
                student.setDepartment(rs.getString("department"));
                student.setYear(rs.getInt("year"));
                student.setEmail(rs.getString("email"));
                student.setQrCodePath(rs.getString("qr_code_path"));

                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

}