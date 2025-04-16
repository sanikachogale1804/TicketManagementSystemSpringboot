package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.Demo.TicketManagementSystemCogent_1.Service.TicketService;

@CrossOrigin(origins = {
	    "http://localhost:3000",
	    "https://creative-cascaron-a830b6.netlify.app"
	})
@RestController
@RequestMapping("/tickets")
public class TicketController {
	
	 @Autowired
	 private TicketService ticketService;
	
	
}
