package com.github.lamprosvasilakos.workouttracker.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateUserResponse(
        UUID id,
        String username,
        LocalDateTime createdAt
) {
}
