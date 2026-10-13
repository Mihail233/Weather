package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weather.data.entity.Session;
import org.weather.data.entity.User;
import org.weather.data.repository.SessionRepository;
import org.weather.exception.SessionExpiredException;

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

    public void deleteExpired(Session session) {
        if (checkExpiration(session)) {
            sessionRepository.delete(session);
            //не особо нравится что здесь кидается ошибка
            throw new SessionExpiredException("Session expired");
        }
    }

    private boolean checkExpiration(Session session) {
        Instant expiredAt = session.getExpiresAt();
        Instant now = Instant.now();

        return expiredAt.getEpochSecond() <= now.getEpochSecond();
    }
}
