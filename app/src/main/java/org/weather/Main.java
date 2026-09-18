package org.weather;

import org.weather.data.LocationResponse;
import org.weather.service.OpenWeatherService;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        OpenWeatherService openWeatherService = new OpenWeatherService();
        LocationResponse f = openWeatherService.requestLocations("Moscow");
    }
}
