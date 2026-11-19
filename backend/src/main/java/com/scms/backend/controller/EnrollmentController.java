package com.scms.backend.controller;

import com.scms.backend.model.Enrollment;
import com.scms.backend.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/{courseId}")
    public ResponseEntity<?> enroll(@PathVariable Long courseId, Authentication authentication) {
        try {
            Enrollment enrollment = enrollmentService.enroll(authentication.getName(), courseId);
            return ResponseEntity.ok(Map.of(
                    "message", "Enrolled successfully",
                    "courseId", enrollment.getCourse().getId(),
                    "courseName", enrollment.getCourse().getCourseName(),
                    "enrollmentDate", enrollment.getEnrollmentDate().toString()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> myEnrollments(Authentication authentication) {
        try {
            List<Map<String, Object>> result = enrollmentService
                    .getMyEnrollments(authentication.getName())
                    .stream()
                    .map(e -> Map.<String, Object>of(
                            "enrollmentId", e.getId(),
                            "enrollmentDate", e.getEnrollmentDate().toString(),
                            "course", e.getCourse()
                    ))
                    .toList();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}