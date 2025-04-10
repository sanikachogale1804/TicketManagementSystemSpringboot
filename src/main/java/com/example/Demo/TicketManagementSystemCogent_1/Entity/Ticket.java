package com.example.Demo.TicketManagementSystemCogent_1.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @JoinColumn(name="user_id",referencedColumnName = "userId", nullable = true)
    private User customer;  // Customer who created the ticket
    
    @ManyToOne
    @JoinColumn(name = "assigned_to", referencedColumnName = "userId", nullable = true)
    private User assignedTo;  // User to whom the ticket is assigned
    
    @Column(nullable = false)
    private String IASSPName;
    
    @Column(nullable = false)
    private String SiteID;
    
    @Column(nullable = false)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Status status;
    
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Column
    private LocalDateTime startDate; 

    @Column
    private LocalDateTime endDate;
    
    @OneToMany(mappedBy = "ticket")
    private List<Comment> comments; 

    public enum Status {
        OPEN,
        IN_PROGRESS,
        CLOSED
    }
}
