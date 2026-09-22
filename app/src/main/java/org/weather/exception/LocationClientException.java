package org.weather.exception;

public class LocationClientException extends RuntimeException {
    public LocationClientException(String message) {
        super(message);
    }
}
