package org.weather.web.mapper;

import org.springframework.stereotype.Component;
import org.weather.data.entity.Location;
import org.weather.web.dto.WeatherResponse;
import org.weather.web.dto.api.WeatherApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class WeatherMapper {
    private static final int STARTED_INDEX = 0;

    public List<WeatherResponse> toResponseList(List<Optional<WeatherApiResponse>> weatherApiResponses, List<Location> locations) {
        if (weatherApiResponses == null) {
            return null;
        }

        List<WeatherResponse> list = new ArrayList<WeatherResponse>(weatherApiResponses.size());

        for (int index = STARTED_INDEX; index < weatherApiResponses.size(); index++) {
            Optional<WeatherApiResponse> weatherApiResponse = weatherApiResponses.get(index);
            if (weatherApiResponse.isPresent()) {
                list.add(map(weatherApiResponse.get(), locations.get(index)));
            }
        }

        return list;
    }

    private WeatherResponse map(WeatherApiResponse weatherApiResponse, Location location) {
        int index = 0;
        WeatherResponse weatherResponse = new WeatherResponse(
                location.getId(),
                location.getName(),
                weatherApiResponse.weatherConditionsResponse()[index].icon(),
                weatherApiResponse.mainApiResponse().temperature(),
                weatherApiResponse.mainApiResponse().feelsLike(),
                weatherApiResponse.weatherConditionsResponse()[index].icon(),
                weatherApiResponse.mainApiResponse().humidity(),
                location.getLatitude().stripTrailingZeros(),
                location.getLongitude().stripTrailingZeros()
        );

        return weatherResponse;
    }
}