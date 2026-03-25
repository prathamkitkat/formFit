package com.pm.formfitbackend.profile.dto;

public class GraphDataPoint {

    private String date;      // "2026-02-15"
    private Double value;     // Duration (minutes), Volume (kg), or Reps (count)

    public GraphDataPoint() {
    }

    public GraphDataPoint(String date, Double value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}