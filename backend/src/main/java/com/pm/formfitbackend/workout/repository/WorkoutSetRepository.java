package com.pm.formfitbackend.workout.repository;

import com.pm.formfitbackend.workout.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {

        @Query("SELECT ws FROM WorkoutSet ws " +
                        "JOIN ws.workoutExercise we " +
                        "JOIN we.workout w " +
                        "WHERE ws.id = :setId " +
                        "AND we.id = :workoutExerciseId " +
                        "AND w.id = :workoutId")
        Optional<WorkoutSet> findByIdAndWorkoutExerciseIdAndWorkoutId(
                        Long setId,
                        Long workoutExerciseId,
                        Long workoutId);

        /* ================= LAST PERFORMANCE ================= */

        @Query(value = """
                        SELECT ws.*
                        FROM workout_sets ws
                        WHERE ws.workout_exercise_id = (
                            SELECT we.id
                            FROM workout_exercises we
                            JOIN workouts w ON we.workout_id = w.id
                            WHERE w.user_id = :userId
                            AND we.exercise_id = :exerciseId
                            AND w.workout_status = 'COMPLETED'
                            ORDER BY w.ended_at DESC
                            LIMIT 1
                        )
                        ORDER BY ws.set_number ASC
                        """, nativeQuery = true)
        List<WorkoutSet> findLastPerformanceForExercise(
                        Long userId,
                        Long exerciseId);

        /* ================= PR QUERIES ================= */

        @Query(value = """
                        SELECT ws.weight, w.ended_at
                        FROM workout_sets ws
                        JOIN workout_exercises we ON ws.workout_exercise_id = we.id
                        JOIN workouts w ON we.workout_id = w.id
                        WHERE w.user_id = :userId
                        AND we.exercise_id = :exerciseId
                        AND w.workout_status = 'COMPLETED'
                        AND ws.weight IS NOT NULL
                        AND w.id != :excludedWorkoutId
                        AND w.ended_at <= COALESCE(cast(:currentWorkoutEndedAt as timestamp), CURRENT_TIMESTAMP)
                        ORDER BY ws.weight DESC
                        LIMIT 1
                        """, nativeQuery = true)
        List<Object[]> getHeaviestWeight(Long userId, Long exerciseId, Long excludedWorkoutId, LocalDateTime currentWorkoutEndedAt);

        @Query(value = """
                        SELECT (ws.weight * (1 + ws.reps / 30.0)) as oneRM, w.ended_at
                        FROM workout_sets ws
                        JOIN workout_exercises we ON ws.workout_exercise_id = we.id
                        JOIN workouts w ON we.workout_id = w.id
                        WHERE w.user_id = :userId
                        AND we.exercise_id = :exerciseId
                        AND w.workout_status = 'COMPLETED'
                        AND ws.weight IS NOT NULL
                        AND ws.reps IS NOT NULL
                        AND w.id != :excludedWorkoutId
                        AND w.ended_at <= COALESCE(cast(:currentWorkoutEndedAt as timestamp), CURRENT_TIMESTAMP)
                        ORDER BY oneRM DESC
                        LIMIT 1
                        """, nativeQuery = true)
        List<Object[]> getBest1RM(Long userId, Long exerciseId, Long excludedWorkoutId, LocalDateTime currentWorkoutEndedAt);

        @Query(value = """
                        SELECT (ws.weight * ws.reps) as volume, w.ended_at
                        FROM workout_sets ws
                        JOIN workout_exercises we ON ws.workout_exercise_id = we.id
                        JOIN workouts w ON we.workout_id = w.id
                        WHERE w.user_id = :userId
                        AND we.exercise_id = :exerciseId
                        AND w.workout_status = 'COMPLETED'
                        AND ws.weight IS NOT NULL
                        AND ws.reps IS NOT NULL
                        AND w.id != :excludedWorkoutId
                        AND w.ended_at <= COALESCE(cast(:currentWorkoutEndedAt as timestamp), CURRENT_TIMESTAMP)
                        ORDER BY volume DESC
                        LIMIT 1
                        """, nativeQuery = true)
        List<Object[]> getBestSetVolume(Long userId, Long exerciseId, Long excludedWorkoutId, LocalDateTime currentWorkoutEndedAt);

        @Query(value = """
                        SELECT SUM(ws.weight * ws.reps) as sessionVolume, w.ended_at
                        FROM workout_sets ws
                        JOIN workout_exercises we ON ws.workout_exercise_id = we.id
                        JOIN workouts w ON we.workout_id = w.id
                        WHERE w.user_id = :userId
                        AND we.exercise_id = :exerciseId
                        AND w.workout_status = 'COMPLETED'
                        AND w.id != :excludedWorkoutId
                        AND w.ended_at <= COALESCE(cast(:currentWorkoutEndedAt as timestamp), CURRENT_TIMESTAMP)
                        GROUP BY w.id, w.ended_at
                        ORDER BY sessionVolume DESC
                        LIMIT 1
                        """, nativeQuery = true)
        List<Object[]> getBestSessionVolume(Long userId, Long exerciseId, Long excludedWorkoutId, LocalDateTime currentWorkoutEndedAt);
}