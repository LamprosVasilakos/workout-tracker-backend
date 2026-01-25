package com.github.lamprosvasilakos.workouttracker.authentication;

import com.github.lamprosvasilakos.workouttracker.dto.AuthenticationRequest;
import com.github.lamprosvasilakos.workouttracker.dto.AuthenticationResponse;
import com.github.lamprosvasilakos.workouttracker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.username(),
                        authenticationRequest.password()
                )
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(authentication.getName());

        return new AuthenticationResponse(
                user.getUsername(),
                token
        );
    }
}
