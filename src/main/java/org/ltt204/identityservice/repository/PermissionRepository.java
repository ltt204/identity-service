package org.ltt204.identityservice.repository;

import org.ltt204.identityservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    List<Permission> findAllByNameIn(Collection<String> names);
}
