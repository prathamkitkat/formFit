package com.pm.formfitbackend.workout.dto;

public class UpdateSetRequest {

    private Double weight;
    private Integer reps;

    public UpdateSetRequest() {
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }
}