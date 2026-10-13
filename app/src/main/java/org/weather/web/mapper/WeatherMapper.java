package org.weather.web.mapper;

import org.springframework.stereotype.Component;
import org.weather.data.entity.Location;
import org.weather.web.dto.WeatherResponse;
import org.weather.web.dto.api.WeatherApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.weather.service.OpenWeatherServiceImpl.STARTED_INDEX;

@Component
public class WeatherMapper {
    public List<WeatherResponse> toResponseList(List<Optional<WeatherApiResponse>> weatherApiResponses, List<Location> locations) {
        if (weatherApiResponses == null) {
            return null;
        }

        List<WeatherResponse> list = new ArrayList<>(weatherApiResponses.size());

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

        return new WeatherResponse(
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
    }
}