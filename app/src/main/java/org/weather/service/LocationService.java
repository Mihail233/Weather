package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Service;
import org.weather.data.entity.Location;
import org.weather.data.entity.User;
import org.weather.data.repository.LocationRepository;
import org.weather.exception.IncorrectLocationException;
import org.weather.exception.LocationAdditionException;
import org.weather.exception.LocationNotFoundException;
import org.weather.exception.LocationRemovalException;
import org.weather.web.dto.LocationAdditionRequest;
import org.weather.web.dto.LocationRemovalRequest;
import org.weather.web.dto.LocationResponse;
import org.weather.web.dto.LocationSearchRequest;
import org.weather.web.dto.api.LocationApiResponse;
import org.weather.web.mapper.LocationMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static org.weather.util.ServiceUtil.isPersistenceException;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final OpenWeatherService openWeatherService;
    private final UserService userService;

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    public List<LocationResponse> findAllByName(LocationSearchRequest locationSearchRequest) throws URISyntaxException, IOException, InterruptedException {
        LocationApiResponse[] locationApiResponses = openWeatherService.requestLocations(locationSearchRequest.getLocationName());
        return locationMapper.toResponseList(locationApiResponses);
    }

    public void addNewLocation(LocationAdditionRequest locationAdditionRequest, Long userId) {
        try {
            User user = userService.findById(userId);
            Location location = new Location(locationAdditionRequest.getName(), user, locationAdditionRequest.getLatitude(), locationAdditionRequest.getLongitude());
            locationRepository.save(location);
        } catch (NestedRuntimeException e) {
            if (isPersistenceException(e)) {
                throw new LocationAdditionException(e.getMessage(), e);
            }
            throw e;
        }
    }

    public void delete(LocationRemovalRequest locationRemovalRequest, Long userId) {
        try {
            Location location = findById(locationRemovalRequest.getLocationId());
            guardUser(userId, location);
            locationRepository.delete(location);
        } catch (NestedRuntimeException e) {
            if (isPersistenceException(e)) {
                throw new LocationRemovalException(e.getMessage(), e);
            }
            throw e;
        }
    }

    private Location findById(Long id) {
        return locationRepository.findLocationById(id)
                .orElseThrow(() -> new LocationNotFoundException("Location not found"));
    }

    private void guardUser(Long userId, Location location) {
        if (!Objects.equals(location.getUser().getId(), userId)) {
            throw new IncorrectLocationException("Access to the location is prohibited");
        }
    }
}