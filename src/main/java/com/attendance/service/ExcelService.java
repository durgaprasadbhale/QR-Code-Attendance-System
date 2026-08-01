package com.attendance.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelService {

    public void exportTable(JTable table, String fileName) {

        try {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Attendance Report");

            TableModel model = table.getModel();

            //--------------------------------
            // Header Style
            //--------------------------------

            CellStyle style = workbook.createCellStyle();

            Font font = workbook.createFont();

            font.setBold(true);

            style.setFont(font);

            //--------------------------------
            // Header
            //--------------------------------

            Row header = sheet.createRow(0);

            for (int i = 0; i < model.getColumnCount(); i++) {

                Cell cell = header.createCell(i);

                cell.setCellValue(model.getColumnName(i));

                cell.setCellStyle(style);

            }

            //--------------------------------
            // Data
            //--------------------------------

            for (int row = 0; row < model.getRowCount(); row++) {

                Row excelRow = sheet.createRow(row + 1);

                for (int col = 0; col < model.getColumnCount(); col++) {

                    Object value = model.getValueAt(row, col);

                    excelRow.createCell(col)
                            .setCellValue(value == null ? "" : value.toString());

                }

            }

            //--------------------------------
            // Auto Size
            //--------------------------------

            for (int i = 0; i < model.getColumnCount(); i++) {

                sheet.autoSizeColumn(i);

            }

            //--------------------------------
            // Save
            //--------------------------------

            File folder = new File("reports");

            if (!folder.exists()) {

                folder.mkdirs();

            }

            FileOutputStream out =
                    new FileOutputStream("reports/" + fileName);

            workbook.write(out);

            out.close();

            workbook.close();

            JOptionPane.showMessageDialog(
                    null,
                    "Report Exported Successfully\n\nreports/" + fileName
            );

        }

        catch (IOException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Failed to export report."
            );

        }

    }

}