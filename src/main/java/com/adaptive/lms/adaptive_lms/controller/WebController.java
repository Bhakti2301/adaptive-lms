package com.adaptive.lms.adaptive_lms.controller;

import com.adaptive.lms.adaptive_lms.entity.Lesson;
import com.adaptive.lms.adaptive_lms.entity.UserProgress;
import com.adaptive.lms.adaptive_lms.repository.LessonRepository;
import com.adaptive.lms.adaptive_lms.repository.UserProgressRepository;
import com.adaptive.lms.adaptive_lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserProgressRepository userProgressRepository;

    // 1. Show all lessons
    @GetMapping("/lessons")
    public String showLessons(Model model) {
        model.addAttribute("lessons", lessonRepository.findAll());
        return "lessons";
    }

    // 2. Show the recommendation result card
    @GetMapping("/recommendation")
    public String showRecommendation(@RequestParam Long courseId, @RequestParam int score, Model model) {
        Lesson recommended = courseService.recommendNextLesson(courseId, score);
        model.addAttribute("lesson", recommended);
        model.addAttribute("score", score);
        return "recommendation";
    }

    // 3. The Student Dashboard
    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam Long userId, Model model) {
        // Find progress for course 1 (our default Java course)
        List<UserProgress> progressList = userProgressRepository.findByUserIdAndCourseId(userId, 1L);

        if (!progressList.isEmpty()) {
            // Get the most recent quiz result
            UserProgress latest = progressList.get(progressList.size() - 1);
            model.addAttribute("latestProgress", latest);

            // Get a fresh recommendation based on that score
            Lesson nextLesson = courseService.recommendNextLesson(1L, latest.getLastScore());
            model.addAttribute("recommendation", nextLesson);
        }

        return "dashboard";
    }
}