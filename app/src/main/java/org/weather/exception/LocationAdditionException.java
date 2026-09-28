package org.weather.exception;

public class LocationAdditionException extends RuntimeException {
    public LocationAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
