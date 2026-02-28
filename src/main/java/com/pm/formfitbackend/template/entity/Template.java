package com.pm.formfitbackend.template.entity;

import com.pm.formfitbackend.user.entity.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "templates",
    indexes = {
        @Index(name = "idx_template_user", columnList = "user_id")
    }
)
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "template_seq_gen")
    @SequenceGenerator(name = "template_seq_gen", sequenceName = "template_id_seq" ,
    allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column
    private String name;

    @OneToMany(
            mappedBy = "template",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<TemplateExercise> exercises = new ArrayList<>();

    @Version
    private Long version;

    public void addExercise(TemplateExercise exercise){
        exercises.add(exercise);
        exercise.setTemplate(this);
    }
    public void removeExercise(TemplateExercise exercise){
        exercises.remove(exercise);
        exercise.setTemplate(null);
    }

    /* ================== Getters & Setters ================== */

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

    public List<TemplateExercise> getExercises() {
        return exercises;
    }

    public Long getVersion() {
        return version;
    }
}
