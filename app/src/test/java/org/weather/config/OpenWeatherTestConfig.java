package org.weather.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:test.properties")
@Import(org.weather.service.OpenWeatherService.class)
public class OpenWeatherTestConfig {
}
