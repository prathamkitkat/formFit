package com.pm.formfitbackend.template.controller;

import com.pm.formfitbackend.security.CustomUserDetails;
import com.pm.formfitbackend.template.dto.*;
import com.pm.formfitbackend.template.service.TemplateService;
import com.pm.formfitbackend.workout.dto.WorkoutDetailResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    /**
     * Create new empty template
     */
    @PostMapping
    public ResponseEntity<TemplateDetailResponse> createTemplate(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        TemplateDetailResponse response =
                templateService.createTemplate(userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all templates (summary view)
     */
    @GetMapping
    public ResponseEntity<List<TemplateSummaryResponse>> getAllTemplates(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<TemplateSummaryResponse> templates =
                templateService.getAllTemplates(userDetails.getUserId());

        return ResponseEntity.ok(templates);
    }

    /**
     * Get template by ID (detail view)
     */
    @GetMapping("/{templateId}")
    public ResponseEntity<TemplateDetailResponse> getTemplateById(
            @PathVariable Long templateId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        TemplateDetailResponse response =
                templateService.getTemplateById(templateId, userDetails.getUserId());

        return ResponseEntity.ok(response);
    }

    /**
     * Update template name
     */
    @PutMapping("/{templateId}/name")
    public ResponseEntity<Void> updateTemplateName(
            @PathVariable Long templateId,
            @Valid @RequestBody UpdateTemplateNameRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        templateService.updateTemplateName(
                templateId,
                request.getName(),
                userDetails.getUserId()
        );

        return ResponseEntity.noContent().build();
    }

    /**
     * Delete entire template
     */
    @DeleteMapping("/{templateId}")
    public ResponseEntity<Void> deleteTemplate(
            @PathVariable Long templateId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        templateService.deleteTemplate(templateId, userDetails.getUserId());

        return ResponseEntity.noContent().build();
    }

    /**
     * Add exercise to template
     */
    @PostMapping("/{templateId}/exercises")
    public ResponseEntity<TemplateDetailResponse> addExerciseToTemplate(
            @PathVariable Long templateId,
            @Valid @RequestBody AddTemplateExerciseRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        TemplateDetailResponse response =
                templateService.addExercisesToTemplate(
                        templateId,
                        request.getExerciseIds(),
                        userDetails.getUserId()
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Delete template exercise
     */
    @DeleteMapping("/{templateId}/exercises/{templateExerciseId}")
    public ResponseEntity<TemplateDetailResponse> deleteTemplateExercise(
            @PathVariable Long templateId,
            @PathVariable Long templateExerciseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        TemplateDetailResponse response =
                templateService.deleteTemplateExercise(
                        templateId,
                        templateExerciseId,
                        userDetails.getUserId()
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Increment default set count
     */
    @PostMapping("/{templateId}/exercises/{templateExerciseId}/sets/increment")
    public ResponseEntity<Void> incrementSetCount(
            @PathVariable Long templateId,
            @PathVariable Long templateExerciseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        templateService.incrementDefaultSetCount(
                templateId,
                templateExerciseId,
                userDetails.getUserId()
        );

        return ResponseEntity.noContent().build();
    }

    /**
     * Decrement default set count
     */
    @PostMapping("/{templateId}/exercises/{templateExerciseId}/sets/decrement")
    public ResponseEntity<Void> decrementSetCount(
            @PathVariable Long templateId,
            @PathVariable Long templateExerciseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        templateService.decrementDefaultSetCount(
                templateId,
                templateExerciseId,
                userDetails.getUserId()
        );

        return ResponseEntity.noContent().build();
    }

    /**
     * Start workout from template
     */
    @PostMapping("/{templateId}/start")
    public ResponseEntity<WorkoutDetailResponse> startWorkoutFromTemplate(
            @PathVariable Long templateId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        WorkoutDetailResponse response =
                templateService.startWorkoutFromTemplate(
                        templateId,
                        userDetails.getUserId()
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //save template

    @PostMapping("/{templateId}/save")
    public ResponseEntity<Void> saveTemplate(
            @PathVariable Long templateId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        templateService.saveTemplate(templateId, userDetails.getUserId());

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<TemplateDetailResponse> updateTemplate(
            @PathVariable Long templateId,
            @Valid @RequestBody TemplateDetailResponse request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        TemplateDetailResponse response =
                templateService.updateTemplate(
                        templateId,
                        request,
                        userDetails.getUserId()
                );

        return ResponseEntity.ok(response);
    }
}