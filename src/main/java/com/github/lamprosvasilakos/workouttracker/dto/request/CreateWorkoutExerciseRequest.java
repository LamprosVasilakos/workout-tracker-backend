package com.github.lamprosvasilakos.workouttracker.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateWorkoutExerciseRequest(
        @NotNull(message = "Exercise ID is required")
        UUID exerciseId,

        @NotNull(message = "Exercise order is required")
        Integer exerciseOrder,

        @Valid
        List<CreateSetRequest> sets
) {
}
