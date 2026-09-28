package org.weather.exception;

public class UserSignInException extends RuntimeException {
    public UserSignInException(String message, Throwable cause) {
        super(message, cause);
    }
}
