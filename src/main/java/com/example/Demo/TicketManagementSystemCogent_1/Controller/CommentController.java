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
import com.example.Demo.TicketManagementSystemCogent_1.Service.EmailService;

import java.time.LocalDateTime;
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
    
    @Autowired
    private EmailService emailService;

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(
            @RequestBody Map<String, Object> body,
            @RequestParam(defaultValue = "false") boolean closeTicket
    ) {
        System.out.println("✅ addComment called");
        System.out.println("Body = " + body);

        Integer ticketId = (Integer) ((Map<?, ?>) body.get("ticket")).get("ticketId");
        Integer userId   = (Integer) ((Map<?, ?>) body.get("user")).get("userId");
        String text      = (String) body.get("comment");

        if (ticketId == null || userId == null) {
            return ResponseEntity.badRequest().body("Ticket or User is missing!");
        }

        Comment comment = new Comment();
        comment.setComment(text);
        comment.setCreatedAt(LocalDateTime.now());

        Ticket ticket = new Ticket();
        ticket.setTicketId(ticketId);

        User user = new User();
        user.setUserId(userId);

        comment.setTicket(ticket);
        comment.setUser(user);

        // 🔥 THIS CALL TRIGGERS EMAIL
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
