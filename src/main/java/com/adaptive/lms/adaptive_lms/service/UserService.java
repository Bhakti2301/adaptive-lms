
package com.adaptive.lms.adaptive_lms.service;

import com.adaptive.lms.adaptive_lms.entity.User;
import com.adaptive.lms.adaptive_lms.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final Set<String> ALLOWED_ROLES =
            Set.of("STUDENT", "TEACHER");

    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Spring Security calls this during authentication.
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException(
                    "Username is required"
            );
        }

        User user = userRepository
                .findByUsername(username.trim())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username
                        )
                );

        String role = user.getRole();

        if (role == null || role.isBlank()) {
            throw new UsernameNotFoundException(
                    "No role configured for this account"
            );
        }

        // Normalize older values such as "teacher" or
        // "ROLE_TEACHER" to the plain role "TEACHER".
        role = role.trim().toUpperCase(Locale.ROOT);

        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        if (!ALLOWED_ROLES.contains(role)) {
            throw new UsernameNotFoundException(
                    "Unsupported role configured for this account"
            );
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_" + role)
                )
        );
    }

    // Public registration can create student accounts only.
    public User registerStudent(
            String username,
            String password) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Username is required"
            );
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Password is required"
            );
        }

        username = username.trim();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Username already taken"
            );
        }

        User user = new User();
        user.setUsername(username);

        // Never store a plaintext password.
        user.setPassword(passwordEncoder.encode(password));

        // Never accept a privileged role from public registration.
        user.setRole("STUDENT");

        return userRepository.save(user);
    }

    // Retrieve the application's User entity by username.
    public User findByUsername(String username) {

        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException(
                    "Username is required"
            );
        }

        return userRepository
                .findByUsername(username.trim())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        )
                );
    }
}
