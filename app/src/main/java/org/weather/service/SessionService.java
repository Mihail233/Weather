package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weather.entity.Session;
import org.weather.entity.User;
import org.weather.repository.SessionRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

//здесь по большей части круды
@Service
@RequiredArgsConstructor
public class SessionService {
    private static final int SESSION_TTL = 1;
    private final SessionRepository sessionRepository;

    public Session createAndSave(User user) {
        Instant expiresAt = Instant.now().plus(SESSION_TTL, ChronoUnit.HOURS);
        return sessionRepository.save(new Session(user, expiresAt));
    }
}
