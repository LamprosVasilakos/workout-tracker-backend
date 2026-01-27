package com.github.lamprosvasilakos.workouttracker.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Lightweight workout response without nested exercise details.
 * Useful for listing workouts without full data.
 */
public record WorkoutSummaryResponse(
        UUID id,
        LocalDate date
) {
}
