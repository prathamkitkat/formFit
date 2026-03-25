package com.pm.formfitbackend.exercise.service;


import com.pm.formfitbackend.exercise.dto.CreateCustomExerciseRequestDTO;
import com.pm.formfitbackend.exercise.dto.ExerciseResponseDTO;
import com.pm.formfitbackend.exercise.entity.Exercise;
import com.pm.formfitbackend.exercise.entity.MuscleGroup;
import com.pm.formfitbackend.exercise.mapper.ExerciseMapper;
import com.pm.formfitbackend.exercise.repository.ExerciseRepository;
import com.pm.formfitbackend.exercise.repository.MuscleGroupRepository;
import com.pm.formfitbackend.user.entity.User;
import com.pm.formfitbackend.user.repository.UserRepository;
import com.pm.formfitbackend.workout.repository.WorkoutExerciseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final MuscleGroupRepository muscleGroupRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository, UserRepository userRepository, MuscleGroupRepository muscleGroupRepository,WorkoutExerciseRepository workoutExerciseRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.muscleGroupRepository = muscleGroupRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    public List<ExerciseResponseDTO> getAllExercises(Long userId) {
        List<Exercise> exercises =
                exerciseRepository.findAvailableExercisesForUser(userId);
        List<ExerciseResponseDTO> exerciseResponseDTOS = exercises.stream().map(ExerciseMapper::toDTO).toList();
        return exerciseResponseDTOS;
    }

    public ExerciseResponseDTO getExerciseById(Long id, Long userId) {
        Exercise exercise = exerciseRepository
                .findAccessibleExerciseById(id, userId)
                .orElseThrow(() ->
                        new RuntimeException("Exercise not found"));
        return ExerciseMapper.toDTO(exercise);
    }

    public ExerciseResponseDTO createCustomExercise(
            CreateCustomExerciseRequestDTO request,
            Long userId
    ) {


        if (exerciseRepository.existsByUserIdAndNameAndArchivedFalse(userId, request.getName().trim())) {
            throw new RuntimeException("Exercise name already exists");
        }


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        List<MuscleGroup> muscleGroups =
                muscleGroupRepository.findAllById(request.getMuscleGroupIds());

        if (muscleGroups.size() != request.getMuscleGroupIds().size()) {
            throw new RuntimeException("Invalid muscle group IDs");
        }


        Exercise exercise = new Exercise();
        exercise.setName(request.getName().trim());
        exercise.setEquipment(request.getEquipment());
        exercise.setImageUrl(request.getImageUrl());
        exercise.setVideoUrl(request.getVideoUrl());
        exercise.setUser(user);
        for (MuscleGroup muscleGroup : muscleGroups) {
            exercise.addMuscleGroup(muscleGroup);
        }

        Exercise saved = exerciseRepository.save(exercise);

        return ExerciseMapper.toDTO(saved);
    }

    public ExerciseResponseDTO updateCustomExercise(
            Long exerciseId,
            CreateCustomExerciseRequestDTO request,
            Long userId
    ) {


        Exercise exercise = exerciseRepository
                .findAccessibleExerciseById(exerciseId, userId)
                .orElseThrow(() ->
                        new RuntimeException("Exercise not found"));


        if (exercise.getUser() == null || !exercise.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to modify this exercise");
        }
//        if (exercise.isArchived()) {
//            throw new RuntimeException("Exercise is archived");
//        }

        String trimmedName = request.getName().trim();

        if (!exercise.getName().equals(trimmedName) &&
                exerciseRepository.existsByUserIdAndNameAndArchivedFalse(userId, trimmedName)) {

            throw new RuntimeException("Exercise name already exists");
        }

        exercise.setName(trimmedName);
        exercise.setEquipment(request.getEquipment());
        exercise.setImageUrl(request.getImageUrl());
        exercise.setVideoUrl(request.getVideoUrl());


        List<MuscleGroup> muscleGroups =
                muscleGroupRepository.findAllById(request.getMuscleGroupIds());

        if (muscleGroups.size() != request.getMuscleGroupIds().size()) {
            throw new RuntimeException("Invalid muscle group IDs");
        }


        for (MuscleGroup mg : new HashSet<>(exercise.getMuscleGroups())) {
            exercise.removeMuscleGroup(mg);
        }


        for (MuscleGroup mg : muscleGroups) {
            exercise.addMuscleGroup(mg);
        }


        Exercise updated = exerciseRepository.save(exercise);


        return ExerciseMapper.toDTO(updated);
    }

    public void deleteCustomExercise(Long exerciseId, Long userId) {

        Exercise exercise = exerciseRepository
                .findAccessibleExerciseById(exerciseId, userId)
                .orElseThrow(() ->
                        new RuntimeException("Exercise not found"));


        if (exercise.getUser() == null ||
                !exercise.getUser().getId().equals(userId)) {

            throw new RuntimeException("You are not allowed to delete this exercise");
        }

        if (exercise.isArchived()){
            return;
        }

        boolean usedInWorkout =
                workoutExerciseRepository.existsByExercise_Id(exerciseId);

        if (usedInWorkout) {
            // Soft delete
            exercise.setArchived(true);
            exerciseRepository.save(exercise);
        } else {
            // Hard delete (safe because not referenced)
            exerciseRepository.delete(exercise);
        }

    }
}





