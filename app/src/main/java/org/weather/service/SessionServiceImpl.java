package org.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.common.service.SessionService;
import org.weather.data.entity.Session;
import org.weather.data.entity.User;
import org.weather.data.repository.SessionRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableAsync
public class SessionServiceImpl implements SessionService {
    private static final int SESSION_TTL_HOURS = 1;
    private static final int SCHEDULER_DELAY_MILLISECONDS = 60000;

    private final SessionRepository sessionRepository;

    public Session createAndSave(User user) {
        Instant expiresAt = Instant.now().plus(SESSION_TTL_HOURS, ChronoUnit.HOURS);
        return sessionRepository.save(new Session(user, expiresAt));
    }

    public Optional<Session> findById(UUID id) {
        return sessionRepository.findSessionById(id);
    }

    @Async
    @Scheduled(fixedDelay = SCHEDULER_DELAY_MILLISECONDS)
    @Transactional
    public void deleteExpired() {
        sessionRepository.deleteExpiredSession(Instant.now());
    }

    public void deleteById(UUID sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}
