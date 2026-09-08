package org.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.weather.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
