package com.github.lamprosvasilakos.workouttracker.service;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateWorkoutRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateWorkoutRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutSummaryResponse;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectAlreadyExistsException;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface WorkoutService {
    WorkoutResponse createWorkout(CreateWorkoutRequest request, UUID userId) throws AppObjectNotFoundException, AppObjectAlreadyExistsException;
    WorkoutResponse updateWorkout(UUID workoutId, UpdateWorkoutRequest request, UUID userId) throws AppObjectNotFoundException, AppObjectAlreadyExistsException;
    void deleteWorkout(UUID workoutId, UUID userId) throws AppObjectNotFoundException;
    WorkoutResponse getWorkoutById(UUID workoutId, UUID userId) throws AppObjectNotFoundException;
    List<WorkoutSummaryResponse> getWorkoutsBetweenDates(UUID userId, LocalDate startDate, LocalDate endDate);
}
