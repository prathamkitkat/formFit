package com.pm.formfitbackend.template.dto;

import java.util.List;

public class TemplateSummaryResponse {

    private Long templateId;
    private String name;
    private List<String> exerciseNames;

    public TemplateSummaryResponse() {
    }

    public TemplateSummaryResponse(Long templateId,
                                   String name,
                                   List<String> exerciseNames) {
        this.templateId = templateId;
        this.name = name;
        this.exerciseNames = exerciseNames;
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

    public List<String> getExerciseNames() {
        return exerciseNames;
    }

    public void setExerciseNames(List<String> exerciseNames) {
        this.exerciseNames = exerciseNames;
    }
}