package com.gmail.dzhivchik.service.Impl;

import com.gmail.dzhivchik.AuthorizedUser;
import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by User on 06.02.2017.
 */

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ShaPasswordEncoder passwordEncoder;

    @Override
    public User getUser(String login) {
        return userDAO.getUser(login);
    }

    @Override
    @Transactional
    public boolean addUser(User user) {
        boolean wasAdded = false;
        if(getUser(user.getLogin()) == null) {
            userDAO.addUser(user);
            wasAdded = true;
        }
        return wasAdded;
    }

    @Override
    public AuthorizedUser loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userDAO.getUser(login);
        if (user == null) {
            throw new UsernameNotFoundException("User " + login + " is not found");
        }
        return new AuthorizedUser(user);
    }
}
