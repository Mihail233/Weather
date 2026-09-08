package org.weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.weather.dto.SignInForm;
import org.weather.dto.SignUpDTO;
import org.weather.dto.UserRegistrationDTO;
import org.weather.handler.ExceptionHandler;
import org.weather.service.UserService;
import org.weather.util.CookieUtil;

@Controller
@RequiredArgsConstructor
public class UserController {
    private static final String USER_SESSION_COOKIE = "user_session";

    private final UserService userService;
    private final ExceptionHandler exceptionHandler;
    //1 ручка отправляется форма sign up

    //2 ручка отправляется sign-up-with-errors(сюда должны вставлять какие-либо данные об ошибки), либо index.html(home page)
    //modelAttribute + связывание данных из формы с объектом + bindingResult вместе с валидацией

    @GetMapping("/sign-up")
    public String getSignUpPage(Model model) {
        model.addAttribute("signUpDTO", new SignUpDTO());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(HttpServletResponse response, @Valid @ModelAttribute SignUpDTO signUpDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "sign-up-with-errors";
        }

        try {
            UserRegistrationDTO userRegistrationDTO = userService.signUp(signUpDTO);
            CookieUtil.setCookie(response, USER_SESSION_COOKIE , userRegistrationDTO.id().toString());
        } catch (Exception e) {
            //можно ли так чтобы на post запрос была ошибка, и был второй запрос с get с statusCode 200
            exceptionHandler.handle(response, bindingResult, e);
            return "sign-up-with-errors";
        }

        return "redirect:/home";
    }

    @GetMapping("/sign-in")
    public String getSignInPage(Model model) {
        model.addAttribute("signInForm", new SignInForm());
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(@Valid @ModelAttribute SignInForm signInForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "sign-in-with-errors";
        }

        try {
//            //работа с сессиями и куками
//            UserService.add();
        } catch (Exception e) {
//            //здесь должен быть объект который (подобен фильтру из 3 проекта)
//            bindingResult.addError(new ObjectError(ANOTHER_ERROR, e.getMessage()));
//            return "sign-in-with-errors";
        }

        return "redirect:/home";
    }
}
