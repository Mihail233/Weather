package org.weather.common.service;

import org.weather.web.dto.api.LocationApiResponse;
import org.weather.web.dto.api.WeatherApiResponse;

public interface OpenWeatherService {
    LocationApiResponse[] requestLocations(String locationName);

    WeatherApiResponse requestWeather(String latitude, String longitude);
}