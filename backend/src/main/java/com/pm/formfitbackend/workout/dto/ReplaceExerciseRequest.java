package com.pm.formfitbackend.workout.dto;

import jakarta.validation.constraints.NotNull;

public class ReplaceExerciseRequest {

    @NotNull(message = "New exercise ID is required")
    private Long newExerciseId;

    public ReplaceExerciseRequest() {
    }

    public Long getNewExerciseId() {
        return newExerciseId;
    }

    public void setNewExerciseId(Long newExerciseId) {
        this.newExerciseId = newExerciseId;
    }
}