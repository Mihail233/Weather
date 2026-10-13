package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.common.service.SessionService;
import org.weather.common.service.UserService;
import org.weather.data.entity.Session;
import org.weather.data.entity.User;
import org.weather.data.repository.UserRepository;
import org.weather.exception.PasswordMismatchException;
import org.weather.exception.UnauthenticatedException;
import org.weather.exception.UserAlreadyExistsException;
import org.weather.exception.UserNotFoundException;
import org.weather.web.dto.SignInRequest;
import org.weather.web.dto.SignInResponse;
import org.weather.web.dto.SignUpRequest;
import org.weather.web.dto.SignUpResponse;

import java.time.Instant;
import java.util.UUID;

import static org.weather.util.ServiceUtil.isConstraintViolationException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
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
            if (isConstraintViolationException(e)) {
                throw new UserAlreadyExistsException("Failed to sign up the user, a user with this name is already registered", e);
            }
            throw e;
        }
    }

    public int getExpiresAtInSeconds(Instant expiresAt) {
        return (int) (expiresAt.getEpochSecond() - Instant.now().getEpochSecond());
    }

    private void checkPasswordsMatches(String password, String repeatedPassword) {
        if (!password.equals(repeatedPassword)) {
            throw new PasswordMismatchException("The entered passwords do not match");
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
    }

    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) {
        String login = signInRequest.getUsername();
        String password = signInRequest.getPassword();
        User user = findByLogin(login);
        checkPasswordMatchesTheHash(password, user.getPassword());

        Session session = sessionService.createAndSave(user);
        int expiresAt = getExpiresAtInSeconds(session.getExpiresAt());

        return new SignInResponse(session.getId().toString(), expiresAt);
    }

    private User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UnauthenticatedException("Incorrect password or username; please check and try again"));
    }

    private void checkPasswordMatchesTheHash(String password, String hash) {
        if (!BCrypt.checkpw(password, hash)) {
            throw new UnauthenticatedException("Incorrect password or username; please check and try again");
        }
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void signOut(UUID sessionId) {
        sessionService.deleteById(sessionId);
    }
}