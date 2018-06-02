package com.gmail.dzhivchik;


import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.to.UserTo;
import com.gmail.dzhivchik.util.UserUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Objects.requireNonNull;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;
    private final UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), false, true, true, true, null);
        this.userTo = UserUtil.asTo(user);
    }

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(1111);
        if (auth == null) {
            System.out.println(00000);
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof AuthorizedUser) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser get() {
        AuthorizedUser user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    public static int id() {
        System.out.println("Heed Id: " + safeGet().userTo.getId());
        return safeGet().userTo.getId();
    }
}
