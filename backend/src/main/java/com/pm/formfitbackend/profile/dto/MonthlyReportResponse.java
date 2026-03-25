package com.pm.formfitbackend.profile.dto;

public class MonthlyReportResponse {

    private Integer year;
    private Integer month;

    // Current month stats
    private Integer workouts;
    private Integer totalDurationMinutes;
    private Double totalVolume;        // in kg
    private Integer totalSets;

    // Comparison with previous month
    private ComparisonData comparison;

    public MonthlyReportResponse() {
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getWorkouts() {
        return workouts;
    }

    public void setWorkouts(Integer workouts) {
        this.workouts = workouts;
    }

    public Integer getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public void setTotalDurationMinutes(Integer totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Integer getTotalSets() {
        return totalSets;
    }

    public void setTotalSets(Integer totalSets) {
        this.totalSets = totalSets;
    }

    public ComparisonData getComparison() {
        return comparison;
    }

    public void setComparison(ComparisonData comparison) {
        this.comparison = comparison;
    }
}