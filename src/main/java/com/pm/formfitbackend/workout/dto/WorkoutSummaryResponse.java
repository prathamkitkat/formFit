package com.pm.formfitbackend.workout.dto;

import java.time.LocalDateTime;

public class WorkoutSummaryResponse {

    private Long id;
    private String name;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer duration;  // in minutes
    private Integer exerciseCount;
    private Integer totalSets;

    public WorkoutSummaryResponse() {
    }

    public WorkoutSummaryResponse(Long id, String name, String status, LocalDateTime startedAt,
                                  LocalDateTime endedAt, Integer duration, Integer exerciseCount,
                                  Integer totalSets) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.duration = duration;
        this.exerciseCount = exerciseCount;
        this.totalSets = totalSets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(Integer exerciseCount) {
        this.exerciseCount = exerciseCount;
    }

    public Integer getTotalSets() {
        return totalSets;
    }

    public void setTotalSets(Integer totalSets) {
        this.totalSets = totalSets;
    }
}