package com.github.lamprosvasilakos.workouttracker.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateWorkoutRequest(
        @NotNull(message = "Workout date is required")
        LocalDate date,

        @Valid
        List<CreateWorkoutExerciseRequest> workoutExercises
) {
}
