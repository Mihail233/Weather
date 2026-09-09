package org.weather.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.weather.exception.PasswordMismatchException;
import org.weather.exception.UserNotFoundException;
import org.weather.exception.UserRegistrationException;

@Component
public class ExceptionHandler {
    private static final String OBJECT_ERROR_NAME = "another_error";

    public void handle(HttpServletResponse response, BindingResult bindingResult, Exception e) {
        String message = e.getMessage();
        switch (e) {
            case PasswordMismatchException passwordMismatchException ->
                    addErrorAndSetStatus(response, bindingResult, message, HttpStatus.BAD_REQUEST);
            case UserRegistrationException userRegistrationException ->
                    addErrorAndSetStatus(response, bindingResult, message, HttpStatus.BAD_REQUEST);
            case UserNotFoundException userNotFoundException ->
                    addErrorAndSetStatus(response, bindingResult, message, HttpStatus.NOT_FOUND);
            default ->
                    addErrorAndSetStatus(response, bindingResult, "Internal Server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addErrorAndSetStatus(HttpServletResponse response, BindingResult bindingResult, String e, HttpStatus status) {
        bindingResult.addError(new ObjectError(OBJECT_ERROR_NAME, e));
        response.setStatus(status.value());
    }
}
