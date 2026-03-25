package com.pm.formfitbackend.profile.dto;

public class GraphAggregate {

    private String date;              // "2026-02-15"
    private Integer totalDuration;    // Sum of workout durations
    private Double totalVolume;       // Sum of (weight × reps)
    private Integer totalReps;        // Sum of reps

    public GraphAggregate(String date, Integer totalDuration,
                          Double totalVolume, Integer totalReps) {
        this.date = date;
        this.totalDuration = totalDuration;
        this.totalVolume = totalVolume;
        this.totalReps = totalReps;
    }

    public String getDate() {
        return date;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public Integer getTotalReps() {
        return totalReps;
    }
}