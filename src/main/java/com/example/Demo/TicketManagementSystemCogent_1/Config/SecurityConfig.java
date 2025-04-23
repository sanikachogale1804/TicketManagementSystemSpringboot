package com.example.Demo.TicketManagementSystemCogent_1.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTFilter jwtFilter; // Assuming JWTFilter is the class that checks JWT token

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Disable CSRF since you are using JWT
            .authorizeRequests(authz -> authz
                    .requestMatchers("/register", "/login").permitAll() // Allow register and login endpoints
                    .requestMatchers("/auth/me","/camera-reports").permitAll()  // ✅ Auth API public access
                    .requestMatchers("/users", "/tickets","/comments").permitAll() // Allow users and tickets endpoints without authentication
                    .requestMatchers("/tickets/**").permitAll() // Allow specific ticket endpoints
                    .requestMatchers("/users/**").permitAll() // Allow specific user endpoints
                    .requestMatchers("/comments/**").permitAll() // Allow specific comment endpoints
                    .requestMatchers("/camera-reports/**").permitAll()
                    .requestMatchers("/siteMasterData/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN") // Only allow ADMIN role for admin endpoints
                    .requestMatchers("/user/**").hasRole("USER") // Only allow USER role for user endpoints
                    .requestMatchers("/team/**").hasRole("TEAMMEMBER") // Grant access to TEAMMEMBER role
             
                    .anyRequest().authenticated() // Require authentication for other requests
                )
            .httpBasic(Customizer.withDefaults())  // Basic authentication for debugging
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  // Add JWT filter
            .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12)); // Use BCrypt for password encoding
        provider.setUserDetailsService(userDetailsService); // Set the UserDetailsService for the provider
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
