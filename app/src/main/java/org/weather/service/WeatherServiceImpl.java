package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weather.common.service.OpenWeatherService;
import org.weather.common.service.WeatherService;
import org.weather.data.entity.Location;
import org.weather.data.repository.LocationRepository;
import org.weather.exception.OpenWeatherRejectedException;
import org.weather.exception.WeatherClientException;
import org.weather.web.dto.WeatherResponse;
import org.weather.web.dto.api.WeatherApiResponse;
import org.weather.web.mapper.WeatherMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final OpenWeatherService openWeatherService;
    private final LocationRepository locationRepository;

    private final WeatherMapper weatherMapper;

    public List<WeatherResponse> getWeather(Long userId) {
        List<Location> locations = locationRepository.findAllByUserId(userId);
        List<Optional<WeatherApiResponse>> weatherApiResponses = requestWeather(locations);
        return weatherMapper.toResponseList(weatherApiResponses, locations);
    }

    private List<Optional<WeatherApiResponse>> requestWeather(List<Location> locations) {
        List<Optional<WeatherApiResponse>> weatherApiResponses = new ArrayList<>();
        for (Location location : locations) {
            try {
                WeatherApiResponse weatherApiResponse = openWeatherService.requestWeather(location.getLatitude().toString(), location.getLongitude().toString());
                weatherApiResponses.add(Optional.of(weatherApiResponse));
            } catch (OpenWeatherRejectedException | WeatherClientException e) {
                weatherApiResponses.add(Optional.empty());
            }
        }
        return weatherApiResponses;
    }
}
