package com.gmail.dzhivchik.service;

import com.gmail.dzhivchik.domain.User;
import org.springframework.stereotype.Service;

/**
 * Created by User on 06.02.2017.
 */

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(String login) {
        User user = new User();
        user.setLogin(login);
        user.setPassword("7110eda4d09e062aa5e4a390b0a572ac0d2c0220");
        return user;
    }
}
