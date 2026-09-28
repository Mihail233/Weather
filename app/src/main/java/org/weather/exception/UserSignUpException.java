package org.weather.exception;

public class UserSignUpException extends RuntimeException {
    public UserSignUpException(String message, Throwable cause) {
        super(message, cause);
    }
}