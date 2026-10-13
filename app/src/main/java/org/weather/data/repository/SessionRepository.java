package org.weather.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.weather.data.entity.Session;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    String SELECT_ALL_WITH_USERS_JPQL = """
            SELECT s
            FROM Session s
            LEFT JOIN FETCH s.user
            """;

    String FILTER_BY_SESSION_ID_JPQL = """
            WHERE s.id = :id
            """;

    String DELETE_BY_EXPIRES_AT = """
            DELETE FROM Session s WHERE s.expiresAt < :now
            """;
    String ID_PARAM = "id";
    String NOW_PARAM = "now";

    @Query(value = SELECT_ALL_WITH_USERS_JPQL + FILTER_BY_SESSION_ID_JPQL)
    Optional<Session> findSessionById(@Param(ID_PARAM) UUID id);

    @Modifying
    @Query(value = DELETE_BY_EXPIRES_AT)
    void deleteExpiredSession(@Param(NOW_PARAM) Instant now);
}
