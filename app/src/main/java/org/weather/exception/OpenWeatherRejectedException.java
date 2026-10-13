package org.weather.exception;

public class OpenWeatherRejectedException extends RuntimeException {
    public OpenWeatherRejectedException(String message) {
        super(message);
    }
}
