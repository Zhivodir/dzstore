package com.gmail.dzhivchik.service;

import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.domain.enums.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by User on 06.02.2017.
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // с помощью нашего сервиса UserService получаем User
        User user = userService.getUser("colibri");
        // указываем роли для этого пользователя
        Set<GrantedAuthority> roles = new HashSet();
        roles.add(new SimpleGrantedAuthority("ROLE_" + UserRoleEnum.USER.name()));

        // на основании полученныйх даных формируем объект UserDetails
        // который позволит проверить введеный пользователем логин и пароль
        // и уже потом аутентифицировать пользователя
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(user.getLogin(),
                        user.getPassword(),
                        roles);
        System.out.println(userDetails);

        return userDetails;
    }
}
