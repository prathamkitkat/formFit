package com.pm.formfitbackend.template.dto;

import java.util.List;

public class TemplateDetailResponse {

    private Long templateId;
    private String name;
    private List<TemplateExerciseDetailResponse> exercises;

    public TemplateDetailResponse() {
    }

    public TemplateDetailResponse(Long templateId,
                                  String name,
                                  List<TemplateExerciseDetailResponse> exercises) {
        this.templateId = templateId;
        this.name = name;
        this.exercises = exercises;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TemplateExerciseDetailResponse> getExercises() {
        return exercises;
    }

    public void setExercises(List<TemplateExerciseDetailResponse> exercises) {
        this.exercises = exercises;
    }
}