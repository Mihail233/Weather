package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weather.data.entity.Location;
import org.weather.data.repository.LocationRepository;
import org.weather.web.dto.api.WeatherApiResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final OpenWeatherService openWeatherService;
    private final LocationRepository locationRepository;

    public void viewWeather(Long userId) throws URISyntaxException, IOException, InterruptedException {
        List<Location> locations = locationRepository.findLocationsById(userId);

        List<WeatherApiResponse> weatherApiResponses = new ArrayList<>();
        for (Location location : locations) {
            WeatherApiResponse weatherApiResponse = openWeatherService.requestWeather(location.getLatitude().toString(), location.getLongitude().toString());
            weatherApiResponses.add(weatherApiResponse);
        }
    }
}
