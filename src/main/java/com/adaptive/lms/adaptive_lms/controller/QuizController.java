package com.adaptive.lms.adaptive_lms.controller;

import com.adaptive.lms.adaptive_lms.entity.Lesson;
import com.adaptive.lms.adaptive_lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private CourseService courseService;

    // Simulate submitting a score and getting a recommendation
    @GetMapping("/recommend")
    public Lesson getRecommendation(@RequestParam Long courseId, @RequestParam int score) {
        return courseService.recommendNextLesson(courseId, score);
    }
}