package com.github.lamprosvasilakos.workouttracker.exception;

public class AuthenticationFailedException extends AppGenericException {

    public AuthenticationFailedException(String code, String message) {
        super(code, message);
    }
}
