package com.pm.formfitbackend.workout.entity;


import com.pm.formfitbackend.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
        @Table(
                name = "workouts",
                indexes = {
                      @Index(name = "idx_workout_user", columnList = "user_id"),
                      @Index(name = "idx_workout_user_started_at",columnList = "user_id,started_at")
                }
        )
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workout_seq_gen")
    @SequenceGenerator(
            name = "workout_seq_gen",
            sequenceName = "workout_id_seq",
            allocationSize = 50
    )
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkoutStatus workoutStatus;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at", nullable = false)
    private LocalDateTime endedAt;

    @Column
    private Integer duration;

    @OneToMany(mappedBy = "workout",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
    )
    private List<WorkoutExercise> exercises = new ArrayList<>();

    @Version
    private Long version;

    public void addExercise(WorkoutExercise exercise) {
        exercises.add(exercise);
        exercise.setWorkout(this);
    }

    public void removeExercise(WorkoutExercise exercise) {
        exercises.remove(exercise);
        exercise.setWorkout(null);
    }


    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkoutStatus getStatus() {
        return workoutStatus;
    }

    public void setStatus(WorkoutStatus workoutStatus) {
        this.workoutStatus = workoutStatus;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<WorkoutExercise> getExercises() {
        return exercises;
    }

    public Long getVersion() {
        return version;
    }

}
