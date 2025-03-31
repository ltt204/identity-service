package org.ltt204.identityservice.infra.persist.jpa.repositories;

import org.ltt204.identityservice.domain.entities.Role;
import org.ltt204.identityservice.infra.persist.jpa.entities.JpaRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface JpaRoleRepository extends JpaRepository<JpaRole, Integer> {
    JpaRole findByName(String name);

    JpaRole findFirstByName(String name);

    List<JpaRole> findAllByNameIn(Collection<String> names);
}
