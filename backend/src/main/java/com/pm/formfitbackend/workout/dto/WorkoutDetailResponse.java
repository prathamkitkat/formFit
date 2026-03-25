package com.pm.formfitbackend.workout.dto;

import java.time.LocalDateTime;
import java.util.List;

public class WorkoutDetailResponse {

    private Long id;
    private String name;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer duration;  // Workout duration in minutes
    private List<WorkoutExerciseResponse> exercises;

    public WorkoutDetailResponse() {
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

    public List<WorkoutExerciseResponse> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExerciseResponse> exercises) {
        this.exercises = exercises;
    }
}