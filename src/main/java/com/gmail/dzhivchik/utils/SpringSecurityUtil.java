package com.gmail.dzhivchik.utils;

import com.gmail.dzhivchik.domain.SpringSecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {
    public static SpringSecurityUser getSecurityUser(){
        return (SpringSecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
