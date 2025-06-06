package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Comment;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.CommentRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.TicketRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.UserRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Service.CommentService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestBody Comment comment) {
        // Log the received comment details
        System.out.println("Received comment: " + comment.getComment());
        System.out.println("Ticket ID: " + comment.getTicket().getTicketId());
        System.out.println("User ID: " + comment.getUser().getUserId());

        // Fetch the ticket and user from the database
        Ticket ticket = ticketRepository.findById(comment.getTicket().getTicketId()).orElse(null);
        User user = userRepository.findById(comment.getUser().getUserId()).orElse(null);

        // Log the fetched ticket and user for debugging
        System.out.println("Fetched Ticket: " + ticket);
        System.out.println("Fetched User: " + user);

        if (ticket != null && user != null) {
            comment.setTicket(ticket);
            comment.setUser(user);

            // Save the comment
            Comment savedComment = commentService.saveComment(comment);
            return ResponseEntity.ok(savedComment);
        } else {
            // If either the ticket or user is invalid, return a bad request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ticketId or userId.");
        }
    }
    
    @PutMapping("/{commentId}/ticket")
    public ResponseEntity<?> assignTicketToComment(
            @PathVariable Integer commentId,
            @RequestBody Integer ticketId) {

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        if (optionalComment.isEmpty() || optionalTicket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Comment comment = optionalComment.get();
        comment.setTicket(optionalTicket.get());
        commentRepository.save(comment);

        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
