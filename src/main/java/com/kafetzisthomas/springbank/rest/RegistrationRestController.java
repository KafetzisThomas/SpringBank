package com.kafetzisthomas.springbank.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kafetzisthomas.springbank.dto.RegistrationForm;
import com.kafetzisthomas.springbank.service.RegistrationService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class RegistrationRestController {

    private final RegistrationService registrationService;

    public RegistrationRestController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerMobileUser(@Valid @RequestBody RegistrationForm form) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }

        try {
            registrationService.registerUser(form);
            return ResponseEntity.ok("Registration successful");
        } catch (IllegalArgumentException err) {
            return ResponseEntity.badRequest().body(err.getMessage());
        }
    }
}
