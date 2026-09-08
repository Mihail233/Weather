package org.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.weather.entity.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
}
