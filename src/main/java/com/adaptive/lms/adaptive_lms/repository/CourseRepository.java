package com.adaptive.lms.adaptive_lms.repository;

import com.adaptive.lms.adaptive_lms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}