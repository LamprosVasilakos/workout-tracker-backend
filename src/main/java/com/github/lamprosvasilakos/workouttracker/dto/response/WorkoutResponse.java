package com.github.lamprosvasilakos.workouttracker.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record WorkoutResponse(
        UUID id,
        LocalDate date,
        UUID userId,
        List<WorkoutExerciseResponse> workoutExercises
) {
}
