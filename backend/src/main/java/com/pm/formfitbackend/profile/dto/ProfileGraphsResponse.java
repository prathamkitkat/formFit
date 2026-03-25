package com.pm.formfitbackend.profile.dto;

import java.util.List;

public class ProfileGraphsResponse {

    private List<GraphDataPoint> duration;   // Duration graph data
    private List<GraphDataPoint> volume;     // Volume graph data
    private List<GraphDataPoint> reps;       // Reps graph data

    public ProfileGraphsResponse() {
    }

    public ProfileGraphsResponse(List<GraphDataPoint> duration,
                                 List<GraphDataPoint> volume,
                                 List<GraphDataPoint> reps) {
        this.duration = duration;
        this.volume = volume;
        this.reps = reps;
    }

    public List<GraphDataPoint> getDuration() {
        return duration;
    }

    public void setDuration(List<GraphDataPoint> duration) {
        this.duration = duration;
    }

    public List<GraphDataPoint> getVolume() {
        return volume;
    }

    public void setVolume(List<GraphDataPoint> volume) {
        this.volume = volume;
    }

    public List<GraphDataPoint> getReps() {
        return reps;
    }

    public void setReps(List<GraphDataPoint> reps) {
        this.reps = reps;
    }
}