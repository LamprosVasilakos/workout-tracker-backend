package com.github.lamprosvasilakos.workouttracker.dto.request;

import com.github.lamprosvasilakos.workouttracker.entity.MuscleGroup;
import jakarta.validation.constraints.Size;

public record UpdateExerciseRequest(
        @Size(min = 3, max = 50, message = "Exercise name must be at least 3 characters")
        String name,

        MuscleGroup muscleGroup
) {
}
