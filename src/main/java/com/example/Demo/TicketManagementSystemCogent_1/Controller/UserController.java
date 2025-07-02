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

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }

    @CrossOrigin(origins = {
		    "http://localhost:3000","http://127.0.0.1:3000",
		    "http://192.168.1.91:3000",
		    "http://45.115.186.228:3000"
	 	})
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        String token = service.verify(user);
        if ("fail".equals(token)) {
            return "Authentication failed";
        }
        return token;
    }
}
