package com.example.Demo.TicketManagementSystemCogent_1.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	@JoinColumn(referencedColumnName = "ticketId", name = "ticket_id")
	@JsonIgnoreProperties("comments") // prevents loop
	private Ticket ticket;

	
	@ManyToOne
	@JoinColumn(referencedColumnName = "userId", name = "user_id")
	@JsonIgnoreProperties("comments") // prevents loop
	private User user;
	
	@Column( columnDefinition = "TEXT")
	private String comment;
	
	@Column(nullable = true)
	@CreationTimestamp
    private LocalDateTime createdAt;


}
