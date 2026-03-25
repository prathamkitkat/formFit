package com.pm.formfitbackend.profile.dto;

public class ComparisonData {

    private Integer workoutsDiff;      // +5 or -4
    private Integer durationDiff;      // in minutes (+517 or -517)
    private Double volumeDiff;         // in kg (+5136.0 or -5136.0)
    private Integer setsDiff;          // +8 or -8

    public ComparisonData() {
    }

    public ComparisonData(Integer workoutsDiff, Integer durationDiff,
                          Double volumeDiff, Integer setsDiff) {
        this.workoutsDiff = workoutsDiff;
        this.durationDiff = durationDiff;
        this.volumeDiff = volumeDiff;
        this.setsDiff = setsDiff;
    }

    public Integer getWorkoutsDiff() {
        return workoutsDiff;
    }

    public void setWorkoutsDiff(Integer workoutsDiff) {
        this.workoutsDiff = workoutsDiff;
    }

    public Integer getDurationDiff() {
        return durationDiff;
    }

    public void setDurationDiff(Integer durationDiff) {
        this.durationDiff = durationDiff;
    }

    public Double getVolumeDiff() {
        return volumeDiff;
    }

    public void setVolumeDiff(Double volumeDiff) {
        this.volumeDiff = volumeDiff;
    }

    public Integer getSetsDiff() {
        return setsDiff;
    }

    public void setSetsDiff(Integer setsDiff) {
        this.setsDiff = setsDiff;
    }
}