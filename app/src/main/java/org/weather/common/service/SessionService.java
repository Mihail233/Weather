package org.weather.common.service;

import org.weather.data.entity.Session;
import org.weather.data.entity.User;

import java.util.Optional;

public interface SessionService {
    Session createAndSave(User user);

    Optional<Session> findById(Long id);

    void deleteExpired(Session session);

    void deleteById(Long sessionId);
}
