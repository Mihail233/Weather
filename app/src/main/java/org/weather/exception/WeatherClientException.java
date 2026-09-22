package org.weather.exception;

public class WeatherClientException extends RuntimeException {
    public WeatherClientException(String message) {
        super(message);
    }
}
