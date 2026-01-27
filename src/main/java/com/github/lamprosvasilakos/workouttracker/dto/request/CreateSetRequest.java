package com.github.lamprosvasilakos.workouttracker.dto.request;

import com.github.lamprosvasilakos.workouttracker.entity.SetType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSetRequest(
        @NotNull(message = "The number of reps is required")
        @Min(value = 1, message = "Reps must be at least 1")
        Integer reps,

        @NotNull(message = "Weight is required")
        @Min(value = 0, message = "Weight must be 0 or greater")
        Double weight,

        @Size(max = 250, message = "Notes must not exceed 250 characters")
        String notes,

        @NotNull(message = "Set type is required")
        SetType setType
) {
}
