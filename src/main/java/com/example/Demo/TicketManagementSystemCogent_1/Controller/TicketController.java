package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Comment;
import com.example.Demo.TicketManagementSystemCogent_1.Service.TicketService;

@CrossOrigin(origins = "http://localhost:3000")  // React App के लिए CORS allow करें
@RestController
@RequestMapping("/tickets")
public class TicketController {
	
	 @Autowired
	 private TicketService ticketService;
	
	 @GetMapping("/{ticketId}/comments")
	    public ResponseEntity<List<Comment>> getCommentsForTicket(@PathVariable int ticketId) {
	        List<Comment> comments = ticketService.getCommentsForTicket(ticketId);
	        return ResponseEntity.ok(comments);  // Return the comments as the response
	    }
}
