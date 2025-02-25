package org.ltt204.identityservice.repository;

import org.ltt204.identityservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);

    Role findAllByNameIn(Collection<String> names);

    Role findFirstByName(String name);
}
