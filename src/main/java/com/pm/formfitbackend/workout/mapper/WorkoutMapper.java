package com.pm.formfitbackend.workout.mapper;

import com.pm.formfitbackend.workout.dto.*;
import com.pm.formfitbackend.workout.entity.Workout;
import com.pm.formfitbackend.workout.entity.WorkoutExercise;
import com.pm.formfitbackend.workout.entity.WorkoutSet;

import java.util.List;
import java.util.stream.Collectors;

public class WorkoutMapper {

    /**
     * Map Workout entity to WorkoutSummaryResponse DTO
     */
    public static WorkoutSummaryResponse toSummaryResponse(Workout workout) {
        int exerciseCount = workout.getExercises().size();

        int totalSets = workout.getExercises().stream()
                .mapToInt(we -> we.getSets().size())
                .sum();

        return new WorkoutSummaryResponse(
                workout.getId(),
                workout.getName(),
                workout.getStatus().name(),
                workout.getStartedAt(),
                workout.getEndedAt(),
                workout.getDuration(),
                exerciseCount,
                totalSets
        );
    }

    /**
     * Map Workout entity to WorkoutDetailResponse DTO
     */
    public static WorkoutDetailResponse toDetailResponse(Workout workout,
                                                         List<PreviousPerformance> previousPerformances) {
        WorkoutDetailResponse response = new WorkoutDetailResponse();
        response.setId(workout.getId());
        response.setName(workout.getName());
        response.setStatus(workout.getStatus().name());
        response.setStartedAt(workout.getStartedAt());
        response.setEndedAt(workout.getEndedAt());
        response.setDuration(workout.getDuration());

        List<WorkoutExerciseResponse> exerciseResponses = workout.getExercises().stream()
                .sorted((a, b) -> a.getOrderIndex().compareTo(b.getOrderIndex()))
                .map(we -> {
                    // Find matching previous performance for this exercise
                    PreviousPerformance prevPerf = previousPerformances.stream()
                            .filter(pp -> pp.getExerciseId().equals(we.getExercise().getId()))
                            .findFirst()
                            .orElse(null);

                    return toExerciseResponse(we, prevPerf);
                })
                .collect(Collectors.toList());

        response.setExercises(exerciseResponses);
        return response;
    }

    /**
     * Map WorkoutExercise to WorkoutExerciseResponse
     */
    public static WorkoutExerciseResponse toExerciseResponse(WorkoutExercise workoutExercise,
                                                             PreviousPerformance previousPerformance) {
        WorkoutExerciseResponse response = new WorkoutExerciseResponse();
        response.setId(workoutExercise.getId());
        response.setExerciseId(workoutExercise.getExercise().getId());
        response.setExerciseName(workoutExercise.getExercise().getName());
        response.setImageUrl(workoutExercise.getExercise().getImageUrl());
        response.setOrderIndex(workoutExercise.getOrderIndex());
        response.setNotes(workoutExercise.getNotes());

        List<SetResponse> setResponses = workoutExercise.getSets().stream()
                .sorted((a, b) -> a.getSetNumber().compareTo(b.getSetNumber()))
                .map(WorkoutMapper::toSetResponse)
                .collect(Collectors.toList());

        response.setSets(setResponses);
        response.setPreviousPerformance(previousPerformance);

        return response;
    }

    /**
     * Map WorkoutSet to SetResponse
     */
    public static SetResponse toSetResponse(WorkoutSet set) {
        SetResponse response = new SetResponse();
        response.setId(set.getId());
        response.setSetNumber(set.getSetNumber());
        response.setWeight(set.getWeight());
        response.setReps(set.getReps());
        return response;
    }
}