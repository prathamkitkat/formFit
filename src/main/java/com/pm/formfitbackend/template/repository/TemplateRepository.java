package com.pm.formfitbackend.template.repository;

import com.pm.formfitbackend.template.entity.Template;
import com.pm.formfitbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {


        Optional<Template> findByIdAndUserId(Long templateId, Long userId);

        @Query("""
        SELECT DISTINCT t
        FROM Template t
        LEFT JOIN FETCH t.exercises te
        LEFT JOIN FETCH te.exercise e
        WHERE t.id = :templateId
        AND t.user.id = :userId
    """)
        Optional<Template> findByIdAndUserIdWithExercises(
                Long templateId,
                Long userId
        );

        @Query("""
        SELECT DISTINCT t
        FROM Template t
        LEFT JOIN FETCH t.exercises te
        LEFT JOIN FETCH te.exercise e
        WHERE t.user.id = :userId
    """)
        List<Template> findAllByUserIdWithExercises(Long userId);

        List<Template> findByUserId(Long userId);

    Long user(User user);
}


