package org.weather.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.weather.data.entity.Location;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    String SELECT_ALL_JPQL = """
            SELECT l
            FROM Location l
            """;
    String ADD_USERS_JPQL = """
            LEFT JOIN FETCH l.user
            """;
    String FILTER_BY_LOCATION_ID_JPQL = """
            WHERE l.id = :id
            """;
    String FILTER_BY_USER_ID_JPQL = """
            WHERE l.user.id = :id
            """;

    String SELECT_ALL_WITH_USER_JPQL = SELECT_ALL_JPQL + ADD_USERS_JPQL;

    String ID_PARAM = "id";

    @Query(value = SELECT_ALL_WITH_USER_JPQL + FILTER_BY_LOCATION_ID_JPQL)
    Optional<Location> findLocationById(@Param(ID_PARAM) Long id);

    @Query(value = SELECT_ALL_JPQL + FILTER_BY_USER_ID_JPQL)
    List<Location> findAllByUserId(@Param(ID_PARAM) Long userId);
}
