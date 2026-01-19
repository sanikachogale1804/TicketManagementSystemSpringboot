package com.example.Demo.TicketManagementSystemCogent_1.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Comment;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.CommentRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.TicketRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.UserRepository;

@Service
public class CommentService {

	@Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public Comment saveComment(Comment comment, boolean closeTicket) {
    	Ticket ticket = ticketRepository.findById(
                comment.getTicket().getTicketId()
        ).orElseThrow(() -> new RuntimeException("Ticket not found"));

        User user = userRepository.findById(
                comment.getUser().getUserId()
        ).orElseThrow(() -> new RuntimeException("User not found"));

        Comment savedComment = commentRepository.save(comment);

        // ✅ IF ticket is CLOSED → mail customer
        if (ticket.getStatus() == Ticket.Status.CLOSED) {

            User customer = ticket.getCustomer();

            if (customer != null && customer.getUserEmail() != null) {
                System.out.println("Sending CLOSED mail to " + customer.getUserEmail());
                emailService.sendTicketClosedMail(ticket);
            }
        }


        return savedComment;
    }


//	    public void addComment(Comment comment) {
//	        // Validate ticketId and userId existence (foreign key constraints)
//	        if (!ticketRepository.existsById(comment.getTicket().getTicketId())) {
//	            throw new IllegalArgumentException("Invalid ticketId");
//	        }
//
//	        if (!userRepository.existsById(comment.getUser().getUserId())) {
//	            throw new IllegalArgumentException("Invalid userId");
//	        }
//
//	        // Save the comment entity to the database
//	        commentRepository.save(comment);
//	    }

}
