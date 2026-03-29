package com.pm.formfitbackend.workout.mapper;

import com.pm.formfitbackend.workout.dto.*;
import com.pm.formfitbackend.workout.entity.Workout;
import com.pm.formfitbackend.workout.entity.WorkoutExercise;
import com.pm.formfitbackend.workout.entity.WorkoutSet;
import com.pm.formfitbackend.workout.repository.WorkoutSetRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        double totalVolume = workout.getExercises().stream()
                .flatMap(we -> we.getSets().stream())
                .filter(s -> s.getWeight() != null && s.getReps() != null)
                .mapToDouble(s -> s.getWeight() * s.getReps())
                .sum();

        List<WorkoutSummaryResponse.ExerciseSetSummary> exerciseSummaries = workout.getExercises().stream()
                .sorted((a, b) -> a.getOrderIndex().compareTo(b.getOrderIndex()))
                .limit(3)
                .map(we -> new WorkoutSummaryResponse.ExerciseSetSummary(
                        we.getExercise().getName(),
                        we.getSets().size(),
                        we.getExercise().getImageUrl()))
                .collect(Collectors.toList());

        return new WorkoutSummaryResponse(
                workout.getId(),
                workout.getName(),
                workout.getStatus().name(),
                workout.getStartedAt(),
                workout.getEndedAt(),
                workout.getDuration(),
                exerciseCount,
                totalSets,
                totalVolume,
                workout.getRecordsCount() != null ? workout.getRecordsCount() : 0,
                exerciseSummaries);
    }

    /**
     * Map Workout entity to WorkoutDetailResponse DTO (no PR detection)
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
                    PreviousPerformance prevPerf = previousPerformances.stream()
                            .filter(pp -> pp != null && pp.getExerciseId().equals(we.getExercise().getId()))
                            .findFirst()
                            .orElse(null);
                    return toExerciseResponse(we, prevPerf);
                })
                .collect(Collectors.toList());

        response.setExercises(exerciseResponses);
        return response;
    }

    /**
     * Map Workout → WorkoutDetailResponse WITH PR badges stamped on each set.
     * Uses the 3 existing PR queries from WorkoutSetRepository.
     */
    public static WorkoutDetailResponse toDetailResponseWithPrs(Workout workout,
            List<PreviousPerformance> previousPerformances,
            WorkoutSetRepository workoutSetRepository,
            Long userId) {

        WorkoutDetailResponse response = toDetailResponse(workout, previousPerformances);

        for (WorkoutExerciseResponse exerciseResp : response.getExercises()) {
            Long exerciseId = exerciseResp.getExerciseId();

            Long excludedWorkoutId = workout.getId();
            LocalDateTime currentWorkoutEndedAt = workout.getEndedAt();

            List<Object[]> heaviestRows = workoutSetRepository.getHeaviestWeight(userId, exerciseId, excludedWorkoutId, currentWorkoutEndedAt);
            List<Object[]> best1RMRows = workoutSetRepository.getBest1RM(userId, exerciseId, excludedWorkoutId, currentWorkoutEndedAt);
            List<Object[]> bestVolRows = workoutSetRepository.getBestSetVolume(userId, exerciseId, excludedWorkoutId, currentWorkoutEndedAt);

            Object[] heaviestRow = heaviestRows.isEmpty() ? null : heaviestRows.get(0);
            Object[] best1RMRow = best1RMRows.isEmpty() ? null : best1RMRows.get(0);
            Object[] bestVolRow = bestVolRows.isEmpty() ? null : bestVolRows.get(0);

            Double heaviestWeight = (heaviestRow != null && heaviestRow[0] != null)
                    ? ((Number) heaviestRow[0]).doubleValue()
                    : null;
            Double best1RM = (best1RMRow != null && best1RMRow[0] != null) ? ((Number) best1RMRow[0]).doubleValue()
                    : null;
            Double bestSetVolume = (bestVolRow != null && bestVolRow[0] != null)
                    ? ((Number) bestVolRow[0]).doubleValue()
                    : null;

            for (SetResponse setResp : exerciseResp.getSets()) {
                if (setResp.getWeight() == null || setResp.getReps() == null)
                    continue;

                double w = setResp.getWeight();
                double r = setResp.getReps();
                double oneRM = w * (1 + r / 30.0); // Epley formula
                double setVol = w * r;

                List<String> prs = new ArrayList<>();
                if (heaviestWeight == null || w > heaviestWeight) {
                    prs.add("Weight");
                    heaviestWeight = w;
                }
                if (best1RM == null || oneRM > best1RM) {
                    prs.add("1RM");
                    best1RM = oneRM;
                }
                if (bestSetVolume == null || setVol > bestSetVolume) {
                    prs.add("Set Volume");
                    bestSetVolume = setVol;
                }

                if (!prs.isEmpty())
                    setResp.setPrs(prs);
            }
        }

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