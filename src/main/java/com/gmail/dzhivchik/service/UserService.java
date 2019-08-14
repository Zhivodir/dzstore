package com.gmail.dzhivchik.service;

import com.gmail.dzhivchik.domain.User;


public interface UserService {
    User getReferenceUser(int userId);
    User getUser(String login);
    boolean addUser(User user);
    void editUser(User user);
}