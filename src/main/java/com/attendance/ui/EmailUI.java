package com.attendance.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import com.attendance.service.EmailService;
import com.attendance.dao.StudentDAO;
import com.attendance.model.Student;
import com.attendance.dao.AttendanceDAO;

public class EmailUI extends JFrame {

    private JRadioButton singleStudentRadio;
    private JRadioButton allStudentsRadio;
    private JRadioButton absentStudentsRadio;
    private JRadioButton customEmailRadio;

    private JTextField rollNumberField;
    private JTextField emailField;
    private JTextField subjectField;

    private JTextArea messageArea;

    private JButton chooseFileButton;
    private JButton sendButton;

    private JLabel selectedFileLabel;

    private File selectedFile;

    public EmailUI() {

        setTitle("Email Module");
        setSize(700, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ==========================
        // Recipient Type
        // ==========================

        singleStudentRadio = new JRadioButton("Single Student", true);
        allStudentsRadio = new JRadioButton("All Students");
        absentStudentsRadio = new JRadioButton("Absent Students");
        customEmailRadio = new JRadioButton("Custom Email");

        ButtonGroup group = new ButtonGroup();

        group.add(singleStudentRadio);
        group.add(allStudentsRadio);
        group.add(absentStudentsRadio);
        group.add(customEmailRadio);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Recipient Type"), gbc);

        gbc.gridy++;
        panel.add(singleStudentRadio, gbc);

        gbc.gridy++;
        panel.add(allStudentsRadio, gbc);

        gbc.gridy++;
        panel.add(absentStudentsRadio, gbc);

        gbc.gridy++;
        panel.add(customEmailRadio, gbc);

        // ==========================
        // Roll Number
        // ==========================

        gbc.gridy++;
        panel.add(new JLabel("Roll Number"), gbc);

        rollNumberField = new JTextField();

        gbc.gridy++;
        panel.add(rollNumberField, gbc);

        // ==========================
        // Email
        // ==========================

        gbc.gridy++;
        panel.add(new JLabel("Email"), gbc);

        emailField = new JTextField();
        emailField.setEnabled(false);

        gbc.gridy++;
        panel.add(emailField, gbc);

        // ==========================
        // Subject
        // ==========================

        gbc.gridy++;
        panel.add(new JLabel("Subject"), gbc);

        subjectField = new JTextField();

        gbc.gridy++;
        panel.add(subjectField, gbc);

        // ==========================
        // Message
        // ==========================

        gbc.gridy++;
        panel.add(new JLabel("Message"), gbc);

        messageArea = new JTextArea(6, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(messageArea);

// Give the scroll pane some height
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        panel.add(scrollPane, gbc);

// Reset for the remaining components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0;

        // ==========================
        // Attachment
        // ==========================

        chooseFileButton =
                new JButton("Choose Attachment");

        selectedFileLabel =
                new JLabel("No File Selected");

        gbc.gridy++;
        panel.add(chooseFileButton, gbc);

        gbc.gridy++;
        panel.add(selectedFileLabel, gbc);

        // ==========================
        // Send Button
        // ==========================

        sendButton =
                new JButton("Send Email");

        gbc.gridy++;
        panel.add(sendButton, gbc);

        add(panel);

        // ==========================
        // Choose File
        // ==========================

        chooseFileButton.addActionListener(e -> {

            JFileChooser chooser =
                    new JFileChooser();

            int result =
                    chooser.showOpenDialog(this);

            if(result == JFileChooser.APPROVE_OPTION){

                selectedFile =
                        chooser.getSelectedFile();

                selectedFileLabel.setText(
                        selectedFile.getName()
                );

            }

        });


        // ==========================
        // Radio Button Behaviour
        // ==========================

        singleStudentRadio.addActionListener(e -> {

            rollNumberField.setEnabled(true);
            emailField.setEnabled(false);

        });

        customEmailRadio.addActionListener(e -> {

            rollNumberField.setEnabled(false);
            emailField.setEnabled(true);

        });

        allStudentsRadio.addActionListener(e -> {

            rollNumberField.setEnabled(false);
            emailField.setEnabled(false);

        });
        absentStudentsRadio.addActionListener(e -> {

            rollNumberField.setEnabled(false);
            emailField.setEnabled(false);

        });

// ==========================
// Send Button
// ==========================

        sendButton.addActionListener(e -> {

                String subject = subjectField.getText().trim();
                String message = messageArea.getText().trim();

                EmailService emailService = new EmailService();

                if (singleStudentRadio.isSelected()) {

                    String rollNumber = rollNumberField.getText().trim();

                    StudentDAO dao = new StudentDAO();
                    Student student = dao.getStudentByRollNumber(rollNumber);

                    if (student == null) {
                        JOptionPane.showMessageDialog(this, "Student not found!");
                        return;
                    }

                    boolean success = emailService.sendEmail(
                            student.getEmail(),
                            subject,
                            message,
                            selectedFile
                    );

                    JOptionPane.showMessageDialog(this,
                            success ? "Email sent successfully!" : "Failed to send email.");
                }

                else if (customEmailRadio.isSelected()) {

                    boolean success = emailService.sendEmail(
                            emailField.getText().trim(),
                            subject,
                            message,
                            selectedFile
                    );

                    JOptionPane.showMessageDialog(this,
                            success ? "Email sent successfully!" : "Failed to send email.");
                }
                else if (allStudentsRadio.isSelected()) {

                    StudentDAO dao = new StudentDAO();

                    for (Student student : dao.getAllStudents()) {

                        emailService.sendEmail(
                                student.getEmail(),
                                subject,
                                message,
                                selectedFile
                        );

                    }

                    JOptionPane.showMessageDialog(this,
                            "Emails sent to all students.");

                }
                else if (absentStudentsRadio.isSelected()) {

                    AttendanceDAO dao = new AttendanceDAO();

                    java.util.List<Student> absentStudents = dao.getAbsentStudentsToday();

                    if (absentStudents.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "No absent students found for today.");
                        return;
                    }

                    int sent = 0;

                    for (Student student : absentStudents) {

                        if (emailService.sendEmail(
                                student.getEmail(),
                                subject,
                                message,
                                selectedFile)) {

                            sent++;
                        }
                    }

                    JOptionPane.showMessageDialog(this,
                            sent + " email(s) sent successfully.");
                }


            });



    }
    }