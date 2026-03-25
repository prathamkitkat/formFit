package com.pm.formfitbackend.workout.dto;

import jakarta.validation.constraints.NotBlank;

public class StartWorkoutRequest {

    @NotBlank(message = "Workout name is required")
    private String name;

    // Optional: template ID if starting from template
    private Long templateId;

    public StartWorkoutRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
}