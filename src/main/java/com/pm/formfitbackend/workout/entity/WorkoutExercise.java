package com.pm.formfitbackend.workout.entity;

import com.pm.formfitbackend.exercise.entity.Exercise;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(
        name = "workout_exercises",
        indexes = {
                @Index(name = "idx_we_workout",
                columnList = "workout_id"),
                @Index(name = "idx_we_exercise",
                columnList = "exercise_id")
        }
)
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "we_seq_gen")
    @SequenceGenerator(
            name = "we_seq_gen",
            sequenceName = "workout_exercise_id_seq",
            allocationSize = 50
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "workout_id",nullable = false)
    private Workout workout;

    // Many workout exercises can reference one master exercise
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(length = 500)
    private String notes;

    // Aggregate child: Sets
    @OneToMany(
            mappedBy = "workoutExercise",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<WorkoutSet> sets = new ArrayList<>();

    public void addSet(WorkoutSet set) {
        sets.add(set);
        set.setWorkoutExercise(this);
    }

    public void removeSet(WorkoutSet set) {
        sets.remove(set);
        set.setWorkoutExercise(null);
    }

    /* ================== Getters & Setters ================== */

    public Long getId() {
        return id;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<WorkoutSet> getSets() {
        return sets;
    }
}
}
