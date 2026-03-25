package com.pm.formfitbackend.exercise.dto;

import java.util.List;

public class ExerciseResponseDTO {

    private Long exerciseId;
    private String name;
    private List<String> muscleGroups;
    private String equipment;
    private String imageUrl;
    private String videoUrl;
    private Boolean isCustom;

    public ExerciseResponseDTO() {
    }

    public ExerciseResponseDTO(Long exerciseId,
                               String name,
                               List<String> muscleGroups,
                               String equipment,
                               String imageUrl,
                               String videoUrl,
                               Boolean isCustom) {
        this.exerciseId = exerciseId;
        this.name = name;
        this.muscleGroups = muscleGroups;
        this.equipment = equipment;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.isCustom = isCustom;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public List<String> getMuscleGroups() {
        return muscleGroups;
    }

    public void setMuscleGroups(List<String> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Boolean getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Boolean custom) {
        isCustom = custom;
    }
}
