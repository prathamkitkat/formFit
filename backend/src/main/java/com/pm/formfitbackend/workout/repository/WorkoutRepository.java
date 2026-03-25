package com.pm.formfitbackend.workout.repository;

import com.pm.formfitbackend.exercise.entity.Exercise;
import com.pm.formfitbackend.workout.entity.Workout;
import com.pm.formfitbackend.workout.entity.WorkoutStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    Optional<Workout> findByUserIdAndWorkoutStatus(Long userId, WorkoutStatus workoutStatus);

    @Query("SELECT w FROM Workout w " +
            "LEFT JOIN FETCH w.exercises we " +
            "LEFT JOIN FETCH we.sets " +
            "WHERE w.id = :workoutId AND w.user.id = :userId")
    Optional<Workout> findByIdAndUserIdWithExercisesAndSets(Long workoutId, Long userId);

    @Query("SELECT w FROM Workout w " +
            "WHERE w.id = :workoutId AND w.user.id = :userId")
    Optional<Workout> findByIdAndUserId(Long workoutId, Long userId);

    @Query("""
    SELECT DISTINCT w
    FROM Workout w
    LEFT JOIN FETCH w.exercises we
    LEFT JOIN FETCH we.sets ws
    WHERE w.user.id = :userId
    AND w.workoutStatus = :workoutStatus
    ORDER BY w.startedAt DESC
    """)
    List<Workout> findByUserIdAndWorkoutStatusWithExercisesAndSetsOrderByStartedAtDesc(
            Long userId,
            WorkoutStatus workoutStatus
    );

    @Query(value = """
    SELECT 
        DATE(w.ended_at) as date,
        COALESCE(SUM(DISTINCT w.duration), 0) as totalDuration,
        COALESCE(SUM(ws.weight * ws.reps), 0) as totalVolume,
        COALESCE(SUM(ws.reps), 0) as totalReps
    FROM workouts w
    LEFT JOIN workout_exercises we ON we.workout_id = w.id
    LEFT JOIN workout_sets ws ON ws.workout_exercise_id = we.id
    WHERE w.user_id = :userId
    AND w.workout_status = 'COMPLETED'
    AND w.ended_at >= :startDate
    AND w.ended_at < :endDate
    GROUP BY DATE(w.ended_at)
    ORDER BY DATE(w.ended_at)
    """, nativeQuery = true)
    List<Object[]> getDailyGraphAggregatesNative(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = """
    SELECT 
        COUNT(DISTINCT w.id),
        COALESCE(SUM(w.duration), 0),
        COALESCE(SUM(ws.weight * ws.reps), 0),
        COUNT(ws.id)
    FROM workouts w
    LEFT JOIN workout_exercises we ON we.workout_id = w.id
    LEFT JOIN workout_sets ws ON ws.workout_exercise_id = we.id
    WHERE w.user_id = :userId
    AND w.workout_status = 'COMPLETED'
    AND w.ended_at >= :startDate
    AND w.ended_at < :endDate
    """, nativeQuery = true)
    List<Object[]> getMonthlyStatsNative(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = """
    SELECT 
        e.id,
        e.name,
        e.image_url,
        COUNT(DISTINCT w.id) as timesPerformed
    FROM workouts w
    JOIN workout_exercises we ON we.workout_id = w.id
    JOIN exercises e ON e.id = we.exercise_id
    WHERE w.user_id = :userId
    AND w.workout_status = 'COMPLETED'
    AND w.ended_at >= :startDate
    GROUP BY e.id, e.name, e.image_url
    ORDER BY timesPerformed DESC
    LIMIT 10
    """, nativeQuery = true)
    List<Object[]> getFrequentExercisesNative(Long userId, LocalDateTime startDate);

    @Query(value = """
    SELECT DISTINCT e.*
    FROM workouts w
    JOIN workout_exercises we ON we.workout_id = w.id
    JOIN exercises e ON e.id = we.exercise_id
    WHERE w.user_id = :userId
    AND w.workout_status = 'COMPLETED'
    ORDER BY e.name
    """, nativeQuery = true)
    List<Exercise> getUserPerformedExercises(Long userId);

    @Query("""
    SELECT w
    FROM Workout w
    WHERE w.user.id = :userId
    AND w.workoutStatus = 'COMPLETED'
    AND w.endedAt >= :startDate
    AND w.endedAt < :endDate
    ORDER BY w.endedAt
    """)
    List<Workout> getWorkoutsForCalendar(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}