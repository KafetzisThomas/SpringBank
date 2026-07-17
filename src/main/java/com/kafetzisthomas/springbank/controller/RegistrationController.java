package com.kafetzisthomas.springbank.controller;

import com.kafetzisthomas.springbank.dto.RegistrationForm;
import com.kafetzisthomas.springbank.service.RegistrationService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "users/register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute() RegistrationForm form, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (bindingResult.hasErrors()) {
            return "users/register";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "users/register";
        }

        try {
            registrationService.registerUser(form);
        } catch (IllegalArgumentException err) {
            model.addAttribute("error", err.getMessage());
            return "users/register";
        }

        redirectAttributes.addFlashAttribute("registered", true);
        return "redirect:/login";
    }

}
