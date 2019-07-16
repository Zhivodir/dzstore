package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.User;

import java.util.List;

public interface UserDAO {
    void addUser(User user);
    User getUser(String login);
    List<User> getUsersById(int[] cancel_share_for_users);
    List<User> getUsersByEmail(String[] cancel_share_for_users);
    void editUser(User user);
    void delete(User user);
    List<User> getShareReceivers(String shareFor);
}
