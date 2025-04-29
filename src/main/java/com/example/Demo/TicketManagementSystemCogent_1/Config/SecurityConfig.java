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
	    private JWTFilter jwtFilter;

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        return http
	            .csrf(csrf -> csrf.disable())  // Disable CSRF since JWT is being used
	            .authorizeRequests(authz -> authz
	            		.requestMatchers("/register", "/login").permitAll()
	            		.requestMatchers("/auth/me").permitAll()  // ✅ Auth API public access
		                .requestMatchers("/users", "/tickets", "/comments").permitAll()
		                .requestMatchers("/tickets/**").permitAll() // Allow specific ticket endpoints
	                    .requestMatchers("/users/**").permitAll() // Allow specific user endpoints
	                    .requestMatchers("/comments/**").permitAll() // Allow specific comment endpoints
		                .requestMatchers("/camera-reports/**").permitAll()
		                .requestMatchers("/siteMasterData/**").permitAll()
		                .requestMatchers("/admin/**").hasRole("ADMIN")
		                .requestMatchers("/user/**").hasRole("USER")
		                .requestMatchers("/team/**").hasRole("TEAMMEMBER")
		                .anyRequest().authenticated()
	            )
	            .httpBasic(Customizer.withDefaults())  // Optional: Basic authentication for debugging
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session
	            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  // Add JWT filter
	            .cors().and()  // Add CORS configuration
	            .build();
	    }

	    @Bean
	    public AuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));  // Use BCrypt for password encoding
	        provider.setUserDetailsService(userDetailsService);  // Set the UserDetailsService for the provider
	        return provider;
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }
}
