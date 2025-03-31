package org.ltt204.identityservice.application.repositories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.domain.entities.User;
import org.ltt204.identityservice.domain.repositories.IRoleRepository;
import org.ltt204.identityservice.domain.repositories.IUserRepository;
import org.ltt204.identityservice.infra.persist.jpa.repositories.JpaUserRepository;
import org.ltt204.identityservice.infra.persist.mappers.UserEntityMapper;
import org.ltt204.identityservice.presentations.web.advices.AppException;
import org.ltt204.identityservice.presentations.web.advices.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRepositoryAdapter implements IUserRepository {
    JpaUserRepository jpaUserRepository;
    UserEntityMapper userEntityMapper;

    IRoleRepository roleRepository;

    @Value("${app.security.default-role:USER}")
    String DEFAULT_ROLE = "USER";

    @Override
    public User findById(String id) {
        var jpaUser = jpaUserRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND.withMessage("User not found"))
        );

        return userEntityMapper.toDomainEntity(jpaUser);
    }

    @Override
    public User findFirstByUserName(String name) {
        var jpaUser = jpaUserRepository.findFirstByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND.withMessage("User not found"))
        );

        return userEntityMapper.toDomainEntity(jpaUser);
    }

    @Override
    public User save(User user) {
        if (jpaUserRepository.existsUsersByUsername(user.getUsername())) {
            throw new AppException(ErrorCode.CONFLICT.withMessage("Username is already taken"));
        }

        var userRoles = roleRepository.findAllByNameIn(List.of(DEFAULT_ROLE));
        user.setRoles(new HashSet<>(userRoles));

        var jpaUser = userEntityMapper.toJpaEntity(user);
        jpaUser = jpaUserRepository.save(jpaUser);

        return userEntityMapper.toDomainEntity(jpaUser);
    }

    @Override
    public User update(User newUser) {
        var jpaUser = jpaUserRepository.findById(newUser.getId()).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND.withMessage("User not found"))
        );

        var user = userEntityMapper.toDomainEntity(jpaUser);
        userEntityMapper.toEntityFromUpdateRequest(user, newUser);
        jpaUser = jpaUserRepository.save(jpaUser);

        return userEntityMapper.toDomainEntity(jpaUser);
    }

    @Override
    public void delete(User user) {
        var jpaUser = jpaUserRepository.findById(user.getId()).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND.withMessage("User not found"))
        );

        jpaUserRepository.delete(jpaUser);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return jpaUserRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "username"))
                )
        ).map(
                userEntityMapper::toDomainEntity
        );
    }

    @Override
    public boolean existsById(String id) {
        return jpaUserRepository.existsById(id);
    }
}
