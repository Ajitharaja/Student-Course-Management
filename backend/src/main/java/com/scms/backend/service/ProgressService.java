package com.scms.backend.service;

import com.scms.backend.model.Course;
import com.scms.backend.model.Progress;
import com.scms.backend.model.Student;
import com.scms.backend.repository.CourseRepository;
import com.scms.backend.repository.ProgressRepository;
import com.scms.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProgressService {

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Progress> getMyProgress(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return progressRepository.findByStudent(student);
    }

    @Transactional
    public Progress completeModule(String email, Long courseId) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Progress progress = progressRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

        int total = course.getTotalModules() == null ? 6 : course.getTotalModules();

        if (progress.getCompletedModules() >= total) {
            throw new RuntimeException("All modules already completed");
        }

        int done = progress.getCompletedModules() + 1;
        progress.setCompletedModules(done);
        progress.setProgressPercentage(Math.round(done * 100.0 / total * 10) / 10.0);

        return progressRepository.save(progress);
    }
}