package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weather.data.entity.Location;
import org.weather.data.repository.LocationRepository;
import org.weather.web.dto.WeatherResponse;
import org.weather.web.dto.api.WeatherApiResponse;
import org.weather.web.mapper.WeatherMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final OpenWeatherService openWeatherService;
    private final LocationRepository locationRepository;

    private final WeatherMapper weatherMapper;

    public List<WeatherResponse> findAll(Long userId) throws URISyntaxException, IOException, InterruptedException {
        List<Location> locations = locationRepository.findAllByUserId(userId);
        List<WeatherApiResponse> weatherApiResponses = requestAll(locations);
        return weatherMapper.toResponseList(weatherApiResponses, locations);
    }

    private List<WeatherApiResponse> requestAll(List<Location> locations) throws URISyntaxException, IOException, InterruptedException {
        List<WeatherApiResponse> weatherApiResponses = new ArrayList<>();
        for (Location location : locations) {
            WeatherApiResponse weatherApiResponse = openWeatherService.requestWeather(location.getLatitude().toString(), location.getLongitude().toString());
            weatherApiResponses.add(weatherApiResponse);
        }
        return weatherApiResponses;
    }
}
