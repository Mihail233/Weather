package org.weather.controller;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.weather.TestService;

@RestController
@RequiredArgsConstructor
public class Test {

    private final TestService testService;

    @GetMapping("/podik")
    public void gigka() {
        testService.add();
    }
}
