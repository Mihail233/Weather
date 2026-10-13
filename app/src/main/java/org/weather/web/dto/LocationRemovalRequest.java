package org.weather.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRemovalRequest {

    @NotNull(message = "No location was selected; please specify one")
    private Long locationId;
}
