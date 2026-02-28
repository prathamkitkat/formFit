package com.pm.formfitbackend.workout.repository;

import com.pm.formfitbackend.workout.entity.Workout;
import com.pm.formfitbackend.workout.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {

    /**
     * Find set by ID, validating it belongs to the specified workout exercise
     */
    @Query("SELECT s FROM WorkoutSet s " +
            "WHERE s.id = :setId " +
            "AND s.workoutExercise.id = :workoutExerciseId " +
            "AND s.workoutExercise.workout.id = :workoutId")
    Optional<WorkoutSet> findByIdAndWorkoutExerciseIdAndWorkoutId(
            @Param("setId") Long setId,
            @Param("workoutExerciseId") Long workoutExerciseId,
            @Param("workoutId") Long workoutId
    );

    /**
     * Find the sets from the most recent completed workout where user performed this exercise
     * Used for showing "previous performance" in the UI
     */
    @Query("SELECT ws FROM WorkoutSet ws " +
            "JOIN ws.workoutExercise we " +
            "JOIN we.workout w " +
            "WHERE w.user.id = :userId " +
            "AND we.exercise.id = :exerciseId " +
            "AND w.workoutStatus = 'COMPLETED' " +
            "AND w.id = (" +
            "    SELECT MAX(w2.id) FROM Workout w2 " +
            "    JOIN w2.exercises we2 " +
            "    WHERE w2.user.id = :userId " +
            "    AND we2.exercise.id = :exerciseId " +
            "    AND w2.workoutStatus = 'COMPLETED'" +
            ") " +
            "ORDER BY ws.setNumber")
    List<WorkoutSet> findLastPerformanceForExercise(
            @Param("userId") Long userId,
            @Param("exerciseId") Long exerciseId
    );

}
