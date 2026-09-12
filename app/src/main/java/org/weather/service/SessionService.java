package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weather.entity.Session;
import org.weather.entity.User;
import org.weather.exception.SessionExpiredException;
import org.weather.repository.SessionRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private static final int SESSION_TTL = 1;
    private final SessionRepository sessionRepository;

    public Session createAndSave(User user) {
        Instant expiresAt = Instant.now().plus(SESSION_TTL, ChronoUnit.HOURS);
        return sessionRepository.save(new Session(user, expiresAt));
    }

    public Optional<Session> getById(Long id) {
        return sessionRepository.findById(id);
    }

    public void checkSessionValidity(Session session) {
        Instant expiredAt = session.getExpiresAt();
        Instant now = Instant.now();

        if (expiredAt.getEpochSecond() <= now.getEpochSecond()) {
            throw new SessionExpiredException("Session expired");
        }
    }
}
