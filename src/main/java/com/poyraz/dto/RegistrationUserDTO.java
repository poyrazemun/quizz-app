package com.poyraz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationUserDTO {

    @NotBlank(message = "username.required")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "password.required")
    @Size(min = 6, max = 100)
    private String password;
}

