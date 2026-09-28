package org.weather.web.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MainApiResponse(
        @JsonProperty("temp")
        Float temperature,

        @JsonProperty("feels_like")
        Float feelsLike,

        @JsonProperty("temp_min")
        Float minTemperature,

        @JsonProperty("temp_max")
        Float maxTemperature,

        @JsonProperty("pressure")
        Integer pressure,

        @JsonProperty("humidity")
        Integer humidity,

        @JsonProperty("sea_level")
        Integer seaLevel,

        @JsonProperty("grnd_level")
        Integer groundLevel
) {
}