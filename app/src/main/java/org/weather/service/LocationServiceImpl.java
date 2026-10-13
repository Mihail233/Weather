package org.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Service;
import org.weather.common.service.LocationService;
import org.weather.common.service.OpenWeatherService;
import org.weather.common.service.UserService;
import org.weather.data.entity.Location;
import org.weather.data.entity.User;
import org.weather.data.repository.LocationRepository;
import org.weather.exception.AccessLocationException;
import org.weather.exception.LocationAlreadyExistsException;
import org.weather.exception.LocationNotFoundException;
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

import static org.weather.util.ServiceUtil.isConstraintViolationException;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {
    private final UserService userService;
    private final OpenWeatherService openWeatherService;

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    public List<LocationResponse> searchLocations(LocationSearchRequest locationSearchRequest) throws URISyntaxException, IOException, InterruptedException {
        LocationApiResponse[] locationApiResponses = openWeatherService.requestLocations(locationSearchRequest.getLocationName());
        return locationMapper.toResponseList(locationApiResponses);
    }

    public void addNewLocation(LocationAdditionRequest locationAdditionRequest, Long userId) {
        User user = userService.findUserById(userId);
        Location location = new Location(locationAdditionRequest.getName(), user, locationAdditionRequest.getLatitude(), locationAdditionRequest.getLongitude());
        try {
            locationRepository.save(location);
        } catch (NestedRuntimeException e) {
            log.error("Database exception", e);
            if (isConstraintViolationException(e)) {
                throw new LocationAlreadyExistsException(String.format("The location has already been added: %s; please select another one", location.getName()));
            }
            throw e;
        }
    }

    public void deleteLocation(LocationRemovalRequest locationRemovalRequest, Long userId) {
        Location location = findById(locationRemovalRequest.getLocationId());
        guardUser(userId, location);
        locationRepository.delete(location);
    }

    private Location findById(Long id) {
        return locationRepository.findLocationById(id)
                .orElseThrow(() -> new LocationNotFoundException("Location not found"));
    }

    private void guardUser(Long userId, Location location) {
        if (!Objects.equals(location.getUser().getId(), userId)) {
            throw new AccessLocationException("Access to the location is prohibited");
        }
    }
}