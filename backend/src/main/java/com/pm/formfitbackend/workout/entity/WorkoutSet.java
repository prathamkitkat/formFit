package com.pm.formfitbackend.workout.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "workout_sets",
        indexes = {
                @Index(name = "idx_set_workout_exercise", columnList = "workout_exercise_id")
        }
)
public class WorkoutSet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workout_set_seq_gen")
    @SequenceGenerator(
            name = "workout_set_seq_gen",
            sequenceName = "workout_set_id_seq",
            allocationSize = 50
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column
    private Integer reps;   // nullable while IN_PROGRESS

    @Column
    private Double weight;  // nullable while IN_PROGRESS

    @Column(name = "is_drop_set", nullable = false)
    private Boolean isDropSet = false;



    /* ================== Getters & Setters ================== */

    public Long getId() {
        return id;
    }

    public WorkoutExercise getWorkoutExercise() {
        return workoutExercise;
    }

    public void setWorkoutExercise(WorkoutExercise workoutExercise) {
        this.workoutExercise = workoutExercise;
    }

    public Integer getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Boolean getIsDropSet() {
        return isDropSet;
    }

    public void setIsDropSet(Boolean dropSet) {
        isDropSet = dropSet;
    }
}
