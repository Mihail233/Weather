package org.weather.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.weather.config.OpenWeatherTestConfig;
import org.weather.exception.OpenWeatherRejectedException;
import org.weather.web.dto.api.LocationApiResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {OpenWeatherTestConfig.class})
@WebAppConfiguration
class OpenWeatherServiceTest {

    private static final String APPID = "TEST_KEY";
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();
    @Autowired
    private OpenWeatherServiceImpl openWeatherService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("openWeather.api.base-url", wireMockServer::baseUrl);
        registry.add("openWeather.api.key", () -> APPID);
    }

    @Test
    void responseFromOpenWeatherContainsExpectedLocationTest() throws URISyntaxException, IOException, InterruptedException {
        wireMockServer.stubFor(get(urlMatching(String.format("/geo/1.0/direct\\?q=Moscow&limit=5&appid=%s", APPID)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))
                        .withBody("""
                                [
                                    {
                                        "name": "Moscow",
                                        "local_names": {
                                            "ab": "Москва",
                                            "gv": "Moscow",
                                            "id": "Moskwa"
                                        },
                                        "lat": 55.7504461,
                                        "lon": 37.6174943,
                                        "country": "RU",
                                        "state": "Moscow"
                                    }
                                ]
                                """)));
        LocationApiResponse[] locationsApiResponse = openWeatherService.requestLocations("Moscow");

        List<String> locationNames = new ArrayList<>();
        for (LocationApiResponse locationApiResponse : locationsApiResponse) {
            locationNames.add(locationApiResponse.name());
        }
        Assertions.assertTrue(locationNames.contains("Moscow"));
    }

    @Test
    void InTheEventOf400ErrorOpenWeatherThrowsTheExpectedExceptionTypeTest() {
        wireMockServer.stubFor(get(urlMatching(String.format("/geo/1.0/direct\\?q=Moscow&limit=5&appid=%s", APPID)))
                .willReturn(aResponse()
                        .withStatus(400)));

        Assertions.assertThrows(OpenWeatherRejectedException.class, () -> openWeatherService.requestLocations("Moscow"));
    }

    @Test
    void InTheEventOf500ErrorOpenWeatherThrowsTheExpectedExceptionTypeTest() {
        wireMockServer.stubFor(get(urlMatching(String.format("/geo/1.0/direct\\?q=Moscow&limit=5&appid=%s", APPID)))
                .willReturn(aResponse()
                        .withStatus(500)));

        Assertions.assertThrows(OpenWeatherRejectedException.class, () -> openWeatherService.requestLocations("Moscow"));
    }
}