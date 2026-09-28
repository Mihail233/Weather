package org.weather.web.dto;

public record WeatherResponse(
        Long locationId,
        String name,
        String icon,
        Float temperature,
        Float feelsLike,
        String description,
        Integer humidity
) {
}
