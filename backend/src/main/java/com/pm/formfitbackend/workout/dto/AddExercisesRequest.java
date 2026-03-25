package com.pm.formfitbackend.workout.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AddExercisesRequest {

    @NotEmpty(message = "Exercise IDs cannot be empty")
    private List<@NotNull Long> exerciseIds;

    public AddExercisesRequest() {
    }

    public List<Long> getExerciseIds() {
        return exerciseIds;
    }

    public void setExerciseIds(List<Long> exerciseIds) {
        this.exerciseIds = exerciseIds;
    }
}