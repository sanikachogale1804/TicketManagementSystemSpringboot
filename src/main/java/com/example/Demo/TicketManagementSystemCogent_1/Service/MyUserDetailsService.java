package com.example.Demo.TicketManagementSystemCogent_1.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.UserPrincipal;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from repository
        Optional<User> user = repo.findByUserName(username);

        // If user not found, throw exception
        if (user.isEmpty()) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException(username);
        }

        // Return UserPrincipal with actual user object
        return new UserPrincipal(user.get());
    }
}
