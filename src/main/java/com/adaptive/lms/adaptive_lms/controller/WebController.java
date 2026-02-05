package com.adaptive.lms.adaptive_lms.controller;

import com.adaptive.lms.adaptive_lms.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // IMPORTANT
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping("/lessons")
    public String showLessons(Model model) {
        // This sends the data to the HTML file
        model.addAttribute("lessons", lessonRepository.findAll());
        // This looks for "lessons.html" in the templates folder
        return "lessons";
    }
}