package org.weather.common.service;

import org.weather.data.entity.User;
import org.weather.web.dto.SignInRequest;
import org.weather.web.dto.SignInResponse;
import org.weather.web.dto.SignUpRequest;
import org.weather.web.dto.SignUpResponse;

public interface UserService {
    SignUpResponse signUp(SignUpRequest signUpRequest);

    SignInResponse signIn(SignInRequest signInRequest);

    User findUserById(Long id);

    void signOut(Long sessionId);

}
