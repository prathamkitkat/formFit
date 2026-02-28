package com.pm.formfitbackend.exercise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CreateCustomExerciseRequestDTO {

    @NotBlank(message = "Exercise name is required")
    @Size(max = 100, message = "Exercise name cannot exceed 100 characters")
    private String name;

    @NotEmpty(message = "At least one muscle group must be selected")
    private List<Long> muscleGroupIds;

    @Size(max = 100, message = "Equipment cannot exceed 100 characters")
    private String equipment;

    private String imageUrl;

    private String videoUrl;


    public String getName() {
        return name;
    }

    public List<Long> getMuscleGroupIds() {
        return muscleGroupIds;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMuscleGroupIds(List<Long> muscleGroupIds) {
        this.muscleGroupIds = muscleGroupIds;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
