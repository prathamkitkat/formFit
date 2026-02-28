package com.pm.formfitbackend.workout.repository;


import com.pm.formfitbackend.workout.entity.Workout;
import com.pm.formfitbackend.workout.entity.WorkoutStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long>{
    Optional<Workout> findByUserIdAndStatus(Long userId, WorkoutStatus status);

    Optional<Workout> findByIdAndUserId(Long workoutId, Long userId);

    @Query("""
    SELECT w
    FROM Workout w
    LEFT JOIN FETCH w.exercises we
    LEFT JOIN FETCH we.sets
    WHERE w.id = :workoutId
    AND w.user.id = :userId
""")
    Optional<Workout> findByIdAndUserIdWithExercisesAndSets(
            Long workoutId,
            Long userId
    );

    @Query("""
    SELECT w
    FROM Workout w
    LEFT JOIN FETCH w.exercises we
    WHERE w.id = :workoutId
    AND w.user.id = :userId
""")
    Optional<Workout> findByIdAndUserIdWithExercises(
            Long workoutId,
            Long userId
    );

    List<Workout> findByUserIdAndStatusOrderByStartedAtDesc(
            Long userId,
            WorkoutStatus status
    );
}
