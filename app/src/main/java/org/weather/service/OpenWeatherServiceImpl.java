package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.weather.common.service.OpenWeatherService;
import org.weather.exception.OpenWeatherRejectedException;
import org.weather.web.dto.api.LocationApiResponse;
import org.weather.web.dto.api.WeatherApiResponse;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenWeatherServiceImpl implements OpenWeatherService {
    public static final int STARTED_INDEX = 0;

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

    private final JsonMapper jsonMapper = new JsonMapper();

    private final HttpClient httpClient;

    @Value("${openWeather.api.base-url}")
    private String OPEN_WEATHER_URI;

    @Value("${openWeather.api.key}")
    private String APPID;

    public LocationApiResponse[] requestLocations(String locationName) {
        List<String> queryParameters = getQueryParameters(List.of(Q_KEY, LIMIT_KEY, APPID_KEY), List.of(locationName, LIMIT, APPID));
        HttpRequest request = buildRequest(OPEN_WEATHER_URI + LOCATIONS_PATH, queryParameters);
        HttpResponse<String> response = sendRequest(request);

        validateHttpResponses(response);
        return deserializeLocationHttpResponses(response);
    }

    private List<String> getQueryParameters(List<String> keys, List<String> values) {
        List<String> queryParameters = new ArrayList<>();
        for (int index = STARTED_INDEX; index < keys.size(); index++) {
            String value = values.get(index);
            queryParameters.add(keys.get(index) + KEY_VALUE_SEPARATOR + URLEncoder.encode(value, StandardCharsets.UTF_8));
        }
        return queryParameters;
    }

    private HttpRequest buildRequest(String URI, List<String> queryParameters) {
        return HttpRequest.newBuilder()
                .uri(buildURIWithQueryParameters(URI, queryParameters))
                .GET()
                .build();
    }

    private URI buildURIWithQueryParameters(String URI, List<String> queryParameters) {
        try {
            return new URI((URI + String.join(QUERY_PARAMETERS_SEPARATOR, queryParameters)));
        } catch (URISyntaxException e) {
            throw new OpenWeatherRejectedException("An exception occurred on the application side when calling OpenWeather");
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new OpenWeatherRejectedException("An exception occurred on the application side when calling OpenWeather");
        }
    }

    private void validateHttpResponses(HttpResponse<String> response) {
        HttpStatus statusCode = HttpStatus.valueOf(response.statusCode());
        if (statusCode.is4xxClientError()) {
            throw new OpenWeatherRejectedException("An exception occurred while executing the request; please check the request");
        }

        if (statusCode.is5xxServerError()) {
            throw new OpenWeatherRejectedException("An exception occurred on the external service side when calling OpenWeather");
        }
    }

    private LocationApiResponse[] deserializeLocationHttpResponses(HttpResponse<String> response) {
        return jsonMapper.readValue(response.body(), LocationApiResponse[].class);
    }

    public WeatherApiResponse requestWeather(String latitude, String longitude) {
        List<String> queryParameters = getQueryParameters(List.of(LAT_KEY, LON_KEY, APPID_KEY, UNITS_KEY), List.of(latitude, longitude, APPID, UNITS));
        HttpRequest request = buildRequest(OPEN_WEATHER_URI + WEATHER_PATH, queryParameters);
        HttpResponse<String> response = sendRequest(request);

        validateHttpResponses(response);
        return deserializeWeatherHttpResponse(response);
    }

    private WeatherApiResponse deserializeWeatherHttpResponse(HttpResponse<String> response) {
        return jsonMapper.readValue(response.body(), WeatherApiResponse.class);
    }
}

