package org.ltt204.identityservice.domain.repositories;

import org.ltt204.identityservice.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserRepository {
    User findById(String id);

    User findFirstByUserName(String name);

    User save(User user);

    User update(User user);

    void delete(User user);

    Page<User> findAll(Pageable pageable);

    boolean existsById(String id);
}
