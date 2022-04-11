package com.self.taskintervale.demoREST.security.config;

import com.self.taskintervale.demoREST.security.UserService;
import com.self.taskintervale.demoREST.security.model.CustomPermission;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/books").permitAll()
                .antMatchers(HttpMethod.POST, "/books").hasAuthority(CustomPermission.BOOK_WRITE.name())
                .antMatchers(HttpMethod.PUT, "/books/**").hasAuthority(CustomPermission.BOOK_WRITE.name())
                .antMatchers(HttpMethod.DELETE, "/books/**").hasAuthority(CustomPermission.BOOK_DELETE.name())
                .antMatchers("/price/**").hasAuthority(CustomPermission.WORK_WITH_BANK.name())
                .anyRequest()
                .authenticated()
                .and()
                    .formLogin()
                .and()
                    .logout()
                    .logoutSuccessUrl("/login")
                .and()
                    .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(15);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }
}
