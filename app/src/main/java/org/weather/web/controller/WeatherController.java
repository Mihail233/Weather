package org.weather.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.weather.service.WeatherService;
import org.weather.web.dto.WeatherResponse;
import org.weather.web.interceptor.CookieInterceptor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/home")
    public String getWeatherPage(@RequestAttribute(name = CookieInterceptor.USER_ID_ATTRIBUTE) Long userId, Model model) throws URISyntaxException, IOException, InterruptedException {
        List<WeatherResponse> weatherResponses = weatherService.findAll(userId);
        model.addAttribute("weatherResponses", weatherResponses);
        return "index";
    }
}
