package com.adaptive.lms.adaptive_lms.repository;

import com.adaptive.lms.adaptive_lms.entity.User; // MUST MATCH YOUR FOLDER
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}