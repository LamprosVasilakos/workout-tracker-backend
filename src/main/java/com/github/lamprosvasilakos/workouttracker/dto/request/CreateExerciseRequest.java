package com.github.lamprosvasilakos.workouttracker.dto.request;

import com.github.lamprosvasilakos.workouttracker.entity.MuscleGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateExerciseRequest(
        @NotBlank(message = "Exercise name is required")
        @Size(min = 3, max = 50, message = "Exercise name must be at least characters")
        String name,

        @NotNull(message = "Muscle group is required")
        MuscleGroup muscleGroup
) {
}
