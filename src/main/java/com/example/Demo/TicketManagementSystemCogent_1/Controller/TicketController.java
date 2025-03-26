package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.TicketRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Service.TicketService;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow only from React frontend
@RequestMapping("/tickets")
public class TicketController {

	 @Autowired
	    private TicketService ticketService;
	 
	 @Autowired
	    private TicketRepository ticketRepository;

	 @GetMapping("/tickets/{id}")
	 public ResponseEntity<Ticket> getTicketById(@PathVariable int id) {
	     Ticket ticket = ticketRepository.findById(id).orElse(null);
	     if (ticket != null) {
	         return ResponseEntity.ok(ticket);
	     } else {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	     }
	 }

	    
}