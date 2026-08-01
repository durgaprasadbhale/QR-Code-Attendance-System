package com.attendance.qr;

import com.attendance.dao.AttendanceDAO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class QRScanner extends JFrame {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private VideoCapture camera;

    private JLabel cameraLabel;
    private JLabel lblStatus;

    private JButton btnClose;

    private volatile boolean running = true;
    private volatile long lastScanTime = 0;
    private static final long SCAN_COOLDOWN = 1000; // 1 second

    public QRScanner() {

        setTitle("QR Code Attendance Scanner");
        setSize(800, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        cameraLabel = new JLabel();
        cameraLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(cameraLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        lblStatus = new JLabel(
                "🟡 Waiting for QR...",
                SwingConstants.CENTER
        );

        lblStatus.setFont(
                new Font("Segoe UI", Font.BOLD, 20)
        );

        lblStatus.setForeground(Color.ORANGE);

        bottomPanel.add(lblStatus, BorderLayout.CENTER);

        btnClose = new JButton("Close Camera");

        bottomPanel.add(btnClose, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        btnClose.addActionListener(e -> {

            running = false;

            if (camera != null && camera.isOpened()) {
                camera.release();
            }

            dispose();

        });

        setVisible(true);

        camera = new VideoCapture(0);

        if (!camera.isOpened()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to Open Camera!"
            );

            dispose();
            return;

        }

        startCamera();

    }
    //=========================================
    // Start Camera
    //=========================================

    private void startCamera() {

        Thread cameraThread = new Thread(() -> {

            Mat frame = new Mat();

            while (running) {

                if (camera.read(frame)) {

                    BufferedImage image = matToBufferedImage(frame);

                    SwingUtilities.invokeLater(() ->
                            cameraLabel.setIcon(new ImageIcon(image))
                    );

                    try {

                        LuminanceSource source =
                                new BufferedImageLuminanceSource(image);

                        BinaryBitmap bitmap =
                                new BinaryBitmap(
                                        new HybridBinarizer(source)
                                );

                        Result result =
                                new MultiFormatReader().decode(bitmap);

                        long currentTime = System.currentTimeMillis();

                        if (currentTime - lastScanTime >= SCAN_COOLDOWN) {

                            lastScanTime = currentTime;

                            String rollNumber = result.getText();

                            onQRCodeScanned(rollNumber);

                            new javax.swing.Timer(1000, e -> {

                                lblStatus.setForeground(Color.ORANGE);
                                lblStatus.setText("🟡 Waiting for QR...");

                                ((Timer) e.getSource()).stop();

                            }).start();

                        }

                    } catch (NotFoundException e) {

                        // No QR found

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            }

            frame.release();

        });

        cameraThread.setDaemon(true);
        cameraThread.start();

    }

    //=========================================
    // Convert Mat to BufferedImage
    //=========================================

    private BufferedImage matToBufferedImage(Mat mat) {

        Mat rgb = new Mat();

        Imgproc.cvtColor(
                mat,
                rgb,
                Imgproc.COLOR_BGR2RGB
        );

        int width = rgb.width();
        int height = rgb.height();
        int channels = rgb.channels();

        byte[] source =
                new byte[width * height * channels];

        rgb.get(0, 0, source);

        BufferedImage image =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_3BYTE_BGR
                );

        byte[] target =
                ((DataBufferByte)
                        image.getRaster()
                                .getDataBuffer())
                        .getData();

        System.arraycopy(
                source,
                0,
                target,
                0,
                source.length
        );

        rgb.release();

        return image;

    }
    //=========================================
    // QR Code Detected
    //=========================================

    private void onQRCodeScanned(String rollNumber) {

        AttendanceDAO attendanceDAO = new AttendanceDAO();

        String status = attendanceDAO.markAttendanceWithStatus(rollNumber);

        SwingUtilities.invokeLater(() -> {

            switch (status) {

                case "SUCCESS":

                    lblStatus.setForeground(new Color(0, 153, 0));
                    lblStatus.setText("🟢 Attendance Marked Successfully");
                    break;

                case "ALREADY_MARKED":

                    lblStatus.setForeground(Color.ORANGE);
                    lblStatus.setText("🟠 Attendance Already Marked");
                    break;

                case "INVALID_QR":

                    lblStatus.setForeground(Color.RED);
                    lblStatus.setText("🔴 Invalid QR Code");
                    break;

                default:

                    lblStatus.setForeground(Color.RED);
                    lblStatus.setText("🔴 Attendance Failed");

            }

        });

    }

    //=========================================
    // Close Camera Properly
    //=========================================

    @Override
    public void dispose() {

        running = false;

        try {

            if (camera != null && camera.isOpened()) {
                camera.release();
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        super.dispose();

    }

}
