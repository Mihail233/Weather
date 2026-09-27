package org.weather.web.dto;

import java.math.BigDecimal;

public record LocationResponse(
        String name,
        BigDecimal latitude,
        BigDecimal longitude,
        String country,
        String state
) {
}
