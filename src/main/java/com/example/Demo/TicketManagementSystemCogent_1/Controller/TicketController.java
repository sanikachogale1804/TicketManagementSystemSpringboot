package com.example.Demo.TicketManagementSystemCogent_1.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Service.TicketService;



@CrossOrigin(origins = {
	    "http://localhost:3000",
	    "https://rainbow-kataifi-7acd83.netlify.app/",//local
	    "https://cogentmobileapp.in:8443",//VM
	    "https://45.115.186.228:8443"

 	})
@RestController
@RequestMapping("/tickets")
public class TicketController {
	
	 @Autowired
	 private TicketService ticketService;
	
	
}
