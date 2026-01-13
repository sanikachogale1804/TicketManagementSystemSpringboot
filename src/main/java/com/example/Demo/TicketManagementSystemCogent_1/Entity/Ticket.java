package com.example.Demo.TicketManagementSystemCogent_1.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int ticketId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User customer;

    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to", referencedColumnName = "userId")
    private User assignedTo;

    
    @Column(nullable = true)
    private String IASSPName;
    
    @Column(nullable = true)
    @JsonProperty("siteID")
    private String siteId;
    
    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private String district;

    
    @Column(nullable = true)
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
    @JsonIgnore
    private List<Comment> comments; 

    public enum Status {
        OPEN,
        IN_PROGRESS,
        CLOSED
    }
}
