package com.attendance.ui;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginUI() {

        setTitle("QR Code Attendance System");
        setSize(400,250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel title = new JLabel("Admin Login");
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        usernameField = new JTextField(18);
        passwordField = new JPasswordField(18);

        loginButton = new JButton("Login");

        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=2;
        add(title,gbc);

        gbc.gridwidth=1;
        gbc.gridx=0;
        gbc.gridy=1;
        add(userLabel,gbc);

        gbc.gridx=1;
        add(usernameField,gbc);

        gbc.gridx=0;
        gbc.gridy=2;
        add(passLabel,gbc);

        gbc.gridx=1;
        add(passwordField,gbc);

        gbc.gridx=0;
        gbc.gridy=3;
        gbc.gridwidth=2;
        add(loginButton,gbc);

        loginButton.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {

        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if(username.equals("admin") && password.equals("admin123")){

            JOptionPane.showMessageDialog(this, "Login Successful");

            dispose();

            new DashboardUI();

        }else{

            JOptionPane.showMessageDialog(this,"Invalid Username or Password");

        }

    }

}