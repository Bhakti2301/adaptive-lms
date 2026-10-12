package com.adaptive.lms.adaptive_lms.repository;

import com.adaptive.lms.adaptive_lms.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicRepository
        extends JpaRepository<Topic, Long> {

    @Query("""
        SELECT t
        FROM Topic t
        JOIN FETCH t.course
        ORDER BY t.id
        """)
    List<Topic> findAllWithCourse();
}