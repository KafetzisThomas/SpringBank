package com.kafetzisthomas.springbank.service;

import com.kafetzisthomas.springbank.dto.RegistrationForm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceImplTest {

    private UserDetailsManager userDetailsManager;
    private PasswordEncoder passwordEncoder;
    private RegistrationServiceImpl registrationService;

    @BeforeEach
    void setUp() {
        userDetailsManager = mock(UserDetailsManager.class);
        passwordEncoder = mock(PasswordEncoder.class);
        registrationService = new RegistrationServiceImpl(userDetailsManager, passwordEncoder);
    }

    private RegistrationForm createForm(String email, String password) {
        RegistrationForm form = new RegistrationForm();
        form.setEmail(email);
        form.setPassword(password);
        form.setConfirmPassword(password);
        return form;
    }

    @Test
    void registerUser_newUser_createsUserWithCorrectDetails() {
        when(userDetailsManager.userExists("user@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");

        registrationService.registerUser(createForm("user@test.com", "password123"));

        ArgumentCaptor<UserDetails> captor = ArgumentCaptor.forClass(UserDetails.class);
        verify(userDetailsManager).createUser(captor.capture());

        UserDetails created = captor.getValue();
        assertEquals("user@test.com", created.getUsername());
        assertTrue(created.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void registerUser_newUser_encodesPassword() {
        when(userDetailsManager.userExists("user@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");

        registrationService.registerUser(createForm("user@test.com", "password123"));

        verify(passwordEncoder).encode("password123");
    }

    @Test
    void registerUser_existingUser_throwsErrorAndDoesNotCreate() {
        when(userDetailsManager.userExists("user@test.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> registrationService.registerUser(createForm("user@test.com", "password123")));

        assertEquals("User already exists.", ex.getMessage());
        verify(userDetailsManager, never()).createUser(any());
    }

}
