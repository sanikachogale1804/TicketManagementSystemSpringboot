package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Comment;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.CommentRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.TicketRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.UserRepository;

@RestController
@RequestMapping("/tickets")
public class CommentController {
	
	private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public CommentController(CommentRepository commentRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }
    
    @PostMapping("/{ticketId}/comments")
    public ResponseEntity<?> addComment(@PathVariable int ticketId, @RequestBody Comment commentRequest) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);

        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();

            // ✅ User find karna hoga (Assuming userId is coming from request)
            Optional<User> userOptional = userRepository.findById(commentRequest.getUser().getUserId());

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // ✅ New Comment Object create karo
                Comment comment = new Comment();
                comment.setTicket(ticket);
                comment.setUser(user);
                comment.setComment(commentRequest.getComment());
                comment.setCreatedAt(LocalDateTime.now());

                // ✅ Comment Save karo
                Comment savedComment = commentRepository.save(comment);
                return ResponseEntity.ok(savedComment);
            } else {
                return ResponseEntity.badRequest().body("User not found!");
            }
        }

        return ResponseEntity.badRequest().body("Ticket not found!");
    }
}
