package org.weather.handler;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.weather.exception.CookieNotFoundException;
import org.weather.exception.SessionExpiredException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({CookieNotFoundException.class})
    public String handleCookieNotFoundExceptionException(CookieNotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({SessionExpiredException.class})
    public String handleSessionExpiredException(SessionExpiredException e) {
        return "error";
    }
}