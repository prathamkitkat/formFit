package com.pm.formfitbackend.workout.dto;

import java.util.List;

/**
 * Used to show user what they did last time for this exercise
 * Frontend displays this as placeholder/reference data
 */
public class PreviousPerformance {

    private Long exerciseId;  // NEW: to match with workout exercise
    private List<PreviousSet> sets;

    public PreviousPerformance() {
    }

    public PreviousPerformance(Long exerciseId, List<PreviousSet> sets) {
        this.exerciseId = exerciseId;
        this.sets = sets;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public List<PreviousSet> getSets() {
        return sets;
    }

    public void setSets(List<PreviousSet> sets) {
        this.sets = sets;
    }

    public static class PreviousSet {
        private Double weight;
        private Integer reps;

        public PreviousSet() {
        }

        public PreviousSet(Double weight, Integer reps) {
            this.weight = weight;
            this.reps = reps;
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
}