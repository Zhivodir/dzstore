package com.gmail.dzhivchik.domain.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by User on 02.02.2017.
 */
public enum UserRoleEnum implements GrantedAuthority {
    ADMIN,
    USER,
    ANONYMOUS;

    UserRoleEnum() {
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
