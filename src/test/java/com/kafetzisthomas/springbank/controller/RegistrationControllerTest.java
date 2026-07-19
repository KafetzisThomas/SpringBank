package com.kafetzisthomas.springbank.controller;

import com.kafetzisthomas.springbank.dto.RegistrationForm;
import com.kafetzisthomas.springbank.service.RegistrationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrationService registrationService;

    @Test
    void showRegistrationForm_returnsRegisterView() throws Exception {
        mockMvc.perform(get("/register")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attributeExists("form"));
    }

    @Test
    void doRegister_validForm_redirectsToLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("email", "user@test.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("registered", true));

        verify(registrationService).registerUser(any(RegistrationForm.class));
    }

    @Test
    void doRegister_passwordsDoNotMatch_returnsRegisterViewWithError() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("email", "user@test.com")
                        .param("password", "password123")
                        .param("confirmPassword", "different123"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void doRegister_serviceThrowsException_returnsRegisterViewWithError() throws Exception {
        doThrow(new IllegalArgumentException("Email already exists")).when(registrationService).registerUser(any(RegistrationForm.class));
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("email", "user@test.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attribute("error", "Email already exists"));
    }
}
