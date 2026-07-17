package com.kafetzisthomas.springbank.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.kafetzisthomas.springbank.dto.RegistrationForm;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegistrationForm form) {
        if (userDetailsManager.userExists(form.getEmail())) {
            throw new IllegalArgumentException("User already exists.");
        }

        UserDetails user = User.withUsername(form.getEmail())
                .passwordEncoder(passwordEncoder::encode)
                .password(form.getPassword())
                .roles("USER")
                .build();

        userDetailsManager.createUser(user);
    }
}
