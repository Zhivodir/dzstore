package com.gmail.dzhivchik.domain;

import com.gmail.dzhivchik.domain.enums.Language;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SpringSecurityUser extends org.springframework.security.core.userdetails.User {
    @Getter private final int id;
    @Getter @Setter private Language language;
    @Getter @Setter private String email;

    public SpringSecurityUser(int id, String username, String password, Collection<? extends GrantedAuthority> authorities, Language language, String email) {
        super(username, password, authorities);
        this.id = id;
        this.language = language;
        this.email = email;
    }
}
