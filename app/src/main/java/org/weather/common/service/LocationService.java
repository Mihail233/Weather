package org.weather.common.service;

import org.weather.web.dto.LocationAdditionRequest;
import org.weather.web.dto.LocationRemovalRequest;
import org.weather.web.dto.LocationResponse;
import org.weather.web.dto.LocationSearchRequest;

import java.util.List;

public interface LocationService {
    List<LocationResponse> searchLocations(LocationSearchRequest locationSearchRequest);

    void addNewLocation(LocationAdditionRequest locationAdditionRequest, Long userId);

    void deleteLocation(LocationRemovalRequest locationRemovalRequest, Long userId);

}
