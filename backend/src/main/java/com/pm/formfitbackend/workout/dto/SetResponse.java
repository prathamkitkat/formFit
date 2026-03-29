package com.pm.formfitbackend.workout.dto;

import java.util.List;

public class SetResponse {

    private Long id;
    private Integer setNumber;
    private Double weight;
    private Integer reps;
    private List<String> prs; // e.g. ["Weight", "1RM", "Set Volume"]

    public SetResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
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

    public List<String> getPrs() {
        return prs;
    }

    public void setPrs(List<String> prs) {
        this.prs = prs;
    }
}