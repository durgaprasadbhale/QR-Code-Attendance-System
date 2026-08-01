package com.attendance.ui;
import com.attendance.ui.EmailUI;

import com.attendance.qr.QRScanner;

import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JFrame {

    public DashboardUI() {

        setTitle("QR Code Attendance Management System");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JLabel title = new JLabel(
                "QR Code Attendance Management System",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainPanel.add(title, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JButton studentBtn = new JButton("Student Management");
        JButton scanBtn = new JButton("Scan QR");
        JButton attendanceBtn = new JButton("Attendance");
        JButton reportBtn = new JButton("Reports");
        JButton emailBtn = new JButton("Email");
        JButton logoutBtn = new JButton("Logout");

        Font buttonFont = new Font("Arial", Font.BOLD, 18);

        JButton[] buttons = {
                studentBtn,
                scanBtn,
                attendanceBtn,
                reportBtn,
                emailBtn,
                logoutBtn
        };

        for (JButton button : buttons) {
            button.setFont(buttonFont);
            centerPanel.add(button);
        }

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        // ==========================
        // Button Actions
        // ==========================

        studentBtn.addActionListener(e -> {
            StudentManagementUI ui = new StudentManagementUI();
            ui.setVisible(true);
        });

        scanBtn.addActionListener(e -> {
            QRScanner scanner = new QRScanner();
            scanner.setVisible(true);
        });

        attendanceBtn.addActionListener(e -> {
            AttendanceUI ui = new AttendanceUI();
            ui.setVisible(true);
        });

        reportBtn.addActionListener(e -> {
            ReportUI ui = new ReportUI();
            ui.setVisible(true);
        });

        emailBtn.addActionListener(e -> {
            EmailUI ui = new EmailUI();
            ui.setVisible(true);
        });

        logoutBtn.addActionListener(e -> {

            dispose();

            new LoginUI();

        });

        setVisible(true);

    }

}