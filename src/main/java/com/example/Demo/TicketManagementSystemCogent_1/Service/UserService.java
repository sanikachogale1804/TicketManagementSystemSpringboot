package com.example.Demo.TicketManagementSystemCogent_1.Service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	private JWTService jwtService;  // Assuming you have a JWT service to generate tokens
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public User register(User user) {
		user.setUserPassword(encoder.encode(user.getUserPassword()));  // Encrypting the password
		return repo.save(user);  // Saving the user in the database
	}

	public String verify(User user) {
		// Verify user credentials
		Authentication authentication = 
				authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getUserPassword()));

		// If authentication is successful, generate a JWT token
		if(authentication.isAuthenticated()) {
			return jwtService.generateToken(user.getUserName());  // Generating the JWT token
		}
		return "fail";  // Return "fail" if authentication fails
	}
	
	public String login(User user) {
	    // Verify user credentials
	    Authentication authentication = 
	            authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getUserPassword()));

	    // If authentication is successful, generate a JWT token
	    if(authentication.isAuthenticated()) {
	        // Convert Role enum to Set of Strings
	        Set<String> roles = Set.of(user.getRole().name());  // Convert role to a set of role names (as String)
	        return jwtService.generateTokenWithRoles(user.getUserName(), roles);  // Generate the JWT token with roles
	    }
	    return "fail";  // Return "fail" if authentication fails
	}
}
