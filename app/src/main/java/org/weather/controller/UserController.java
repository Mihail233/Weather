package org.weather.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.weather.dto.SignInRequest;
import org.weather.dto.SignInResponse;
import org.weather.dto.SignUpRequest;
import org.weather.dto.SignUpResponse;
import org.weather.handler.ExceptionHandler;
import org.weather.service.UserService;
import org.weather.util.CookieUtil;

@Controller
@RequiredArgsConstructor
public class UserController {
    private static final String USER_SESSION_COOKIE = "user_session";

    private final UserService userService;
    private final ExceptionHandler exceptionHandler;

    @GetMapping("/sign-up")
    public String getSignUpPage(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(HttpServletResponse response, @Valid @ModelAttribute SignUpRequest signUpRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "sign-up-with-errors";
        }

        try {
            SignUpResponse signUpResponse = userService.signUp(signUpRequest);
            CookieUtil.setCookie(response, USER_SESSION_COOKIE, signUpResponse.id(), signUpResponse.expiresAt());
        } catch (Exception e) {
            exceptionHandler.handle(response, bindingResult, e);
            return "sign-up-with-errors";
        }

        return "redirect:/home";
    }

    @GetMapping("/sign-in")
    public String getSignInPage(Model model) {
        model.addAttribute("signInRequest", new SignInRequest());
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(HttpServletRequest r, HttpServletResponse response, @Valid @ModelAttribute SignInRequest signInRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "sign-in-with-errors";
        }

        try {
            SignInResponse signInResponse = userService.signIn(signInRequest);
            CookieUtil.setCookie(response, USER_SESSION_COOKIE, signInResponse.id(), signInResponse.expiresAt());
        } catch (Exception e) {
            exceptionHandler.handle(response, bindingResult, e);
            return "sign-in-with-errors";
        }

        return "redirect:/home";
    }
}
