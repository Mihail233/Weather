package org.weather.exception;

public class LocationRemovalException extends RuntimeException {
    public LocationRemovalException(String message, Throwable cause) {
        super(message, cause);
    }
}
