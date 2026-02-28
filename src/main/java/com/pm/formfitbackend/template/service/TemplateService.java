package com.pm.formfitbackend.template.service;

import com.pm.formfitbackend.exercise.entity.Exercise;
import com.pm.formfitbackend.exercise.repository.ExerciseRepository;
import com.pm.formfitbackend.template.dto.*;
import com.pm.formfitbackend.template.entity.Template;
import com.pm.formfitbackend.template.entity.TemplateExercise;
import com.pm.formfitbackend.template.mapper.TemplateMapper;
import com.pm.formfitbackend.template.repository.TemplateRepository;
import com.pm.formfitbackend.user.entity.User;
import com.pm.formfitbackend.user.repository.UserRepository;
import com.pm.formfitbackend.workout.dto.WorkoutDetailResponse;
import com.pm.formfitbackend.workout.entity.*;
import com.pm.formfitbackend.workout.mapper.WorkoutMapper;
import com.pm.formfitbackend.workout.repository.WorkoutRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;

    public TemplateService(
            TemplateRepository templateRepository,
            ExerciseRepository exerciseRepository,
            UserRepository userRepository,
            WorkoutRepository workoutRepository
    ) {
        this.templateRepository = templateRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
    }

    /* ================= CREATE ================= */

    public TemplateDetailResponse createTemplate(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Template template = new Template();
        template.setUser(user);
        template.setName(null);

        template = templateRepository.save(template);

        return TemplateMapper.toDetailResponse(template);
    }

    /* ================= GET ================= */

    public List<TemplateSummaryResponse> getAllTemplates(Long userId) {

        return templateRepository.findAllByUserIdWithExercises(userId)
                .stream()
                .map(TemplateMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    public TemplateDetailResponse getTemplateById(Long templateId, Long userId) {

        Template template = templateRepository
                .findByIdAndUserIdWithExercises(templateId, userId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        return TemplateMapper.toDetailResponse(template);
    }

    /* ================= UPDATE NAME ================= */

    public void updateTemplateName(Long templateId, String name, Long userId) {

        if (name == null || name.trim().isBlank()) {
            throw new RuntimeException("Template name cannot be blank");
        }

        Template template = templateRepository
                .findByIdAndUserId(templateId, userId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        template.setName(name.trim());
    }

    /* ================= FULL UPDATE (PUT /{templateId}) ================= */

    public TemplateDetailResponse updateTemplate(
            Long templateId,
            TemplateDetailResponse request,
            Long userId
    ) {

        Template template = templateRepository
                .findByIdAndUserIdWithExercises(templateId, userId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        // Update name
        if (request.getName() == null || request.getName().trim().isBlank()) {
            throw new RuntimeException("Template name cannot be blank");
        }
        template.setName(request.getName());

        // Clear existing exercises (orphanRemoval = true will delete)
        template.getExercises().clear();

        // Rebuild from request
        for (TemplateExerciseDetailResponse dto : request.getExercises()) {

            Exercise exercise = exerciseRepository.findAccessibleExerciseById(dto.getExerciseId(),userId)
                    .orElseThrow(() -> new RuntimeException("Exercise not found"));

            TemplateExercise te = new TemplateExercise();
            te.setExercise(exercise);
            te.setOrderIndex(dto.getOrderIndex());
            te.setDefaultSetCount(dto.getDefaultSetCount());

            template.addExercise(te);
        }

//        Safer approach:
//
//        Ignore frontend orderIndex and reassign sequentially:
//
//        int index = 0;
//        for (TemplateExerciseDetailResponse dto : request.getExercises()) {
//            te.setOrderIndex(index++);
//        }

        return TemplateMapper.toDetailResponse(template);
    }

    /* ================= DELETE TEMPLATE ================= */

    public void deleteTemplate(Long templateId, Long userId) {

        Template template = templateRepository
                .findByIdAndUserId(templateId, userId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        templateRepository.delete(template);
    }

    /* ================= ADD EXERCISES ================= */

    public TemplateDetailResponse addExercisesToTemplate(
            Long templateId,
            List<Long> exerciseIds,
            Long userId
    ) {

        Template template = templateRepository
                .findByIdAndUserIdWithExercises(templateId, userId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        int maxOrder = template.getExercises().stream()
                .mapToInt(TemplateExercise::getOrderIndex)
                .max()
                .orElse(0);

        for (Long exerciseId : exerciseIds) {

            Exercise exercise = exerciseRepository.findAccessibleExerciseById(exerciseId,userId)
                    .orElseThrow(() -> new RuntimeException("Exercise not found"));

            TemplateExercise te = new TemplateExercise();
            te.setExercise(exercise);
            te.setOrderIndex(++maxOrder);
            te.setDefaultSetCount(0);

            template.addExercise(te);
        }

        return TemplateMapper.toDetailResponse(template);
    }

    /* ================= DELETE TEMPLATE EXERCISE ================= */

    public TemplateDetailResponse deleteTemplateExercise(
            Long templateId,
            Long templateExerciseId,
            Long userId
    ) {

        Template template = templateRepository
                .findByIdAndUserIdWithExercises(templateId, userId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        TemplateExercise te = template.getExercises().stream()
                .filter(e -> e.getId().equals(templateExerciseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Template exercise not found"));

        template.removeExercise(te);

        // Renumber
        List<TemplateExercise> sorted =
                template.getExercises().stream()
                        .sorted(Comparator.comparing(TemplateExercise::getOrderIndex))
                        .collect(Collectors.toList());

        for (int i = 0; i < sorted.size(); i++) {
            sorted.get(i).setOrderIndex(i+1);
        }

        return TemplateMapper.toDetailResponse(template);
    }

    /* ================= SET COUNT ================= */

    public void incrementDefaultSetCount(
            Long templateId,
            Long templateExerciseId,
            Long userId
    ) {

        Template template = templateRepository
                .findByIdAndUserIdWithExercises(templateId, userId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        TemplateExercise te = template.getExercises().stream()
                .filter(e -> e.getId().equals(templateExerciseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Template exercise not found"));

        te.setDefaultSetCount(te.getDefaultSetCount() + 1);
    }

    public void decrementDefaultSetCount(
            Long templateId,
            Long templateExerciseId,
            Long userId
    ) {

        Template template = templateRepository
                .findByIdAndUserIdWithExercises(templateId, userId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        TemplateExercise te = template.getExercises().stream()
                .filter(e -> e.getId().equals(templateExerciseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Template exercise not found"));

        if (te.getDefaultSetCount() > 0) {
            te.setDefaultSetCount(te.getDefaultSetCount() - 1);
        }
    }

    /* ================= SAVE TEMPLATE ================= */

    public void saveTemplate(Long templateId, Long userId) {

        Template template = templateRepository
                .findByIdAndUserId(templateId, userId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (template.getName() == null || template.getName().trim().isBlank()) {
            throw new RuntimeException("Template name required before saving");
        }
    }

    /* ================= START WORKOUT FROM TEMPLATE ================= */

    public WorkoutDetailResponse startWorkoutFromTemplate(
            Long templateId,
            Long userId
    ) {

        // Check active workout
        workoutRepository.findByUserIdAndStatus(userId, WorkoutStatus.IN_PROGRESS)
                .ifPresent(w -> {
                    throw new RuntimeException("Active workout already exists");
                });

        Template template = templateRepository
                .findByIdAndUserIdWithExercises(templateId, userId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        Workout workout = new Workout();
        workout.setUser(template.getUser());
        workout.setName(template.getName());
        workout.setStatus(WorkoutStatus.IN_PROGRESS);
        workout.setStartedAt(LocalDateTime.now());

        for (TemplateExercise te : template.getExercises()) {

            WorkoutExercise we = new WorkoutExercise();
            we.setExercise(te.getExercise());
            we.setOrderIndex(te.getOrderIndex());

            Integer count = te.getDefaultSetCount();
            if (count != null && count > 0) {
                for (int i = 1; i <= count; i++) {
                    WorkoutSet set = new WorkoutSet();
                    set.setSetNumber(i);
                    we.addSet(set);
                }
            }

            workout.addExercise(we);
        }

        workout = workoutRepository.save(workout);

        return WorkoutMapper.toDetailResponse(workout, List.of());
    }
}