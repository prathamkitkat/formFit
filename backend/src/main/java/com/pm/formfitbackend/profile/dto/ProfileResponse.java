package com.pm.formfitbackend.profile.dto;

import com.pm.formfitbackend.workout.dto.WorkoutSummaryResponse;
import java.util.List;

public class ProfileResponse {

    // Graph data
    private ProfileGraphsResponse graphs;

    // Recent workouts (descending order)
    private List<WorkoutSummaryResponse> recentWorkouts;

    public ProfileResponse() {
    }

    public ProfileResponse(ProfileGraphsResponse graphs,
                           List<WorkoutSummaryResponse> recentWorkouts) {
        this.graphs = graphs;
        this.recentWorkouts = recentWorkouts;
    }

    public ProfileGraphsResponse getGraphs() {
        return graphs;
    }

    public void setGraphs(ProfileGraphsResponse graphs) {
        this.graphs = graphs;
    }

    public List<WorkoutSummaryResponse> getRecentWorkouts() {
        return recentWorkouts;
    }

    public void setRecentWorkouts(List<WorkoutSummaryResponse> recentWorkouts) {
        this.recentWorkouts = recentWorkouts;
    }
}