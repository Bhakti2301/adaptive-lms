package com.adaptive.lms.adaptive_lms.repository;

import com.adaptive.lms.adaptive_lms.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProgressRepository
        extends JpaRepository<UserProgress, Long> {

    @Query("""
        SELECT p
        FROM UserProgress p
        WHERE p.user.id = :userId
          AND p.quiz.lesson.topic.id = :topicId
        ORDER BY p.completedAt ASC
        """)
    List<UserProgress> findTopicAttempts(
        @Param("userId") Long userId,
        @Param("topicId") Long topicId
    );

    @Query("""
        SELECT p
        FROM UserProgress p
        JOIN FETCH p.user
        JOIN FETCH p.quiz q
        JOIN FETCH q.lesson l
        JOIN FETCH l.topic t
        JOIN FETCH t.course
        WHERE p.user.id = :userId
        ORDER BY p.completedAt DESC
        """)
    List<UserProgress> findDetailedAttemptsByUserId(
        @Param("userId") Long userId
    );

    @Query("""
        SELECT p
        FROM UserProgress p
        JOIN FETCH p.user
        JOIN FETCH p.quiz q
        JOIN FETCH q.lesson l
        JOIN FETCH l.topic t
        JOIN FETCH t.course
        ORDER BY p.completedAt DESC
        """)
    List<UserProgress> findAllDetailedAttempts();
}