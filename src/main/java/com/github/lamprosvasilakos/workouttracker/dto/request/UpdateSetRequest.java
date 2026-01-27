package com.github.lamprosvasilakos.workouttracker.dto.request;

import com.github.lamprosvasilakos.workouttracker.entity.SetType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateSetRequest(
        @Min(value = 1, message = "The number of reps must be at least 1")
        Integer reps,

        @Min(value = 0, message = "Weight must be 0 or greater")
        Double weight,

        @Size(max = 250, message = "Notes must not exceed 500 characters")
        String notes,

        SetType setType
) {
}
