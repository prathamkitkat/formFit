package com.pm.formfitbackend.exercise.controller;

import com.pm.formfitbackend.exercise.dto.CreateCustomExerciseRequestDTO;
import com.pm.formfitbackend.exercise.dto.ExerciseResponseDTO;
import com.pm.formfitbackend.exercise.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;
    public ExerciseController(ExerciseService exerciseService){
        this.exerciseService = exerciseService;
    }
    @GetMapping
    public ResponseEntity<List<ExerciseResponseDTO>> getAllExercises(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<ExerciseResponseDTO> exercises = exerciseService.getAllExercises(userDetails.getId());
        return ResponseEntity.ok().body(exercises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> getExerciseById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){
        ExerciseResponseDTO exercise = exerciseService.getExerciseById(id,userDetails.getId());
        return ResponseEntity.ok().body(exercise);
    }




    @PostMapping("/custom")
    public ResponseEntity<ExerciseResponseDTO> createCustomExercise(
            @Valid @RequestBody CreateCustomExerciseRequestDTO customExerciseRequestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        ExerciseResponseDTO response =
                exerciseService.createCustomExercise(customExerciseRequestDTO, userDetails.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/custom/{exerciseId}")
    public ResponseEntity<ExerciseResponseDTO> updateCustomExercise(
            @PathVariable Long exerciseId,
            @Valid @RequestBody CreateCustomExerciseRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        ExerciseResponseDTO response =
                exerciseService.updateCustomExercise(exerciseId, request, userDetails.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/custom/{exerciseId}")
    public ResponseEntity<Void> deleteCustomExercise(
            @PathVariable Long exerciseId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        exerciseService.deleteCustomExercise(exerciseId, userDetails.getId());

        return ResponseEntity.noContent().build();
    }




}

