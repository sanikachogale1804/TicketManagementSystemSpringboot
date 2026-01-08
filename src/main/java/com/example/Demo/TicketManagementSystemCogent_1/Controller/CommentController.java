package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
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
    public ResponseEntity<?> addComment(
            @RequestBody Comment comment,
            @RequestParam(defaultValue = "false") boolean closeTicket
    ) {
        if (comment.getTicket() == null || comment.getUser() == null) {
            System.out.println("❌ ticket or user is null in request body!");
            return ResponseEntity.badRequest().body("Ticket or User is missing in request body!");
        }
        Comment savedComment = commentService.saveComment(comment, closeTicket);
        return ResponseEntity.ok(savedComment);
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
