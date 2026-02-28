package com.pm.formfitbackend.exercise.entity;

import com.pm.formfitbackend.user.entity.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "exercises",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "ux_exercise_user_name",
                        columnNames = {"user_id", "name"}
                )
        }
)
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_seq_gen")
    @SequenceGenerator(
            name = "exercise_seq_gen",
            sequenceName = "exercise_id_seq",
            allocationSize = 50
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String equipment;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "video_url")
    private String videoUrl;

    // Null means global exercise
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private boolean archived = false;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "exercise_muscle_groups",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "muscle_group_id")
    )
    private Set<MuscleGroup> muscleGroups = new HashSet<>();

    public Exercise() {
    }

    /* ================== Getters & Setters ================== */

    public Long getId() {
        return id;
    }
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }

    public void addMuscleGroup(MuscleGroup muscleGroup) {
        this.muscleGroups.add(muscleGroup);
        muscleGroup.getExercises().add(this);
    }

    public void removeMuscleGroup(MuscleGroup muscleGroup) {
        this.muscleGroups.remove(muscleGroup);
        muscleGroup.getExercises().remove(this);
    }
}
