package com.pm.formfitbackend.workout.controller;

import com.pm.formfitbackend.workout.dto.*;
import com.pm.formfitbackend.workout.service.WorkoutService;
import com.pm.formfitbackend.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    /**
     * POST /api/workouts
     * Start a new workout
     */
    @PostMapping
    public ResponseEntity<WorkoutDetailResponse> startWorkout(
            @Valid @RequestBody StartWorkoutRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkoutDetailResponse workout = workoutService.startWorkout(request, userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(workout);
    }

    /**
     * GET /api/workouts/active
     * Get the current in-progress workout
     */
    @GetMapping("/active")
    public ResponseEntity<WorkoutDetailResponse> getActiveWorkout(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkoutDetailResponse workout = workoutService.getActiveWorkout(userDetails.getUserId());

        if (workout == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(workout);
    }

    /**
     * GET /api/workouts/{workoutId}
     * Get a specific workout by ID
     */
    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutDetailResponse> getWorkoutById(
            @PathVariable Long workoutId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkoutDetailResponse workout = workoutService.getWorkoutById(workoutId, userDetails.getUserId());
        return ResponseEntity.ok(workout);
    }

    /**
     * GET /api/workouts
     * Get workout history (completed workouts)
     * Returns list of WorkoutDetailResponse
     */
    @GetMapping
    public ResponseEntity<List<WorkoutSummaryResponse>> getWorkoutHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<WorkoutSummaryResponse> workouts =
                workoutService.getWorkoutHistory(userDetails.getUserId());

        return ResponseEntity.ok(workouts);
    }


    /**
     * POST /api/workouts/{workoutId}/exercises
     * Add multiple exercises to workout at once
     */
    @PostMapping("/{workoutId}/exercises")
    public ResponseEntity<WorkoutDetailResponse> addExercisesToWorkout(
            @PathVariable Long workoutId,
            @Valid @RequestBody AddExercisesRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkoutDetailResponse workout = workoutService.addExercisesToWorkout(
                workoutId,
                request,
                userDetails.getUserId()
        );
        return ResponseEntity.ok(workout);
    }

    /**
     * PUT /api/workouts/{workoutId}/exercises/{workoutExerciseId}/replace
     * Replace an exercise with another while keeping the same order
     */
    @PutMapping("/{workoutId}/exercises/{workoutExerciseId}/replace")
    public ResponseEntity<WorkoutDetailResponse> replaceExercise(
            @PathVariable Long workoutId,
            @PathVariable Long workoutExerciseId,
            @Valid @RequestBody ReplaceExerciseRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkoutDetailResponse workout = workoutService.replaceExercise(
                workoutId,
                workoutExerciseId,
                request,
                userDetails.getUserId()
        );
        return ResponseEntity.ok(workout);
    }

    /**
     * DELETE /api/workouts/{workoutId}/exercises/{workoutExerciseId}
     * Remove an exercise from workout
     */
    @DeleteMapping("/{workoutId}/exercises/{workoutExerciseId}")
    public ResponseEntity<WorkoutDetailResponse> removeExerciseFromWorkout(
            @PathVariable Long workoutId,
            @PathVariable Long workoutExerciseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkoutDetailResponse workout = workoutService.removeExerciseFromWorkout(
                workoutId,
                workoutExerciseId,
                userDetails.getUserId()
        );
        return ResponseEntity.ok(workout);
    }

    /**
     * PUT /api/workouts/{workoutId}/exercises/{workoutExerciseId}/notes
     * Update notes for a specific exercise in the workout
     */
    @PutMapping("/{workoutId}/exercises/{workoutExerciseId}/notes")
    public ResponseEntity<Void> updateExerciseNotes(
            @PathVariable Long workoutId,
            @PathVariable Long workoutExerciseId,
            @Valid @RequestBody UpdateExerciseNotesRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        workoutService.updateExerciseNotes(
                workoutId,
                workoutExerciseId,
                request,
                userDetails.getUserId()
        );

        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/workouts/{workoutId}/exercises/{workoutExerciseId}/sets
     * Add a blank set to an exercise (will be filled in later via update)
     */
    @PostMapping("/{workoutId}/exercises/{workoutExerciseId}/sets")
    public ResponseEntity<Long> addSetToExercise(
            @PathVariable Long workoutId,
            @PathVariable Long workoutExerciseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long setId = workoutService.addSetToExercise(
                workoutId,
                workoutExerciseId,
                userDetails.getUserId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(setId);
    }

    /**
     * PUT /api/workouts/{workoutId}/exercises/{workoutExerciseId}/sets/{setId}
     * Update a set (weight, reps)
     */
    @PutMapping("/{workoutId}/exercises/{workoutExerciseId}/sets/{setId}")
    public ResponseEntity<Void> updateSet(
            @PathVariable Long workoutId,
            @PathVariable Long workoutExerciseId,
            @PathVariable Long setId,
            @Valid @RequestBody UpdateSetRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        workoutService.updateSet(
                workoutId,
                workoutExerciseId,
                setId,
                request,
                userDetails.getUserId()
        );

        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/workouts/{workoutId}/exercises/{workoutExerciseId}/sets/{setId}
     * Delete a set
     */
    @DeleteMapping("/{workoutId}/exercises/{workoutExerciseId}/sets/{setId}")
    public ResponseEntity<Void> deleteSet(
            @PathVariable Long workoutId,
            @PathVariable Long workoutExerciseId,
            @PathVariable Long setId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        workoutService.deleteSet(
                workoutId,
                workoutExerciseId,
                setId,
                userDetails.getUserId()
        );

        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/workouts/{workoutId}/complete
     * Complete the workout
     */
    @PostMapping("/{workoutId}/complete")
    public ResponseEntity<WorkoutDetailResponse> completeWorkout(
            @PathVariable Long workoutId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkoutDetailResponse workout = workoutService.completeWorkout(workoutId, userDetails.getUserId());
        return ResponseEntity.ok(workout);
    }

    /**
     * POST /api/workouts/{workoutId}/discard
     * Discard the workout
     */
    @PostMapping("/{workoutId}/discard")
    public ResponseEntity<Void> discardWorkout(
            @PathVariable Long workoutId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        workoutService.discardWorkout(workoutId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/workouts/{workoutId}
     * Delete a workout permanently
     */
    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> deleteWorkout(
            @PathVariable Long workoutId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        workoutService.deleteWorkout(workoutId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}