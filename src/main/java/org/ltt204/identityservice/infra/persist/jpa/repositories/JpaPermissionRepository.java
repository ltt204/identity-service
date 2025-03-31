package org.ltt204.identityservice.infra.persist.jpa.repositories;

import org.ltt204.identityservice.infra.persist.jpa.entities.JpaPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface JpaPermissionRepository extends JpaRepository<JpaPermission, Integer> {
    List<JpaPermission> findAllByNameIn(Collection<String> names);
}
