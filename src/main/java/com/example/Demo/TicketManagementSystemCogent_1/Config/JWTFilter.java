package com.example.Demo.TicketManagementSystemCogent_1.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Demo.TicketManagementSystemCogent_1.Service.JWTService;
import com.example.Demo.TicketManagementSystemCogent_1.Service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private MyUserDetailsService myUserDetailsService; // Injecting directly instead of ApplicationContext

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");  // Get Authorization header
        String token = null;
        String username = null;

        // Check if Authorization header exists and starts with "Bearer"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract token
            username = jwtService.extractUserName(token); // Extract username from the token
        }

        // If token is valid and username is not null, proceed with authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load the user details using the username
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

            // Validate the token
            if (jwtService.validateToken(token, userDetails)) {
                // Extract roles from the token
                List<String> roles = jwtService.extractRoles(token); // Extract roles from the JWT token

                // Create an authentication token and set it in the security context
                UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                // Adding roles as authorities (in case you're using role-based access control)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication context with the user's authorities (roles)
                SecurityContextHolder.getContext().setAuthentication(authToken);  // Set the authentication context
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
