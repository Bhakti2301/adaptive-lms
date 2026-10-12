package com.adaptive.lms.adaptive_lms.service;

import com.adaptive.lms.adaptive_lms.entity.*;
import com.adaptive.lms.adaptive_lms.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private UserRepository userRepository;

    public Course createCourse(Course course) {
        if (course.getTitle() == null
                || course.getTitle().isBlank()) {
            throw new IllegalArgumentException(
                "Course title is required"
            );
        }

        return courseRepository.save(course);
    }

    public Topic createTopic(Long courseId, Topic topic) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Course not found: " + courseId
                )
            );

        if (topic.getTitle() == null
                || topic.getTitle().isBlank()) {
            throw new IllegalArgumentException(
                "Topic title is required"
            );
        }

        topic.setCourse(course);
        return topicRepository.save(topic);
    }

    public Lesson addLessonToTopic(
            Long topicId, Lesson lesson) {

        Topic topic = topicRepository.findById(topicId)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Topic not found: " + topicId
                )
            );

        if (lesson.getTitle() == null
                || lesson.getTitle().isBlank()) {
            throw new IllegalArgumentException(
                "Lesson title is required"
            );
        }

        if (lesson.getDifficultyLevel() < 1
                || lesson.getDifficultyLevel() > 3) {
            throw new IllegalArgumentException(
                "Difficulty must be 1, 2, or 3"
            );
        }

        lesson.setTopic(topic);
        return lessonRepository.save(lesson);
    }

    public Quiz createQuiz(Long lessonId, Quiz quiz) {
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Lesson not found: " + lessonId
                )
            );

        if (quiz.getTitle() == null
                || quiz.getTitle().isBlank()) {
            throw new IllegalArgumentException(
                "Quiz title is required"
            );
        }

        quiz.setLesson(lesson);
        return quizRepository.save(quiz);
    }

    public RecommendationResult submitQuizAndRecommend(
            String username,
            Long quizId,
            int score) {

        if (score < 0 || score > 100) {
            throw new IllegalArgumentException(
                "Quiz score must be between 0 and 100"
            );
        }

        User user = userRepository.findByUsername(username)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "User not found"
                )
            );

        Quiz quiz = quizRepository.findDetailedById(quizId)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Quiz not found: " + quizId
                )
            );

        Long topicId = quiz.getLesson()
                           .getTopic()
                           .getId();

        // Fetch attempts before recording the current one.
        List<UserProgress> previousAttempts =
            userProgressRepository.findTopicAttempts(
                user.getId(), topicId
            );

        double historicalAverage;

        if (previousAttempts.isEmpty()) {
            // First attempt: use the current score as baseline.
            historicalAverage = score;
        } else {
            historicalAverage = previousAttempts.stream()
                .mapToInt(UserProgress::getScore)
                .average()
                .orElse(score);
        }

        int attemptCount = previousAttempts.size() + 1;

        // Ten-point penalty for each earlier attempt.
        double adjustedScore = Math.max(
            0,
            score - 10.0 * previousAttempts.size()
        );

        double compositeScore =
                0.50 * score
              + 0.30 * historicalAverage
              + 0.20 * adjustedScore;

        compositeScore = Math.round(
            compositeScore * 100.0
        ) / 100.0;

        // Persist the current attempt and its calculation.
        UserProgress progress = new UserProgress();
        progress.setUser(user);
        progress.setQuiz(quiz);
        progress.setScore(score);
        progress.setHistoricalAverage(historicalAverage);
        progress.setAttemptCount(attemptCount);
        progress.setCompositeScore(compositeScore);
        progress.setCompletedAt(LocalDateTime.now());

        userProgressRepository.save(progress);

        int targetLevel = getDifficultyLevel(compositeScore);

        Lesson recommended = recommendNextLesson(
            topicId,
            compositeScore,
            quiz.getLesson().getId()
        );

        return new RecommendationResult(
            score,
            historicalAverage,
            attemptCount,
            adjustedScore,
            compositeScore,
            targetLevel,
            recommended
        );
    }

    @Transactional(readOnly = true)
    public Lesson recommendNextLesson(
            Long topicId,
            double compositeScore,
            Long currentLessonId) {

        int targetLevel = getDifficultyLevel(compositeScore);

        return lessonRepository
            .findByTopic_IdAndDifficultyLevelAndIdNotOrderByIdAsc(
                topicId,
                targetLevel,
                currentLessonId
            )
            .stream()
            .findFirst()
            .orElse(null);
    }

    private int getDifficultyLevel(double compositeScore) {
        if (compositeScore >= 80) {
            return 3;
        } else if (compositeScore >= 50) {
            return 2;
        } else {
            return 1;
        }
    }
}