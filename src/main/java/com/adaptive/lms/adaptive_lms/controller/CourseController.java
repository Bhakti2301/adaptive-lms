package com.adaptive.lms.adaptive_lms.controller;

import com.adaptive.lms.adaptive_lms.entity.*;
import com.adaptive.lms.adaptive_lms.service.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/courses")
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PostMapping("/topics")
    public Map<String, Object> createTopic(
            @RequestParam Long courseId,
            @RequestBody Topic topic) {

        Topic saved =
            courseService.createTopic(courseId, topic);

        return Map.of(
            "id", saved.getId(),
            "title", saved.getTitle()
        );
    }

    @PostMapping("/lessons")
    public Map<String, Object> addLesson(
            @RequestParam Long topicId,
            @RequestBody Lesson lesson) {

        Lesson saved =
            courseService.addLessonToTopic(topicId, lesson);

        return Map.of(
            "id", saved.getId(),
            "title", saved.getTitle(),
            "difficultyLevel", saved.getDifficultyLevel()
        );
    }

    @PostMapping("/quizzes")
    public Map<String, Object> createQuiz(
            @RequestParam Long lessonId,
            @RequestBody Quiz quiz) {

        Quiz saved =
            courseService.createQuiz(lessonId, quiz);

        return Map.of(
            "id", saved.getId(),
            "title", saved.getTitle()
        );
    }
}