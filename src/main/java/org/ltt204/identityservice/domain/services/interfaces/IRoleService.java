package org.ltt204.identityservice.domain.services.interfaces;

import org.ltt204.identityservice.application.dtos.role.RoleDto;
import org.ltt204.identityservice.domain.entities.Role;

import java.util.List;

public interface IRoleService {
    Role findById(int id);

    Role save(RoleDto role);

    Role update(int id, RoleDto role);

    void delete(int id);

    List<Role> findAll();

    List<Role> findAllByNames(List<String> names);
}
