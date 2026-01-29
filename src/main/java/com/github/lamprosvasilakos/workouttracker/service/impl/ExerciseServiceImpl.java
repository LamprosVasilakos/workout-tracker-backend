package com.github.lamprosvasilakos.workouttracker.service.impl;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.ExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.entity.Exercise;
import com.github.lamprosvasilakos.workouttracker.entity.MuscleGroup;
import com.github.lamprosvasilakos.workouttracker.entity.User;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectAlreadyExistsException;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;
import com.github.lamprosvasilakos.workouttracker.mapper.ExerciseMapper;
import com.github.lamprosvasilakos.workouttracker.repository.ExerciseRepository;
import com.github.lamprosvasilakos.workouttracker.repository.UserRepository;
import com.github.lamprosvasilakos.workouttracker.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ExerciseResponse createExercise(CreateExerciseRequest request, UUID userId) throws AppObjectAlreadyExistsException, AppObjectNotFoundException {
        User user = findUserById(userId);

        if (exerciseRepository.existsByUserIdAndMuscleGroupAndExerciseNameIgnoreCase(userId, request.muscleGroup(), request.name())) {
            throw new AppObjectAlreadyExistsException("Exercise", 
                "Exercise with name " + request.name() + " already exists for muscle group " + request.muscleGroup());
        }

        Exercise exercise = exerciseMapper.toEntity(request);
        exercise.setUser(user);
        Exercise savedExercise = exerciseRepository.save(exercise);
        log.info("User {} created a new exercise: {} ({})", userId, savedExercise.getExerciseName(), savedExercise.getMuscleGroup());
        return exerciseMapper.toResponse(savedExercise);
    }

    @Override
    @Transactional
    public ExerciseResponse updateExercise(UUID exerciseId, UpdateExerciseRequest request, UUID userId) throws AppObjectNotFoundException, AppObjectAlreadyExistsException {
        Exercise exercise = findExerciseByIdAndUserId(exerciseId, userId);

        if (request.name() != null || request.muscleGroup() != null) {
            String name = request.name() != null ? request.name() : exercise.getExerciseName();
            MuscleGroup muscleGroup = request.muscleGroup() != null ? request.muscleGroup() : exercise.getMuscleGroup();
            
            exerciseRepository.findByUserIdAndMuscleGroupAndExerciseNameIgnoreCase(userId, muscleGroup, name)
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(exerciseId)) {
                            // Note: Since this is inside ifPresent, we can't throw a checked exception directly 
                            // unless we wrap it or use a different approach.
                            // However, the original code had this throw. Let's fix the lambda issue.
                        }
                    });
            
            // Refactoring to avoid checked exception in lambda
            var existingExercise = exerciseRepository.findByUserIdAndMuscleGroupAndExerciseNameIgnoreCase(userId, muscleGroup, name);
            if (existingExercise.isPresent() && !existingExercise.get().getId().equals(exerciseId)) {
                throw new AppObjectAlreadyExistsException("Exercise", 
                    "Another exercise with name " + name + " already exists for muscle group " + muscleGroup);
            }
        }

        exerciseMapper.updateEntityFromRequest(request, exercise);
        Exercise updatedExercise = exerciseRepository.save(exercise);
        log.info("User {} updated exercise with ID {}", userId, exerciseId);
        return exerciseMapper.toResponse(updatedExercise);
    }

    @Override
    @Transactional
    public void deleteExercise(UUID exerciseId, UUID userId) throws AppObjectNotFoundException {
        Exercise exercise = findExerciseByIdAndUserId(exerciseId, userId);
        exerciseRepository.delete(exercise);
        log.info("User {} deleted exercise with ID {}", userId, exerciseId);
    }

    @Override
    @Transactional(readOnly = true)
    public ExerciseResponse getExerciseById(UUID exerciseId, UUID userId) throws AppObjectNotFoundException {
        Exercise exercise = findExerciseByIdAndUserId(exerciseId, userId);
        return exerciseMapper.toResponse(exercise);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseResponse> getExercisesByMuscleGroup(MuscleGroup muscleGroup, UUID userId) {
        return exerciseRepository.findByUserIdAndMuscleGroupOrderByExerciseNameAsc(userId, muscleGroup).stream()
                .map(exerciseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseResponse> getAllExercises(UUID userId) {
        return exerciseRepository.findAll().stream()
                .filter(e -> e.getUser().getId().equals(userId))
                .map(exerciseMapper::toResponse)
                .collect(Collectors.toList());
    }

    private User findUserById(UUID userId) throws AppObjectNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with ID " + userId + " not found"));
    }

    private Exercise findExerciseByIdAndUserId(UUID exerciseId, UUID userId) throws AppObjectNotFoundException {
        return exerciseRepository.findByIdAndUserId(exerciseId, userId)
                .orElseThrow(() -> new AppObjectNotFoundException("Exercise", 
                    "Exercise with ID " + exerciseId + " not found for user " + userId));
    }
}
