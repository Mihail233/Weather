package org.weather.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
public class LocationResponse {
    @JsonProperty("name")
    private String name;

    @JsonProperty("local_names")
    private Map<String, String> localNames;

    @JsonProperty("lat")
    private BigDecimal latitude;

    @JsonProperty("lon")
    private BigDecimal longitude;

    @JsonProperty("country")
    private String country;

    @JsonProperty("state")
    private String state;
}
