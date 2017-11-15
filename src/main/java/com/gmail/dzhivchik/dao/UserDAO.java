package com.gmail.dzhivchik.dao;

import com.gmail.dzhivchik.domain.User;

import java.util.List;

/**
 * Created by User on 09.02.2017.
 */
public interface UserDAO {
    void addUser(User user);
    User getUser(String login);
    List<User> getUsersById(int[] cancel_share_for_users);
    void editUser(User user);
    void delete(User user);
    List<User> getShareReceivers(String shareFor);
}
