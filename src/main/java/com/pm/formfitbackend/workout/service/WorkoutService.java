package com.pm.formfitbackend.workout.service;

//import com.pm.formfitbackend.common.exception.AccessDeniedException;
//import com.pm.formfitbackend.common.exception.IllegalStateException;
//import com.pm.formfitbackend.common.exception.ResourceNotFoundException;
import com.pm.formfitbackend.exercise.entity.Exercise;
import com.pm.formfitbackend.exercise.repository.ExerciseRepository;
import com.pm.formfitbackend.user.entity.User;
import com.pm.formfitbackend.user.repository.UserRepository;
import com.pm.formfitbackend.workout.dto.*;
import com.pm.formfitbackend.workout.entity.Workout;
import com.pm.formfitbackend.workout.entity.WorkoutExercise;
import com.pm.formfitbackend.workout.entity.WorkoutSet;
import com.pm.formfitbackend.workout.entity.WorkoutStatus;
import com.pm.formfitbackend.workout.mapper.WorkoutMapper;
import com.pm.formfitbackend.workout.repository.WorkoutExerciseRepository;
import com.pm.formfitbackend.workout.repository.WorkoutRepository;
import com.pm.formfitbackend.workout.repository.WorkoutSetRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutSetRepository workoutSetRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    public WorkoutService(WorkoutRepository workoutRepository,
                          WorkoutExerciseRepository workoutExerciseRepository,
                          WorkoutSetRepository workoutSetRepository,
                          ExerciseRepository exerciseRepository,
                          UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.workoutSetRepository = workoutSetRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    /**
     * Start a new workout
     */
    public WorkoutDetailResponse startWorkout(StartWorkoutRequest request, Long userId) {
        // Precondition: Check for existing IN_PROGRESS workout
        workoutRepository.findByUserIdAndStatus(userId, WorkoutStatus.IN_PROGRESS)
                .ifPresent(w -> {
                    throw new RuntimeException("You already have an active workout in progress");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Workout workout = new Workout();
        workout.setUser(user);
        workout.setName(request.getName());
        workout.setStatus(WorkoutStatus.IN_PROGRESS);
        workout.setStartedAt(LocalDateTime.now());

        workout = workoutRepository.save(workout);

        return mapToDetailResponse(workout, userId);
    }

    /**
     * Get active workout
     */
    @Transactional(readOnly = true)
    public WorkoutDetailResponse getActiveWorkout(Long userId) {
        return workoutRepository.findByUserIdAndStatus(userId, WorkoutStatus.IN_PROGRESS)
                .map(workout -> mapToDetailResponse(workout, userId))
                .orElse(null);
    }

    /**
     * Get workout by ID
     */
    public WorkoutDetailResponse getWorkoutById(Long workoutId, Long userId) {

        Workout workout = workoutRepository
                .findByIdAndUserIdWithExercisesAndSets(workoutId, userId)
                .orElseThrow(() ->
                        new RuntimeException("Workout not found"));

        return mapToDetailResponse(workout, userId);
    }
    /**
     * Get workout history
     */
    @Transactional(readOnly = true)
    public List<WorkoutSummaryResponse> getWorkoutHistory(Long userId) {

        return workoutRepository
                .findByUserIdAndStatusOrderByStartedAtDesc(userId, WorkoutStatus.COMPLETED)
                .stream()
                .map(WorkoutMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Add multiple exercises to workout
     */
    public WorkoutDetailResponse addExercisesToWorkout(Long workoutId, AddExercisesRequest request, Long userId) {
        Workout workout = getWorkoutAndValidate(workoutId, userId, WorkoutStatus.IN_PROGRESS);

        int currentMaxOrder = workout.getExercises().stream()
                .mapToInt(WorkoutExercise::getOrderIndex)
                .max()
                .orElse(0);

        for (Long exerciseId : request.getExerciseIds()) {
            Exercise exercise = exerciseRepository.findById(exerciseId)
                    .orElseThrow(() -> new RuntimeException("Exercise not found: " + exerciseId));

            WorkoutExercise workoutExercise = new WorkoutExercise();
            workoutExercise.setExercise(exercise);
            workoutExercise.setOrderIndex(++currentMaxOrder);

            workout.addExercise(workoutExercise);
        }

        workout = workoutRepository.save(workout);
        return mapToDetailResponse(workout, userId);
    }

    /**
     * Replace an exercise while keeping the same orderIndex
     */
    public WorkoutDetailResponse replaceExercise(Long workoutId, Long workoutExerciseId,
                                                 ReplaceExerciseRequest request, Long userId) {
        Workout workout = getWorkoutAndValidate(workoutId, userId, WorkoutStatus.IN_PROGRESS);

        // ✅ Direct query instead of filtering
        // Find in the already-loaded exercises
        WorkoutExercise workoutExercise = workout.getExercises().stream()
                .filter(we -> we.getId().equals(workoutExerciseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Exercise not found in workout"));

        Exercise newExercise = exerciseRepository.findById(request.getNewExerciseId())
                .orElseThrow(() -> new RuntimeException("New exercise not found"));

        int orderIndex = workoutExercise.getOrderIndex();

        workout.removeExercise(workoutExercise);

        WorkoutExercise newWorkoutExercise = new WorkoutExercise();
        newWorkoutExercise.setExercise(newExercise);
        newWorkoutExercise.setOrderIndex(orderIndex);

        workout.addExercise(newWorkoutExercise);

        workout = workoutRepository.save(workout);
        return mapToDetailResponse(workout, userId);
    }

    /**
     * Remove exercise from workout
     */
    public WorkoutDetailResponse removeExerciseFromWorkout(
            Long workoutId,
            Long workoutExerciseId,
            Long userId
    ) {
        //preloads the exercise
        Workout workout = workoutRepository.findByIdAndUserIdWithExercises(workoutId,userId).
                orElseThrow(() -> new RuntimeException("Workout not found"));

        if (workout.getStatus() != WorkoutStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Workout must be " + WorkoutStatus.IN_PROGRESS +
                            " to perform this action. Current status: " +
                            workout.getStatus()
            );
        }

        WorkoutExercise workoutExercise = workout.getExercises().stream()
                .filter(we -> we.getId().equals(workoutExerciseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Exercise not found in workout"));

        // Remove
        workout.removeExercise(workoutExercise);

        // 🔥 Reorder remaining exercises
        List<WorkoutExercise> sorted =
                workout.getExercises().stream()
                        .sorted(Comparator.comparing(WorkoutExercise::getOrderIndex))
                        .toList();

        for (int i = 0; i < sorted.size(); i++) {
            sorted.get(i).setOrderIndex(i+1);
        }

        workout = workoutRepository.save(workout);

        return mapToDetailResponse(workout, userId);
    }

    /**
     * Update notes for a workout exercise
     */
    public void updateExerciseNotes(Long workoutId,
                                    Long workoutExerciseId,
                                    UpdateExerciseNotesRequest request,
                                    Long userId) {

        Workout workout = getWorkoutAndValidate(workoutId, userId, WorkoutStatus.IN_PROGRESS);

        WorkoutExercise workoutExercise = workout.getExercises().stream()
                .filter(we -> we.getId().equals(workoutExerciseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Exercise not found in workout"));

        workoutExercise.setNotes(request.getNotes());

        workoutRepository.save(workout);
    }

    /**
     * Add a blank set to exercise
     */
    /**
     * Add a blank set to exercise
     * Returns only created set id
     */
    public Long addSetToExercise(Long workoutId,
                                 Long workoutExerciseId,
                                 Long userId) {

        Workout workout = getWorkoutAndValidate(workoutId, userId, WorkoutStatus.IN_PROGRESS);

        WorkoutExercise workoutExercise = workoutExerciseRepository
                .findByIdAndWorkoutIdWithSets(workoutExerciseId, workoutId)
                .orElseThrow(() -> new RuntimeException("Exercise not found in workout"));

        int nextSetNumber = workoutExercise.getSets().stream()
                .mapToInt(WorkoutSet::getSetNumber)
                .max()
                .orElse(0) + 1;

        WorkoutSet set = new WorkoutSet();
        set.setSetNumber(nextSetNumber);
        set.setIsDropSet(false);

        workoutExercise.addSet(set);

        workoutRepository.save(workout);

        return set.getId(); // return only created ID
    }

    /**
     * Update a set
     */
    /**
     * Update a set
     */
    public void updateSet(Long workoutId,
                          Long workoutExerciseId,
                          Long setId,
                          UpdateSetRequest request,
                          Long userId) {

        Workout workout = getWorkoutAndValidate(workoutId, userId, WorkoutStatus.IN_PROGRESS);

        WorkoutSet set = workoutSetRepository
                .findByIdAndWorkoutExerciseIdAndWorkoutId(setId, workoutExerciseId, workoutId)
                .orElseThrow(() -> new RuntimeException("Set not found"));

        if (request.getWeight() != null) {
            set.setWeight(request.getWeight());
        }
        if (request.getReps() != null) {
            set.setReps(request.getReps());
        }

        workoutRepository.save(workout);
    }

    /**
     * Delete a set
     */
    /**
     * Delete a set
     */
    public void deleteSet(Long workoutId,
                          Long workoutExerciseId,
                          Long setId,
                          Long userId) {

        Workout workout = getWorkoutAndValidate(workoutId, userId, WorkoutStatus.IN_PROGRESS);

        WorkoutSet setToRemove = workoutSetRepository
                .findByIdAndWorkoutExerciseIdAndWorkoutId(setId, workoutExerciseId, workoutId)
                .orElseThrow(() -> new RuntimeException("Set not found"));

        int removedSetNumber = setToRemove.getSetNumber();

        WorkoutExercise workoutExercise = setToRemove.getWorkoutExercise();
        workoutExercise.removeSet(setToRemove);

        workoutExercise.getSets().stream()
                .filter(s -> s.getSetNumber() > removedSetNumber)
                .forEach(s -> s.setSetNumber(s.getSetNumber() - 1));

        workoutRepository.save(workout);
    }

    /**
     * Complete workout
     */
    public WorkoutDetailResponse completeWorkout(Long workoutId, Long userId) {
        Workout workout = getWorkoutAndValidate(workoutId, userId, WorkoutStatus.IN_PROGRESS);

        if (workout.getExercises().isEmpty()) {
            throw new RuntimeException("Cannot complete workout with no exercises");
        }

        for (WorkoutExercise we : workout.getExercises()) {
            if (we.getSets().isEmpty()) {
                throw new RuntimeException(
                        "Exercise '" + we.getExercise().getName() + "' has no sets. Add at least one set."
                );
            }

            for (WorkoutSet set : we.getSets()) {
                if (set.getReps() == null || set.getWeight() == null) {
                    throw new RuntimeException(
                            "All sets must have weight and reps filled before completing workout"
                    );
                }
            }
        }

        LocalDateTime endedAt = LocalDateTime.now();
        workout.setStatus(WorkoutStatus.COMPLETED);
        workout.setEndedAt(endedAt);

        long durationMinutes = Duration.between(workout.getStartedAt(), endedAt).toMinutes();
        workout.setDuration((int) durationMinutes);

        workout = workoutRepository.save(workout);
        return mapToDetailResponse(workout, userId);
    }

    /**
     * Discard workout
     */
    public void discardWorkout(Long workoutId, Long userId) {
        Workout workout = getWorkoutAndValidate(workoutId, userId, WorkoutStatus.IN_PROGRESS);
        workoutRepository.delete(workout);
    }

    /**
     * Delete workout
     */
    public void deleteWorkout(Long workoutId, Long userId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found"));

        if (!workout.getUser().getId().equals(userId)) {
            throw new RuntimeException("You do not have access to this workout");
        }

        workoutRepository.delete(workout);
    }

    /* ================== Helper Methods ================== */

    private Workout getWorkoutAndValidate(
            Long workoutId,
            Long userId,
            WorkoutStatus requiredStatus
    ) {

        Workout workout = workoutRepository
                .findByIdAndUserId(workoutId, userId)
                .orElseThrow(() ->
                        new RuntimeException("Workout not found")
                );

        if (workout.getStatus() != requiredStatus) {
            throw new IllegalStateException(
                    "Workout must be " + requiredStatus +
                            " to perform this action. Current status: " +
                            workout.getStatus()
            );
        }

        return workout;
    }

    private WorkoutDetailResponse mapToDetailResponse(Workout workout, Long userId) {
        List<PreviousPerformance> previousPerformances = workout.getExercises().stream()
                .map(we -> getPreviousPerformance(userId, we.getExercise().getId()))
                .collect(Collectors.toList());

        return WorkoutMapper.toDetailResponse(workout, previousPerformances);
    }

    private PreviousPerformance getPreviousPerformance(Long userId, Long exerciseId) {
        List<WorkoutSet> previousSets = workoutSetRepository
                .findLastPerformanceForExercise(userId, exerciseId);

        if (previousSets.isEmpty()) {
            return null;
        }

        List<PreviousPerformance.PreviousSet> prevSets = previousSets.stream()
                .map(set -> new PreviousPerformance.PreviousSet(set.getWeight(), set.getReps()))
                .collect(Collectors.toList());

        return new PreviousPerformance(exerciseId, prevSets);
    }
}