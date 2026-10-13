package org.weather.service;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.data.entity.Session;
import org.weather.data.entity.User;
import org.weather.data.repository.UserRepository;
import org.weather.exception.PasswordMismatchException;
import org.weather.exception.UserLoginException;
import org.weather.exception.UserNotFoundException;
import org.weather.exception.UserRegistrationException;
import org.weather.web.dto.SignInRequest;
import org.weather.web.dto.SignInResponse;
import org.weather.web.dto.SignUpRequest;
import org.weather.web.dto.SignUpResponse;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final int LOG_ROUNDS = 12;

    private final SessionService sessionService;
    private final UserRepository userRepository;

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        String login = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();
        checkPasswordsMatches(password, signUpRequest.getRepeatedPassword());

        try {
            String hashedPassword = hashPassword(password);
            User user = userRepository.save(new User(login, hashedPassword));

            Session session = sessionService.createAndSave(user);
            int expiresAt = getExpiresAtInSeconds(session.getExpiresAt());

            return new SignUpResponse(session.getId().toString(), expiresAt);
        } catch (NestedRuntimeException e) {
            if (isPersistenceException(e)) {
                throw new UserRegistrationException(e.getMessage(), e);
            }
            throw e;
        }
    }

    public int getExpiresAtInSeconds(Instant expiresAt) {
        return (int) (expiresAt.getEpochSecond() - Instant.now().getEpochSecond());
    }

    private boolean isPersistenceException(NestedRuntimeException e) {
        Throwable cause = e.getCause();
        return PersistenceException.class.isAssignableFrom(cause.getClass());
    }

    private void checkPasswordsMatches(String password, String repeatedPassword) {
        if (!password.equals(repeatedPassword)) {
            throw new PasswordMismatchException("Password does not match");
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
    }

    private void checkPasswordMatchesTheHash(String password, String hash) {
        if (!BCrypt.checkpw(password, hash)) {
            throw new PasswordMismatchException("Password does not match");
        }
    }

    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) {
        String login = signInRequest.getUsername();
        String password = signInRequest.getPassword();

        try {
            User user = getByLogin(login);
            checkPasswordMatchesTheHash(password, user.getPassword());

            Session session = sessionService.createAndSave(user);
            int expiresAt = getExpiresAtInSeconds(session.getExpiresAt());

            return new SignInResponse(session.getId().toString(), expiresAt);
        } catch (NestedRuntimeException e) {
            if (isPersistenceException(e)) {
                throw new UserLoginException(e.getMessage(), e);
            }
            throw e;
        }
    }

    private User getByLogin(String login) {
        return userRepository.getByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found with name: " + login));
    }
}