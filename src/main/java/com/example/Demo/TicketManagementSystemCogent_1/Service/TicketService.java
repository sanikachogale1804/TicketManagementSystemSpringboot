package com.example.Demo.TicketManagementSystemCogent_1.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.TicketRepository;

@Service
public class TicketService {
	
	  @Autowired
	    private TicketRepository ticketRepository;

	    public List<Ticket> getAllTickets() {
	        return ticketRepository.findAll(); // Return all tickets if no filter is provided
	    }
	    
	  

}
