package com.kafetzisthomas.springbank.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserDetailsManager userDetailsManager;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void whenValidRegistration_thenCreateUserAndRedirect() throws Exception {
        Mockito.when(userDetailsManager.userExists("test@example.com")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("email", "test@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("registered", true));

        Mockito.verify(userDetailsManager).createUser(any(UserDetails.class));
    }

    @Test
    void whenEmailOrPasswordMissing_thenReturnError() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("email", "")
                        .param("password", "")
                        .param("confirmPassword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void whenPasswordsDoNotMatch_thenReturnError() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("email", "test@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "different123"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attribute("error", "Passwords do not match."));
    }

    @Test
    void whenUserAlreadyExists_thenReturnError() throws Exception {
        Mockito.when(userDetailsManager.userExists("test@example.com")).thenReturn(true);

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("email", "test@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attribute("error", "User already exists."));
    }

}
