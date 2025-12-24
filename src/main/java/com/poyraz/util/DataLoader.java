package com.poyraz.util;

import com.poyraz.entity.User;
import com.poyraz.enums.Role;
import com.poyraz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (!userRepository.existsByUsername("admin")) {
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ADMIN);
            roles.add(Role.USER);
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(roles)
                    .build();
            userRepository.save(admin);
            log.info("Created demo admin user: admin");
        }

        if (!userRepository.existsByUsername("user")) {
            Set<Role> roles = new HashSet<>();
            roles.add(Role.USER);
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .roles(roles)
                    .build();
            userRepository.save(user);
            log.info("Created demo user: user");
        }
    }
}
