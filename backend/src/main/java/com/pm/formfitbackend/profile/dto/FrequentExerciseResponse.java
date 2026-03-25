package com.pm.formfitbackend.profile.dto;

public class FrequentExerciseResponse {

    private Long exerciseId;
    private String exerciseName;
    private String imageUrl;
    private Integer timesPerformed;      // e.g., "12 times"

    public FrequentExerciseResponse() {
    }

    public FrequentExerciseResponse(Long exerciseId, String exerciseName,
                                    String imageUrl, Integer timesPerformed) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.imageUrl = imageUrl;
        this.timesPerformed = timesPerformed;
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

    public Integer getTimesPerformed() {
        return timesPerformed;
    }

    public void setTimesPerformed(Integer timesPerformed) {
        this.timesPerformed = timesPerformed;
    }
}