package com.github.lamprosvasilakos.workouttracker.service;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateWorkoutExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateWorkoutExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.entity.Workout;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;

import java.util.List;
import java.util.UUID;

public interface WorkoutExerciseService {
    WorkoutExerciseResponse createWorkoutExercise(CreateWorkoutExerciseRequest request, Workout workout, UUID userId) throws AppObjectNotFoundException;
    WorkoutExerciseResponse updateWorkoutExercise(UUID workoutExerciseId, UpdateWorkoutExerciseRequest request, UUID userId) throws AppObjectNotFoundException;
    void deleteWorkoutExercise(UUID workoutExerciseId, UUID userId) throws AppObjectNotFoundException;
    WorkoutExerciseResponse getWorkoutExerciseById(UUID workoutExerciseId, UUID userId) throws AppObjectNotFoundException;
    List<WorkoutExerciseResponse> getAllWorkoutExercisesByWorkoutId(UUID workoutId, UUID userId);
}
