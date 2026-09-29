package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weather.common.service.OpenWeatherService;
import org.weather.common.service.WeatherService;
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
public class WeatherServiceImpl implements WeatherService {
    private final OpenWeatherService openWeatherService;
    private final LocationRepository locationRepository;

    private final WeatherMapper weatherMapper;

    public List<WeatherResponse> showWeather(Long userId) throws URISyntaxException, IOException, InterruptedException {
        List<Location> locations = locationRepository.findAllByUserId(userId);
        List<WeatherApiResponse> weatherApiResponses = requestWeather(locations);
        return weatherMapper.toResponseList(weatherApiResponses, locations);
    }

    private List<WeatherApiResponse> requestWeather(List<Location> locations) throws URISyntaxException, IOException, InterruptedException {
        List<WeatherApiResponse> weatherApiResponses = new ArrayList<>();
        for (Location location : locations) {
            WeatherApiResponse weatherApiResponse = openWeatherService.requestWeather(location.getLatitude().toString(), location.getLongitude().toString());
            weatherApiResponses.add(weatherApiResponse);
        }
        return weatherApiResponses;
    }
}
