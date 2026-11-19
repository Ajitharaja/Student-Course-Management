package com.scms.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotBlank(message = "Instructor is required")
    private String instructor;

    private int duration;

    private String description;

    private String category;

    @Column(columnDefinition = "integer default 6")
    private Integer totalModules = 6;
}