package org.weather.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.weather.util.CookieUtil;

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
