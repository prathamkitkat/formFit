package com.pm.formfitbackend.template.dto;

public class TemplateExerciseDetailResponse {

    private Long templateExerciseId;
    private Long exerciseId;
    private String exerciseName;
    private Integer defaultSetCount;
    private Integer orderIndex;

    public TemplateExerciseDetailResponse() {
    }

    public TemplateExerciseDetailResponse(Long templateExerciseId,
                                          Long exerciseId,
                                          String exerciseName,
                                          Integer defaultSetCount,
                                          Integer orderIndex) {
        this.templateExerciseId = templateExerciseId;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.defaultSetCount = defaultSetCount;
        this.orderIndex = orderIndex;
    }

    public Long getTemplateExerciseId() {
        return templateExerciseId;
    }

    public void setTemplateExerciseId(Long templateExerciseId) {
        this.templateExerciseId = templateExerciseId;
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

    public Integer getDefaultSetCount() {
        return defaultSetCount;
    }

    public void setDefaultSetCount(Integer defaultSetCount) {
        this.defaultSetCount = defaultSetCount;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
}