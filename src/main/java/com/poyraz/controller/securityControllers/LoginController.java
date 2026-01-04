package com.poyraz.controller.securityControllers;

import com.poyraz.dto.RegistrationUserDTO;
import com.poyraz.exceptions.UsernameAlreadyExistsException;
import com.poyraz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "auth/logout-success";
    }

    @GetMapping("/auth/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("registrationUser")) {
            model.addAttribute("registrationUser", new RegistrationUserDTO());
        }
        return "auth/register";
    }

    @PostMapping("/auth/register/form")
    public String registerUser(@Valid @ModelAttribute("registrationUser") RegistrationUserDTO registrationUserDTO,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.register(registrationUserDTO);
        } catch (UsernameAlreadyExistsException ex) {
            bindingResult.rejectValue("username", "username.exists", "Username already exists");
            return "auth/register";
        }

        return "redirect:/login?registered";
    }

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return "auth/access-denied";
    }

}
