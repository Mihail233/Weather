package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weather.data.entity.Location;
import org.weather.data.entity.User;
import org.weather.data.repository.LocationRepository;
import org.weather.exception.IncorrectLocationException;
import org.weather.exception.LocationNotFoundException;
import org.weather.web.dto.AddingLocationRequest;
import org.weather.web.dto.LocationResponse;
import org.weather.web.dto.RemovalLocationRequest;
import org.weather.web.dto.SearchResultRequest;
import org.weather.web.dto.api.LocationApiResponse;
import org.weather.web.mapper.LocationMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final OpenWeatherService openWeatherService;
    private final LocationRepository locationRepository;
    private final UserService userService;

    private final LocationMapper locationMapper;

    public List<LocationResponse> findAllByName(SearchResultRequest searchResultRequest) throws URISyntaxException, IOException, InterruptedException {
        LocationApiResponse[] locationApiResponses = openWeatherService.requestLocations(searchResultRequest.getLocationName());
        return locationMapper.toResponseList(locationApiResponses);
    }

    public void add(AddingLocationRequest addingLocationRequest, Long userId) {
        User user = userService.findById(userId);
        Location location = new Location(addingLocationRequest.getName(), user, addingLocationRequest.getLatitude(), addingLocationRequest.getLongitude());
        locationRepository.save(location);
    }

    public void remove(RemovalLocationRequest removalLocationRequest, User user) {
        Location location = findById(removalLocationRequest.getLocationId());
        guardUser(user.getId(), location);
        locationRepository.delete(location);
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