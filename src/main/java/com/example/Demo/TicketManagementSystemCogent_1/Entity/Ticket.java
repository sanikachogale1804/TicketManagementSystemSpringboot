package com.example.Demo.TicketManagementSystemCogent_1.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int ticketId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "userId", nullable = false)
    private User customer;  // Customer who created the ticket
    
    @ManyToOne
    @JoinColumn(name = "assigned_to", referencedColumnName = "userId", nullable = true)
    private User assignedTo;  // User to whom the ticket is assigned
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum Status {
        OPEN,
        IN_PROGRESS,
        CLOSED
    }
}
