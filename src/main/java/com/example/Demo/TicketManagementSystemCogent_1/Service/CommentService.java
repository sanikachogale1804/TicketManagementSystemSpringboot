package com.example.Demo.TicketManagementSystemCogent_1.Service;

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
	    
	    public Comment saveComment(Comment comment) {
	        // Fetch the ticket and user based on ticketId and userId
	        Ticket ticket = ticketRepository.findById(comment.getTicket().getTicketId())
	                .orElseThrow(() -> new RuntimeException("Ticket not found"));

	        User user = userRepository.findById(comment.getUser().getUserId())
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        // Set the ticket and user to the comment
	        comment.setTicket(ticket);
	        comment.setUser(user);

	        // Save the comment
	        return commentRepository.save(comment);
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
