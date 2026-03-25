package com.pm.formfitbackend.profile.dto;

import java.util.List;

public class CalendarDayResponse {

    private String date;              // "2026-02-15"
    private Integer workoutCount;     // Number of workouts on this day
    private List<Long> workoutIds;    // IDs to fetch details

    public CalendarDayResponse() {
    }

    public CalendarDayResponse(String date, Integer workoutCount, List<Long> workoutIds) {
        this.date = date;
        this.workoutCount = workoutCount;
        this.workoutIds = workoutIds;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getWorkoutCount() {
        return workoutCount;
    }

    public void setWorkoutCount(Integer workoutCount) {
        this.workoutCount = workoutCount;
    }

    public List<Long> getWorkoutIds() {
        return workoutIds;
    }

    public void setWorkoutIds(List<Long> workoutIds) {
        this.workoutIds = workoutIds;
    }
}