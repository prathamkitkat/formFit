package com.pm.formfitbackend.exercise.mapper;

import com.pm.formfitbackend.exercise.dto.ExerciseResponseDTO;
import com.pm.formfitbackend.exercise.entity.Exercise;
import com.pm.formfitbackend.exercise.entity.MuscleGroup;

import java.util.List;
import java.util.stream.Collectors;

public class ExerciseMapper {

    public static ExerciseResponseDTO toDTO(Exercise exercise) {

        ExerciseResponseDTO dto = new ExerciseResponseDTO();

        dto.setExerciseId(exercise.getId());
        dto.setName(exercise.getName());
        dto.setEquipment(exercise.getEquipment());
        dto.setImageUrl(exercise.getImageUrl());
        dto.setVideoUrl(exercise.getVideoUrl());

        // Convert MuscleGroup entities to list of names
        List<String> muscleGroupNames = exercise.getMuscleGroups()
                .stream()
                .map(MuscleGroup::getName)
                .collect(Collectors.toList());

        dto.setMuscleGroups(muscleGroupNames);

        // Derived field
        dto.setIsCustom(exercise.getUser() != null);

        return dto;
    }
}
