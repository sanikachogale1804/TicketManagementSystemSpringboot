package com.example.Demo.TicketManagementSystemCogent_1.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Service.TicketService;



@CrossOrigin(origins = {
	    "http://localhost:3000",
	    "https://rainbow-kataifi-7acd83.netlify.app/",//local
	    "https://taupe-bubblegum-e3d51c.netlify.app/"//VM
 	})
@RestController
@RequestMapping("/tickets")
public class TicketController {
	
	 @Autowired
	 private TicketService ticketService;
	
	
}
