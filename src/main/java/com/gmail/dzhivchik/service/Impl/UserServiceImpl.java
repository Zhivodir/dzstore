package com.gmail.dzhivchik.service.Impl;

import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 06.02.2017.
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional
    public User getUser(String login) {
        return userDAO.getUser(login);
    }

    @Override
    @Transactional
    public void addUser(User user) {
        userDAO.addUser(user);
    }

    @Override
    public boolean existsByLogin(String login) {
        return false;
    }
}
