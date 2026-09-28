package org.weather.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationSearchRequest {
    @NotBlank(message = "Location name cannot be empty")
    @Size(min = 1, max = 100, message = "Location name must be between 1 and 100 characters long")
    private String locationName;
}
