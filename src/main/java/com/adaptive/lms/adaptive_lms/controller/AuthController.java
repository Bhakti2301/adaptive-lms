package com.adaptive.lms.adaptive_lms.controller;

import com.adaptive.lms.adaptive_lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support
        .RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

 @PostMapping("/register")
public String register(
        @RequestParam String username,
        @RequestParam String password,
        RedirectAttributes attr) {

    try {
        userService.registerStudent(username, password);

        attr.addFlashAttribute(
            "success",
            "Account created! Please log in."
        );

        return "redirect:/login";

    } catch (IllegalArgumentException e) {
        attr.addFlashAttribute("error", e.getMessage());
        return "redirect:/register";
    }
}
}