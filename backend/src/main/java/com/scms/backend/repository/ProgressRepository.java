package com.scms.backend.repository;

import com.scms.backend.model.Course;
import com.scms.backend.model.Progress;
import com.scms.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByStudentAndCourse(Student student, Course course);
}