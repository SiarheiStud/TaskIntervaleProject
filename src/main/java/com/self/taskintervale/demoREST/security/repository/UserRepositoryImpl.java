package com.self.taskintervale.demoREST.security.repository;

import com.self.taskintervale.demoREST.security.model.UserEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository{

    private final JdbcTemplate jdbcTemplate;
    private static final String FIND_BY_USERNAME = "SELECT * FROM user_info where username = ?";

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        UserEntity userEntity = jdbcTemplate
                .queryForObject(FIND_BY_USERNAME, new BeanPropertyRowMapper<>(UserEntity.class), username);
        return Optional.ofNullable(userEntity);
    }
}
