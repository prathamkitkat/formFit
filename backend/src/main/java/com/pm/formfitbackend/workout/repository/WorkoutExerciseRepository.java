package com.pm.formfitbackend.workout.repository;

import com.pm.formfitbackend.workout.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
    boolean existsByExercise_Id(Long exerciseId);

    /**
     * Find workout exercise by ID with workout and exercise loaded
     * Validates it belongs to the specified workout
     */
    @Query("SELECT we FROM WorkoutExercise we " +
            "JOIN FETCH we.workout w " +
            "JOIN FETCH we.exercise " +
            "WHERE we.id = :workoutExerciseId " +
            "AND w.id = :workoutId")
    Optional<WorkoutExercise> findByIdAndWorkoutId(
            @Param("workoutExerciseId") Long workoutExerciseId,
            @Param("workoutId") Long workoutId
    );

    /**
     * Find workout exercise with all its sets loaded
     */
    @Query("SELECT we FROM WorkoutExercise we " +
            "LEFT JOIN FETCH we.sets " +
            "WHERE we.id = :workoutExerciseId " +
            "AND we.workout.id = :workoutId")
    Optional<WorkoutExercise> findByIdAndWorkoutIdWithSets(
            @Param("workoutExerciseId") Long workoutExerciseId,
            @Param("workoutId") Long workoutId
    );

}
