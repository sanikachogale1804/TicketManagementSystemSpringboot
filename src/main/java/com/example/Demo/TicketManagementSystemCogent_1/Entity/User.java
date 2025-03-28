package com.example.Demo.TicketManagementSystemCogent_1.Entity;

import java.util.List;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int userId;
    
    @Column(nullable = false, unique = true)
    private String userName;
    
    @Column(nullable = false)
    private String userPassword;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, unique = true)
    private String userEmail;
    
    @OneToMany(mappedBy = "assignedTo")
    private List<Ticket> assignedTickets; // List of tickets assigned to this user
    
    // Enum for User Role
    public enum Role {
        CUSTOMER,
        ADMIN,
        TEAMMEMBER;
    }
}
