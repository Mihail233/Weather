package org.weather.web.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

public record WeatherApiResponse(
        @JsonProperty("coord")
        CoordinateApiResponse coordinateApiResponse,

        @JsonProperty("weather")
        WeatherConditionResponse[] weatherConditionsResponse,

        String base,

        @JsonProperty("main")
        MainApiResponse mainApiResponse,

        Integer visibility,

        @JsonProperty("wind")
        WindApiResponse windApiResponse,

        @JsonProperty("rain")
        RainApiResponse rainApiResponse,

        @JsonProperty("clouds")
        CloudApiResponse cloudApiResponse,

        @JsonProperty("dt")
        Date timeCalculation,

        @JsonProperty("sys")
        SysApiResponse sysApiResponse,

        @JsonProperty("timezone")
        Integer timezone,

        Integer id,

        String name,

        Integer cod
) {
}

record CoordinateApiResponse(
        @JsonProperty("lat")
        BigDecimal latitude,

        @JsonProperty("lon")
        BigDecimal longitude
) {
}

record WeatherConditionResponse(
        Integer id,
        String main,
        String description,
        String icon
) {
}

record MainApiResponse(
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

record WindApiResponse(
        Float speed,
        @JsonProperty("deg")
        Integer direction
) {
}

record RainApiResponse(
        @JsonProperty("1h")
        Float precipitation
) {
}

record CloudApiResponse(
        Integer all
) {
}

record SysApiResponse(
        Integer type,
        Integer id,
        String country,
        Date sunrise,
        Date sunset
) {
}