package com.pm.formfitbackend.profile.dto;

public class MonthlyStatsAggregate {

    private Long workoutCount;
    private Integer totalDurationMinutes;
    private Double totalVolume;
    private Long totalSets;

    public MonthlyStatsAggregate(Long workoutCount, Integer totalDurationMinutes,
                                 Double totalVolume, Long totalSets) {
        this.workoutCount = workoutCount;
        this.totalDurationMinutes = totalDurationMinutes;
        this.totalVolume = totalVolume;
        this.totalSets = totalSets;
    }

    public Long getWorkoutCount() {
        return workoutCount;
    }

    public Integer getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public Long getTotalSets() {
        return totalSets;
    }
}