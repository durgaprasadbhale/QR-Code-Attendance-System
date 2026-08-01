package com.attendance.ui;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Calendar;
import com.attendance.dao.AttendanceDAO;
import com.attendance.model.Attendance;
import com.attendance.service.ExcelService;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReportUI extends JFrame {

    private JRadioButton dailyRadio;
    private JRadioButton monthlyRadio;

    private JDateChooser dateChooser;

    private JComboBox<String> monthCombo;
    private JComboBox<Integer> yearCombo;

    private JButton viewButton;
    private JButton exportExcelButton;
    private JButton exportPdfButton;

    private JTable reportTable;
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private ExcelService excelService = new ExcelService();

    public ReportUI() {

        System.out.println("ReportUI Constructor Called");

        setTitle("Attendance Reports");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //---------------- TOP PANEL ----------------//

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Radio Buttons
        dailyRadio = new JRadioButton("Daily Report");
        monthlyRadio = new JRadioButton("Monthly Report");

        dailyRadio.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(dailyRadio);
        group.add(monthlyRadio);

        // Date Chooser
        dateChooser = new JDateChooser();

        // Month Combo
        monthCombo = new JComboBox<>(new String[]{
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
        });

        // Year Combo
        yearCombo = new JComboBox<>();

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = currentYear - 5; i <= currentYear + 1; i++) {
            yearCombo.addItem(i);
        }

        // Buttons
        viewButton = new JButton("View Report");

        exportExcelButton = new JButton("Export Excel");
        exportPdfButton = new JButton("Export PDF");

        exportExcelButton.setEnabled(false);
        exportPdfButton.setEnabled(false);

        // Layout

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Report Type:"), gbc);

        gbc.gridx = 1;
        topPanel.add(dailyRadio, gbc);

        gbc.gridx = 2;
        topPanel.add(monthlyRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        topPanel.add(dateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        topPanel.add(new JLabel("Month:"), gbc);

        gbc.gridx = 1;
        topPanel.add(monthCombo, gbc);

        gbc.gridx = 2;
        topPanel.add(yearCombo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        topPanel.add(viewButton, gbc);

        //---------------- TABLE ----------------//

        reportTable = new JTable(new DefaultTableModel(
                new Object[]{
                        "Roll No",
                        "Student Name",
                        "Department",
                        "Year",
                        "Date",
                        "Time",
                        "Status"
                }, 0
        ));

        JScrollPane scrollPane = new JScrollPane(reportTable);

        //---------------- BOTTOM PANEL ----------------//

        JPanel bottomPanel = new JPanel();

        bottomPanel.add(exportExcelButton);
        bottomPanel.add(exportPdfButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        //---------------- INITIAL STATE ----------------//

        dateChooser.setVisible(true);

        monthCombo.setVisible(false);
        yearCombo.setVisible(false);

        //---------------- EVENTS ----------------//

        dailyRadio.addActionListener(e -> {

            dateChooser.setVisible(true);

            monthCombo.setVisible(false);
            yearCombo.setVisible(false);

            topPanel.revalidate();
            topPanel.repaint();

        });

        monthlyRadio.addActionListener(e -> {

            dateChooser.setVisible(false);

            monthCombo.setVisible(true);
            yearCombo.setVisible(true);

            topPanel.revalidate();
            topPanel.repaint();

        });
        //=============================
// View Report Button
//=============================

        viewButton.addActionListener(e -> {

            DefaultTableModel model =
                    (DefaultTableModel) reportTable.getModel();

            model.setRowCount(0);

            List<Attendance> attendanceList;

            // --------------------------
            // Daily Report
            // --------------------------

            if (dailyRadio.isSelected()) {

                if (dateChooser.getDate() == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please select a date."
                    );

                    return;
                }

                Date selectedDate =
                        new Date(dateChooser.getDate().getTime());

                attendanceList =
                        attendanceDAO.getAttendanceByDate(selectedDate);

            }

            // --------------------------
            // Monthly Report
            // --------------------------

            else {

                int month = monthCombo.getSelectedIndex() + 1;
                int year = (Integer) yearCombo.getSelectedItem();

                attendanceList =
                        attendanceDAO.getAttendanceByMonth(month, year);

            }

            // --------------------------
            // No Records
            // --------------------------

            if (attendanceList.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No attendance records found."
                );

                exportExcelButton.setEnabled(false);
                exportPdfButton.setEnabled(false);

                return;

            }

            // --------------------------
            // Fill Table
            // --------------------------

            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("dd-MM-yyyy");

            SimpleDateFormat timeFormat =
                    new SimpleDateFormat("hh:mm:ss a");

            for (Attendance attendance : attendanceList) {

                model.addRow(new Object[]{

                        attendance.getRollNumber(),
                        attendance.getStudentName(),
                        attendance.getDepartment(),
                        attendance.getYear(),
                        dateFormat.format(attendance.getAttendanceDate()),
                        timeFormat.format(attendance.getAttendanceTime()),
                        attendance.getStatus()

                });

            }

            exportExcelButton.setEnabled(true);
            exportPdfButton.setEnabled(true);

        });
        exportExcelButton.addActionListener(e -> {

            String fileName;

            if (dailyRadio.isSelected()) {

                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("dd-MM-yyyy");

                fileName =
                        "Daily_Report_" +
                                sdf.format(dateChooser.getDate()) +
                                ".xlsx";

            }

            else {

                String month =
                        monthCombo.getSelectedItem().toString();

                Integer year =
                        (Integer) yearCombo.getSelectedItem();

                fileName =
                        "Monthly_Report_" +
                                month +
                                "_" +
                                year +
                                ".xlsx";

            }

            excelService.exportTable(reportTable, fileName);

        });

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new ReportUI().setVisible(true);

        });

    }

}