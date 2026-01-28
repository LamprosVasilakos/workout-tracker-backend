package com.github.lamprosvasilakos.workouttracker.dto.response;

import com.github.lamprosvasilakos.workouttracker.entity.MuscleGroup;

import java.util.UUID;

public record ExerciseResponse(
        UUID id,
        String name,
        MuscleGroup muscleGroup
) {
}
