package com.scms.backend.controller;

import com.scms.backend.model.Progress;
import com.scms.backend.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping("/my")
    public ResponseEntity<?> myProgress(Authentication authentication) {
        try {
            List<Map<String, Object>> result = progressService
                    .getMyProgress(authentication.getName())
                    .stream()
                    .map(this::toResponse)
                    .toList();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{courseId}/complete")
    public ResponseEntity<?> completeModule(@PathVariable Long courseId, Authentication authentication) {
        try {
            Progress updated = progressService.completeModule(authentication.getName(), courseId);
            return ResponseEntity.ok(toResponse(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private Map<String, Object> toResponse(Progress p) {
        Integer total = p.getCourse().getTotalModules();
        return Map.<String, Object>of(
                "courseId", p.getCourse().getId(),
                "courseName", p.getCourse().getCourseName(),
                "completedModules", p.getCompletedModules(),
                "totalModules", total == null ? 6 : total,
                "progressPercentage", p.getProgressPercentage()
        );
    }
}