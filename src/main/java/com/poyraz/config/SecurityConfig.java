package com.poyraz.config;

import com.poyraz.repository.UserRepository;
import com.poyraz.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/question/**", "/quiz/**", "/auth/register"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/login", "/auth/register/**", "/logout-success",
                                "/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico"
                        )
                        .permitAll()

                        .requestMatchers(HttpMethod.GET, "/ui/questions").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/ui/question/random").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/ui/question/{id}/view").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/ui/question/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/ui/question/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/ui/question/edit").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/ui/question/{id}/delete").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/question/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/question/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/quiz/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/quiz/submit/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/quiz/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/ui/questions", true)
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")//Spring Security default logout URL, yazmasam da olurdu
                        .logoutSuccessUrl("/logout-success")
                        .permitAll())
                .exceptionHandling(conf -> conf.accessDeniedPage("/access-denied"))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
