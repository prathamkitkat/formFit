package com.pm.formfitbackend.exercise.repository;

import com.pm.formfitbackend.exercise.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    boolean existsByUserIdAndNameAndArchivedFalse(Long userId, String name);

    @Query("""
       SELECT e FROM Exercise e
       WHERE e.archived = false
       AND (e.user IS NULL OR e.user.id = :userId)
       """)
    List<Exercise> findAvailableExercisesForUser(@Param("userId") Long userId);

    @Query("""
       SELECT e FROM Exercise e
       WHERE e.id = :id
       AND e.archived = false
       AND (e.user IS NULL OR e.user.id = :userId)
       """)
    Optional<Exercise> findAccessibleExerciseById(
            @Param("id") Long id,
            @Param("userId") Long userId);
}
