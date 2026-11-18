package com.scms.backend.controller;

import com.scms.backend.model.Student;
import com.scms.backend.security.JwtUtil;
import com.scms.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        try {
            Student savedStudent = studentService.registerStudent(student);
            return ResponseEntity.ok(Map.of(
                    "id", savedStudent.getId(),
                    "name", savedStudent.getName(),
                    "email", savedStudent.getEmail(),
                    "department", savedStudent.getDepartment()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            Student student = studentService.loginStudent(email, password);

            String token = jwtUtil.generateToken(student.getEmail());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "name", student.getName(),
                    "email", student.getEmail(),
                    "department", student.getDepartment()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}