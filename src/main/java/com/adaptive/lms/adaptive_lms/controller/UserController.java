package com.adaptive.lms.adaptive_lms.controller;

import com.adaptive.lms.adaptive_lms.entity.User;
import com.adaptive.lms.adaptive_lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") // All URLs in this class start with this
public class UserController {

    @Autowired
    private UserService userService;

    // This method listens for POST requests to /api/users/register
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }
}