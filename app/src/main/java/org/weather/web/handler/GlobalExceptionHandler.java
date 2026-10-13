package org.weather.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.weather.exception.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String delimiter = ". ";

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({CookieNotFoundException.class})
    public String handleCookieNotFoundExceptionException(CookieNotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({InvalidSessionIdException.class})
    public String handleInvalidSessionIdException(InvalidSessionIdException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({SessionExpiredException.class})
    public String handleSessionExpiredException(SessionExpiredException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e, Model model) {
        model.addAttribute("message", getAllMessages(e.getBindingResult()));
        return "error";
    }

    private String getAllMessages(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            messages.add(objectError.getDefaultMessage());
        }
        return String.join(delimiter, messages);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LocationClientException.class)
    public String handleLocationClientException(LocationClientException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(OpenWeatherServerException.class)
    public String handleOpenWeatherServerException(OpenWeatherServerException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WeatherClientException.class)
    public String handleWeatherClientException(WeatherClientException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleUnexpected(Exception e, Model model) {
        log.error("Unhandled exception", e);
        model.addAttribute("message", INTERNAL_SERVER_ERROR);
        return "error";
    }
}