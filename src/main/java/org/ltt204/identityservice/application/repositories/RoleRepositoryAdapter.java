package org.ltt204.identityservice.application.repositories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.domain.entities.Permission;
import org.ltt204.identityservice.domain.entities.Role;
import org.ltt204.identityservice.domain.repositories.IRoleRepository;
import org.ltt204.identityservice.infra.persist.jpa.repositories.JpaRoleRepository;
import org.ltt204.identityservice.infra.persist.mappers.RoleEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleRepositoryAdapter implements IRoleRepository {
    JpaRoleRepository jpaRoleRepository;
    RoleEntityMapper roleEntityMapper;

    @Override
    public Role findById(int id) {
        var appEntity = jpaRoleRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Role not found")
        );

        return roleEntityMapper.toDomainEntity(appEntity);
    }

    @Override
    public Role save(Role role) {
        var appEntity = roleEntityMapper.toJpaEntity(role);
        log.info("Saving role jpa with permissions {}",
                role.getPermissions().stream().map(Permission::getName).toList());

        return roleEntityMapper.toDomainEntity(
                jpaRoleRepository.save(appEntity)
        );
    }

    @Override
    public void delete(int id) {
        var role = findById(id);

        jpaRoleRepository.delete(
                roleEntityMapper.toJpaEntity(role)
        );
    }

    @Override
    public List<Role> findAll() {
        return jpaRoleRepository.findAll().stream()
                .map(roleEntityMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Role> findAllByNameIn(List<String> names) {
        return jpaRoleRepository.findAllByNameIn(names).stream().map(
                roleEntityMapper::toDomainEntity
        ).toList();
    }
}
