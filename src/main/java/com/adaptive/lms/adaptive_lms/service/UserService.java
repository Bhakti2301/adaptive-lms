package com.adaptive.lms.adaptive_lms.service;

import com.adaptive.lms.adaptive_lms.entity.User;
import com.adaptive.lms.adaptive_lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // The Service coordinates the save operation
        return userRepository.save(user);
    }
}