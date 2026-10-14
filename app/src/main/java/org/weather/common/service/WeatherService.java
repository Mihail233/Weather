package org.weather.common.service;

import org.weather.web.dto.WeatherResponse;

import java.util.List;

public interface WeatherService {
    List<WeatherResponse> getWeather(Long userId);
}
