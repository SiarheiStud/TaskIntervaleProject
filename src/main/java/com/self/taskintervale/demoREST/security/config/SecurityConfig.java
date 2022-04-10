/*
 * @author Сергей Студенков
 * @since "10.04.2022, 12:50"
 * @version V 1.0.0
 */

package com.self.taskintervale.demoREST.security.config;

import com.self.taskintervale.demoREST.security.model.CustomPermission;
import com.self.taskintervale.demoREST.security.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.GET, "/books/**").hasAuthority(CustomPermission.BOOK_READ.name())
                .antMatchers(HttpMethod.POST, "/books/**").hasAuthority(CustomPermission.BOOK_WRITE.name())
                .antMatchers(HttpMethod.PUT, "/books/**").hasAuthority(CustomPermission.BOOK_WRITE.name())
                .antMatchers(HttpMethod.DELETE, "/books/**").hasAuthority(CustomPermission.BOOK_DELETE.name())
                .antMatchers("/price/**").hasAuthority(CustomPermission.WORK_WITH_BANK.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("admin"))
                        .authorities(Role.ADMIN.getAuthority())
                        .build(),
                User.builder()
                        .username("user")
                        .password(passwordEncoder().encode("user"))
                        .authorities(Role.USER.getAuthority())
                        .build(),
                User.builder()
                        .username("manager")
                        .password(passwordEncoder().encode("manager"))
                        .authorities(Role.MANAGER.getAuthority())
                        .build()
        );
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(15);
    }
}
