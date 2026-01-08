package com.example.Demo.TicketManagementSystemCogent_1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Comment;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

	@Autowired
    private JavaMailSender mailSender;

    public void sendTicketAssignedMail(Ticket ticket, User teamMember) {
    	System.out.println("Sending email to " + teamMember.getUserEmail() + " for ticket " + ticket.getTicketId());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vmssupport@cogentsecurity.ai"); // SAME as spring.mail.username
        message.setTo(teamMember.getUserEmail());
        message.setSubject("New Ticket Assigned | Ticket ID: " + ticket.getTicketId());

        message.setText(
            "Hello " + teamMember.getUserName() + ",\n\n" +
            "A new ticket has been assigned to you.\n\n" +
            "Ticket ID: " + ticket.getTicketId() + "\n" +
            "Status: ASSIGNED\n\n" +
            "Please login to the system.\n\n" +
            "Regards,\nTicket Management System"
        );

        mailSender.send(message);
    }
    
    public void sendTicketClosedMail(String to, Ticket ticket) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Ticket Closed | Ticket ID: " + ticket.getTicketId());

        message.setText(
                "Hello,\n\n" +
                        "Your ticket has been successfully closed.\n\n" +
                        "Ticket Details:\n" +
                        "Ticket ID: " + ticket.getTicketId() + "\n" +
                        "Description: " + ticket.getDescription() + "\n" +
                        "Status: " + ticket.getStatus() + "\n\n" +
                        "Thank you,\nCogent Support Team"
        );

        mailSender.send(message);
        System.out.println("✅ Closed mail sent to: " + to);
    }
//    public void sendCommentAddedMail(String to, Ticket ticket, Comment comment) {
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("New Comment on Ticket #" + ticket.getTicketId());
//        message.setText(
//            "A new comment has been added.\n\n" +
//            "Comment: " + comment.getComment()
//        );
//
//        mailSender.send(message);
//    }


}
