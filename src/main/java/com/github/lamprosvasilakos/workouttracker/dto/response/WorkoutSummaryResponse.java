package com.github.lamprosvasilakos.workouttracker.dto.response;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Lightweight workout response without nested exercise details.
 */
public record WorkoutSummaryResponse(
        UUID id,
        LocalDate date
) {
}
