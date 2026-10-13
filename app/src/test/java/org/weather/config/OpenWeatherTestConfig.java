package org.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@PropertySource("classpath:test.properties")
@Import(org.weather.service.OpenWeatherServiceImpl.class)
public class OpenWeatherTestConfig {

    @Bean
    public HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }
}
