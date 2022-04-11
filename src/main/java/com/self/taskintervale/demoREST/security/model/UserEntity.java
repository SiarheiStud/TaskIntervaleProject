package com.self.taskintervale.demoREST.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private Long id;
    @Column(value = "username")
    private String username;
    @Column(value = "password")
    private String password;
    @Column(value = "first_name")
    private String firstName;
    @Column(value = "last_name")
    private String lastName;
    @Column(value = "email")
    private String email;
    @Column(value = "role")
    private Role role;
    @Column(value = "status")
    private Status status;

}
