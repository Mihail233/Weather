package org.weather.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.weather.data.entity.Session;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    String SELECT_SESSION_WITH_USER_JPQL = """
            SELECT s
            FROM Session s
            LEFT JOIN FETCH s.user
            WHERE s.id = :id
            """;
    String ID_PARAM = "id";

    @Query(value = SELECT_SESSION_WITH_USER_JPQL)
    Optional<Session> findSessionById(@Param(ID_PARAM) Long id);
}
