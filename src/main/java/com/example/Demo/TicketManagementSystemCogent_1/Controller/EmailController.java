package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;
import com.example.Demo.TicketManagementSystemCogent_1.Service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

@RestController
@RequestMapping("/mail-test")
public class EmailController {
	
	 @Autowired
	 private JavaMailSender mailSender;

	 public void sendTicketAssignedMail(Ticket ticket, User teamMember) {

	     SimpleMailMessage message = new SimpleMailMessage();
	     message.setFrom("vmssupport@cogentsecurity.ai"); // MUST be same as SMTP username
	     message.setTo(teamMember.getUserEmail());
	     message.setSubject("New Ticket Assigned | Ticket ID: " + ticket.getTicketId());

	     message.setText(
	         "Hello " + teamMember.getUserName() + ",\n\n" +
	         "A new ticket has been assigned to you.\n\n" +
	         "Ticket Details:\n" +
	         "Ticket ID: " + ticket.getTicketId() + "\n" +
//	         "Title: " + ticket.getTitle() + "\n" +
//	         "Priority: " + ticket.getPriority() + "\n" +
	         "Status: ASSIGNED\n\n" +
	         "Please login to the system and take action.\n\n" +
	         "Regards,\n" +
	         "Ticket Management System"
	     );

        mailSender.send(message);
	    }
}
