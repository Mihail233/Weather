package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weather.common.service.SessionService;
import org.weather.data.entity.Session;
import org.weather.data.entity.User;
import org.weather.data.repository.SessionRepository;
import org.weather.exception.UnauthenticatedException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private static final int SESSION_TTL = 1;

    private final SessionRepository sessionRepository;

    public Session createAndSave(User user) {
        Instant expiresAt = Instant.now().plus(SESSION_TTL, ChronoUnit.HOURS);
        return sessionRepository.save(new Session(user, expiresAt));
    }

    public Optional<Session> findById(UUID id) {
        return sessionRepository.findSessionById(id);
    }

    public void deleteExpired(Session session) {
        if (checkExpiration(session)) {
            sessionRepository.delete(session);
            throw new UnauthenticatedException("Failed to authentication the user, please sign in");
        }
    }

    private boolean checkExpiration(Session session) {
        Instant expiredAt = session.getExpiresAt();
        Instant now = Instant.now();

        return expiredAt.getEpochSecond() <= now.getEpochSecond();
    }

    public void deleteById(UUID sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}
