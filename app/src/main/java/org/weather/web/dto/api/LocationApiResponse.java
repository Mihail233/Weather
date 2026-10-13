package org.weather.web.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public record LocationApiResponse(
        @JsonProperty("name")
        String name,

        @JsonProperty("local_names")
        Map<String, String> localNames,

        @JsonProperty("lat")
        BigDecimal latitude,

        @JsonProperty("lon")
        BigDecimal longitude,

        @JsonProperty("country")
        String country,

        @JsonProperty("state")
        String state
) {
}