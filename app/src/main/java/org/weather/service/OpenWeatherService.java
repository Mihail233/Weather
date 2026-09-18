package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.weather.data.LocationResponse;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OpenWeatherService {
    private static final String LOCATIONS_URI = "http://api.openweathermap.org/geo/1.0/direct?";

    private static final String APPID_KEY = "appid";
    private static final String APPID = "5aea56b59574fba09c7e3b0213d2abbd";

    private static final String LIMIT_KEY = "limit";
    private static final String LIMIT = "5";

    private static final String Q_KEY = "q";

    private static final String QUERY_PARAMETERS_SEPARATOR = "&";
    private static final String KEY_VALUE_SEPARATOR = "=";
    private static final int STARTED_INDEX = 0;

    private final JsonMapper jsonMapper = new JsonMapper();

    public LocationResponse requestLocations(String name) throws URISyntaxException, IOException, InterruptedException {
        List<String> queryParameters = getQueryParameters(List.of(Q_KEY, LIMIT_KEY, APPID_KEY), List.of(name, LIMIT, APPID));
        HttpRequest request = buildRequest(LOCATIONS_URI, queryParameters);
        HttpResponse<String> response = sendRequest(request);
        LocationResponse[] deserializeResponse = deserializeLocationsResponse(response);
        return deserializeResponse[STARTED_INDEX];
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
                .uri(new URI(URI + String.join(QUERY_PARAMETERS_SEPARATOR, queryParameters)))
                .GET()
                .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    private LocationResponse[] deserializeLocationsResponse(HttpResponse<String> response) {
        return jsonMapper.readValue(response.body(), LocationResponse[].class);
    }

    public void requestWeather() {

    }
}

