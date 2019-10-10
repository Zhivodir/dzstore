package com.gmail.dzhivchik.service.Impl;

import com.gmail.dzhivchik.dao.UserDAO;
import com.gmail.dzhivchik.domain.User;
import com.gmail.dzhivchik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    @Transactional
    public boolean create(User user) {
        return userDAO.save(user) != null;
    }

    @Override
    @Transactional
    public void edit(User user) {
        userDAO.save(user);
    }
}
