package com.pm.formfitbackend.workout.dto;

import java.time.LocalDateTime;
import java.util.List;

public class WorkoutSummaryResponse {

    private Long id;
    private String name;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer duration; // in minutes
    private Integer exerciseCount;
    private Integer totalSets;
    private Double totalVolume; // sum of weight * reps across all sets
    private Integer recordsCount;
    private List<ExerciseSetSummary> exerciseSummaries; // first 3 exercises only

    // --- Inner class ---
    public static class ExerciseSetSummary {
        private String exerciseName;
        private Integer setCount;
        private String imageUrl;

        public ExerciseSetSummary() {
        }

        public ExerciseSetSummary(String exerciseName, Integer setCount, String imageUrl) {
            this.exerciseName = exerciseName;
            this.setCount = setCount;
            this.imageUrl = imageUrl;
        }

        public String getExerciseName() {
            return exerciseName;
        }

        public void setExerciseName(String exerciseName) {
            this.exerciseName = exerciseName;
        }

        public Integer getSetCount() {
            return setCount;
        }

        public void setSetCount(Integer setCount) {
            this.setCount = setCount;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    // --- Constructors ---
    public WorkoutSummaryResponse() {
    }

    public WorkoutSummaryResponse(Long id, String name, String status, LocalDateTime startedAt,
            LocalDateTime endedAt, Integer duration, Integer exerciseCount,
            Integer totalSets, Double totalVolume, Integer recordsCount,
            List<ExerciseSetSummary> exerciseSummaries) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.duration = duration;
        this.exerciseCount = exerciseCount;
        this.totalSets = totalSets;
        this.totalVolume = totalVolume;
        this.recordsCount = recordsCount;
        this.exerciseSummaries = exerciseSummaries;
    }

    // --- Getters & Setters ---
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

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public List<ExerciseSetSummary> getExerciseSummaries() {
        return exerciseSummaries;
    }

    public void setExerciseSummaries(List<ExerciseSetSummary> exerciseSummaries) {
        this.exerciseSummaries = exerciseSummaries;
    }

    public Integer getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(Integer recordsCount) {
        this.recordsCount = recordsCount;
    }
}