package org.weather.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.weather.data.entity.User;
import org.weather.web.interceptor.CookieInterceptor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/home")
    public String getHomePage(@RequestAttribute(name = CookieInterceptor.USER_ID_ATTRIBUTE) User user) {
        return "index";
    }
}
