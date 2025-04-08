package com.example.Demo.TicketManagementSystemCogent_1.Service;

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
	private JWTService jwtService;  // JWT service for token generation
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	// ✅ USER REGISTER
	public User register(User user) {
		user.setUserPassword(encoder.encode(user.getUserPassword()));  // Encrypting the password
		return repo.save(user);  // Saving the user in the database
	}

	// ✅ USER VERIFY
	 public String verify(User user) {
	        Authentication authentication = authManager.authenticate(
	            new UsernamePasswordAuthenticationToken(user.getUserName(), user.getUserPassword())
	        );

	        if (authentication.isAuthenticated()) {
	            User dbUser = repo.findByUserName(user.getUserName()).orElse(null);
	            if (dbUser == null) {
	                System.out.println("❌ User not found in DB: " + user.getUserName());
	                return "fail";
	            }

	            // Instead of passing username, userId and roles separately, pass the User object directly
	            String token = jwtService.generateToken(dbUser);  // Pass entire User object to generateToken()

	            System.out.println("🔹 Generated JWT Token: " + token);
	            return token;
	        }
	        return "fail";
	    }

	// ✅ USER LOGIN
	public String login(User user) {
	    Authentication authentication = authManager.authenticate(
	        new UsernamePasswordAuthenticationToken(user.getUserName(), user.getUserPassword())
	    );

	    if (authentication.isAuthenticated()) {
	        User dbUser = repo.findByUserName(user.getUserName()).orElse(null);
	        if (dbUser == null) {
	            System.out.println("❌ User not found during login: " + user.getUserName());
	            return "fail";
	        }

	        Set<String> roles = Set.of(dbUser.getRole().name());  // ✅ Extract roles
	        int userId = dbUser.getUserId();  // ✅ Use int instead of Long

	        // ✅ Generate JWT with userId
	        String token = jwtService.generateToken(dbUser); 
	        System.out.println("🔹 Generated JWT Token (Login): " + token);
	        return token;
	    }
	    return "fail";
	}

}
