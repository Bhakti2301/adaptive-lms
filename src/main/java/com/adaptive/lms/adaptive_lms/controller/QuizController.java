package com.adaptive.lms.adaptive_lms.controller;

import com.adaptive.lms.adaptive_lms.entity.Lesson;
import com.adaptive.lms.adaptive_lms.service.CourseService;
import com.adaptive.lms.adaptive_lms.service.RecommendationResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/submit")
    public Map<String, Object> submitQuiz(
            @RequestParam Long quizId,
            @RequestParam int score,
            Principal principal) {

        RecommendationResult result =
            courseService.submitQuizAndRecommend(
                principal.getName(),
                quizId,
                score
            );

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("currentScore", result.getCurrentScore());
        response.put(
            "historicalAverage",
            result.getHistoricalAverage()
        );
        response.put("attemptCount", result.getAttemptCount());
        response.put("adjustedScore", result.getAdjustedScore());
        response.put("compositeScore", result.getCompositeScore());
        response.put(
            "recommendedDifficulty",
            result.getRecommendedDifficulty()
        );

        Lesson lesson = result.getRecommendedLesson();

        if (lesson != null) {
            response.put(
                "recommendedLesson",
                Map.of(
                    "id", lesson.getId(),
                    "title", lesson.getTitle(),
                    "difficultyLevel", lesson.getDifficultyLevel()
                )
            );
        } else {
            response.put("recommendedLesson", null);
        }

        return response;
    }
}