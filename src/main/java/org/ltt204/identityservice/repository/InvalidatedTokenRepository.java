package org.ltt204.identityservice.repository;

import org.ltt204.identityservice.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Integer> {
    List<InvalidatedToken> findByToken(String token);
}
