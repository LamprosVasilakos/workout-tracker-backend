package com.github.lamprosvasilakos.workouttracker.service;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateSetRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateSetRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.SetResponse;
import com.github.lamprosvasilakos.workouttracker.entity.WorkoutExercise;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;

import java.util.List;
import java.util.UUID;

public interface SetService {
    SetResponse createSet(CreateSetRequest request, WorkoutExercise workoutExercise);
    SetResponse updateSet(UUID setId, UpdateSetRequest request) throws AppObjectNotFoundException;
    void deleteSet(UUID setId) throws AppObjectNotFoundException;
    SetResponse getSetById(UUID setId) throws AppObjectNotFoundException;
    List<SetResponse> getAllSetsByWorkoutExerciseId(UUID workoutExerciseId);
}
