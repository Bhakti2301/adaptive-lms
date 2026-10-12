package com.adaptive.lms.adaptive_lms.controller;

import com.adaptive.lms.adaptive_lms.entity.*;
import com.adaptive.lms.adaptive_lms.repository.*;
import com.adaptive.lms.adaptive_lms.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
public class WebController {

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
    private UserService userService;

    @Autowired
    private CourseService courseService;

    // STUDENT: list lessons and the quizzes associated with them.
    @GetMapping("/lessons")
    public String showLessons(Model model) {

        List<Lesson> lessons =
            lessonRepository.findAllWithTopicAndCourse();

        Map<Long, List<Quiz>> quizzesByLesson =
            new HashMap<>();

        for (Lesson lesson : lessons) {
            quizzesByLesson.put(
                lesson.getId(),
                quizRepository.findByLesson_IdOrderByIdAsc(
                    lesson.getId()
                )
            );
        }

        model.addAttribute("lessons", lessons);
        model.addAttribute("quizzesByLesson", quizzesByLesson);

        return "lessons";
    }

    // STUDENT: submit a score through the Thymeleaf workflow.
    @PostMapping("/recommendation")
    public String showRecommendation(
            @RequestParam Long quizId,
            @RequestParam int score,
            Principal principal,
            Model model) {

        RecommendationResult result =
            courseService.submitQuizAndRecommend(
                principal.getName(),
                quizId,
                score
            );

        model.addAttribute("result", result);

        return "recommendation";
    }

    // STUDENT: use the authenticated user, not a URL userId.
    @GetMapping("/dashboard")
    public String showDashboard(
            Principal principal,
            Model model) {

        User currentUser =
            userService.findByUsername(principal.getName());

        List<UserProgress> history =
            userProgressRepository.findDetailedAttemptsByUserId(
                currentUser.getId()
            );

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("progressHistory", history);

        UserProgress latest =
            history.isEmpty() ? null : history.get(0);

        model.addAttribute("latestProgress", latest);

        Lesson recommendation = null;

        if (latest != null) {
            Long topicId = latest.getQuiz()
                                 .getLesson()
                                 .getTopic()
                                 .getId();

            Long currentLessonId = latest.getQuiz()
                                         .getLesson()
                                         .getId();

            recommendation = courseService.recommendNextLesson(
                topicId,
                latest.getCompositeScore(),
                currentLessonId
            );
        }

        model.addAttribute("recommendation", recommendation);

        // Calculate topic mastery from the student's quiz history.
        Map<String, List<Integer>> scoresByTopic =
            new LinkedHashMap<>();

        for (UserProgress attempt : history) {
            String topicTitle = attempt.getQuiz()
                    .getLesson()
                    .getTopic()
                    .getTitle();

            scoresByTopic
                .computeIfAbsent(
                    topicTitle,
                    key -> new ArrayList<>()
                )
                .add(attempt.getScore());
        }

        Map<String, Double> topicMastery =
            new LinkedHashMap<>();

        Map<String, Double> weakTopics =
            new LinkedHashMap<>();

        for (Map.Entry<String, List<Integer>> entry
                : scoresByTopic.entrySet()) {

            double average = entry.getValue().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

            average = Math.round(average * 100.0) / 100.0;

            topicMastery.put(entry.getKey(), average);

            if (average < 60.0) {
                weakTopics.put(entry.getKey(), average);
            }
        }

        model.addAttribute("topicMastery", topicMastery);
        model.addAttribute("weakTopics", weakTopics);

        return "dashboard";
    }

    // STUDENT: keep the existing student dashboard URL compatible.
    @GetMapping("/student/dashboard")
    public String studentDashboard() {
        return "redirect:/dashboard";
    }

    // TEACHER: provide the data needed by the management forms.
    @GetMapping("/teacher/dashboard")
    public String teacherDashboard(Model model) {

        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("topics", topicRepository.findAllWithCourse());
        model.addAttribute(
            "lessons",
            lessonRepository.findAllWithTopicAndCourse()
        );
        model.addAttribute(
            "quizzes",
            quizRepository.findAllWithLessonAndTopic()
        );
        model.addAttribute(
            "progressHistory",
            userProgressRepository.findAllDetailedAttempts()
        );

        return "teacher-dashboard";
    }

    @PostMapping("/teacher/courses")
    public String createCourseFromForm(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            RedirectAttributes attr) {

        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);

        courseService.createCourse(course);

        attr.addFlashAttribute(
            "success", "Course created successfully."
        );

        return "redirect:/teacher/dashboard";
    }

    @PostMapping("/teacher/topics")
    public String createTopicFromForm(
            @RequestParam Long courseId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            RedirectAttributes attr) {

        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setDescription(description);

        courseService.createTopic(courseId, topic);

        attr.addFlashAttribute(
            "success", "Topic created successfully."
        );

        return "redirect:/teacher/dashboard";
    }

    @PostMapping("/teacher/lessons")
    public String createLessonFromForm(
            @RequestParam Long topicId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam int difficultyLevel,
            RedirectAttributes attr) {

        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        lesson.setContent(content);
        lesson.setDifficultyLevel(difficultyLevel);

        courseService.addLessonToTopic(topicId, lesson);

        attr.addFlashAttribute(
            "success", "Lesson created successfully."
        );

        return "redirect:/teacher/dashboard";
    }

    @PostMapping("/teacher/quizzes")
    public String createQuizFromForm(
            @RequestParam Long lessonId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            RedirectAttributes attr) {

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);

        courseService.createQuiz(lessonId, quiz);

        attr.addFlashAttribute(
            "success", "Quiz created successfully."
        );

        return "redirect:/teacher/dashboard";
    }
}