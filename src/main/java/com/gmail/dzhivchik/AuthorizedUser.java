package com.gmail.dzhivchik;

import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.to.UserTo;
import com.gmail.dzhivchik.util.UserUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.HashSet;

import static java.util.Objects.requireNonNull;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private static UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), true, true, true, true,
                new HashSet<GrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()))));
        this.userTo = UserUtil.asTo(user);
    }

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser get() {
        AuthorizedUser user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    public int getId() {
        return userTo.getId();
    }

    public static int id() {
        return safeGet().userTo.getId();
    }

    public UserTo getUserTo() {
        return userTo;
    }

    public void updateBusySize(long additionalSize) {
        this.getUserTo().changeBusySize(additionalSize);
        //userTo.changeSize(sizes);
    }

    public static String[] getShowBusySize() {
        return userTo.getshowBusySize();
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}
