package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Student;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class StudentController {

    // List of students with proper constructor usage
    private List<Student> students = new ArrayList<Student>(List.of(
        new Student(1, "Sanika", 89),
        new Student(2, "Sneha", 75)
    ));

    // Endpoint to get the list of students
    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    }
    
    // Endpoint to get the CSRF token
    @GetMapping("/Csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");  // Corrected line
    }
    
    // Endpoint to add a new student
    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        students.add(student);
        return student;
    }
}
