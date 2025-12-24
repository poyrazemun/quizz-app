package com.poyraz.service.impl;

import com.poyraz.dto.RegistrationResponseDTO;
import com.poyraz.dto.RegistrationUserDTO;
import com.poyraz.entity.User;
import com.poyraz.enums.Role;
import com.poyraz.exceptions.UsernameAlreadyExistsException;
import com.poyraz.repository.UserRepository;
import com.poyraz.service.UserService;
import com.poyraz.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public RegistrationResponseDTO register(RegistrationUserDTO registrationUserDTO) {
        if (userRepository.existsByUsername(registrationUserDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists: " + registrationUserDTO.getUsername());
        }

        User user = User.builder()
                .username(registrationUserDTO.getUsername())
                .password(passwordEncoder.encode(registrationUserDTO.getPassword()))
                .roles(defaultRoles())
                .build();
        userRepository.save(user);

        return userMapper.savedUserToRegistrationResponseDTO(user);

    }

    private Set<Role> defaultRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        return roles;
    }
}
