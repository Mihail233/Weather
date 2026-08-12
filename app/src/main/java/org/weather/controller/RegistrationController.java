package org.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    //1 ручка отправляется форма sign up
    //2 ручка отправляется sign-up-with-errors(сюда должны вставлять какие-либо данные об ошибки), либо index.html(home page)
    //modelAttribute + связывание данных из формы с объектом + bindingResult вместе с валидацией

    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }
}
