package com.github.lamprosvasilakos.workouttracker.service;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateWorkoutRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateWorkoutRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutSummaryResponse;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface WorkoutService {
    WorkoutResponse createWorkout(CreateWorkoutRequest request, UUID userId) throws AppObjectNotFoundException;
    WorkoutResponse updateWorkout(UUID workoutId, UpdateWorkoutRequest request, UUID userId) throws AppObjectNotFoundException;
    void deleteWorkout(UUID workoutId, UUID userId) throws AppObjectNotFoundException;
    WorkoutResponse getWorkoutById(UUID workoutId, UUID userId) throws AppObjectNotFoundException;
    List<WorkoutSummaryResponse> getWorkoutsByDateRange(UUID userId, LocalDate start, LocalDate end);
    List<WorkoutResponse> getWorkoutsByDate(UUID userId, LocalDate date);
    List<LocalDate> getWorkoutDatesBetween(UUID userId, LocalDate start, LocalDate end);
}
