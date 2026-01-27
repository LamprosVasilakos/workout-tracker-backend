package com.github.lamprosvasilakos.workouttracker.dto.response;

public record AuthenticationResponse(
        String username,
        String JwtToken
) {
}
