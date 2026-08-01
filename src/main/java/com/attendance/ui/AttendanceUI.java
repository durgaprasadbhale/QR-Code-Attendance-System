package com.attendance.ui;

import com.attendance.dao.AttendanceDAO;
import com.attendance.model.Attendance;
import com.attendance.util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AttendanceUI extends JFrame {

    private JTextField txtSearch;

    private JButton btnSearch;
    private JButton btnToday;
    private JButton btnRefresh;

    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    private JLabel lblTotalRecords;

    private AttendanceDAO attendanceDAO;

    public AttendanceUI() {

        attendanceDAO = new AttendanceDAO();

        initializeUI();

        loadAttendance();

        registerEvents();

        setVisible(true);

    }

    private void initializeUI() {

        setTitle("Attendance Management");
        setSize(1200,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        //---------------- Header ----------------//

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PRIMARY);
        header.setPreferredSize(new Dimension(100,70));

        JLabel title = new JLabel(
                "Attendance Management",
                SwingConstants.CENTER
        );

        title.setForeground(Color.WHITE);
        title.setFont(Theme.TITLE_FONT);

        header.add(title);

        container.add(header,BorderLayout.NORTH);

        //---------------- Toolbar ----------------//

        JPanel toolBar = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        10,
                        10
                )
        );

        toolBar.setBackground(Color.WHITE);
        toolBar.setBorder(new EmptyBorder(5,10,5,10));

        toolBar.add(new JLabel("Search"));

        txtSearch = new JTextField(20);

        toolBar.add(txtSearch);

        btnSearch = new JButton("Search");
        btnToday = new JButton("Today's Attendance");
        btnRefresh = new JButton("Refresh");

        btnSearch.setFont(Theme.BUTTON_FONT);
        btnToday.setFont(Theme.BUTTON_FONT);
        btnRefresh.setFont(Theme.BUTTON_FONT);

        btnSearch.setBackground(Color.DARK_GRAY);
        btnSearch.setForeground(Color.WHITE);

        btnToday.setBackground(Theme.SUCCESS);
        btnToday.setForeground(Color.WHITE);

        btnRefresh.setBackground(Theme.WARNING);
        btnRefresh.setForeground(Color.WHITE);

        toolBar.add(btnSearch);
        toolBar.add(btnToday);
        toolBar.add(btnRefresh);

        container.add(toolBar,BorderLayout.BEFORE_FIRST_LINE);

        //---------------- Table ----------------//

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new String[]{
                "Roll Number",
                "Student Name",
                "Department",
                "Year",
                "Date",
                "Time",
                "Status"
        });

        attendanceTable = new JTable(tableModel);

        attendanceTable.setRowHeight(28);
        attendanceTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(attendanceTable);

        container.add(scrollPane,BorderLayout.CENTER);

        //---------------- Footer ----------------//

        JPanel footer = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        lblTotalRecords = new JLabel();

        lblTotalRecords.setFont(
                Theme.LABEL_FONT
        );

        footer.add(lblTotalRecords);

        container.add(footer,BorderLayout.SOUTH);

        add(container);


    }
    //=========================================
    // Register Events
    //=========================================

    private void registerEvents() {

        btnRefresh.addActionListener(e -> loadAttendance());

        btnSearch.addActionListener(e -> searchAttendance());

        btnToday.addActionListener(e -> loadTodayAttendance());

    }

    //=========================================
    // Load Attendance
    //=========================================

    private void loadAttendance() {

        tableModel.setRowCount(0);

        List<Attendance> attendanceList =
                attendanceDAO.getAllAttendance();

        for (Attendance attendance : attendanceList) {

            tableModel.addRow(new Object[]{
                    attendance.getRollNumber(),
                    attendance.getStudentName(),
                    attendance.getDepartment(),
                    attendance.getYear(),
                    attendance.getAttendanceDate(),
                    attendance.getAttendanceTime(),
                    attendance.getStatus()
            });

        }

        lblTotalRecords.setText(
                "Total Records : " + attendanceList.size()
        );

    }

    //=========================================
    // Search Attendance
    //=========================================

    private void searchAttendance() {

        String keyword = txtSearch.getText().trim();

        tableModel.setRowCount(0);

        List<Attendance> attendanceList;

        if (keyword.isEmpty()) {

            attendanceList = attendanceDAO.getAllAttendance();

        } else {

            attendanceList = attendanceDAO.searchAttendance(keyword);

        }

        for (Attendance attendance : attendanceList) {

            tableModel.addRow(new Object[]{
                    attendance.getRollNumber(),
                    attendance.getStudentName(),
                    attendance.getDepartment(),
                    attendance.getYear(),
                    attendance.getAttendanceDate(),
                    attendance.getAttendanceTime(),
                    attendance.getStatus()
            });

        }

        lblTotalRecords.setText(
                "Total Records : " + attendanceList.size()
        );

    }

    //=========================================
    // Today's Attendance
    //=========================================

    private void loadTodayAttendance() {

        tableModel.setRowCount(0);

        List<Attendance> attendanceList =
                attendanceDAO.getTodayAttendance();

        for (Attendance attendance : attendanceList) {

            tableModel.addRow(new Object[]{
                    attendance.getRollNumber(),
                    attendance.getStudentName(),
                    attendance.getDepartment(),
                    attendance.getYear(),
                    attendance.getAttendanceDate(),
                    attendance.getAttendanceTime(),
                    attendance.getStatus()
            });

        }

        lblTotalRecords.setText(
                "Today's Attendance : " + attendanceList.size()
        );

    }

    //=========================================
    // Refresh Table
    //=========================================

    public void refreshTable() {

        txtSearch.setText("");

        loadAttendance();

    }

}