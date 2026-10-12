package com.adaptive.lms.adaptive_lms.repository;

import com.adaptive.lms.adaptive_lms.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LessonRepository
        extends JpaRepository<Lesson, Long> {

    List<Lesson>
    findByTopic_IdAndDifficultyLevelAndIdNotOrderByIdAsc(
        Long topicId,
        int difficultyLevel,
        Long excludedLessonId
    );

    List<Lesson> findByTopic_IdOrderByIdAsc(Long topicId);

    @Query("""
        SELECT l
        FROM Lesson l
        JOIN FETCH l.topic t
        JOIN FETCH t.course
        ORDER BY l.id
        """)
    List<Lesson> findAllWithTopicAndCourse();
}