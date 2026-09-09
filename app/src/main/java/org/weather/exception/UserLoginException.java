package org.weather.exception;

public class UserLoginException extends RuntimeException {
    public UserLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
