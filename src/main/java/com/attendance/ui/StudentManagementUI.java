package com.attendance.ui;

import com.attendance.dao.StudentDAO;
import com.attendance.model.Student;
import com.attendance.util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentManagementUI extends JFrame {

    private JTextField txtSearch;

    private JButton btnSearch;
    private JButton btnRefresh;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnViewQR;

    private JTable studentTable;
    private DefaultTableModel tableModel;

    private JLabel lblTotalStudents;

    private StudentDAO studentDAO;

    public StudentManagementUI() {

        studentDAO = new StudentDAO();

        initializeUI();

        loadStudents();

        registerEvents();

        setVisible(true);

    }

    private void initializeUI() {

        setTitle("Student Management");
        setSize(1100,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        //---------------- Header ----------------//

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PRIMARY);
        header.setPreferredSize(new Dimension(100,70));

        JLabel title = new JLabel(
                "Student Management",
                SwingConstants.CENTER
        );

        title.setForeground(Color.WHITE);
        title.setFont(Theme.TITLE_FONT);

        header.add(title);

        container.add(header,BorderLayout.NORTH);

        //---------------- Toolbar ----------------//

        JPanel toolBar = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                10,
                10
        ));

        toolBar.setBackground(Color.WHITE);
        toolBar.setBorder(new EmptyBorder(5,10,5,10));

        toolBar.add(new JLabel("Search"));

        txtSearch = new JTextField(20);

        toolBar.add(txtSearch);

        btnSearch = new JButton("Search");
        btnRefresh = new JButton("Refresh");
        btnAdd = new JButton("Add Student");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        btnViewQR = new JButton("View QR");

        btnSearch.setFont(Theme.BUTTON_FONT);
        btnRefresh.setFont(Theme.BUTTON_FONT);
        btnAdd.setFont(Theme.BUTTON_FONT);
        btnEdit.setFont(Theme.BUTTON_FONT);
        btnDelete.setFont(Theme.BUTTON_FONT);
        btnViewQR.setFont(Theme.BUTTON_FONT);

        btnSearch.setBackground(Color.DARK_GRAY);
        btnSearch.setForeground(Color.WHITE);

        btnRefresh.setBackground(Theme.WARNING);
        btnRefresh.setForeground(Color.WHITE);

        btnAdd.setBackground(Theme.PRIMARY);
        btnAdd.setForeground(Color.WHITE);

        btnEdit.setBackground(Theme.SUCCESS);
        btnEdit.setForeground(Color.WHITE);

        btnDelete.setBackground(Theme.DANGER);
        btnDelete.setForeground(Color.WHITE);

        btnViewQR.setBackground(new Color(98,0,238));
        btnViewQR.setForeground(Color.WHITE);

        toolBar.add(btnSearch);
        toolBar.add(btnRefresh);
        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.add(btnViewQR);

        container.add(toolBar,BorderLayout.BEFORE_FIRST_LINE);

        //---------------- Table ----------------//

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new String[]{
                "Roll Number",
                "Name",
                "Department",
                "Year",
                "Email"
        });

        studentTable = new JTable(tableModel);

        studentTable.setRowHeight(28);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(studentTable);

        container.add(scrollPane,BorderLayout.CENTER);

        //---------------- Footer ----------------//

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        lblTotalStudents = new JLabel();

        lblTotalStudents.setFont(Theme.LABEL_FONT);

        footer.add(lblTotalStudents);

        container.add(footer,BorderLayout.SOUTH);

        add(container);

    }
    private void registerEvents() {

        btnRefresh.addActionListener(e -> loadStudents());

        btnSearch.addActionListener(e -> searchStudents());

        btnAdd.addActionListener(e -> {
            new StudentRegistrationUI();
        });

        btnDelete.addActionListener(e -> deleteStudent());

        btnEdit.addActionListener(e -> editStudent());

        btnViewQR.addActionListener(e -> viewQRCode());

    }

    //=========================================
    // Load Students
    //=========================================

    private void loadStudents() {

        tableModel.setRowCount(0);

        List<Student> students = studentDAO.getAllStudents();

        for (Student student : students) {

            tableModel.addRow(new Object[]{
                    student.getRollNumber(),
                    student.getName(),
                    student.getDepartment(),
                    student.getYear(),
                    student.getEmail()
            });

        }

        lblTotalStudents.setText(
                "Total Students : " + students.size()
        );

    }

    //=========================================
    // Search Student
    //=========================================

    private void searchStudents() {

        String keyword = txtSearch.getText().trim();

        tableModel.setRowCount(0);

        List<Student> students;

        if (keyword.isEmpty()) {

            students = studentDAO.getAllStudents();

        } else {

            students = studentDAO.searchStudents(keyword);

        }

        for (Student student : students) {

            tableModel.addRow(new Object[]{
                    student.getRollNumber(),
                    student.getName(),
                    student.getDepartment(),
                    student.getYear(),
                    student.getEmail()
            });

        }

        lblTotalStudents.setText(
                "Total Students : " + students.size()
        );

    }

    //=========================================
    // Delete Student
    //=========================================

    private void deleteStudent() {

        int row = studentTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student."
            );

            return;

        }

        String rollNumber =
                tableModel.getValueAt(row,0).toString();

        int option = JOptionPane.showConfirmDialog(
                this,
                "Delete Student : " + rollNumber + " ?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if(option == JOptionPane.YES_OPTION){

            if(studentDAO.deleteStudent(rollNumber)){

                JOptionPane.showMessageDialog(
                        this,
                        "Student Deleted Successfully"
                );

                loadStudents();

            }else{

                JOptionPane.showMessageDialog(
                        this,
                        "Delete Failed"
                );

            }

        }

    }
    //=========================================
    // Edit Student
    //=========================================

    private void editStudent() {

        int row = studentTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student."
            );

            return;

        }

        String rollNumber = tableModel.getValueAt(row,0).toString();

        Student student = studentDAO.getStudentByRollNumber(rollNumber);

        if(student == null){

            JOptionPane.showMessageDialog(
                    this,
                    "Student not found."
            );

            return;

        }

        StudentRegistrationUI registrationUI = new StudentRegistrationUI();

        registrationUI.loadStudent(student);

    }

    //=========================================
    // View QR Code
    //=========================================

    private void viewQRCode() {

        int row = studentTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student."
            );

            return;

        }

        String rollNumber =
                tableModel.getValueAt(row,0).toString();

        Student student =
                studentDAO.getStudentByRollNumber(rollNumber);

        if(student == null){

            JOptionPane.showMessageDialog(
                    this,
                    "Student not found."
            );

            return;

        }

        String qrPath = student.getQrCodePath();

        if(qrPath == null || qrPath.isEmpty()){

            JOptionPane.showMessageDialog(
                    this,
                    "QR Code not available."
            );

            return;

        }

        ImageIcon imageIcon = new ImageIcon(qrPath);

        Image image = imageIcon.getImage().getScaledInstance(
                300,
                340,
                Image.SCALE_SMOOTH
        );

        JLabel label = new JLabel(new ImageIcon(image));

        JOptionPane.showMessageDialog(
                this,
                label,
                "QR Code - " + rollNumber,
                JOptionPane.PLAIN_MESSAGE
        );

    }
    //=========================================
    // Refresh Table
    //=========================================

    public void refreshTable() {

        loadStudents();

    }

    //=========================================
    // Clear Search
    //=========================================

    private void clearSearch() {

        txtSearch.setText("");

        loadStudents();

    }

    //=========================================
    // Get Selected Roll Number
    //=========================================

    private String getSelectedRollNumber() {

        int row = studentTable.getSelectedRow();

        if (row == -1) {
            return null;
        }

        return tableModel.getValueAt(row, 0).toString();

    }

}