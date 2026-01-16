package com.example.Demo.TicketManagementSystemCogent_1.Controller;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.TicketRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.UserRepository;
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
	 
	 @Autowired
	 private TicketRepository ticketRepository;
	 
	 @Autowired
	 private UserRepository userRepository;
	 
	 @GetMapping
	 public List<Ticket> getAllTickets() {
	     return ticketService.getAllTickets();
	 }
	 
	 @PostMapping("/createTicket")
	 public Ticket createTicket(@RequestParam int customerId,
	                            @RequestParam(required = false) String description,
	                            @RequestParam(required = false) String district) {

	     // 1️⃣ Fetch the managed customer
	     User customer = userRepository.findById(customerId)
	             .orElseThrow(() -> new RuntimeException("Customer not found"));

	     // 2️⃣ Create a new ticket object
	     Ticket ticket = new Ticket();
	     ticket.setCustomer(customer);
	     ticket.setDescription(description);
	     ticket.setDistrict(district);
	     ticket.setStatus(Ticket.Status.OPEN); // set default status
	     ticket.setStartDate(LocalDateTime.now());

	     // 3️⃣ Save ticket
	     Ticket savedTicket = ticketRepository.save(ticket);

	     System.out.println("Customer after save: " + savedTicket.getCustomer());

	     return savedTicket;
	 }


	 
//	 @PutMapping("/close/{ticketId}")
//	 public Ticket closeTicket(@PathVariable int ticketId) {
//	     return ticketService.closeTicket(ticketId);
//	 }

	
}
