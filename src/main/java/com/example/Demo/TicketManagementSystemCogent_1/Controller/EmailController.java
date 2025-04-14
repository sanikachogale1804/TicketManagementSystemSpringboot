package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

@RestController
public class EmailController {
	
	@Autowired
    private EmailService emailService;

    @GetMapping(value = "/sendemail")
    public String sendEmail() throws AddressException, MessagingException, IOException {
        emailService.sendmail();  // Call the method from your service
        return "Email sent successfully";
    }

}
