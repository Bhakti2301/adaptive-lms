package com.adaptive.lms.adaptive_lms.controller;

import com.adaptive.lms.adaptive_lms.entity.Course;
import com.adaptive.lms.adaptive_lms.entity.Lesson;
import com.adaptive.lms.adaptive_lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PostMapping("/lessons")
    public Lesson addLesson(@RequestBody Lesson lesson) {
        return courseService.addLessonToCourse(lesson);
    }
}