package org.weather;

import org.weather.service.OpenWeatherService;
import org.weather.web.dto.api.LocationApiResponse;
import org.weather.web.dto.api.WeatherApiResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        OpenWeatherService openWeatherService = new OpenWeatherService();
        //LocationApiResponse f = openWeatherService.requestLocations("Moscow");
//
        WeatherApiResponse w = openWeatherService.requestWeather("43.2271", "-0.3878");
    }
}
