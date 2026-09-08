package org.weather.service;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Service;
import org.weather.dto.SignUpDTO;
import org.weather.dto.UserRegistrationDTO;
import org.weather.entity.Session;
import org.weather.entity.User;
import org.weather.exception.PasswordMismatchException;
import org.weather.exception.UserRegistrationException;
import org.weather.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final int LOG_ROUNDS = 12;

    private final SessionService sessionService;
    private final UserRepository userRepository;

    public UserRegistrationDTO signUp(SignUpDTO signUpDTO) {
        String login = signUpDTO.getUsername();
        String password = signUpDTO.getPassword();
        checkPasswordMatches(password, signUpDTO.getRepeatedPassword());

        String hashedPassword = hashPassword(password);

        try {
            User user = userRepository.save(new User(login, hashedPassword));
            Session session = sessionService.createAndSave(user);
            return new UserRegistrationDTO(session.getId());
        } catch (NestedRuntimeException e) {
            changeOnlyPersistenceException(e);
            throw e;
        }
    }

    private void changeOnlyPersistenceException(NestedRuntimeException e) {
        Throwable cause = e.getCause();
        if (PersistenceException.class.isAssignableFrom(cause.getClass())) {
            throw new UserRegistrationException(e.getMessage(), e);
        }
    }

    private void checkPasswordMatches(String password, String repeatedPassword) {
        if (!password.equals(repeatedPassword)) {
            throw new PasswordMismatchException("Password does not match");
        }
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
    }
}