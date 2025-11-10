package com.example.Demo.TicketManagementSystemCogent_1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;

import java.util.List;

@CrossOrigin
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	
	
	@Query("SELECT COUNT(t) FROM Ticket t WHERE t.assignedTo = :user AND (t.status = 'OPEN' OR t.status = 'IN_PROGRESS')")
	long countActiveTicketsByUser(User user);

	@Query("SELECT COUNT(t) FROM Ticket t WHERE t.assignedTo = :user AND t.status = 'CLOSED'")
	long countClosedTicketsByUser(User user);
	
	

}