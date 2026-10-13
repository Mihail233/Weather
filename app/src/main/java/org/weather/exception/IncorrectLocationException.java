package org.weather.exception;

public class IncorrectLocationException extends RuntimeException {
    public IncorrectLocationException(String message) {
        super(message);
    }
}
