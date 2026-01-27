package com.github.lamprosvasilakos.workouttracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
    String username,
    String password
) {
}
