package com.example.Demo.TicketManagementSystemCogent_1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;

@CrossOrigin
public interface TicketRepository extends JpaRepository<Ticket, Integer>{

	 List<Ticket> findByAssignedTo(User assignedTo);
	 
	  List<Ticket> findByCustomer_UserId(int customerId);

	  Optional<Ticket> findById(int ticketId);
	  
	 
	
}
