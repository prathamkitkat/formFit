package com.pm.formfitbackend.template.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AddTemplateExerciseRequest {

    @NotEmpty(message = "Exercise IDs cannot be empty")
    private List<@NotNull Long> exerciseIds;

    public AddTemplateExerciseRequest() {
    }

    public List<Long> getExerciseIds() {
        return exerciseIds;
    }

    public void setExerciseIds(List<Long> exerciseIds) {
        this.exerciseIds = exerciseIds;
    }
}