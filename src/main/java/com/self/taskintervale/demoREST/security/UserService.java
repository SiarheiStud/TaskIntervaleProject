package com.self.taskintervale.demoREST.security;

import com.self.taskintervale.demoREST.security.model.Status;
import com.self.taskintervale.demoREST.security.model.UserEntity;
import com.self.taskintervale.demoREST.security.repository.UserRepositoryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service()
public class UserService implements UserDetailsService {

    private final UserRepositoryImpl userRepositoryImpl;

    public UserService(UserRepositoryImpl userRepositoryImpl) {
        this.userRepositoryImpl = userRepositoryImpl;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepositoryImpl.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not fount in DataBase"));

        //return new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.getRole().getAuthority());
        return userEntityIntoUserDetails(userEntity);
    }

    private UserDetails userEntityIntoUserDetails(UserEntity userEntity) {
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(userEntity.getRole().getAuthority())
                .accountExpired(false)
                .accountLocked(userEntity.getStatus().name().equals(Status.BANNED.name()))
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
