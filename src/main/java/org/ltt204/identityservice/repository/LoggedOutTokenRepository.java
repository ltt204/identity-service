package org.ltt204.identityservice.repository;

import org.ltt204.identityservice.entity.LoggedOutToken;
import org.ltt204.identityservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LoggedOutTokenRepository extends JpaRepository<LoggedOutToken, Integer> {
    List<LoggedOutToken> findByToken(String token);
}
