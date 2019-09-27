package com.gmail.dzhivchik.service;

import com.gmail.dzhivchik.domain.User;


public interface UserService {
    User getReferenceUser(int userId);
    User get(String login);
    boolean create(User user);
    void edit(User user);
}