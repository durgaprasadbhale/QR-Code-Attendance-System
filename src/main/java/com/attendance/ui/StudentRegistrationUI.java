package com.attendance.ui;

import com.attendance.dao.StudentDAO;
import com.attendance.model.Student;
import com.attendance.qr.QRCodeGenerator;
import com.attendance.util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentRegistrationUI extends JFrame {

    private JTextField txtRollNumber;
    private JTextField txtStudentName;
    private JComboBox<String> cmbDepartment;
    private JComboBox<String> cmbYear;
    private JTextField txtEmail;

    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnClear;

    private StudentDAO studentDAO;
    private boolean editMode = false;
    private String currentRollNumber = "";

    public StudentRegistrationUI() {

        studentDAO = new StudentDAO();

        setTitle("Student Registration");
        setSize(550, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Theme.BACKGROUND);
        mainPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Student Registration", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(Theme.PRIMARY);
        title.setForeground(Color.WHITE);
        title.setFont(Theme.TITLE_FONT);
        title.setPreferredSize(new Dimension(550,60));

        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20,30,20,30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblRoll = new JLabel("Roll Number");
        lblRoll.setFont(Theme.LABEL_FONT);

        JLabel lblName = new JLabel("Student Name");
        lblName.setFont(Theme.LABEL_FONT);

        JLabel lblDepartment = new JLabel("Department");
        lblDepartment.setFont(Theme.LABEL_FONT);

        JLabel lblYear = new JLabel("Year");
        lblYear.setFont(Theme.LABEL_FONT);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(Theme.LABEL_FONT);

        txtRollNumber = new JTextField(20);
        txtStudentName = new JTextField(20);

        cmbDepartment = new JComboBox<>(new String[]{
                "Select Department",
                "CSE",
                "CSE-AI",
                "CSE-DS",
                "IT",
                "ECE",
                "EEE",
                "MECH",
                "CIVIL"
        });

        cmbYear = new JComboBox<>(new String[]{
                "Select Year",
                "1",
                "2",
                "3",
                "4"
        });

        txtEmail = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblRoll, gbc);

        gbc.gridx = 1;
        formPanel.add(txtRollNumber, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(lblName, gbc);

        gbc.gridx = 1;
        formPanel.add(txtStudentName, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(lblDepartment, gbc);

        gbc.gridx = 1;
        formPanel.add(cmbDepartment, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(lblYear, gbc);

        gbc.gridx = 1;
        formPanel.add(cmbYear, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(lblEmail, gbc);

        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        btnSave = new JButton("Save");
        btnUpdate = new JButton("Update");
        btnClear = new JButton("Clear");

        btnSave.setBackground(Theme.PRIMARY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(Theme.BUTTON_FONT);

        btnUpdate.setBackground(Theme.SUCCESS);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(Theme.BUTTON_FONT);

        btnClear.setBackground(Theme.WARNING);
        btnClear.setForeground(Color.WHITE);
        btnClear.setFont(Theme.BUTTON_FONT);

        btnUpdate.setEnabled(false);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnClear);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnSave.addActionListener(e -> saveStudent());

        btnClear.addActionListener(e -> clearFields());

        btnUpdate.addActionListener(e -> updateStudent());

        setVisible(true);

    }

    private void saveStudent() {

        if (!validateFields()) {
            return;
        }

        Student student = new Student();

        student.setRollNumber(txtRollNumber.getText().trim());
        student.setName(txtStudentName.getText().trim());
        student.setDepartment(cmbDepartment.getSelectedItem().toString());
        student.setYear(Integer.parseInt(cmbYear.getSelectedItem().toString()));
        student.setEmail(txtEmail.getText().trim());

        String qrPath = QRCodeGenerator.generateQRCode(student.getRollNumber());

        student.setQrCodePath(qrPath);

        if (studentDAO.addStudent(student)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Student Registered Successfully"
            );
            clearFields();



        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Roll Number Already Exists!"
            );

        }

    }
    private void updateStudent() {

        if (!validateFields()) {
            return;
        }

        Student student = new Student();

        student.setRollNumber(currentRollNumber);
        student.setName(txtStudentName.getText().trim());
        student.setDepartment(cmbDepartment.getSelectedItem().toString());
        student.setYear(Integer.parseInt(cmbYear.getSelectedItem().toString()));
        student.setEmail(txtEmail.getText().trim());

        if(studentDAO.updateStudent(student)){

            JOptionPane.showMessageDialog(
                    this,
                    "Student Updated Successfully"
            );

            dispose();

        }else{

            JOptionPane.showMessageDialog(
                    this,
                    "Update Failed"
            );

        }

    }

    private boolean validateFields() {

        if (txtRollNumber.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Roll Number");
            txtRollNumber.requestFocus();
            return false;
        }

        if (txtStudentName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Student Name");
            txtStudentName.requestFocus();
            return false;
        }

        if (cmbDepartment.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Select Department");
            return false;
        }

        if (cmbYear.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Select Year");
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Email");
            txtEmail.requestFocus();
            return false;
        }

        String email = txtEmail.getText().trim();

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this, "Invalid Email Address");
            txtEmail.requestFocus();
            return false;
        }

        return true;
    }

    private void clearFields() {

        txtRollNumber.setText("");
        txtStudentName.setText("");
        cmbDepartment.setSelectedIndex(0);
        cmbYear.setSelectedIndex(0);
        txtEmail.setText("");

        txtRollNumber.setEditable(true);

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);

        txtRollNumber.requestFocus();

    }
    public void loadStudent(Student student) {

        editMode = true;

        currentRollNumber = student.getRollNumber();

        txtRollNumber.setText(student.getRollNumber());
        txtStudentName.setText(student.getName());
        cmbDepartment.setSelectedItem(student.getDepartment());
        cmbYear.setSelectedItem(String.valueOf(student.getYear()));
        txtEmail.setText(student.getEmail());

        txtRollNumber.setEditable(false);

        btnSave.setEnabled(false);
        btnUpdate.setEnabled(true);

    }
}