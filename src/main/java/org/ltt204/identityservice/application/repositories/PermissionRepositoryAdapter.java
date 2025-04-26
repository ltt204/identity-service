package org.ltt204.identityservice.application.repositories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ltt204.identityservice.domain.entities.Permission;
import org.ltt204.identityservice.domain.repositories.IPermissionRepository;
import org.ltt204.identityservice.infra.persist.jpa.repositories.JpaPermissionRepository;
import org.ltt204.identityservice.infra.persist.mappers.PermissionEntityMapper;
import org.ltt204.identityservice.presentations.web.advices.AppException;
import org.ltt204.identityservice.presentations.web.advices.ErrorCode;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Repository
public class PermissionRepositoryAdapter implements IPermissionRepository {
    JpaPermissionRepository jpaPermissionRepository;
    PermissionEntityMapper permissionEntityMapper;
    private final ListableBeanFactory listableBeanFactory;

    @Override
    public Permission findById(int id) {
        var appEntity = jpaPermissionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND, "Permission not found")
        );
        return permissionEntityMapper.toDomainEntity(appEntity);
    }

    @Override
    public Permission save(Permission permission) {
        var appEntity = permissionEntityMapper.toJpaEntity(permission);

        return permissionEntityMapper.toDomainEntity(
                jpaPermissionRepository.save(appEntity)
        );
    }

    @Override
    public List<Permission> saveAll(List<String> names) {
        return List.of();
    }

    @Override
    public void delete(Permission permission) {
        jpaPermissionRepository.delete(
                permissionEntityMapper.toJpaEntity(permission)
        );
    }

    @Override
    public List<Permission> findAll() {
        return jpaPermissionRepository.findAll().stream()
                .map(permissionEntityMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Permission> findByRoleId(int roleId) {
        return List.of();
    }

    @Override
    public List<Permission> findAllByNameIn(List<String> names) {
        return jpaPermissionRepository.findAllByNameIn(names).stream()
                .map(permissionEntityMapper::toDomainEntity)
                .toList();
    }
}
