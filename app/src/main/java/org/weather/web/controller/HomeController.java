package org.weather.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

//    @GetMapping("/home")
//    public String getHomePage(@RequestAttribute(name = CookieInterceptor.USER_ID_ATTRIBUTE) User user) {
//        return "index";
//    }

    @GetMapping("/home")
    public String getHomePage() {
        return "index";
    }
}
