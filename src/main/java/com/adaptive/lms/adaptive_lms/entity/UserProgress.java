package com.adaptive.lms.adaptive_lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "user_progress",
    indexes = {
        @Index(
            name = "idx_progress_user_completed",
            columnList = "user_id, completed_at"
        ),
        @Index(
            name = "idx_progress_user_quiz",
            columnList = "user_id, quiz_id"
        )
    }
)
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    // Raw score for this particular attempt: 0 to 100.
    @Column(nullable = false)
    private int score;

    // Average of earlier attempts on this topic.
    @Column(nullable = false)
    private double historicalAverage;

    // Number of attempts on this topic, including this attempt.
    @Column(nullable = false)
    private int attemptCount;

    // The final weighted score used for recommendations.
    @Column(nullable = false)
    private double compositeScore;

    @Column(nullable = false)
    private LocalDateTime completedAt;

    public UserProgress() {}

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getHistoricalAverage() {
        return historicalAverage;
    }

    public void setHistoricalAverage(double historicalAverage) {
        this.historicalAverage = historicalAverage;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public double getCompositeScore() {
        return compositeScore;
    }

    public void setCompositeScore(double compositeScore) {
        this.compositeScore = compositeScore;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}