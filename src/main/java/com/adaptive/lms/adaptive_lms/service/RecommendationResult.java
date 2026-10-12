package com.adaptive.lms.adaptive_lms.service;

import com.adaptive.lms.adaptive_lms.entity.Lesson;

public class RecommendationResult {

    private final int currentScore;
    private final double historicalAverage;
    private final int attemptCount;
    private final double adjustedScore;
    private final double compositeScore;
    private final int recommendedDifficulty;
    private final Lesson recommendedLesson;

    public RecommendationResult(
            int currentScore,
            double historicalAverage,
            int attemptCount,
            double adjustedScore,
            double compositeScore,
            int recommendedDifficulty,
            Lesson recommendedLesson) {

        this.currentScore = currentScore;
        this.historicalAverage = historicalAverage;
        this.attemptCount = attemptCount;
        this.adjustedScore = adjustedScore;
        this.compositeScore = compositeScore;
        this.recommendedDifficulty = recommendedDifficulty;
        this.recommendedLesson = recommendedLesson;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public double getHistoricalAverage() {
        return historicalAverage;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public double getAdjustedScore() {
        return adjustedScore;
    }

    public double getCompositeScore() {
        return compositeScore;
    }

    public int getRecommendedDifficulty() {
        return recommendedDifficulty;
    }

    public Lesson getRecommendedLesson() {
        return recommendedLesson;
    }
}