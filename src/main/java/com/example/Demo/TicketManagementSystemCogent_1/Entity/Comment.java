package com.example.Demo.TicketManagementSystemCogent_1.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int commentId;
	
	@ManyToOne
    @JoinColumn(referencedColumnName = "ticketId", nullable = true)
//	@JsonBackReference
	private Ticket ticket;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "userId", nullable = true)
//	@JsonManagedReference
	private User user; 
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String comment;
	
	@Column(nullable = false)
    private LocalDateTime createdAt;

}
