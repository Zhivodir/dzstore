package com.gmail.dzhivchik.service;

import com.gmail.dzhivchik.domain.User;


public interface UserService {
    User getUser(String login);
    boolean addUser(User user);
}