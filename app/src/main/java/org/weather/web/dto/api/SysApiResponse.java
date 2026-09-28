package org.weather.web.dto.api;

import java.util.Date;

public record SysApiResponse(
        Integer type,
        Integer id,
        String country,
        Date sunrise,
        Date sunset
) {
}