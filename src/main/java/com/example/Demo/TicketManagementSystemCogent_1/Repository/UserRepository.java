package com.example.Demo.TicketManagementSystemCogent_1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;
import java.util.Optional;

@CrossOrigin
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String username); // Find user by username
    
   
}

