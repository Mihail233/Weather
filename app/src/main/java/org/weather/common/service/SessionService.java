package org.weather.common.service;

import org.weather.data.entity.Session;
import org.weather.data.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface SessionService {
    Session createAndSave(User user);

    Optional<Session> findById(UUID id);

    void deleteExpired(Session session);

    void deleteById(UUID sessionId);
}
