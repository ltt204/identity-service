package org.ltt204.identityservice.domain.repositories;

import org.ltt204.identityservice.domain.entities.Role;

import java.util.List;


public interface IRoleRepository {
    Role findById(int id);

    Role save(Role role);

    void delete(int id);

    List<Role> findAll();

    List<Role> findAllByNameIn(List<String> names);
}
