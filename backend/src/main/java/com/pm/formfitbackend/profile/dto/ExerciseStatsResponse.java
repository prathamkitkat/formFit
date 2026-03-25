package com.pm.formfitbackend.profile.dto;

public class ExerciseStatsResponse {

    private Long exerciseId;
    private String exerciseName;

    // PRs (calculated on-the-fly)
    private Double heaviestWeight;           // Max weight in any set
    private Double best1RM;                  // Best calculated 1RM
    private Double bestSetVolume;            // Max (weight × reps) in single set
    private Double bestSessionVolume;        // Max total volume in one workout

    // When these PRs were achieved
    private String heaviestWeightDate;       // "2026-02-15"
    private String best1RMDate;
    private String bestSetVolumeDate;
    private String bestSessionVolumeDate;

    public ExerciseStatsResponse() {
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

    public Double getHeaviestWeight() {
        return heaviestWeight;
    }

    public void setHeaviestWeight(Double heaviestWeight) {
        this.heaviestWeight = heaviestWeight;
    }

    public Double getBest1RM() {
        return best1RM;
    }

    public void setBest1RM(Double best1RM) {
        this.best1RM = best1RM;
    }

    public Double getBestSetVolume() {
        return bestSetVolume;
    }

    public void setBestSetVolume(Double bestSetVolume) {
        this.bestSetVolume = bestSetVolume;
    }

    public Double getBestSessionVolume() {
        return bestSessionVolume;
    }

    public void setBestSessionVolume(Double bestSessionVolume) {
        this.bestSessionVolume = bestSessionVolume;
    }

    public String getHeaviestWeightDate() {
        return heaviestWeightDate;
    }

    public void setHeaviestWeightDate(String heaviestWeightDate) {
        this.heaviestWeightDate = heaviestWeightDate;
    }

    public String getBest1RMDate() {
        return best1RMDate;
    }

    public void setBest1RMDate(String best1RMDate) {
        this.best1RMDate = best1RMDate;
    }

    public String getBestSetVolumeDate() {
        return bestSetVolumeDate;
    }

    public void setBestSetVolumeDate(String bestSetVolumeDate) {
        this.bestSetVolumeDate = bestSetVolumeDate;
    }

    public String getBestSessionVolumeDate() {
        return bestSessionVolumeDate;
    }

    public void setBestSessionVolumeDate(String bestSessionVolumeDate) {
        this.bestSessionVolumeDate = bestSessionVolumeDate;
    }
}