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

	        // 🔹 1. Validate Ticket
	        Integer ticketId = comment.getTicket().getTicketId();

	        Ticket ticket = ticketRepository.findById(ticketId)
	                .orElseThrow(() -> new RuntimeException("❌ Ticket not found with id: " + ticketId));

	        // 🔹 2. Validate User
	        Integer userId = comment.getUser().getUserId();

	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("❌ User not found with id: " + userId));

	        // 🔹 3. Attach managed entities
	        comment.setTicket(ticket);
	        comment.setUser(user);
	        comment.setCreatedAt(LocalDateTime.now());

	        // 🔹 4. Save comment
	        Comment savedComment = commentRepository.save(comment);
	        System.out.println("✅ Comment saved in DB");

	        // 🔹 5. Close ticket if required
	        if (closeTicket) {
	            ticket.setStatus(Ticket.Status.CLOSED);
	            ticketRepository.save(ticket);
	            System.out.println("🟢 Ticket closed");

	            // 🔹 6. Send mail to CUSTOMER (not assigned user)
	            if (ticket.getCustomer() != null) {
	            	emailService.sendTicketClosedMail(ticket);

	            } else {
	                System.out.println("❌ Customer is NULL — ticket data issue");
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
