package com.example.Demo.TicketManagementSystemCogent_1.Service;
 
 import java.util.List;
 import java.util.Optional;
 
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.TicketRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.UserRepository;
 
 @Service
 public class TicketService {
 	
 	  @Autowired
 	  private TicketRepository ticketRepository;
 	  
 	  @Autowired
 	  private UserRepository userRepository;
 	  
 	  @Autowired
 	  private EmailService emailService;
 
 	  public List<Ticket> getAllTickets() {
 		  System.out.println("Fetching Tickets");
 	     return ticketRepository.findAll(); 
 	     
 	  }
 	  
 	 public Ticket assignTicket(int ticketId, int userId) {

         Ticket ticket = ticketRepository.findById(ticketId)
                 .orElseThrow(() -> new RuntimeException("Ticket not found"));

         User teamMember = userRepository.findById(userId)
                 .orElseThrow(() -> new RuntimeException("User not found"));

         if (teamMember.getRole() != User.Role.TEAMMEMBER) {
             throw new RuntimeException("User is not a TEAM MEMBER");
         }

         ticket.setAssignedTo(teamMember);
         ticket.setStatus(Ticket.Status.IN_PROGRESS); // ✅ ENUM FIX

         Ticket savedTicket = ticketRepository.save(ticket);

         // 📩 EMAIL
         emailService.sendTicketAssignedMail(savedTicket, teamMember);

         return savedTicket;
     }
 	  
 }