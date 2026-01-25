package com.github.lamprosvasilakos.workouttracker.dto;

public record AuthenticationResponse(
        String username,
        String token
) {
}
