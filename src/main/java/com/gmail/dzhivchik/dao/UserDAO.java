package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.User;

/**
 * Created by User on 09.02.2017.
 */
public interface UserDAO {
    void addUser(User user);
    User getUser(String login);
    void editUser(User user);
    void delete(User user);
}
