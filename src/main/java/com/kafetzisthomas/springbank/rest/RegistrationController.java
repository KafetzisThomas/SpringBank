package com.kafetzisthomas.springbank.rest;

import com.kafetzisthomas.springbank.dto.RegistrationForm;
import com.kafetzisthomas.springbank.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String doRegister(@ModelAttribute() RegistrationForm form, Model model, RedirectAttributes redirectAttributes) {
        if (form.getEmail() == null || form.getEmail().isBlank() || form.getPassword() == null || form.getPassword().isBlank()) {
            model.addAttribute("error", "Email and password are required.");
            return "users/register";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "users/register";
        }

        try {
            registrationService.registerUser(form);
        } catch (Exception err) {
            model.addAttribute("error", err.getMessage());
            return "users/register";
        }

        redirectAttributes.addFlashAttribute("registered", true);
        return "redirect:/login";
    }

}
