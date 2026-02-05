package com.adaptive.lms.adaptive_lms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    // This is key for the "Adaptive" part: 1 = Easy, 2 = Medium, 3 = Hard
    private int difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Lesson() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}