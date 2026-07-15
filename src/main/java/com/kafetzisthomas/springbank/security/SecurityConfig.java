package com.kafetzisthomas.springbank.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery("select email as username, password, enabled from users where email = ?");
        manager.setAuthoritiesByUsernameQuery("select email as username, authority from authorities where email = ?");

        // Custom SQL queries for managing users by email with JDBC
        manager.setUserExistsSql("select 1 from users where email = ?");
        manager.setCreateUserSql("insert into users (email, password, enabled) values (?,?,?)");
        manager.setUpdateUserSql("update users set password = ?, enabled = ? where email = ?");
        manager.setChangePasswordSql("update users set password = ? where email = ?");
        manager.setCreateAuthoritySql("insert into authorities (email, authority) values (?,?)");
        manager.setDeleteUserAuthoritiesSql("delete from authorities where email = ?");
        manager.setDeleteUserSql("delete from users where email = ?");

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/register", "/register/**").permitAll()
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .usernameParameter("email")  // use email as principal parameter
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(configurer -> configurer
                        .accessDeniedPage("/showAccessDenied")
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
