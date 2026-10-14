package org.weather.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.weather.common.service.WeatherService;
import org.weather.web.dto.WeatherResponse;
import org.weather.web.interceptor.PrivateEndpointInterceptor;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/weather")
    public String getWeatherPage(@RequestAttribute(name = PrivateEndpointInterceptor.USER_ID_ATTRIBUTE) Long userId, Model model) {
        List<WeatherResponse> weatherResponses = weatherService.getWeather(userId);
        model.addAttribute("weatherResponses", weatherResponses);
        return "index";
    }
}
