package com.example.Demo.TicketManagementSystemCogent_1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;

import java.util.List;

@CrossOrigin
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
   
}