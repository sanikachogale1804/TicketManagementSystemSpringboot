package com.example.Demo.TicketManagementSystemCogent_1.Service;

import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

    public void sendmail() throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("cogenttech60@gmail.com", "");
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("cogenttech60@gmail.com", false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("cogenttech60@gmail.com"));
        msg.setSubject("Tutorials point email");
        msg.setContent("Tutorials point email", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Tutorials point email", "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attachPart = new MimeBodyPart();
        attachPart.attachFile(new File("/var/tmp/image19.png")); // Make sure file exists
        multipart.addBodyPart(attachPart);

        msg.setContent(multipart);
        Transport.send(msg);
    }
}
