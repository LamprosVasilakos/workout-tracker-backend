package com.github.lamprosvasilakos.workouttracker.controller;

import com.github.lamprosvasilakos.workouttracker.authentication.AuthenticationService;
import com.github.lamprosvasilakos.workouttracker.dto.request.AuthenticationRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.CreateUserRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.AuthenticationResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.CreateUserResponse;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectAlreadyExistsException;
import com.github.lamprosvasilakos.workouttracker.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> register(@Valid  @RequestBody CreateUserRequest createUserRequest,BindingResult bindingResult) throws AppObjectAlreadyExistsException, ValidationException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CreateUserResponse createUserResponse = authenticationService.registerUser(createUserRequest);
        return new ResponseEntity<>(createUserResponse, HttpStatus.CREATED);
    }
}
