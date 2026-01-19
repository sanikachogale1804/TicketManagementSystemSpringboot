package com.example.Demo.TicketManagementSystemCogent_1.Entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
    
    private User user;
    
    public User getUser() {
        return this.user;
    }
    
    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming user.getRole() gives a valid Role enum
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Adjust as per your needs
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Adjust as per your needs
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Adjust as per your needs
    }

    @Override
    public boolean isEnabled() {
        return true; // Adjust as per your needs
    }
}
