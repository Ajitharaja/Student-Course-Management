package com.scms.backend.repository;

import com.scms.backend.model.Course;
import com.scms.backend.model.Enrollment;
import com.scms.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentAndCourse(Student student, Course course);
    List<Enrollment> findByStudent(Student student);
}