package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.User;

import java.util.List;

/**
 * Created by User on 31.01.2017.
 */
public interface UserDAO {
    void add(User user);
    void delete(User user);
    List<User> getList();
}
