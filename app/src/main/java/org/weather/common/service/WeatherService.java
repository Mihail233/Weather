package org.weather.common.service;

import org.weather.web.dto.WeatherResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface WeatherService {
    List<WeatherResponse> getWeather(Long userId) throws URISyntaxException, IOException, InterruptedException;
}
