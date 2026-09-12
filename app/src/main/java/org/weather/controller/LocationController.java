package org.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.weather.entity.Session;
import org.weather.entity.User;
import org.weather.repository.SessionRepository;
import org.weather.repository.UserRepository;
import org.weather.service.SessionService;
import org.weather.util.CookieUtil;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class LocationController {

    @GetMapping("/home")
    public String getHomePage(@RequestAttribute(name = CookieUtil.USER_SESSION_COOKIE) Long sessionId) {
        return "index";
    }

    @PostMapping("/home")
    public void g(@CookieValue("user_session") String fooCookie) {
        int i = 0;
    }
}
