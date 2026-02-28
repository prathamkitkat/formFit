package com.pm.formfitbackend.workout.dto;

import java.util.List;

public class WorkoutExerciseResponse {

    private Long id;
    private Long exerciseId;
    private String exerciseName;
    private String imageUrl;
    private Integer orderIndex;
    private String notes;  // Exercise-specific notes
    private List<SetResponse> sets;

    // For pre-loading previous workout data (frontend display only)
    private PreviousPerformance previousPerformance;

    public WorkoutExerciseResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<SetResponse> getSets() {
        return sets;
    }

    public void setSets(List<SetResponse> sets) {
        this.sets = sets;
    }

    public PreviousPerformance getPreviousPerformance() {
        return previousPerformance;
    }

    public void setPreviousPerformance(PreviousPerformance previousPerformance) {
        this.previousPerformance = previousPerformance;
    }
}