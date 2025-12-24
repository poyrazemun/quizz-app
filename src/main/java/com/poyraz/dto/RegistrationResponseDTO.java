package com.poyraz.dto;

import com.poyraz.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class RegistrationResponseDTO {
    private Long id;
    private String username;
    private Set<Role> roles;
}
