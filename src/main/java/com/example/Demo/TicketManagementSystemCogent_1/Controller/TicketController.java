package com.example.Demo.TicketManagementSystemCogent_1.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Service.AutoAssignmentService;
import com.example.Demo.TicketManagementSystemCogent_1.Service.TicketService;



@CrossOrigin(origins = {
	    "http://localhost:3000","http://127.0.0.1:3000", 
        "http://192.168.1.91:3000",
	    "https://rainbow-kataifi-7acd83.netlify.app/",//local
//	    "https://cogentmobileapp.in:8443",//VM
	    "http://117.250.211.51:3000"

 	})
@RestController
@RequestMapping("/tickets")
public class TicketController {
	
	 @Autowired
	 private TicketService ticketService;
	
	 @Autowired
	 private AutoAssignmentService autoAssignmentService;
	 
	 @GetMapping
	 public List<Ticket> getAllTickets() {
	     return ticketService.getAllTickets();
	 }
	 
	 @PostMapping
	 public Ticket createTicket(@RequestBody Ticket ticket)
	 {
		 return autoAssignmentService.autoAssign(ticket);
	 }
	
}
