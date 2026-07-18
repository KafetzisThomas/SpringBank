package com.kafetzisthomas.springbank.rest;

import com.kafetzisthomas.springbank.service.RegistrationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistrationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrationService registrationService;

    private static final String REGISTER_URL = "/api/users/register";

    private String registrationJson(String email, String password, String confirmPassword) {
        return """
            {
                "email": "%s",
                "password": "%s",
                "confirmPassword": "%s"
            }
            """.formatted(email, password, confirmPassword);
    }

    @Test
    void register_validForm_returns200() throws Exception {
        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson("user@test.com", "password123", "password123")))
                .andExpect(status().isOk());

        verify(registrationService).registerUser(any());
    }

    @Test
    void register_passwordMismatch_returns400() throws Exception {
        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson("user@test.com", "password123", "different123")))
                .andExpect(status().isBadRequest());

        verify(registrationService, never()).registerUser(any());
    }

    @Test
    void register_userAlreadyExists_returns400() throws Exception {
        doThrow(new IllegalArgumentException("User already exists.")).when(registrationService).registerUser(any());
        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson("user@test.com", "password123", "password123")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_blankEmail_returns400() throws Exception {
        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson("", "password123", "password123")))
                .andExpect(status().isBadRequest());

        verify(registrationService, never()).registerUser(any());
    }

    @Test
    void register_blankPassword_returns400() throws Exception {
        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson("user@test.com", "", "")))
                .andExpect(status().isBadRequest());

        verify(registrationService, never()).registerUser(any());
    }

}
