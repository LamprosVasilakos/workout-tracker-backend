package com.github.lamprosvasilakos.workouttracker.service;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.ExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.entity.MuscleGroup;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectAlreadyExistsException;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ExerciseService {
    ExerciseResponse createExercise(CreateExerciseRequest request, UUID userId) throws AppObjectAlreadyExistsException, AppObjectNotFoundException;
    ExerciseResponse updateExercise(UUID exerciseId, UpdateExerciseRequest request, UUID userId) throws AppObjectNotFoundException, AppObjectAlreadyExistsException;
    void deleteExercise(UUID exerciseId, UUID userId) throws AppObjectNotFoundException;
    ExerciseResponse getExerciseById(UUID exerciseId, UUID userId) throws AppObjectNotFoundException;
    List<ExerciseResponse> getExercisesByMuscleGroup(MuscleGroup muscleGroup, UUID userId);
    List<ExerciseResponse> getAllExercises(UUID userId);
}
