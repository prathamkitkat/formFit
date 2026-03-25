package com.pm.formfitbackend.profile.dto;

public class UserExerciseResponse {

    private Long exerciseId;
    private String exerciseName;
    private String imageUrl;

    public UserExerciseResponse() {
    }

    public UserExerciseResponse(Long exerciseId, String exerciseName, String imageUrl) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.imageUrl = imageUrl;
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
}
