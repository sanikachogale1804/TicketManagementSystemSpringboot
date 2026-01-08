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

	        // 1️⃣ Fetch ticket
	        Ticket ticket = ticketRepository.findById(comment.getTicket().getTicketId())
	                .orElseThrow(() -> new RuntimeException("Ticket not found"));

	        // 2️⃣ Fetch user
	        User user = userRepository.findById(comment.getUser().getUserId())
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        comment.setTicket(ticket);
	        comment.setUser(user);

	        // 3️⃣ Save comment
	        Comment savedComment = commentRepository.save(comment);
	        System.out.println("✅ Comment saved in DB");

	        // 4️⃣ Close ticket if requested
	        if (closeTicket) {
	            ticket.setStatus(Ticket.Status.CLOSED);
	            ticket.setEndDate(LocalDateTime.now());
	            ticketRepository.save(ticket);

	            System.out.println("🟢 Ticket closed: " + ticket.getTicketId());

	            // 5️⃣ Send mail to customer
	            User customer = ticket.getCustomer();
	            if (customer != null && customer.getUserEmail() != null) {
	                System.out.println("📧 Sending CLOSED mail to " + customer.getUserEmail());
	                emailService.sendTicketClosedMail(customer.getUserEmail(), ticket);
	            } else {
	                System.out.println("❌ Customer email missing!");
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
