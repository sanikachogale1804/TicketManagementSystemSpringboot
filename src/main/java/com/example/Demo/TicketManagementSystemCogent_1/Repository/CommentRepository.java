package com.example.Demo.TicketManagementSystemCogent_1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Comment;

@CrossOrigin
public interface CommentRepository extends JpaRepository<Comment, Integer>{

}
