package com.example.Demo.TicketManagementSystemCogent_1.Service;
 
 import java.util.List;
 import java.util.Optional;
 
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Comment;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.CommentRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.TicketRepository;
 
 @Service
 public class TicketService {
 	
 	  @Autowired
 	    private TicketRepository ticketRepository;
 
 	    public List<Ticket> getAllTickets() {
 	        return ticketRepository.findAll(); // Return all tickets if no filter is provided
 	    }
 	    
 	   @Autowired
 	    private CommentRepository commentRepository;

// 	     This method gets all comments for a ticket by its ticketId
 	    public List<Comment> getCommentsForTicket(int ticketId) {
 	        return commentRepository.findByTicket_TicketId(ticketId);
 	    }
 }