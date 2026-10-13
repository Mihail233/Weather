package org.weather.web.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.weather.exception.PasswordMismatchException;
import org.weather.exception.UnauthenticatedException;
import org.weather.exception.UserAlreadyExistsException;

@Component
public class ExceptionHandler {
    private static final String OBJECT_ERROR_NAME = "another_error";
    private static final String INTERNAL_SERVER_ERROR = "Internal Server error";

    public void handle(HttpServletResponse response, BindingResult bindingResult, Exception e) {
        String message = e.getMessage();

        switch (e) {
            case PasswordMismatchException passwordMismatchException ->
                    addErrorAndSetStatus(response, bindingResult, message, HttpStatus.BAD_REQUEST);
            case UserAlreadyExistsException userAlreadyExistsException ->
                    addErrorAndSetStatus(response, bindingResult, message, HttpStatus.CONFLICT);
            case UnauthenticatedException unauthenticatedException ->
                    addErrorAndSetStatus(response, bindingResult, message, HttpStatus.UNAUTHORIZED);
            default ->
                    addErrorAndSetStatus(response, bindingResult, INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addErrorAndSetStatus(HttpServletResponse response, BindingResult bindingResult, String e, HttpStatus status) {
        bindingResult.addError(new ObjectError(OBJECT_ERROR_NAME, e));
        response.setStatus(status.value());
    }
}
