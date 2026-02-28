package com.pm.formfitbackend.template.repository;

import com.pm.formfitbackend.template.entity.TemplateExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateExerciseRepository extends JpaRepository<TemplateExercise, Long> {
}
