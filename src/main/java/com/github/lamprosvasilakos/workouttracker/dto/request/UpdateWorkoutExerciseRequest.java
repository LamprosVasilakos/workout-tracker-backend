package com.github.lamprosvasilakos.workouttracker.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.UUID;

public record UpdateWorkoutExerciseRequest(
        UUID exerciseId,

        Integer exerciseOrder,

        @Valid
        List<CreateSetRequest> sets
) {
}
