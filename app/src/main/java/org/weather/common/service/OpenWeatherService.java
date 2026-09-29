package org.weather.common.service;

import org.weather.web.dto.api.LocationApiResponse;
import org.weather.web.dto.api.WeatherApiResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public interface OpenWeatherService {
    LocationApiResponse[] requestLocations(String locationName) throws URISyntaxException, IOException, InterruptedException;

    WeatherApiResponse requestWeather(String latitude, String longitude) throws URISyntaxException, IOException, InterruptedException;
}
