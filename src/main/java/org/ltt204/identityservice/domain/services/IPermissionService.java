package org.ltt204.identityservice.domain.services;

import org.ltt204.identityservice.domain.entities.Permission;
import org.ltt204.identityservice.domain.entities.User;

import java.util.List;

public interface IPermissionService {
    Permission findById(int id);

    Permission save(Permission permission);

    List<Permission> saveAll(List<String> names);

    void delete(int id);

    List<Permission> findAll();

    List<Permission> findAllByNameIn(List<String> names);

    List<Permission> findByUser(User user);

    void assignPermissionToUser(User user, Permission permission);

    void removePermissionFromUser(User user, Permission permission);
}

