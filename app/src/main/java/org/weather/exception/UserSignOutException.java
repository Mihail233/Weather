package org.weather.exception;

public class UserSignOutException extends RuntimeException {
    public UserSignOutException(String message, Throwable cause) {
        super(message, cause);
    }
}