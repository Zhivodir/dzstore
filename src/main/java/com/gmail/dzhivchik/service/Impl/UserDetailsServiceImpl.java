package com.gmail.dzhivchik.service.Impl;

import com.gmail.dzhivchik.domain.SpringSecurityUser;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.get(login);
        if(user == null){
            System.out.println("---------------Takogo usera net v base----------------");
        }
        // указываем роли для этого пользователя
        Set<GrantedAuthority> roles = new HashSet();
        roles.add(new SimpleGrantedAuthority(user.getRole().toString()));
        System.out.println("test");

        return new SpringSecurityUser(user.getId(), user.getLogin(), user.getPassword(), roles, user.getLanguage(), user.getEmail());
    }
}
