package org.weather.web.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.weather.exception.CookieNotFoundException;
import org.weather.exception.SessionExpiredException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String delimiter = ". ";

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public String handleConstraintViolationException(ConstraintViolationException e, Model model) {
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

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleUnexpected(Exception e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}