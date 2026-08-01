package com.attendance.service;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.util.Properties;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailService {

    // Replace with your project Gmail
    private final String senderEmail = "YOUR_EMAIL@gmail.com";
    private final String appPassword = "YOUR_APP_PASSWORD";

    public boolean sendEmail(String recipient,
                             String subject,
                             String message,
                             File attachment) {

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties,

                new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                senderEmail,
                                appPassword
                        );

                    }

                });

        try {

            Message email = new MimeMessage(session);

            email.setFrom(new InternetAddress(senderEmail));

            email.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient)
            );

            email.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            // Message Part

            MimeBodyPart textPart = new MimeBodyPart();

            textPart.setText(message);

            multipart.addBodyPart(textPart);

            // Attachment Part

            if (attachment != null) {

                MimeBodyPart attachmentPart =
                        new MimeBodyPart();

                FileDataSource source =
                        new FileDataSource(attachment);

                attachmentPart.setDataHandler(
                        new DataHandler(source)
                );

                attachmentPart.setFileName(
                        attachment.getName()
                );

                multipart.addBodyPart(attachmentPart);

            }

            email.setContent(multipart);

            Transport.send(email);

            return true;

        }

        catch (Exception e) {

            e.printStackTrace();

            return false;

        }

    }

}