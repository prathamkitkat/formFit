package com.pm.formfitbackend.template.entity;

import com.pm.formfitbackend.exercise.entity.Exercise;
import jakarta.persistence.*;

@Entity
@Table(
        name = "template_exercises",
        indexes = {
                @Index(name = "idx_te_template", columnList = "template_id"),
                @Index(name = "idx_te_exercise", columnList = "exercise_id")
        }
)
public class TemplateExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "template_exercise_seq_gen")
    @SequenceGenerator(
            name = "template_exercise_seq_gen",
            sequenceName = "template_exercise_id_seq",
            allocationSize = 50
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "default_set_count", nullable = false)
    private Integer defaultSetCount = 0;

    public TemplateExercise() {
    }

    /* ================== Getters & Setters ================== */

    public Long getId() {
        return id;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
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

    public Integer getDefaultSetCount() {
        return defaultSetCount;
    }

    public void setDefaultSetCount(Integer defaultSetCount) {
        this.defaultSetCount = defaultSetCount;
    }
}
