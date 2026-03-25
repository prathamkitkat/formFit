package com.pm.formfitbackend.profile.controller;

import com.pm.formfitbackend.profile.dto.*;
import com.pm.formfitbackend.profile.service.ProfileService;
import com.pm.formfitbackend.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
    /**
     * GET /api/profile
     * Get main profile data (graphs for 1M + recent workouts)
     */
    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ProfileResponse profile = profileService.getProfile(userDetails.getUserId());

        return ResponseEntity.ok(profile);
    }

    /**
     * GET /api/profile/graphs?period=1M|3M|6M|12M
     * Get graph data only (when user changes period dropdown)
     */
    @GetMapping("/graphs")
    public ResponseEntity<ProfileGraphsResponse> getGraphs(
            @RequestParam(defaultValue = "1M") String period,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ProfileGraphsResponse graphs = profileService.getGraphs(
                userDetails.getUserId(),
                period
        );

        return ResponseEntity.ok(graphs);
    }


    /**
     * GET /api/profile/stats/monthly-report?year=2026&month=2
     * Get monthly report for a specific month
     */
    @GetMapping("/stats/monthly-report")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        MonthlyReportResponse report = profileService.getMonthlyReport(
                userDetails.getUserId(),
                year,
                month
        );

        return ResponseEntity.ok(report);
    }

    /**
     * GET /api/profile/stats/frequent-exercises?days=30
     * Get most frequently performed exercises
     */
    @GetMapping("/stats/frequent-exercises")
    public ResponseEntity<List<FrequentExerciseResponse>> getFrequentExercises(
            @RequestParam(defaultValue = "30") Integer days,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<FrequentExerciseResponse> exercises = profileService.getFrequentExercises(
                userDetails.getUserId(),
                days
        );

        return ResponseEntity.ok(exercises);
    }

    /**
     * GET /api/profile/exercises
     * Get all exercises user has performed
     */
    @GetMapping("/exercises")
    public ResponseEntity<List<UserExerciseResponse>> getUserExercises(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<UserExerciseResponse> exercises = profileService.getUserExercises(
                userDetails.getUserId()
        );

        return ResponseEntity.ok(exercises);
    }

    /**
     * GET /api/profile/exercises/{exerciseId}/stats
     * Get detailed stats for a specific exercise
     */
    @GetMapping("/exercises/{exerciseId}/stats")
    public ResponseEntity<ExerciseStatsResponse> getExerciseStats(
            @PathVariable Long exerciseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ExerciseStatsResponse stats = profileService.getExerciseStats(
                userDetails.getUserId(),
                exerciseId
        );

        return ResponseEntity.ok(stats);
    }

    /**
     * GET /api/profile/calendar?year=2026&month=2
     * Get calendar data for a specific month
     */
    @GetMapping("/calendar")
    public ResponseEntity<List<CalendarDayResponse>> getCalendar(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<CalendarDayResponse> calendar = profileService.getCalendar(
                userDetails.getUserId(),
                year,
                month
        );

        return ResponseEntity.ok(calendar);
    }
}