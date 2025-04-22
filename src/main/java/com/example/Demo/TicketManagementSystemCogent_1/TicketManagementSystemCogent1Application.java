package com.example.Demo.TicketManagementSystemCogent_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TicketManagementSystemCogent1Application extends SpringBootServletInitializer{
	
	 @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	        return builder.sources(TicketManagementSystemCogent1Application.class);
	    }

	public static void main(String[] args) {
		SpringApplication.run(TicketManagementSystemCogent1Application.class, args);
	}

}
