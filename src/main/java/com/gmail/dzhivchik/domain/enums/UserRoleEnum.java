package com.gmail.dzhivchik.domain.enums;

public enum UserRoleEnum {
    ADMIN,
    USER,
    ANONYMOUS;

    UserRoleEnum() {
    }


    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
