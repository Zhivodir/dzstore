package com.gmail.dzhivchik.service.Impl;

import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by User on 06.02.2017.
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;


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
}
