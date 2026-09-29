package com.scms.backend.service;

import com.scms.backend.model.Student;
import com.scms.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student registerStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        return studentRepository.save(student);
    }

    public Student loginStudent(String email, String password) {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);

        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        Student student = studentOpt.get();

        if (!student.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }

        return student;
    }
}