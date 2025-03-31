package org.ltt204.identityservice.domain.repositories;

import org.ltt204.identityservice.domain.entities.Permission;

import java.util.List;

public interface IPermissionRepository {
    Permission findById(int id);

    Permission save(Permission permission);

    List<Permission> saveAll(List<String> names);

    void delete(Permission permission);

    List<Permission> findAll();

    List<Permission> findByRoleId(int roleId);

    List<Permission> findAllByNameIn(List<String> names);
}
