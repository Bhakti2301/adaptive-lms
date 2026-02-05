package com.adaptive.lms.adaptive_lms.service;

import com.adaptive.lms.adaptive_lms.entity.Course;
import com.adaptive.lms.adaptive_lms.entity.Lesson;
import com.adaptive.lms.adaptive_lms.entity.User;
import com.adaptive.lms.adaptive_lms.entity.UserProgress;
import com.adaptive.lms.adaptive_lms.repository.CourseRepository;
import com.adaptive.lms.adaptive_lms.repository.LessonRepository;
import com.adaptive.lms.adaptive_lms.repository.UserProgressRepository;
import com.adaptive.lms.adaptive_lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private UserRepository userRepository;

    // Save a new course
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    // Save a new lesson
    public Lesson addLessonToCourse(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    // This is for the Adaptive Engine: Get lessons of a specific difficulty
    public List<Lesson> getLessonsByDifficulty(Long courseId, int level) {
        return lessonRepository.findByCourseIdAndDifficultyLevel(courseId, level);
    }

    // Logic to save the student's quiz result
    public UserProgress saveProgress(Long userId, Long courseId, int score) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found."));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course with ID " + courseId + " not found."));

        UserProgress progress = new UserProgress();
        progress.setUser(user);
        progress.setCourse(course);
        progress.setLastScore(score);
        progress.setCompletedAt(LocalDateTime.now());

        return userProgressRepository.save(progress);
    }

    public Lesson recommendNextLesson(Long courseId, int previousScore) {
        // 1. VALIDATION: Check if the course actually exists
        courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course with ID " + courseId + " not found in our database."));

        // 2. ADAPTIVE LOGIC: Determine the next difficulty tier
        int nextLevel;
        if (previousScore >= 80) {
            nextLevel = 3;
        } else if (previousScore >= 50) {
            nextLevel = 2;
        } else {
            nextLevel = 1;
        }

        // 3. DATABASE RETRIEVAL: Fetch the appropriate lesson
        List<Lesson> recommended = lessonRepository.findByCourseIdAndDifficultyLevel(courseId, nextLevel);

        // 4. FALLBACK: Return the lesson
        return recommended.isEmpty() ? null : recommended.get(0);
    }
}