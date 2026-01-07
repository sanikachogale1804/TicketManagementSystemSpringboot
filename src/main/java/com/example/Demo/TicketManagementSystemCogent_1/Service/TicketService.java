package com.example.Demo.TicketManagementSystemCogent_1.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Fetch all tickets
    public List<Ticket> getAllTickets() {
        System.out.println("Fetching Tickets");
        return ticketRepository.findAll();
    }

    // Assign ticket manually
    public Ticket assignTicket(int ticketId, int userId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User teamMember = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (teamMember.getRole() != User.Role.TEAMMEMBER) {
            throw new RuntimeException("User is not a TEAM MEMBER");
        }

        ticket.setAssignedTo(teamMember);
        ticket.setStatus(Ticket.Status.IN_PROGRESS);

        // Ensure customer is set
        User customer = ticket.getCustomer();
        if (customer == null) {
            customer = userRepository.findByRole(User.Role.CUSTOMER)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No customer found"));
            ticket.setCustomer(customer);
        }

        Ticket savedTicket = ticketRepository.save(ticket);

        // Send email to team member
        emailService.sendTicketAssignedMail(savedTicket, teamMember);

        return savedTicket;
    }

//    public Ticket closeTicket(int ticketId) {
//
//        Ticket ticket = ticketRepository.findById(ticketId)
//                .orElseThrow(() -> new RuntimeException("Ticket not found"));
//
//        ticket.setStatus(Ticket.Status.CLOSED);
//        ticket.setEndDate(LocalDateTime.now());
//
//        Ticket savedTicket = ticketRepository.save(ticket);
//
//        User customer = savedTicket.getCustomer();
//
//        if (customer != null && customer.getUserEmail() != null) {
//            System.out.println("📧 Closing ticket → sending mail to " + customer.getUserEmail());
//
//            emailService.sendTicketClosedMail(
//                customer.getUserEmail(),
//                savedTicket
//            );
//        }
//
//        return savedTicket;
//    }


}
