package org.weather.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.weather.data.repository.LocationRepository;
import org.weather.service.WeatherService;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequiredArgsConstructor
public class Test {
    private final WeatherService weatherService;
    private final LocationRepository locationRepository;

    @GetMapping("/test")
    public void getHomePage() throws URISyntaxException, IOException, InterruptedException {
        weatherService.findAll(1L);
    }
}
