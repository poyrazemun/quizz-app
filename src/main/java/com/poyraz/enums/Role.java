package com.poyraz.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public enum Role {
    ADMIN,
    USER;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }

    public List<SimpleGrantedAuthority> toGrantedAuthorities() {
        return List.of(new SimpleGrantedAuthority(getAuthority()));
    }

    public static List<SimpleGrantedAuthority> toAuthoritiesList(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getAuthority())).collect(Collectors.toList());
    }
}

