package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.weather.exception.LocationClientException;
import org.weather.exception.OpenWeatherServerException;
import org.weather.exception.WeatherClientException;
import org.weather.web.dto.api.LocationApiResponse;
import org.weather.web.dto.api.WeatherApiResponse;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenWeatherService {

    private static final String LOCATIONS_PATH = "/geo/1.0/direct?";
    private static final String WEATHER_PATH = "/data/2.5/weather?";

    private static final String APPID_KEY = "appid";

    private static final String LIMIT_KEY = "limit";
    private static final String LIMIT = "5";

    private static final String Q_KEY = "q";
    private static final String LAT_KEY = "lat";
    private static final String LON_KEY = "lon";

    private static final String UNITS_KEY = "units";
    private static final String UNITS = "metric";

    private static final String QUERY_PARAMETERS_SEPARATOR = "&";
    private static final String KEY_VALUE_SEPARATOR = "=";
    private static final int STARTED_INDEX = 0;

    private final JsonMapper jsonMapper = new JsonMapper();

    @Value("${openWeather.api.base-url}")
    private String OPEN_WEATHER_URI;

    @Value("${openWeather.api.key}")
    private String APPID;

    public LocationApiResponse[] requestLocations(String locationName) throws URISyntaxException, IOException, InterruptedException {
        List<String> queryParameters = getQueryParameters(List.of(Q_KEY, LIMIT_KEY, APPID_KEY), List.of(locationName, LIMIT, APPID));
        HttpRequest request = buildRequest(OPEN_WEATHER_URI + LOCATIONS_PATH, queryParameters);
        HttpResponse<String> response = sendRequest(request);

        validateLocationHttpResponses(response);
        return deserializeLocationHttpResponses(response);
    }

    private List<String> getQueryParameters(List<String> keys, List<String> values) {
        List<String> queryParameters = new ArrayList<>();
        for (int index = STARTED_INDEX; index < keys.size(); index++) {
            queryParameters.add(keys.get(index) + KEY_VALUE_SEPARATOR + values.get(index));
        }
        return queryParameters;
    }

    private HttpRequest buildRequest(String URI, List<String> queryParameters) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI((URI + String.join(QUERY_PARAMETERS_SEPARATOR, queryParameters)).replace(" ", "+")))
                .GET()
                .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void validateLocationHttpResponses(HttpResponse<String> response) {
        HttpStatus statusCode = HttpStatus.valueOf(response.statusCode());
        if (statusCode.is4xxClientError()) {
            throw new LocationClientException("An exception occurred while requesting locations, please check the location name");
        }

        if (statusCode.is5xxServerError()) {
            throw new OpenWeatherServerException("An service-side exception occurred while accessing the OpenWeather");
        }
    }

    private void validateWeatherHttpResponse(HttpResponse<String> response) {
        HttpStatus statusCode = HttpStatus.valueOf(response.statusCode());
        if (statusCode.is4xxClientError()) {
            throw new WeatherClientException("An exception occurred while requesting weather, please check the latitude, longitude");
        }

        if (statusCode.is5xxServerError()) {
            throw new OpenWeatherServerException("An service-side exception occurred while accessing the OpenWeather");
        }
    }

    private LocationApiResponse[] deserializeLocationHttpResponses(HttpResponse<String> response) {
        return jsonMapper.readValue(response.body(), LocationApiResponse[].class);
    }

    public WeatherApiResponse requestWeather(String latitude, String longitude) throws URISyntaxException, IOException, InterruptedException {
        List<String> queryParameters = getQueryParameters(List.of(LAT_KEY, LON_KEY, APPID_KEY, UNITS_KEY), List.of(latitude, longitude, APPID, UNITS));
        HttpRequest request = buildRequest(OPEN_WEATHER_URI + WEATHER_PATH, queryParameters);
        HttpResponse<String> response = sendRequest(request);

        validateWeatherHttpResponse(response);
        return deserializeWeatherHttpResponse(response);
    }

    private WeatherApiResponse deserializeWeatherHttpResponse(HttpResponse<String> response) {
        return jsonMapper.readValue(response.body(), WeatherApiResponse.class);
    }
}

