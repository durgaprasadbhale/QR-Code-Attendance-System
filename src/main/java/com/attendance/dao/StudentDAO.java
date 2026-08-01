package com.attendance.dao;

import com.attendance.database.DBConnection;
import com.attendance.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // ===========================
    // Add Student
    // ===========================
    public boolean addStudent(Student student) {

        String sql = "INSERT INTO students (roll_number,name,department,year,email,qr_code_path) VALUES (?,?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getRollNumber());
            ps.setString(2, student.getName());
            ps.setString(3, student.getDepartment());
            ps.setInt(4, student.getYear());
            ps.setString(5, student.getEmail());
            ps.setString(6, student.getQrCodePath());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===========================
    // Update Student
    // ===========================
    public boolean updateStudent(Student student) {

        String sql = "UPDATE students SET name=?, department=?, year=?, email=? WHERE roll_number=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getDepartment());
            ps.setInt(3, student.getYear());
            ps.setString(4, student.getEmail());
            ps.setString(5, student.getRollNumber());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===========================
    // Delete Student
    // ===========================
    public boolean deleteStudent(String rollNumber) {

        String getQr = "SELECT qr_code_path FROM students WHERE roll_number=?";
        String deleteAttendance = "DELETE FROM attendance WHERE roll_number=?";
        String deleteStudent = "DELETE FROM students WHERE roll_number=?";

        try (Connection conn = DBConnection.getConnection()) {

            String qrPath = "";

            PreparedStatement ps1 = conn.prepareStatement(getQr);
            ps1.setString(1, rollNumber);

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                qrPath = rs.getString("qr_code_path");
            }

            if (qrPath != null && !qrPath.isEmpty()) {

                java.io.File file = new java.io.File(qrPath);

                if (file.exists()) {
                    file.delete();
                }

            }

            PreparedStatement ps2 = conn.prepareStatement(deleteAttendance);
            ps2.setString(1, rollNumber);
            ps2.executeUpdate();

            PreparedStatement ps3 = conn.prepareStatement(deleteStudent);
            ps3.setString(1, rollNumber);

            return ps3.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===========================
    // Get All Students
    // ===========================
    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students ORDER BY roll_number";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

    // ===========================
    // Search Student
    // ===========================
    public List<Student> searchStudents(String keyword) {

        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT * FROM students
                WHERE roll_number LIKE ?
                OR name LIKE ?
                OR department LIKE ?
                ORDER BY roll_number
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            ResultSet rs = ps.executeQuery();

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

    // ===========================
    // Get Student By Roll Number
    // ===========================
    public Student getStudentByRollNumber(String rollNumber) {

        String sql = "SELECT * FROM students WHERE roll_number=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rollNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Student student = new Student();

                student.setStudentId(rs.getInt("student_id"));
                student.setRollNumber(rs.getString("roll_number"));
                student.setName(rs.getString("name"));
                student.setDepartment(rs.getString("department"));
                student.setYear(rs.getInt("year"));
                student.setEmail(rs.getString("email"));
                student.setQrCodePath(rs.getString("qr_code_path"));

                return student;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}