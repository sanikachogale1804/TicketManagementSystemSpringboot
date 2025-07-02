package com.example.Demo.TicketManagementSystemCogent_1.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Service.TicketService;



@CrossOrigin(origins = {
	    "http://localhost:3000","http://127.0.0.1:3000", 
        "http://192.168.1.91:3000",
	    "https://rainbow-kataifi-7acd83.netlify.app/",//local
//	    "https://cogentmobileapp.in:8443",//VM
	    "http://45.115.186.228:3000"

 	})
@RestController
@RequestMapping("/tickets")
public class TicketController {
	
	 @Autowired
	 private TicketService ticketService;
	
	
}
