package com.poyraz.controller.securityControllers;

import com.poyraz.dto.RegistrationResponseDTO;
import com.poyraz.dto.RegistrationUserDTO;
import com.poyraz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDTO> register(@Valid @RequestBody RegistrationUserDTO requestDTO) {
        RegistrationResponseDTO registered = userService.register(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    }
}

