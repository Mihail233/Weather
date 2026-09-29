package org.weather.web.dto;

import java.math.BigDecimal;

public record WeatherResponse(
        Long locationId,
        String name,
        String icon,
        Float temperature,
        Float feelsLike,
        String description,
        Integer humidity,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
