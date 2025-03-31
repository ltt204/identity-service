package org.ltt204.identityservice.application.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.application.dtos.role.RoleDto;
import org.ltt204.identityservice.domain.entities.Role;
import org.ltt204.identityservice.domain.repositories.IRoleRepository;
import org.ltt204.identityservice.domain.services.IPermissionService;
import org.ltt204.identityservice.domain.services.IRoleService;
import org.ltt204.identityservice.infra.persist.mappers.RoleEntityMapper;
import org.ltt204.identityservice.presentations.web.advices.AppException;
import org.ltt204.identityservice.presentations.web.advices.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    IRoleRepository roleRepository;
    RoleEntityMapper roleEntityMapper;
    IPermissionService permissionService;

    @Override
    public Role findById(int id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role save(RoleDto role) {
        log.info("Saving role with permissions {}", role.getPermissions());
        var permissions = permissionService.findAllByNameIn(
                role.getPermissions().stream().toList()
        );

        if (permissions.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND.withMessage("No permissions found"));
        }

        var roleEntity = roleEntityMapper.toDomainEntity(role);
        roleEntity.setPermissions(new HashSet<>(permissions));

        roleEntity = roleRepository.save(roleEntity);
        return roleEntity;
    }

    @Override
    public void delete(int id) {
        roleRepository.delete(id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findAllByNames(List<String> names) {
        return roleRepository.findAllByNameIn(names);
    }

    @Override
    public Role update(int id, RoleDto newRole) {
        var role = findById(id);

        var permissions = permissionService.findAllByNameIn(
                newRole.getPermissions().stream().toList()
        );

        role.setPermissions(new HashSet<>(permissions));

        return save(newRole);
    }
}
