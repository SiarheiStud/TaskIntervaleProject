package com.self.taskintervale.demoREST.security.repository;

import com.self.taskintervale.demoREST.security.model.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<UserEntity> findByUsername(String username);
}
