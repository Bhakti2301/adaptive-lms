package com.adaptive.lms.adaptive_lms.config;

import com.adaptive.lms.adaptive_lms.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider =
            new DaoAuthenticationProvider();

        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            DaoAuthenticationProvider authProvider)
            throws Exception {

        http
            .authenticationProvider(authProvider)

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/register",
                    "/login",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/error"
                ).permitAll()

                // Teacher dashboard and teacher form submissions.
                .requestMatchers("/teacher/**")
                    .hasRole("TEACHER")

                // Teacher-only content-management APIs.
                .requestMatchers(
                    "/api/courses",
                    "/api/courses/**",
                    "/api/topics",
                    "/api/topics/**",
                    "/api/lessons",
                    "/api/lessons/**",
                    "/api/quizzes",
                    "/api/quizzes/**"
                ).hasRole("TEACHER")

                // Student pages and quiz-submission API.
                .requestMatchers(
                    "/student/**",
                    "/dashboard",
                    "/lessons",
                    "/recommendation",
                    "/api/quiz/**"
                ).hasRole("STUDENT")

                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")

                .successHandler((req, res, auth) -> {

                    boolean isTeacher = auth.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                            a.getAuthority().equals("ROLE_TEACHER"));

                    boolean isStudent = auth.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                            a.getAuthority().equals("ROLE_STUDENT"));

                    if (isTeacher) {
                        res.sendRedirect("/teacher/dashboard");
                    } else if (isStudent) {
                        res.sendRedirect("/dashboard");
                    } else {
                        res.sendError(
                            HttpServletResponse.SC_FORBIDDEN
                        );
                    }
                })

                .failureUrl("/login?error=true")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}