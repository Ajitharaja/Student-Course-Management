package com.scms.backend.service;

import com.scms.backend.model.Course;
import com.scms.backend.model.Enrollment;
import com.scms.backend.model.Progress;
import com.scms.backend.model.Student;
import com.scms.backend.repository.CourseRepository;
import com.scms.backend.repository.EnrollmentRepository;
import com.scms.backend.repository.ProgressRepository;
import com.scms.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Enrollment enroll(String email, Long courseId) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new RuntimeException("Already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        Enrollment saved = enrollmentRepository.save(enrollment);

        Progress progress = new Progress();
        progress.setStudent(student);
        progress.setCourse(course);
        progress.setCompletedModules(0);
        progress.setProgressPercentage(0.0);
        progressRepository.save(progress);

        notificationService.sendToUser(
                email,
                "ENROLLMENT",
                "You are enrolled in " + course.getCourseName()
        );

        return saved;
    }

    public List<Enrollment> getMyEnrollments(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return enrollmentRepository.findByStudent(student);
    }
}