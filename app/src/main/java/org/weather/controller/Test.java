package org.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.weather.TestService;

@Controller
@RequiredArgsConstructor
public class Test {

    private final TestService testService;

    @GetMapping("/podik")
    public String gigka() {
        return "weather/index";
    }
}
