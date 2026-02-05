package com.adaptive.lms.adaptive_lms.repository;

import com.adaptive.lms.adaptive_lms.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    // This will be vital for the "Adaptive" engine later:
    List<Lesson> findByCourseIdAndDifficultyLevel(Long courseId, int difficultyLevel);
}