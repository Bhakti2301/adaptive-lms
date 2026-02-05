package com.adaptive.lms.adaptive_lms.repository;

import com.adaptive.lms.adaptive_lms.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    // This allows us to see the history of a specific student in a specific course
    List<UserProgress> findByUserIdAndCourseId(Long userId, Long courseId);
}