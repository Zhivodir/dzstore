package com.gmail.dzhivchik.service.Impl;

import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;


    @Override
    public User get(String login) {
        return userDAO.get(login);
    }

    @Override
    public User getReferenceUser(int userId) {
        return userDAO.getUserReference(userId);
    }

    @Override
    public boolean create(User user) {
        return userDAO.save(user) != null;
    }

    @Override
    public void edit(User user) {
        userDAO.save(user);
    }
}
