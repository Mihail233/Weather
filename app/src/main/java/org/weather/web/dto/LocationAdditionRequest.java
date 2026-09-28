package org.weather.web.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocationAdditionRequest {
    @NotBlank(message = "Location name cannot be empty")
    @Size(min = 1, max = 100, message = "Location name must be between 1 and 100 characters long")
    private String name;

    @DecimalMin(value = "-90", message = "Latitude cannot be less than -90 degrees")
    @DecimalMax(value = "90", message = "Latitude cannot exceed 90 degrees")
    private BigDecimal latitude;

    @DecimalMin(value = "-180", message = "Longitude cannot be less than -90 degrees")
    @DecimalMax(value = "180", message = "Longitude cannot exceed 90 degrees")
    private BigDecimal longitude;
}