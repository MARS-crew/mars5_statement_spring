package com.mars.statement.api.user.repository;

import com.mars.statement.api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String username);
    User findByEmail(String email);

}
