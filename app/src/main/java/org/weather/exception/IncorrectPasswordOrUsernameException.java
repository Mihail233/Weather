package org.weather.exception;

public class IncorrectPasswordOrUsernameException extends RuntimeException {
    public IncorrectPasswordOrUsernameException(String message) {
        super(message);
    }
}
