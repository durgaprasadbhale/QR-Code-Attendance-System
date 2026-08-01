package com.attendance.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    public static String generateQRCode(String rollNumber) {

        try {

            String folder = "qrcodes";

            File dir = new File(folder);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = folder + "/" + rollNumber + ".png";

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix matrix = new MultiFormatWriter().encode(
                    rollNumber,
                    BarcodeFormat.QR_CODE,
                    300,
                    300,
                    hints
            );

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);

            int width = qrImage.getWidth();
            int height = qrImage.getHeight();

            BufferedImage finalImage = new BufferedImage(width, height + 40, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = finalImage.createGraphics();

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height + 40);

            g.drawImage(qrImage, 0, 0, null);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 18));

            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(rollNumber);

            g.drawString(rollNumber, (width - textWidth) / 2, height + 25);

            g.dispose();

            ImageIO.write(finalImage, "PNG", new File(filePath));

            return filePath;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}