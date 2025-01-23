package org.ltt204.identityservice.repository;

import org.ltt204.identityservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>, PagingAndSortingRepository<User, String> {
    User getUserById(String id);

    boolean existsUsersByUserName(String userName);
}
