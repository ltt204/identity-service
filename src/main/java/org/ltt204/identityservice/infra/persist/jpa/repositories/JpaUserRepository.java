package org.ltt204.identityservice.infra.persist.jpa.repositories;


import org.ltt204.identityservice.infra.persist.jpa.entities.JpaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

//@Repository
public interface JpaUserRepository extends JpaRepository<JpaUser, String>, PagingAndSortingRepository<JpaUser, String> {
    boolean existsUsersByUsername(String username);

    Optional<JpaUser> findFirstByUsername(String username);
}
