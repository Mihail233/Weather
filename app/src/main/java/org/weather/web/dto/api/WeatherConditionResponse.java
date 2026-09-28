package org.weather.web.dto.api;

public record WeatherConditionResponse(
        Integer id,
        String main,
        String description,
        String icon
) {
}