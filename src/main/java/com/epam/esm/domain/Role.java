package com.epam.esm.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    GUEST, USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
