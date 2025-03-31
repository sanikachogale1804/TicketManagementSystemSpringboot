package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;
import com.example.Demo.TicketManagementSystemCogent_1.Service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService service;

	@CrossOrigin(origins = "http://localhost:3000")  // Allow requests from React frontend
	@PostMapping("/register")
	public User register(@RequestBody User user) {
		return service.register(user);
	}
	
	@CrossOrigin(origins = "http://localhost:3000")  // Allow requests from React frontend
	 @PostMapping("/login")
    public String login(@RequestBody User user) {
        // Login method to authenticate and generate JWT token based on user roles
//		String token=service.login(user);
		String token = service.verify(user);
        if (token.equals("fail")) {
            return "Authentication failed";  // If authentication fails, return error
        }
        return token;  // If login is successful, return the JWT token with roles
    }

}
