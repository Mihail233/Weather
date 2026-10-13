package org.weather.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.weather.config.TestDatabaseConfig;
import org.weather.data.entity.Session;
import org.weather.data.entity.User;
import org.weather.data.repository.SessionRepository;
import org.weather.data.repository.UserRepository;
import org.weather.exception.SessionExpiredException;
import org.weather.exception.UserRegistrationException;
import org.weather.service.SessionService;
import org.weather.service.UserService;
import org.weather.web.dto.SignUpRequest;
import org.weather.web.dto.SignUpResponse;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDatabaseConfig.class})
@WebAppConfiguration
class UserControllerTest {
    private static final String LOGIN = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final String REPEATED_PASSWORD = "testPassword";

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    @Sql(scripts = {"/data/cleanUp.sql"})
    @Transactional
    void signUpUserAndSessionAssociationTest() {
        SignUpRequest signUpRequest = fillSignUpRequest(new SignUpRequest());
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);

        Optional<User> user = userRepository.getByLogin(LOGIN);
        Assertions.assertDoesNotThrow(() -> {
            user.get();
        });

        Session session = sessionRepository.getReferenceById(Long.valueOf(signUpResponse.id()));
        Assertions.assertDoesNotThrow(() -> {
            session.getExpiresAt();
        });

        Assertions.assertEquals(session.getUser().getId(), user.get().getId());
    }

    private SignUpRequest fillSignUpRequest(SignUpRequest signUpRequest) {
        signUpRequest.setUsername(LOGIN);
        signUpRequest.setPassword(PASSWORD);
        signUpRequest.setRepeatedPassword(REPEATED_PASSWORD);
        return signUpRequest;
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql"})
    void signUpUserAlreadyRegistered() {
        SignUpRequest signUpRequest = fillSignUpRequest(new SignUpRequest());
        userService.signUp(signUpRequest);

        Assertions.assertThrows(UserRegistrationException.class, () -> userService.signUp(signUpRequest));
        Assertions.assertEquals(1, userRepository.count());
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql"})
    @Transactional
    void sessionServiceCheckSessionExpirationTest() {
        User user = userRepository.save(new User(LOGIN, PASSWORD));

        Instant expiredAt = Instant.now().minus(1L, ChronoUnit.HOURS);
        Session session = sessionRepository.save(new Session(user, expiredAt));

        Assertions.assertThrows(SessionExpiredException.class, () -> sessionService.deleteExpired(session));
        Assertions.assertEquals(0, sessionRepository.count());
    }
}