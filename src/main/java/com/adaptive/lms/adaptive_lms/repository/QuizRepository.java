package com.adaptive.lms.adaptive_lms.repository;

import com.adaptive.lms.adaptive_lms.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizRepository
        extends JpaRepository<Quiz, Long> {

    List<Quiz> findByLesson_IdOrderByIdAsc(Long lessonId);

    @Query("""
        SELECT q
        FROM Quiz q
        JOIN FETCH q.lesson l
        JOIN FETCH l.topic t
        JOIN FETCH t.course
        WHERE q.id = :quizId
        """)
    Optional<Quiz> findDetailedById(
        @Param("quizId") Long quizId
    );

    @Query("""
        SELECT q
        FROM Quiz q
        JOIN FETCH q.lesson l
        JOIN FETCH l.topic
        ORDER BY q.id
        """)
    List<Quiz> findAllWithLessonAndTopic();
}