package com.github.lamprosvasilakos.workouttracker.service.impl;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateWorkoutExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateWorkoutExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.entity.Exercise;
import com.github.lamprosvasilakos.workouttracker.entity.Workout;
import com.github.lamprosvasilakos.workouttracker.entity.WorkoutExercise;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;
import com.github.lamprosvasilakos.workouttracker.mapper.WorkoutExerciseMapper;
import com.github.lamprosvasilakos.workouttracker.repository.ExerciseRepository;
import com.github.lamprosvasilakos.workouttracker.repository.WorkoutExerciseRepository;
import com.github.lamprosvasilakos.workouttracker.service.WorkoutExerciseService;
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
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutExerciseMapper workoutExerciseMapper;
    private final ExerciseRepository exerciseRepository;

    @Override
    @Transactional
    public WorkoutExerciseResponse createWorkoutExercise(CreateWorkoutExerciseRequest request, Workout workout, UUID userId) throws AppObjectNotFoundException {
        Exercise exercise = exerciseRepository.findByIdAndUserId(request.exerciseId(), userId)
                .orElseThrow(() -> new AppObjectNotFoundException("Exercise", 
                    "Exercise with ID " + request.exerciseId() + " not found for user " + userId));

        WorkoutExercise workoutExercise = workoutExerciseMapper.toEntity(request);
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);

        if (workoutExercise.getSets() != null) {
            workoutExercise.getSets().forEach(set -> set.setWorkoutExercise(workoutExercise));
        }

        WorkoutExercise savedWorkoutExercise = workoutExerciseRepository.save(workoutExercise);
        log.info("User {} added exercise {} to workout {}", userId, exercise.getExerciseName(), workout.getId());
        return workoutExerciseMapper.toResponse(savedWorkoutExercise);
    }

    @Override
    @Transactional
    public WorkoutExerciseResponse updateWorkoutExercise(UUID workoutExerciseId, UpdateWorkoutExerciseRequest request, UUID userId) throws AppObjectNotFoundException {
        WorkoutExercise workoutExercise = findWorkoutExerciseById(workoutExerciseId);

        if (!workoutExercise.getWorkout().getUser().getId().equals(userId)) {
            throw new AppObjectNotFoundException("WorkoutExercise", 
                "WorkoutExercise with ID " + workoutExerciseId + " not found for user " + userId);
        }

        if (request.exerciseId() != null) {
            Exercise exercise = exerciseRepository.findByIdAndUserId(request.exerciseId(), userId)
                    .orElseThrow(() -> new AppObjectNotFoundException("Exercise", 
                        "Exercise with ID " + request.exerciseId() + " not found for user " + userId));
            workoutExercise.setExercise(exercise);
        }

        workoutExerciseMapper.updateEntityFromRequest(request, workoutExercise);
        
        if (workoutExercise.getSets() != null) {
            workoutExercise.getSets().forEach(set -> set.setWorkoutExercise(workoutExercise));
        }

        WorkoutExercise updatedWorkoutExercise = workoutExerciseRepository.save(workoutExercise);
        log.info("User {} updated workout exercise with ID {}", userId, workoutExerciseId);
        return workoutExerciseMapper.toResponse(updatedWorkoutExercise);
    }

    @Override
    @Transactional
    public void deleteWorkoutExercise(UUID workoutExerciseId, UUID userId) throws AppObjectNotFoundException {
        WorkoutExercise workoutExercise = findWorkoutExerciseById(workoutExerciseId);
        
        if (!workoutExercise.getWorkout().getUser().getId().equals(userId)) {
            throw new AppObjectNotFoundException("WorkoutExercise", 
                "WorkoutExercise with ID " + workoutExerciseId + " not found for user " + userId);
        }

        workoutExerciseRepository.delete(workoutExercise);
        log.info("User {} deleted workout exercise with ID {}", userId, workoutExerciseId);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutExerciseResponse getWorkoutExerciseById(UUID workoutExerciseId, UUID userId) throws AppObjectNotFoundException {
        WorkoutExercise workoutExercise = findWorkoutExerciseById(workoutExerciseId);
        
        if (!workoutExercise.getWorkout().getUser().getId().equals(userId)) {
            throw new AppObjectNotFoundException("WorkoutExercise", 
                "WorkoutExercise with ID " + workoutExerciseId + " not found for user " + userId);
        }

        return workoutExerciseMapper.toResponse(workoutExercise);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutExerciseResponse> getAllWorkoutExercisesByWorkoutId(UUID workoutId, UUID userId) {
        return workoutExerciseRepository.findAllByWorkoutIdOrderByExerciseOrderAsc(workoutId).stream()
                .filter(we -> we.getWorkout().getUser().getId().equals(userId))
                .map(workoutExerciseMapper::toResponse)
                .collect(Collectors.toList());
    }

    private WorkoutExercise findWorkoutExerciseById(UUID workoutExerciseId) throws AppObjectNotFoundException {
        return workoutExerciseRepository.findById(workoutExerciseId)
                .orElseThrow(() -> new AppObjectNotFoundException("WorkoutExercise", 
                    "WorkoutExercise with ID " + workoutExerciseId + " not found"));
    }
}
