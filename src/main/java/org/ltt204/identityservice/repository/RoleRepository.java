package org.ltt204.identityservice.repository;

import org.ltt204.identityservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
