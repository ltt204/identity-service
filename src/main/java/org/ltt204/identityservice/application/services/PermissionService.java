package org.ltt204.identityservice.application.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.domain.entities.Permission;
import org.ltt204.identityservice.domain.entities.User;
import org.ltt204.identityservice.domain.repositories.IPermissionRepository;
import org.ltt204.identityservice.domain.services.IPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionService implements IPermissionService {
    IPermissionRepository permissionRepository;

    @Override
    public Permission findById(int id) {
        log.info("Permission Id {}", id);

        return permissionRepository.findById(id);
    }

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public List<Permission> saveAll(List<String> names) {
//        return permissionRepository.save(names);
        return List.of();
    }


    @Override
    public void delete(int id) {
        var permission = permissionRepository.findById(id);

        permissionRepository.delete(permission);
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public List<Permission> findAllByNameIn(List<String> names) {
        return permissionRepository.findAllByNameIn(names);
    }

    @Override
    public List<Permission> findByUser(User user) {
        return List.of();
    }

    @Override
    public void assignPermissionToUser(User user, Permission permission) {

    }

    @Override
    public void removePermissionFromUser(User user, Permission permission) {

    }
}
